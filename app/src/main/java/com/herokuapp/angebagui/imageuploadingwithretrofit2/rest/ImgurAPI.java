package com.herokuapp.angebagui.imageuploadingwithretrofit2.rest;

import com.herokuapp.angebagui.imageuploadingwithretrofit2.model.ImageResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by angebagui on 27/05/2016.
 */
public interface ImgurAPI {

    /****************************************
     * Upload
     * Image upload API
     */

    /**
     * @param auth        #Type of authorization for upload
     * @param title       #Title of image
     * @param description #Description of image
     * @param albumId     #ID for album (if the user is adding this image to an album)
     * @param username    username for upload
     * @param file        image
     */

    @Multipart
    @POST("image")
    Call<ImageResponse>  postImage(
            @Header("Authorization") String auth,
            @Query("title") String title,
            @Query("description") String description,
            @Query("album") String albumId,
            @Query("account_url") String username,
            @Part MultipartBody.Part file

    );
}
