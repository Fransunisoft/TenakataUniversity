package com.example.tenakatauniversity.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenakatauniversity.Model.Student;
import com.example.tenakatauniversity.R;
import com.example.tenakatauniversity.Utils.DataConverter;

import java.util.List;


public class StudentDetailsRecyclerAdapter extends RecyclerView.Adapter<StudentDetailsRecyclerAdapter.StudentDetailHolder> {


    private List<Student> data;

    public StudentDetailsRecyclerAdapter(List<Student> students) {
        data = students;
    }

    @NonNull
    @Override
    public StudentDetailsRecyclerAdapter.StudentDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new StudentDetailHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StudentDetailHolder holder, int position) {
        Student student = data.get(position);
        holder.name.setText(String.format("Name: %s", student.getName()));
        holder.age.setText(String.format("Age: %d", student.getAge()));
        holder.maritalStatus.setText(String.format("Marital Status: %s", student.getMaritalStatus()));
        holder.height.setText(String.format("Height: %s", student.getHeight()));
        holder.textResult.setText(String.format("IQ Test Result: %d", student.getTestResults()));
        holder.country.setText(String.format("Country: %s", student.getCountry()));
        holder.admissibility.setText(String.format("Admissibity Score: %d", student.getAdmissibility()));

        if (student.getAdmissibility() <= 50)
            holder.textAdmissibilityStatus.setText(String.format("Admissibility: %s", "Not Admissible"));
        else if (student.getAdmissibility() > 50 && student.getAdmissibility() <= 70)
            holder.textAdmissibilityStatus.setText(String.format("Admissibility: %s", "Slightly Admissible"));
        else
            holder.textAdmissibilityStatus.setText(String.format("Admissibility: %s", "Highly Admissible"));

        holder.mImageView.setImageBitmap(DataConverter.convertByteArray2Image(student.getImage()));

    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    class StudentDetailHolder extends RecyclerView.ViewHolder {

        private TextView name, age, maritalStatus, height, country, textResult, admissibility, textAdmissibilityStatus;
        ImageView mImageView;

        public StudentDetailHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.details_name);
            age = itemView.findViewById(R.id.details_age);
            maritalStatus = itemView.findViewById(R.id.details_marital);
            height = itemView.findViewById(R.id.details_height);
            country = itemView.findViewById(R.id.details_country);
            textResult = itemView.findViewById(R.id.details_testsResult);
            admissibility = itemView.findViewById(R.id.details_admissibility);
            textAdmissibilityStatus = itemView.findViewById(R.id.status_admissibility);

            mImageView = itemView.findViewById(R.id.details_image);

        }
    }


}
