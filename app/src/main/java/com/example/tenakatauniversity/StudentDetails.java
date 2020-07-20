package com.example.tenakatauniversity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.print.PrintDocumentAdapter;


import com.example.tenakatauniversity.Adapters.StudentDetailsRecyclerAdapter;
import com.example.tenakatauniversity.Model.MyAppDatabase;
import com.example.tenakatauniversity.Model.MyDao;
import com.example.tenakatauniversity.Model.Student;

import java.util.List;


public class StudentDetails extends AppCompatActivity {

    PrintDocumentAdapter adapter;
    private List<Student> students;
    public MyDao mMyDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);


        mMyDao = MyAppDatabase.getDBInstance(this).mMyDao();

        StudentDetailsRecyclerAdapter studentDetailsRecyclerAdapter = new StudentDetailsRecyclerAdapter(mMyDao.getAllStudent());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(studentDetailsRecyclerAdapter);


    }

}
