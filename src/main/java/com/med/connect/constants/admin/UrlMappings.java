package com.med.connect.constants.admin;

public class UrlMappings {

    //############## APP CONTEXT #####################
    public static final String APPLICATION_CONTEXT_PATH = "/med-connect";

    //############## AUTH BASE URL #####################
    public static final String AUTH_BASE_URL = APPLICATION_CONTEXT_PATH + "/api/auth";

    public static final String SIGN_IN = "/signIn";
    public static final String SIGN_UP = "/signUp";

    public static final String OTP_VERIFY = "/otpVerify";

    public static final String SIGN_UP_USER = "/signUpUser";
    public static final String SIGN_IN_USER = "/signInUser";

    public static final String FORGOT_PASSWORD = "/forgotPassword";

    public static final String FORGOT_PASSWORD_OTP_VERIFY = "/forgotPasswordOtpVerify";

    public static final String CHANGE_FORGOT_PASSWORD= "/changeForgetPassword";

    public static final String VERIFY_TOKEN_EMAIL = "/verifyTokenEmail";

    public static final String RESEND_EMAIL_LINK = "/resendEmailLink/{username}";







    //############## VERSION #####################
    public static final String VERSION_URL =  "/api/v1";

    public static final String MED_ADMIN=  "/med-admin";

    //############## BASE URL #####################
    public static final String ADMIN_BASE_URL=  MED_ADMIN + VERSION_URL;


    //################# TEST END POINTS #######################
    public static final String ALL = "/all";
    public static final String USER = "/user";
    public static final String MOD = "/mod";
    public static final String ADMIN = "/admin";
    public static final String STUDENT = "/student";

    public static final String DOCTOR = "/doctor";





    //################# MED ROLES #######################
    public static final String GET_MED_ROLES = "/getMedRoles";



    //#################GET USERS LIST #######################
    public static final String GET_MED_USER_LIST=  "/getMedUserList";
    public static final String MED_USER_LOCKED=  "/medUserLocked/{id}";


    //#################GET USERS LIST #######################
    public static final String UPLOAD_PROFILE_PICTURE=  "/uploadProfilePicture";


    //################# USER POST  #######################
    public static final String SUBMIT_POST=  "/submitPost";


}
