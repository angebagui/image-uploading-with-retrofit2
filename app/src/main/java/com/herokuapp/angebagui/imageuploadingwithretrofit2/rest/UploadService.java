package com.herokuapp.angebagui.imageuploadingwithretrofit2.rest;

import android.content.Context;

import com.herokuapp.angebagui.imageuploadingwithretrofit2.Constants;
import com.herokuapp.angebagui.imageuploadingwithretrofit2.helpers.NotificationHelper;
import com.herokuapp.angebagui.imageuploadingwithretrofit2.model.ImageResponse;
import com.herokuapp.angebagui.imageuploadingwithretrofit2.model.Upload;
import com.herokuapp.angebagui.imageuploadingwithretrofit2.utils.NetworkUtils;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by angebagui on 27/05/2016.
 */
public class UploadService {
    private static final String API_URL= "https://api.imgur.com";
    private static final String server = API_URL+"/3/";

    private static ImgurAPI imgurAPI;

    public static ImgurAPI get(){
        if (imgurAPI==null){
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(server)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient())
                    .build();
            imgurAPI = retrofit.create(ImgurAPI.class);
        }
        return imgurAPI;
    }

    public static void execute(Context context , Upload upload, Callback<ImageResponse> callback){
        final Callback<ImageResponse> cb = callback;

        // Vérifiez s'il y a internet
        if (!NetworkUtils.isConnected(context)) {
            //Callback sera appellé, donc prévenir qu'une notification n'est pas nécessaire
            cb.onFailure(null,new Throwable("Internet error see the log message error"));
            return;
        }



        final NotificationHelper notificationHelper = new NotificationHelper(context);
        /*
         Notifier que le chargement est en cours
          */
        notificationHelper.createUploadingNotification();

        get().postImage(
                Constants.getClientAuth(),
                upload.title,
                upload.description,
                upload.albumId,
                null,
                MultipartBody.Part.createFormData(
                        "image",
                        upload.image.getName(),
                        RequestBody.create(MediaType.parse("image/*"), upload.image)
                )
        ).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                // Passer les données au callback
                if(cb != null)cb.onResponse(call,response);

                if (response == null) {
                            /*
                              Notifier lorsque l'image n'a pas été uploadé avec succès
                            */
                    notificationHelper.createFailedUploadNotification();
                    return;
                }
                 /*
                   Notifier lorsque l'image a été uploadé avec succès
                  */
                if (response.isSuccessful()){
                    notificationHelper.createUploadedNotification(response.body());
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                // Passer les données au callback
                if(cb != null)cb.onFailure(call,t);
                 /*
                    Notifier lorsque l'image n'a pas été uploadé avec succès
                  */
                notificationHelper.createFailedUploadNotification();
            }
        });
    }
}
