package com.herokuapp.angebagui.imageuploadingwithretrofit2;

/**
 * Created by angebagui on 27/05/2016.
 */
public class Constants {
    /*
      Logging flag
     */
    public static final boolean LOGGING = true;

    /*
      Your imgur client id. You need this to upload to imgur.
      More here: https://api.imgur.com/
     */
    public static final String MY_IMGUR_CLIENT_ID = "";
    public static final String MY_IMGUR_CLIENT_SECRET = "";


    /*
      Client Auth
     */
    public static String getClientAuth() {
        return "Client-ID " + MY_IMGUR_CLIENT_ID;
    }

}