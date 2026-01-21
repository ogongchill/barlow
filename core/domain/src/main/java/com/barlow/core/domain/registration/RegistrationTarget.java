package com.barlow.core.domain.registration;

import com.barlow.core.domain.User;

sealed class RegistrationTarget {

    public static final class NewUser extends RegistrationTarget {

        public final String nickname;

        public NewUser(String nickname) {
            this.nickname = nickname;
        }

        public String getNickname() {
            return nickname;
        }
    }

    public static final class ExistingUser extends RegistrationTarget {

        private final Long userNo;

        public ExistingUser(Long memberNo) {
            this.userNo = memberNo;
        }

        public static ExistingUser from(User user) {
            return new ExistingUser(user.getUserNo());
        }


        public Long getUserNo() {
            return userNo;
        }
    }
}
