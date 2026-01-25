package com.barlow.core.domain.registration;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.UserCreateCommand;
import com.barlow.core.enumerate.DeviceOs;

sealed class RegistrationTarget {

    public static final class NewUser extends RegistrationTarget {

        private final String nickname;
        private final DeviceOs os;
        private final String deviceId;
        private final String deviceToken;

        public NewUser(String nickname, DeviceOs os, String deviceId, String deviceToken) {
            this.nickname = nickname;
            this.os = os;
            this.deviceId = deviceId;
            this.deviceToken = deviceToken;
        }

        public UserCreateCommand toCommand() {
            return new UserCreateCommand(
                    os,
                    deviceId,
                    deviceToken,
                    nickname,
                    User.Role.MEMBER
            );
        }

        public String getNickname() {
            return nickname;
        }

        public DeviceOs getOs() {
            return os;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public String getDeviceToken() {
            return deviceToken;
        }
    }

    public static final class ExistingUser extends RegistrationTarget {

        private final User user;

        public ExistingUser(User user) {
            this.user = user;
        }

        public static ExistingUser from(User user) {
            return new ExistingUser(user);
        }

        public Long getUserNo() {
            return user.getUserNo();
        }

        public User getUser() {
            return user;
        }
    }
}
