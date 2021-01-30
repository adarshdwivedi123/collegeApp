package com.example.collegeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView uploadNotice,AddgalleryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadNotice=findViewById(R.id.addNotice);
        AddgalleryImage=findViewById(R.id.addGalleryImage);

        uploadNotice.setOnClickListener(this);
        AddgalleryImage.setOnClickListener(this);


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
                }
        }

}