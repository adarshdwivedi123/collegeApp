package com.example.collegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UploadPdf extends AppCompatActivity {
    private CardView addPdf;
    private EditText pdfTitle;
    private Button uploadpdfBtn;
    private  final int REQ=1;
    private String pdfname,title;

    //convert uri into bitmap
    private Uri pdfData;


    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String downalodurl="";
    private ProgressDialog progressDialog;
    private TextView pdfTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        addPdf = findViewById(R.id.addPdf);
        pdfTitle = findViewById(R.id.pdf_Title);
        uploadpdfBtn = findViewById(R.id.upload_pdfbtn);
        pdfTextView=findViewById(R.id.pdfTextView);

        progressDialog = new ProgressDialog(this);
        uploadpdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=pdfTitle.getText().toString();
                if(title.isEmpty())
                {
                    pdfTitle.setError("Empty");
                    pdfTitle.requestFocus();
                }
                else if(pdfData==null){
                    Toast.makeText(UploadPdf.this, "Please Upload pdf", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    uploadPdf();
                }
            }
        });


        addPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


    }

    private void uploadPdf() {
        progressDialog.setTitle("please wait");
        progressDialog.setMessage("uploading pdf");
        progressDialog.show();
        StorageReference reference=storageReference.child("pdf/"+pdfname+":"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri>  uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri uri=uriTask.getResult();
                        uploadData(String.valueOf(uri));



                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UploadPdf.this, "some thing went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String valueOf) {
        String uniquekey=databaseReference.child("pdf").push().getKey();
        HashMap data =new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",downalodurl);

       databaseReference.child("pdf").child(uniquekey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               progressDialog.dismiss();
               Toast.makeText(UploadPdf.this, "pdf Uploaded Succesfukky", Toast.LENGTH_SHORT).show();
               pdfTitle.setText("");

           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               progressDialog.dismiss();
               Toast.makeText(UploadPdf.this, "Fail to Upload Pdf", Toast.LENGTH_SHORT).show();
           }
       });

    }


    private void openGallery() {

                Intent intent=new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select pdf file"),REQ);
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode ==REQ && resultCode  == RESULT_OK){
              pdfData=data.getData();
                if(pdfData.toString().startsWith("content://"))
                {

                    Cursor cursor=null;
                    cursor=UploadPdf.this.getContentResolver().query(pdfData,null,null,null,null);
                    if(cursor !=null && cursor.moveToFirst()) try {
                        {
                            pdfname=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(pdfData.toString().startsWith("file://"))
                {
                    pdfname=new File(pdfData.toString()).getName();
                }
                pdfTextView.setText(pdfname);


            }

        }

    }
