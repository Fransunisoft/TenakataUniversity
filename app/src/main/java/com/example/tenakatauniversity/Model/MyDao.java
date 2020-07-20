package com.example.tenakatauniversity.Model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDao {

    @Query("Select * from Students ORDER BY admissibility DESC")
    List<Student> getAllStudent();

    @Insert
    long insertStudent(Student student);



    }



