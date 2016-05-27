package com.herokuapp.angebagui.imageuploadingwithretrofit2.utils;

import android.util.Log;

import com.herokuapp.angebagui.imageuploadingwithretrofit2.Constants;
/**
 * Created by angebagui on 27/05/2016.
 */
public class aLog {
  public static void w (String TAG, String msg){
    if(Constants.LOGGING) {
      if (TAG != null && msg != null)
        Log.w(TAG, msg);
    }
  }

}