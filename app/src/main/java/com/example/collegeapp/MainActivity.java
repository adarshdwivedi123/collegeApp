package com.example.collegeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.collegeapp.faculty.UpdateFaculty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView uploadNotice,AddgalleryImage,AddEbook,AddFaculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadNotice=findViewById(R.id.addNotice);
        AddgalleryImage=findViewById(R.id.addGalleryImage);
        AddEbook=findViewById(R.id.addEbook);
        AddFaculty=findViewById(R.id.faculty);

        uploadNotice.setOnClickListener(this);
        AddgalleryImage.setOnClickListener(this);
        AddEbook.setOnClickListener(this);
        AddFaculty.setOnClickListener(this);

    }


            @Override
            public void onClick(View v) {
                Intent intent;
                switch(v.getId())   {
                    case R.id.addNotice:
                        intent=new Intent(MainActivity.this,UploadNotice.class);
                        startActivity(intent);
                        break;
                    case R.id.addGalleryImage:
                        intent=new Intent(MainActivity.this,UploadImage.class);
                        startActivity(intent);
                        break;
                    case R.id.addEbook:
                        intent=new Intent(MainActivity.this,UploadPdf.class);
                        startActivity(intent);
                        break;

                    case R.id.faculty:
                        intent=new Intent(MainActivity.this, UpdateFaculty.class);
                        startActivity(intent);
                        break;

                }
        }

}