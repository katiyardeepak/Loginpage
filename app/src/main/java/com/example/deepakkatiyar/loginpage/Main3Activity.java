package com.example.deepakkatiyar.loginpage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class Main3Activity extends Activity  {
    Uri videoFileUri;
    public static final int VIDEO_CAPTURED = 1;
    StorageReference videoref;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        videoView=(VideoView)findViewById(R.id.videoView);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
       videoref=storageReference.child("/videos/" +uid+ "/userinform.3gp");
    }
    public  void upload(View v)
    {
        if(videoFileUri!=null) {
            final UploadTask uploadTask = videoref.putFile(videoFileUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Main3Activity.this, "upload failed" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Main3Activity.this, "upload successfully", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(Main3Activity.this,form.class);
                    startActivity(intent);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
               updateprogress(taskSnapshot);

                }
            });
        }
        else{
            Toast.makeText(Main3Activity.this,"nothindg to upload",Toast.LENGTH_LONG).show();
        }
    }
    public void updateprogress(UploadTask.TaskSnapshot taskSnapshot)
    {
        long filesize=taskSnapshot.getTotalByteCount();
        long uploadbytes=taskSnapshot.getBytesTransferred();
        long progress=(100 * uploadbytes)/filesize;
        ProgressBar progressBar=(ProgressBar) findViewById(R.id.probar);
        progressBar.setProgress((int)progress);
    }
    public void record(View v)
    {
        Intent captureVideoIntent =new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(captureVideoIntent,VIDEO_CAPTURED);

    }
    public void download( View v) {

           videoView.setVideoURI(videoFileUri);
            videoView.start();
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        videoFileUri = data.getData();
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == VIDEO_CAPTURED) {

            Toast.makeText(Main3Activity.this, " video saved " +videoFileUri, Toast.LENGTH_LONG).show();
        }
        else if(resultCode==RESULT_CANCELED)
        {
            Toast.makeText(Main3Activity.this, " video recording cancelled ",  Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(Main3Activity.this, " failed to record video " , Toast.LENGTH_LONG).show();
        }

    }
    }

