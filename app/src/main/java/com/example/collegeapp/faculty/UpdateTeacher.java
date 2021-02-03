package com.example.collegeapp.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.collegeapp.R;
import com.example.collegeapp.UploadImage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateTeacher extends AppCompatActivity {
    private ImageView UpdateTeacherImage;
    private EditText UpdateTeacherName,UpdateTeacherEmail,UpdateTeacherPost;
    private Button UpdateTeacherButton,DeleteTeacherButton;
    private String name,email,image,post;
    private final int REQ=1;
    private Bitmap bitmap=null;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private  String downalodurl="",category,uniquekey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);
        //accept the data
        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        image=getIntent().getStringExtra("image");
        post=getIntent().getStringExtra("post");
         uniquekey=getIntent().getStringExtra("key");
         category=getIntent().getStringExtra("category");

        reference= FirebaseDatabase.getInstance().getReference().child("teacher");
        storageReference= FirebaseStorage.getInstance().getReference();


        UpdateTeacherImage=findViewById(R.id.updateTeacherImage);
        UpdateTeacherName=findViewById(R.id.updateTeacherName);
        UpdateTeacherEmail=findViewById(R.id.updateTeacherEmail);
        UpdateTeacherPost=findViewById(R.id.updateTeacherPost);
        UpdateTeacherButton=findViewById(R.id.updateTeacherBtn);
        DeleteTeacherButton=findViewById(R.id.deleteTeacherBtn);
        try {
            Picasso.get().load(image).into(UpdateTeacherImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UpdateTeacherEmail.setText(email);
        UpdateTeacherName.setText(name);
        UpdateTeacherPost.setText(post);
        UpdateTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        UpdateTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=UpdateTeacherName.getText().toString();
                email=UpdateTeacherEmail.getText().toString();
                post=UpdateTeacherPost.getText().toString();
                
                checkValidation();
            }
        });
                DeleteTeacherButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteData();
                    }
                });


    }


    private void deleteData() {
        reference.child(category).child(uniquekey).removeValue()
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       Toast.makeText(UpdateTeacher.this, "Teacher Deleted Succesfully", Toast.LENGTH_SHORT).show();
                       Intent  intent=new Intent(UpdateTeacher.this,UpdateFaculty.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(intent);

                   }
               }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacher.this, "something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    private void checkValidation() {
        if(name.isEmpty()){
            UpdateTeacherName.setError("Empty");
            UpdateTeacherName.requestFocus();
        }else   if(post.isEmpty()){
            UpdateTeacherPost.setError("Empty");
            UpdateTeacherPost.requestFocus();
        }else   if(email.isEmpty()){
            UpdateTeacherEmail.setError("Empty");
            UpdateTeacherEmail.requestFocus();
        }
        else if(bitmap==null) 
        {
            UpdateData(image);
        }
        else
        {
             uploadImage();
        }
        




    }

    private void uploadImage() {
        //first we compress the image then we store

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimage=baos.toByteArray();

        final StorageReference filepath;
        filepath=storageReference.child("teacher").child(finalimage+"jpg");
        final UploadTask uploadTask=filepath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(UpdateTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    //if image upload succesfully we need to get the path of image
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    downalodurl=String.valueOf(uri);
                                    UpdateData(downalodurl);

                                }
                            });
                        }
                    });
                }
                else
                {
                  //  progressDialog.dismiss();
                    Toast.makeText(UpdateTeacher.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }
            }

        });

    }
    private void UpdateData(String s) {
        HashMap hp =new HashMap();
        hp.put("name",name);
        hp.put("email",email);
        hp.put("post",post);
        hp.put("image",s);



        reference.child(category).child(uniquekey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateTeacher.this, "Teacher Updated Succesfully", Toast.LENGTH_SHORT).show();
                Intent  intent=new Intent(UpdateTeacher.this,UpdateFaculty.class);
                //on back press same activity pe na reh
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacher.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void openGallery() {
        Intent pickImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==REQ && resultCode  == RESULT_OK){
            Uri uri=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        UpdateTeacherImage.setImageBitmap(bitmap);

    }

}