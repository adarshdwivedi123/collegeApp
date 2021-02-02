package com.example.collegeapp.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.collegeapp.NoticeData;
import com.example.collegeapp.R;
import com.example.collegeapp.UploadNotice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTeacher extends AppCompatActivity {
    private ImageView AddTeacherImage;
    private EditText AddTeacherName,AddTeacherEmail,AddTeacherPost;
    private Spinner  AddTeacherCategory;
    private Button AddTeacherButton;
    private final int REQ=1;
    private Bitmap  bitmap=null;
    private String category;
    private String name,email,post,downloadUrl= "";
    private ProgressDialog progressDialog;
    private StorageReference storageReference ;
    private DatabaseReference reference,dbRef;
    private String downalodurl="";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_teacher);
        progressDialog=new ProgressDialog(this);
        AddTeacherImage=findViewById(R.id.addTeacherImage);
        AddTeacherEmail=findViewById(R.id.addTeacherEmail);
        AddTeacherName=findViewById(R.id.addTeacherName);
        AddTeacherPost=findViewById(R.id.addTeacherPost);
        AddTeacherCategory=findViewById(R.id.addTeacherCategory);
        AddTeacherButton=findViewById(R.id.addTeachereBtn);
        reference= FirebaseDatabase.getInstance().getReference().child("teacher");
        storageReference= FirebaseStorage.getInstance().getReference();



        //spinner added
        String [] items =new String[]{"Select Category","Computer Science","Mechanical","Physics","Chemistry"};
        AddTeacherCategory.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items));
        AddTeacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category =AddTeacherCategory.getSelectedItem().toString();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AddTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();
            }
        });
        AddTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddTeacher.this, "hi2", Toast.LENGTH_SHORT).show();
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        name=AddTeacherName.getText().toString();
        email=AddTeacherEmail.getText().toString();
        post=AddTeacherPost.getText().toString();
        if(name.isEmpty())
        {
            AddTeacherName.setError("Empty");
            AddTeacherName.requestFocus();

        }
        else if(email.isEmpty())
        {
            AddTeacherEmail.setError("Empty");
            AddTeacherName.requestFocus();

        }
        else if(post.isEmpty())
        {
            AddTeacherPost.setError("Empty");
            AddTeacherPost.requestFocus();

        }
        else if(category.equals("Select Category "))
        {

            Toast.makeText(this, "Please Provide Teacher Category", Toast.LENGTH_SHORT).show();
        }
        else if(bitmap ==null)
        {
            progressDialog.setMessage("Uploading");
            progressDialog.show();
                        insertData();
        }
        else
        {
           progressDialog.setMessage("Uploading");
            progressDialog.show();
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
        uploadTask.addOnCompleteListener(AddTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                  insertData();

                                }
                            });
                        }
                    });
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(AddTeacher.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }
            }

        });

    }

    private void insertData() {
        dbRef=reference.child(category);
        final String uniquekey= dbRef.push().getKey();


        TeacherData teacherData=new TeacherData(name,email,post,downalodurl,uniquekey);
        dbRef.child(uniquekey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();

                Toast.makeText(AddTeacher.this, "Teacher Added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddTeacher.this, "something went wrong", Toast.LENGTH_SHORT).show();
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
        AddTeacherImage.setImageBitmap(bitmap);

    }


}