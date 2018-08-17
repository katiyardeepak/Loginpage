package com.example.deepakkatiyar.loginpage;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class form extends Activity {
    EditText inspection1,inspection2,inspection4,inspection5;
    private FirebaseUser user;
    Button submit;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        submit=(Button) findViewById(R.id.submitform);
        inspection1=(EditText) findViewById(R.id.editText1);
        inspection2=(EditText) findViewById(R.id.editText2);
        inspection4=(EditText) findViewById(R.id.editText4);
        inspection5=(EditText) findViewById(R.id.editText5);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        user = FirebaseAuth.getInstance().getCurrentUser();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    progressBar.setVisibility(View.VISIBLE);

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();
                    DatabaseReference usersRef = ref.child("users");
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("Name",inspection1.getText().toString());
                    hashMap.put("Email",inspection2.getText().toString());
                    hashMap.put("heighest qualification",inspection4.getText().toString());
                    hashMap.put("Mobile number",inspection5.getText().toString());
                    usersRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){


                                Toast.makeText(form.this, "User data successfully saved.", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(form.this,MainActivity.class);
                                startActivity(intent);

                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(form.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
