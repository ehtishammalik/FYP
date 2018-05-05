package com.example.android.rideshareplanner;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class SignUp extends AppCompatActivity {

    private ImageView userImage;
    private EditText EmailAddress;
    private EditText Password;
    private EditText ConfirmPassword;
    private EditText FullName;
    private Button SignUp;
    private TextView alreadyuser;
    private String password,email,name,phone;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE_REQUEST =101;
    private String imageUrl;
    private Uri imageUri;
    private StorageReference storageRef;
    static Users userData;
    private FirebaseUser firebaseUser;
    static String userId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth    =FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userImage = (ImageView) findViewById(R.id.profile_lv_image);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


        EmailAddress =(EditText) findViewById(R.id.userEmailId);

        Password =(EditText) findViewById(R.id.password);

        ConfirmPassword =(EditText) findViewById(R.id.confirmPassword);

        FullName     =(EditText) findViewById(R.id.fullName);

        SignUp = (Button)  findViewById(R.id.signUpBtn);


        alreadyuser = (TextView) findViewById(R.id.already_user);
        alreadyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Password.getText().toString().isEmpty() || EmailAddress.getText().toString().isEmpty() || FullName.getText().toString().isEmpty() || ConfirmPassword.getText().toString().isEmpty() || userImage ==null || imageUri == null){
                    Toast.makeText(SignUp.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (!Password.getText().toString().equals(ConfirmPassword.getText().toString())){
                    Toast.makeText(SignUp.this, "Both Passwords doesn't Match", Toast.LENGTH_SHORT).show();
                }
                else {
                    btnSignup_Click(v);
                }
            }
        });
    }

    public void btnSignup_Click(View v){

        progressDialog = ProgressDialog.show(SignUp.this,"It may take a while","Processing ...");
        (firebaseAuth.createUserWithEmailAndPassword(EmailAddress.getText().toString(),
                Password.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    email = EmailAddress.getText().toString();
                    Log.e("SignUp", "onComplete: Email" + email);
                    name = FullName.getText().toString();
                    Log.e("SignUp", "onComplete: Name" + name);
                    Toast.makeText(SignUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    sendemailverification();
                    upload();
                }
                else {
                    progressDialog.dismiss();
                    Log.e("ERROR", task.getException().toString());
                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendemailverification() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            userId = user.getUid();
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUp.this, "Check your Email for Verification", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }


        public void showFileChooser() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    userImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    private void upload() {
        if (imageUri != null) {
            storageRef = FirebaseStorage.getInstance().getReference().child("DPs").child(System.currentTimeMillis()+"."+getImageExt(imageUri));
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            imageUrl=taskSnapshot.getDownloadUrl().toString();

                            userData = new Users();
                            userData.setName(name);
                            userData.setEmail(email);
                            userData.setImageUrl(imageUrl);


                            progressDialog.dismiss();
                            Intent i  = new Intent(SignUp.this,DriverRegistration.class);
                            startActivity(i);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(SignUp.this,"Upload Image" , Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
            ;
        }
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }
}