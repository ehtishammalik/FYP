package com.example.android.rideshareplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class GoogleSignUp extends AppCompatActivity {

    private EditText EmailAddress;
    private EditText FullName;
    private Button SignUp;
    private TextView alreadyuser;
    private ImageView userImage;
    static String email,name;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private String imageUrl;
    private Uri imageUri;
    private StorageReference storageRef;
    private static final int PICK_IMAGE_REQUEST =101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_up);

        firebaseAuth    =FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        userImage = (ImageView) findViewById(R.id.gsingup_image);

        EmailAddress =(EditText) findViewById(R.id.gsingup_userEmailId);


        FullName     =(EditText) findViewById(R.id.gsingup_fullName);
        name = FullName.getText().toString();

        SignUp = (Button)  findViewById(R.id.gsingup_signUpBtn);

        email = EmailAddress.getText().toString();


        alreadyuser = (TextView) findViewById(R.id.gsingup_already_user);
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
                if (EmailAddress.getText().toString().isEmpty() || FullName.getText().toString().isEmpty()){
                    Toast.makeText(GoogleSignUp.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else {

                }
            }
        });
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
}
