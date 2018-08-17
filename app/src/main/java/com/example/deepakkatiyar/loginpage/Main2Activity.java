package com.example.deepakkatiyar.loginpage;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Main2Activity extends AppCompatActivity {
    private Uri mImageUri = null;
    Button btn, mSubmitBtn;
    private ImageView imageview;
    private int GALLERY = 1;
    private ProgressBar progressBar;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btn = (Button) findViewById(R.id.capture);
        mSubmitBtn = (Button) findViewById(R.id.save);
        imageview = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Upload");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });

    }


    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }


    private void startPosting() {

        if (mImageUri != null) {
            progressBar.setVisibility(View.VISIBLE);

            StorageReference filepath = mStorage.child("Images").child(mImageUri.getLastPathSegment());


            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                    DatabaseReference newPost = mDatabase.push();//push kreira uniq random id


                    newPost.child("image").setValue(downloadUrl.toString());
                    Toast.makeText(Main2Activity.this, "image updated.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                mImageUri = data.getData();
                imageview.setImageURI(mImageUri);


            }
        }


    }
}
