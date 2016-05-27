package com.herokuapp.angebagui.imageuploadingwithretrofit2.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.herokuapp.angebagui.imageuploadingwithretrofit2.Constants;
import com.herokuapp.angebagui.imageuploadingwithretrofit2.R;
import com.herokuapp.angebagui.imageuploadingwithretrofit2.helpers.DocumentHelper;
import com.herokuapp.angebagui.imageuploadingwithretrofit2.helpers.IntentHelper;
import com.herokuapp.angebagui.imageuploadingwithretrofit2.model.ImageResponse;
import com.herokuapp.angebagui.imageuploadingwithretrofit2.model.Upload;
import com.herokuapp.angebagui.imageuploadingwithretrofit2.rest.UploadService;
import com.herokuapp.angebagui.imageuploadingwithretrofit2.utils.aLog;
import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<ImageResponse>{

    private static final String TAG = MainActivity.class.getSimpleName();

    ImageView uploadImage;
    EditText uploadTitle;
    EditText uploadDesc;
    Toolbar toolbar;
    FloatingActionButton fab;

    private Upload upload; // Upload object containing image and meta data
    private File chosenFile; //chosen file from intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (TextUtils.isEmpty(Constants.MY_IMGUR_CLIENT_ID)){
            new AlertDialog.Builder(this)
                    .setMessage("Your imgur client id. You need this to upload to imgur. " +
                            "More here: https://api.imgur.com and add it in Constants.java")
                    .setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
        uploadImage = (ImageView)findViewById(R.id.imageview);
        uploadTitle = (EditText)findViewById(R.id.editText_upload_title);
        uploadDesc = (EditText)findViewById(R.id.editText_upload_desc);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDesc.clearFocus();
                uploadTitle.clearFocus();
                IntentHelper.chooseFileIntent(MainActivity.this);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri returnUri;

        if (requestCode != IntentHelper.FILE_PICK) {
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        returnUri = data.getData();
        String filePath = DocumentHelper.getPath(this, returnUri);
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return;
        chosenFile = new File(filePath);


        aLog.w(TAG, "Display with Picasso now");
        Picasso.with(getBaseContext())
                .load(chosenFile)
                .placeholder(R.drawable.ic_photo_library_black)
                .into(uploadImage);

    }
    private void clearInput() {
        uploadTitle.setText("");
        uploadDesc.clearFocus();
        uploadDesc.setText("");
        uploadTitle.clearFocus();
        uploadImage.setImageResource(R.drawable.ic_photo_library_black);
    }
    public void uploadImage() {
    /*
      Create the @Upload object
     */
        if (chosenFile == null) return;
        createUpload(chosenFile);

    /*
      Start upload
     */
        UploadService.execute(this,upload, this);
    }

    private void createUpload(File image) {
        upload = new Upload();

        upload.image = image;
        upload.title = uploadTitle.getText().toString();
        upload.description = uploadDesc.getText().toString();
    }

    @Override
    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
        clearInput();
    }

    @Override
    public void onFailure(Call<ImageResponse> call, Throwable t) {
        if (call == null){
            Snackbar.make(findViewById(R.id.rootView), "No internet connection", Snackbar.LENGTH_SHORT).show();
        }
    }
}
