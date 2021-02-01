package com.example.collegeapp.faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.collegeapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateFaculty extends AppCompatActivity {

    FloatingActionButton FAB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);
        FAB=findViewById(R.id.fab1);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateFaculty.this, "hi", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateFaculty.this,AddTeacher.class));

            }
        });

    }
}