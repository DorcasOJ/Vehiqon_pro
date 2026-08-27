package com.vehiqon.features.insights.auditLog.enums;

public enum AuditActionType {
    USER_PASSWORD_CHANGED( "User Changed Password"),
    USER_VERIFIED_EMAIL("User Email Verified"),
    USER_REGISTERED("User registered"),
    USER_LOGGED_IN("User logged in"),
    USER_LOGIN_FAILED("User login failed"),
    USER_PASSWORD_RESET_REQUESTED("User requested password reset"),
    USER_PASSWORD_RESET_COMPLETED("User reset password"),


    //    USER_EMAIL_VERIFIED("User email verified"),
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
    ROLE_UPDATED("User role updated"),
    ROLE_SYNCED("User role synced"),
    GET_USER("Admin view user"),
    USER_DELETED("User deleted by admin"),
    USER_RESTORED("User restored by admin"),

    VEHICLE_REGISTERED("User registered a vehicle"),
    VEHICLE_DOCUMENT_UPLOADED("User uploaded a vehicle document"),
    VEHICLE_DOCUMENT_UPDATED("User uploaded a vehicle document"),
    VEHICLE_DOCUMENT_DELETED("User uploaded a vehicle document"),
    VEHICLE_DOCUMENT_RESTORED("User uploaded a vehicle document"),
    VEHICLE_UPDATED("User Updated Vehicle details"),
    VEHICLE_VIEWED("User viewed Vehicle"),
    GET_VEHICLE_STATISTICS("User viewed Vehicle Statistics"),
    VEHICLE_DELETED("User deleted a Vehicle"),
    VEHICLE_RESTORED("Admin restored a deleted Vehicle"),
    VEHICLE_DOCUMENT_VERIFIED("Admin approved a user Vehicle document"),
    VEHICLE_DOCUMENT_REJECTED("Admin rejected a user Vehicle document"),
    VIEW_VEHICLE_DOCUMENT("User view one or all Vehicle documents"),

    SEARCH("User seared for an entity");

//    GET_USER("Admin view user"),

    private final String description;

    AuditActionType(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }


}
