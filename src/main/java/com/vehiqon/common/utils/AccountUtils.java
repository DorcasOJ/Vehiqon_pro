package com.vehiqon.common.utils;

import jakarta.servlet.http.HttpServletRequest;

public class AccountUtils {
    public static final String USER_EXIST_CODE = "001";
    public static final String USER_CREATION_CODE = "002";
    public static final String USER_LOGIN_CODE = "003";
    public static final String USER_NOT_FOUND_CODE = "004";
    public static final String USER_FOUND_CODE = "004";
    public static final String VALIDATION_ERROR_CODE= "401";
    public static final String UNAUTHORIZED_ERROR_CODE= "401";
    public static final String INVALID_CREDENTIALS_CODE = "005";
    public static final String SUCCESS_CODE = "200";


    public static final String SUCCESS_MESSAGE = "Success";
    public static final String FAILED_MESSAGE = "Success";
    public static final String UNAUTHORIZED_ERROR_MESSAGE= "User is unauthorised";
    public static final String USER_FOUND_MESSAGE = "User Found";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Credentials is invalid, Kindly supply valid credentials";
    public static final String USER_LOGIN_MESSAGE = "User Login Successful";
    public static final String USER_EXIST_MESSAGE = "This user already has an account created";
    public static final String USER_CREATION_MESSAGE = "Account has been successfully created.\n A verification email has been sent to your email.\n Please verify your email.";
    public static final String USER_NOT_FOUND_MESSAGE = "User Account Found";
    public static final String VALIDATION_ERROR_MESSAGE= "VALIDATION_FAILED";
    
    
//    public static final String password_changed_action = "PASSWORD_CHANGED";
//    public static final String user_registered_action = "USER_REGISTERED";
//    public static final String user_login_action = "USER_LOGGED_IN";
//    public static final String user_logout_action = "USER_LOGGED_OUT";
//    public static final String user_logout_all_action = "USER_LOGGED_OUT_ALL";
//    public static final String password_reset_action = "PASSWORD_RESET";
//    public static final String token_refreshed_action = "TOKEN_REFRESHED";
//    public static final String email_verified_action = "EMAIL_VERIFIED";
    public static final String password_changed_description = "User Changed Password";
    public static final String password_reset_requested_description = "Password Reset Requested Via Forgot Password";
    public static final String user_email_verified_description = "User Email Verified";
    public static final String user_requested_email_verification_description = "User Requested Email Verification Token";
    public static final String user_registered_description = "User Registered";
    public static final String user_login_description = "User Logged In";
    public static final String user_logout_description = "User Logged Out";
    public static final String user_logout_all_description = "User Logged Out On All Devices ";
    public static final String password_reset_description = "User Reset Password";
    public static final String token_refreshed_description = "Refreshed Login Token was generated or refreshed";
    public static final String email_verified_description = "User Email Verified";



}
