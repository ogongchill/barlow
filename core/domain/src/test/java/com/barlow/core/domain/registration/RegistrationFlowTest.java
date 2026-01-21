package com.barlow.core.domain.registration;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.AuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class RegistrationFlowTest {

    @Test
    @DisplayName("신규 유저 플로우는 AUTHENTICATED 상태이고 기본 닉네임(issuer+sub)을 가진다")
    void fromNewUser_shouldCreateAuthenticatedFlowWithDefaultNickname() {
        // given
        ExternalPrincipal principal = mockExternalPrincipal("KAKAO", "123");

        // when
        RegistrationFlow flow = RegistrationFlow.fromNewUser(principal);

        // then
        assertThat(flow.getExternalPrincipal()).isSameAs(principal);
        assertThat(flow.getStatus()).isEqualTo(RegistrationFlow.Status.AUTHENTICATED);
        assertThat(flow.getTermAgreements()).isEmpty();

        assertThat(flow.getTarget()).isInstanceOf(RegistrationTarget.NewUser.class);
        RegistrationTarget.NewUser newUser = (RegistrationTarget.NewUser) flow.getTarget();
        assertThat(newUser.getNickname()).isEqualTo("KAKAO123");
    }

    @Test
    @DisplayName("기존 유저 플로우는 AUTHENTICATED 상태이고 ExistingUser 타겟을 가진다")
    void fromExistingUser_shouldCreateAuthenticatedFlowWithExistingUserTarget() {
        // given
        ExternalPrincipal principal = mockExternalPrincipal("KAKAO", "123");
        var user = mock(User.class);
        when(user.getUserNo()).thenReturn(10L);

        // when
        RegistrationFlow flow = RegistrationFlow.fromExistingUser(principal, user);

        // then
        assertThat(flow.getExternalPrincipal()).isSameAs(principal);
        assertThat(flow.getStatus()).isEqualTo(RegistrationFlow.Status.AUTHENTICATED);
        assertThat(flow.getTermAgreements()).isEmpty();

        assertThat(flow.getTarget()).isInstanceOf(RegistrationTarget.ExistingUser.class);
        RegistrationTarget.ExistingUser existing = (RegistrationTarget.ExistingUser) flow.getTarget();
        assertThat(existing.getUserNo()).isEqualTo(10L);
    }

    @Test
    @DisplayName("기존 유저면 닉네임 변경시 예외 처리된다")
    void changeNickname_shouldThrowWhenNotNewUser() {
        // given
        ExternalPrincipal principal = mockExternalPrincipal("KAKAO", "123");
        User user = mock(User.class);
        when(user.getUserNo()).thenReturn(10L);

        RegistrationFlow existingFlow = RegistrationFlow.fromExistingUser(principal, user);

        // when & then
        assertThatThrownBy(() -> existingFlow.changeNickname("hello"))
                .isInstanceOf(RegistrationException.class);
    }

    @Test
    @DisplayName("신규 유저가 이면 닉네임 변경할 수 있다")
    void changeNickname_doesNotThrowWhenNewUser() {
        // given
        ExternalPrincipal principal = mockExternalPrincipal("KAKAO", "123");

        RegistrationFlow flow = RegistrationFlow.fromNewUser(principal);
        RegistrationFlow changed = flow.changeNickname("changed");

        // when & then
        assertThat(((RegistrationTarget.NewUser) changed.getTarget()).nickname)
                .isEqualTo("changed");
    }

    @Test
    @DisplayName("TERMS_AGREED 상태가 아니면 markAsComplete() 호출시 예외")
    void complete_shouldThrowWhenNotTermsAgreed() {
        // given
        ExternalPrincipal principal = mockExternalPrincipal("KAKAO", "123");
        RegistrationFlow flow = RegistrationFlow.fromNewUser(principal); // AUTHENTICATED

        // when & then
        assertThatThrownBy(flow::complete)
                .isInstanceOf(RegistrationException.class)
                .hasMessageContaining("약관 동의 상태가 아닙니다");
    }

    @Test
    @DisplayName("TERMS_AGREED 상태면 markAsComplete()시 REGISTERED로 전이한다")
    void complete_shouldMoveToRegistered() {
        // given
        ExternalPrincipal principal = mockExternalPrincipal("KAKAO", "123");
        RegistrationFlow flow = RegistrationFlow.fromNewUser(principal);

        TermsPolicy policy = mock(TermsPolicy.class);
        List<TermAgreement> agreements = List.of(mock(TermAgreement.class));

        RegistrationFlow termsAgreed = flow.agreeTerms(agreements, policy);

        // when
        RegistrationFlow registered = termsAgreed.complete();

        // then
        assertThat(registered.getStatus()).isEqualTo(RegistrationFlow.Status.REGISTERED);
        assertThat(registered.getExternalPrincipal()).isSameAs(termsAgreed.getExternalPrincipal());
        assertThat(registered.getTarget()).isEqualTo(termsAgreed.getTarget());
        assertThat(registered.getTermAgreements()).isEqualTo(termsAgreed.getTermAgreements());
    }

    // ---------- helpers ----------
    private ExternalPrincipal mockExternalPrincipal(String issuer, String sub) {
        ExternalPrincipal principal = mock(ExternalPrincipal.class);
        var authProvider = mock(AuthProvider.class);

        when(principal.authProvider()).thenReturn(authProvider);
        when(authProvider.getIssuer()).thenReturn(issuer);
        when(principal.sub()).thenReturn(sub);

        return principal;
    }
}