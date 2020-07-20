package com.example.tenakatauniversity.Utils;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.example.tenakatauniversity.Model.Student;

import java.util.List;

public class Util {

    public boolean isCountrykenya(Geocoder geocoder, Location location) {
        try {
            String country_name;
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                country_name = addresses.get(0).getCountryName();
                return "Kenya".equalsIgnoreCase(country_name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * this method computes the admissibility of a student by considering
     * the candidate properties as suggested by the imput
     *
     * @param student
     * @return an integer denoting the percentage admissibility of the student
     */
    public static int computeAdmissibility(Student student) {
        try {
            //Out of five inputs, two inputs are constant
            double unitRatio = 0.33;

            double genderInput = 0.0;
            double ageInput = 0.0;
            double maritalStatusInput = 0.0;
            //Considering four inputs
            //1. Gender
            //2. Age
            //3. IQ Score
            //4. Country
            //5. Marital Status (this is negligible according to the explanation)
            //Divide the three variable inputs into equal percentage ratio
            //each ratio is (1/3 * 100) = 33.33%

            //IQ Score of above 100 and Kenya Citizen is basic entry requirement
            if (student.getTestResults() <= 100 || !student.isCountryKenya()) {
                return 0;
            }

            //Gender
            if ("Female".equalsIgnoreCase(student.getGender())) {
                //Female gender gets full unit ratio
                genderInput = unitRatio * 100;
            } else {
                //male gets 56.5 less than full unit ratio
                genderInput = ((100 - 56.5) / 100) * 33.33;
            }

            //Age
            if (student.getAge() > 43) {
                //age above 43 years old gets full unit ration
                ageInput = unitRatio * 100;
            } else if (student.getAge() < 26) {
                //age less than 26 gets half admissibility of age greater than 43
                ageInput = unitRatio / 2 * 100;
            } else {
                //other age range gets normal full age admissibility ratio
                ageInput = unitRatio * 100;
            }

            //Marital Status
            //University does not consider marital status in it's selection
            if (student.getMaritalStatus().equalsIgnoreCase("Single") || student.getMaritalStatus().equalsIgnoreCase("Married")) {
                maritalStatusInput = unitRatio * 100;
            }

            System.out.println(unitRatio);
            System.out.println(genderInput);
            System.out.println(ageInput);
            System.out.println(maritalStatusInput);


            return (int) Math.round(genderInput + ageInput + maritalStatusInput);


        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

//    public static void main(String... args){
//        Student student = new Student();
//        student.setImage("".getBytes());
//        student.setTestResults(101);
//        student.setHeight((float) 71.0);
//        student.setAge(23);
//        student.setGender("Male");
//        student.setMaritalStatus("single");
//        student.setCountryKenya(true);
//        student.setName("Oluwaseyi Ayodele");
//        student.setLatitude("");
//        student.setLongitude("");
//        System.out.println("Student Admissibility: " + computeAdmissibility(student));
//    }
}
