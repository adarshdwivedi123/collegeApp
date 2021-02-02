package com.example.collegeapp.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.collegeapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {

    FloatingActionButton FAB;
    private RecyclerView CsDepartment,PhysicsDepartment,MechanicalDepartment,ChemistryDepartment;
    private LinearLayout CsNoData,MechanicalNodata,PhysicsNodata,ChemistryNodata;
    private List<TeacherData>list1,list2,list3,list4;
    private DatabaseReference reference,Dbref;
    private TeacherAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);
        FAB=findViewById(R.id.fab1);
        ChemistryDepartment=findViewById(R.id.chemistryDepartment);
        MechanicalDepartment=findViewById(R.id.mechDepartment);
        PhysicsDepartment=findViewById(R.id.physicsDepartment);
        CsDepartment=findViewById(R.id.csDepartment);

        CsNoData=findViewById(R.id.csNoData);
        MechanicalNodata=findViewById(R.id.mechNoData);
        PhysicsNodata=findViewById(R.id.physicsNoData);
        ChemistryNodata=findViewById(R.id.chemistryNoData);

        reference= FirebaseDatabase.getInstance().getReference().child("teacher");
        CsDepartment();
        MechanicalDepartment();
        PhysicsDepartment();
        ChemistryDepartment();

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateFaculty.this, "hi", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateFaculty.this,AddTeacher.class));

            }
        });

    }




    private void CsDepartment() {
        Dbref=reference.child("Computer Science");
        Dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1=new ArrayList<>();
                if(!snapshot.exists())
                {
                    CsNoData.setVisibility(View.VISIBLE);
                    CsDepartment.setVisibility(View.GONE);


                }
                else
                {
                    CsNoData.setVisibility(View.GONE);
                    CsDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot Snapshot1: snapshot.getChildren()){
                        TeacherData data=Snapshot1.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    CsDepartment.setHasFixedSize(true);
                    CsDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list1,UpdateFaculty.this);
                    CsDepartment.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void MechanicalDepartment() {
        Dbref=reference.child("Mechanical");
        Dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2=new ArrayList<>();
                if(!snapshot.exists())
                {
                    MechanicalNodata.setVisibility(View.VISIBLE);
                    MechanicalDepartment.setVisibility(View.GONE);


                }
                else
                {
                    MechanicalNodata.setVisibility(View.GONE);
                    MechanicalDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot Snapshot1: snapshot.getChildren()){
                        TeacherData data=Snapshot1.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    MechanicalDepartment.setHasFixedSize(true);
                    MechanicalDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list2,UpdateFaculty.this);
                    MechanicalDepartment.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void PhysicsDepartment() {
        Dbref=reference.child("Physics");
        Dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3=new ArrayList<>();
                if(!snapshot.exists())
                {
                    PhysicsNodata.setVisibility(View.VISIBLE);
                    PhysicsDepartment.setVisibility(View.GONE);


                }
                else
                {
                    PhysicsNodata.setVisibility(View.GONE);
                    PhysicsDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot Snapshot1: snapshot.getChildren()){
                        TeacherData data=Snapshot1.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    PhysicsDepartment.setHasFixedSize(true);
                    PhysicsDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list3,UpdateFaculty.this);
                    PhysicsDepartment.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void ChemistryDepartment() {
        Dbref=reference.child("Chemistry");
        Dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4=new ArrayList<>();
                if(!snapshot.exists())
                {
                    ChemistryNodata.setVisibility(View.VISIBLE);
                    ChemistryDepartment.setVisibility(View.GONE);


                }
                else
                {
                    ChemistryNodata.setVisibility(View.GONE);
                    ChemistryDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot Snapshot1: snapshot.getChildren()){
                        TeacherData data=Snapshot1.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    ChemistryDepartment.setHasFixedSize(true);
                    ChemistryDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list4,UpdateFaculty.this);
                    ChemistryDepartment.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}