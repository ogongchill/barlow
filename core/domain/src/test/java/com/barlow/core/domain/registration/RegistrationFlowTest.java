package com.barlow.core.domain.registration;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.AuthProvider;
import com.barlow.core.enumerate.DeviceOs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RegistrationFlowTest {

    @Test
    @DisplayName("ExistingUser 타겟으로 컨텍스트를 생성할 수 있다")
    void shouldCreateContextWithExistingUserTarget() {
        // given
        ExternalPrincipal principal = new ExternalPrincipal(AuthProvider.KAKAO, "123");
        var user = mock(User.class);
        when(user.getUserNo()).thenReturn(10L);

        // when
        MemberRegistrationContext context = MemberRegistrationContext
                .withExistingUser(principal, RegistrationTarget.ExistingUser.from(user));

        // then
        assertThat(context.principal()).isSameAs(principal);
        assertThat(context.agreements()).isEmpty();
        assertThat(context.target()).isInstanceOf(RegistrationTarget.ExistingUser.class);

        RegistrationTarget.ExistingUser existing = (RegistrationTarget.ExistingUser) context.target();
        assertThat(existing.getUserNo()).isEqualTo(10L);
    }

    @Test
    @DisplayName("NewUser 타겟으로 컨텍스트를 생성할 수 있다")
    void shouldCreateContextWithNewUserTarget() {
        // given
        ExternalPrincipal principal = new ExternalPrincipal(AuthProvider.KAKAO, "123");
        RegistrationTarget.NewUser newUser = new RegistrationTarget.NewUser(
                "nickname", DeviceOs.ANDROID, "deviceId", "deviceToken"
        );

        // when
        MemberRegistrationContext context = MemberRegistrationContext
                .withNewUser(principal, newUser);

        // then
        assertThat(context.principal()).isSameAs(principal);
        assertThat(context.target()).isInstanceOf(RegistrationTarget.NewUser.class);

        RegistrationTarget.NewUser target = (RegistrationTarget.NewUser) context.target();
        assertThat(target.getNickname()).isEqualTo("nickname");
        assertThat(target.getOs()).isEqualTo(DeviceOs.ANDROID);
    }

    @Test
    @DisplayName("약관 동의 목록을 컨텍스트에 추가할 수 있다")
    void shouldAddAgreementsToContext() {
        // given
        ExternalPrincipal principal = new ExternalPrincipal(AuthProvider.KAKAO, "123");
        RegistrationTarget.NewUser newUser = new RegistrationTarget.NewUser(
                "nickname", DeviceOs.ANDROID, "deviceId", "deviceToken"
        );
        Term term = new Term(1L, "서비스 이용약관", "1.0", "https://example.com/terms", true);
        List<TermAgreement> agreements = List.of(TermAgreement.agree(term));

        // when
        MemberRegistrationContext context = MemberRegistrationContext
                .withNewUser(principal, newUser)
                .withAgreements(agreements);

        // then
        assertThat(context.agreements()).hasSize(1);
        assertThat(context.agreements().get(0).agreed()).isTrue();
    }

    @Test
    @DisplayName("TermsPolicy는 필수 약관에 모두 동의하지 않으면 예외를 발생시킨다")
    void termsPolicyShouldThrowWhenRequiredTermsNotAgreed() {
        // given
        Term requiredTerm1 = new Term(1L, "서비스 이용약관", "1.0", "https://example.com/terms1", true);
        Term requiredTerm2 = new Term(2L, "개인정보 처리방침", "1.0", "https://example.com/terms2", true);
        TermsPolicy policy = TermsPolicy.from(List.of(requiredTerm1, requiredTerm2));

        List<TermAgreement> agreements = List.of(TermAgreement.agree(requiredTerm1));

        // when & then
        assertThatThrownBy(() -> policy.validate(agreements))
                .isInstanceOf(RegistrationException.class)
                .hasMessageContaining("필수 동의 약관에 모두 동의하지 않았습니다")
                .hasMessageContaining("2");
    }

    @Test
    @DisplayName("TermsPolicy는 필수 약관에 모두 동의하면 예외를 발생시키지 않는다")
    void termsPolicyShouldPassWhenAllRequiredTermsAgreed() {
        // given
        Term requiredTerm1 = new Term(1L, "서비스 이용약관", "1.0", "https://example.com/terms1", true);
        Term requiredTerm2 = new Term(2L, "개인정보 처리방침", "1.0", "https://example.com/terms2", true);
        TermsPolicy policy = TermsPolicy.from(List.of(requiredTerm1, requiredTerm2));

        List<TermAgreement> agreements = List.of(
                TermAgreement.agree(requiredTerm1),
                TermAgreement.agree(requiredTerm2)
        );

        // when & then
        policy.validate(agreements);
    }

    @Test
    @DisplayName("NewUser의 toCommand는 MEMBER 역할로 UserCreateCommand를 생성한다")
    void newUserToCommandShouldCreateMemberRoleCommand() {
        // given
        RegistrationTarget.NewUser newUser = new RegistrationTarget.NewUser(
                "nickname", DeviceOs.IOS, "deviceId", "deviceToken"
        );

        // when
        var command = newUser.toCommand();

        // then
        assertThat(command.nickname()).isEqualTo("nickname");
        assertThat(command.os()).isEqualTo(DeviceOs.IOS);
        assertThat(command.deviceId()).isEqualTo("deviceId");
        assertThat(command.deviceToken()).isEqualTo("deviceToken");
        assertThat(command.role()).isEqualTo(User.Role.MEMBER);
    }
}
