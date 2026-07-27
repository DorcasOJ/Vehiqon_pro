package com.vehiqon.features.insights.auditLog.enums;

public enum AuditAction {
    USER_PASSWORD_CHANGED( "User Changed Password"),
    USER_VERIFIED_EMAIL("User Email Verified"),
    USER_REGISTERED("User registered"),
    USER_LOGGED_IN("User logged in"),
    USER_LOGIN_FAILED("User login failed"),
    USER_PASSWORD_RESET_REQUESTED("User requested password reset"),
    USER_PASSWORD_RESET_COMPLETED("User reset password"),
    USER_EMAIL_VERIFIED("User email verified"),
    USER_VERIFICATION_EMAIL_RESENT("Verification email resent"),
    USER_REQUESTED_VERIFICATION_EMAIL_TOKEN("Verification email token requested"),
    USER_LOGGED_OUT("user logged out"),
    USER_LOGGED_OUT_ALL("user logged out on all devices"),
    USER_REFRESHED_TOKEN("User refreshed token was isued"),
    LOGIN_FAILED("INVALID PASSWORD"),
    ACCOUNT_LOCKED("TOO MANY FAILED LOGIN ATTEMPTS"),
    LOGIN_SUCCESS("User successfully logged in"),
    ACCOUNT_UNLOCKED("An Admin unlocked a user account"),

    USER_PROFILE_UPDATED("User updated profile"),
    USER_VIEWS_PROFILE("User viewed profile"),
    USER_ROLE_UPDATED("User role updated"),
    USER_ROLE_SYNCED("User role synced"),
    USER_UNLOCKED("User is now unlocked to login"),

    GET_USER("Admin view user");
//    GET_USER("Admin view user"),
//    GET_USER("Admin view user"),

    private final String description;

    AuditAction(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }


}
