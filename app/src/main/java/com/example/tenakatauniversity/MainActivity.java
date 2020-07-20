package com.example.tenakatauniversity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.tenakatauniversity.Model.MyAppDatabase;
import com.example.tenakatauniversity.Model.MyDao;

import android.os.*;

import com.example.tenakatauniversity.Model.Student;
import com.example.tenakatauniversity.Utils.DataConverter;
import com.example.tenakatauniversity.Utils.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private EditText name;
    private EditText age;
    private EditText gender;
    private EditText maritalStatus;
    private EditText height;
    private EditText textResult;
    DatabaseReference databaseReference;
    ImageView mImageView;
    Bitmap mBitmap, bmp, scaledbmp;

    int pagewidth = 1200;

    public static final int CAMERA_INTENT = 51;
    public MyDao mMyDao;

    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    private boolean mIsCountryKenya;
    private String mMCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitudeTextView = (TextView) findViewById((R.id.latitude_textview));
        mLongitudeTextView = (TextView) findViewById((R.id.longitude_textview));

        handleUserLocation();

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        gender = (EditText) findViewById(R.id.gender);
        maritalStatus = (EditText) findViewById(R.id.maritalStatus);
        height = (EditText) findViewById(R.id.height);
        textResult = (EditText) findViewById(R.id.testsResult);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.tenakata);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 1200, 518, false);

        mImageView = findViewById(R.id.userImage);
        mBitmap = null;

        mMyDao = MyAppDatabase.getDBInstance(this).mMyDao();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Student");


        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

    }

    void handleUserLocation() {
        if (isLocationEnabled()) {
            MainActivityPermissionsDispatcher.startLocationUpdateWithPermissionCheck(this);
            MainActivityPermissionsDispatcher.getLastLocationWithPermissionCheck(this);
        } else {
            Toast.makeText(this, "Location is not enabled on device, please enable location in order to use the application", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(this::finish, 3000);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handleUserLocation();
    }


    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                location.getLatitude() + "," +
                location.getLongitude();

        mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        mLongitudeTextView.setText(String.valueOf(location.getLongitude()));

        Log.e(TAG, msg);

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        String country_name = null;
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(getApplicationContext());
        for (String provider : lm.getAllProviders()) {
            @SuppressWarnings("ResourceType") Location mlocation = lm.getLastKnownLocation(provider);
            if (mlocation != null) {
                mMCountry = getCountryName(this, location.getLatitude(), location.getLongitude());
                Util util = new Util();
                mIsCountryKenya = util.isCountrykenya(geocoder, location);
            }
        }
        //Toast.makeText(getApplicationContext(), country_name, Toast.LENGTH_LONG).show();
    }

    public static String getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                String countryName = addresses.get(0).getCountryName();
                Log.e(TAG, "Country Name " + countryName);
                return countryName;
            }
            return null;
        } catch (IOException ignored) {
            return "";
        }
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void getLastLocation() {
        FusedLocationProviderClient locationProviderClient = getFusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        locationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        onLocationChanged(location);
                    }
                }).addOnFailureListener(e -> e.printStackTrace());
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void startLocationUpdate() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        // Create Location Settings Object Using Location Request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        //Check Whether Location Settings are satisfied
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        }, Looper.myLooper());
    }


    public void takepicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_INTENT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_INTENT:
                if (resultCode == Activity.RESULT_OK) {
                    mBitmap = (Bitmap) data.getExtras().get("data");
                    if (mBitmap != null) {
                        mImageView.setImageBitmap(mBitmap);
                    } else {
                        Toast.makeText(this, "Bitmap is Null", Toast.LENGTH_SHORT).show();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Action Canceled", Toast.LENGTH_SHORT).show();
                }

        }

    }


    public void predict(View view) {

        savetolocal();

        savetoremote();
        finish();
        Toast.makeText(this, "Save to Local and Realtime Database", Toast.LENGTH_SHORT).show();

        showDetails(view);


        savetopdf();
        Toast.makeText(this, "Save to PDF, Check Storage", Toast.LENGTH_LONG).show();
    }

    //Save to offline Storage
    public void savetolocal() {

        if (name.getText().toString().isEmpty() || age.getText().toString().isEmpty() || maritalStatus.getText().toString().isEmpty()
                || height.getText().toString().isEmpty() || mLatitudeTextView.getText().toString().isEmpty() || mLongitudeTextView.getText().toString().isEmpty()
                || textResult.getText().toString().isEmpty()) {
            Toast.makeText(this, "Student Data Missing", Toast.LENGTH_SHORT).show();
        } else {

            String mname = name.getText().toString().trim();
            int mage = Integer.parseInt(age.getText().toString().trim());
            String mgender = gender.getText().toString().trim();
            String mmarital = maritalStatus.getText().toString().trim();
            float mheight = Float.parseFloat(height.getText().toString().trim());
            String mmLongitudeTextView = mLongitudeTextView.getText().toString().trim();
            String mmLatitudeTextView = mLatitudeTextView.getText().toString().trim();
            int mresult = Integer.parseInt(textResult.getText().toString().trim());

            Student student = new Student();
            student.setName(mname);
            student.setAge(mage);
            student.setGender(mgender);
            student.setMaritalStatus(mmarital);
            student.setHeight(mheight);
            student.setLatitude(mmLatitudeTextView);
            student.setLongitude(mmLongitudeTextView);
            student.setTestResults(mresult);
            student.setImage(DataConverter.convertImage2ByteArray(mBitmap));
            student.setCountryKenya(mIsCountryKenya);
            student.setCountry(mMCountry);
            student.setAdmissibility(Util.computeAdmissibility(student));

            long studentId = mMyDao.insertStudent(student);

            //Toast.makeText(this, "Save to Local Database", Toast.LENGTH_SHORT).show();

        }

    }

    //Save to Online Storage
    public void savetoremote() {

        String mname = name.getText().toString().trim();
        int mage = Integer.parseInt(age.getText().toString().trim());
        String mgender = gender.getText().toString().trim();
        String mmarital = maritalStatus.getText().toString().trim();
        float mheight = Float.parseFloat(height.getText().toString().trim());
        String mmLongitudeTextView = mLongitudeTextView.getText().toString().trim();
        String mmLatitudeTextView = mLatitudeTextView.getText().toString().trim();
        int mresult = Integer.parseInt(textResult.getText().toString().trim());


        Student student = new Student();
        student.setName(mname);
        student.setAge(mage);
        student.setGender(mgender);
        student.setMaritalStatus(mmarital);
        student.setHeight(mheight);
        student.setLatitude(mmLatitudeTextView);
        student.setLongitude(mmLongitudeTextView);
        student.setTestResults(mresult);
        student.setCountry(mMCountry);
        databaseReference.push().setValue(student);
        //Toast.makeText(this, "Save to Realtime Database", Toast.LENGTH_SHORT).show();

    }


    //Save as pdf
    public void savetopdf() {

        Student student = new Student();

        String mname = name.getText().toString();
        int mage = Integer.parseInt(age.getText().toString());
        String mgender = gender.getText().toString();
        String mmarital = maritalStatus.getText().toString();
        String mheight = height.getText().toString();
        String mCountry = mMCountry;
        String madmissibility = String.valueOf(student.getAdmissibility());
        int mresult = Integer.parseInt(textResult.getText().toString());

        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint = new Paint();
        Paint tittlePaint = new Paint();

        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaledbmp, 0, 0, myPaint);
        tittlePaint.setTextAlign(Paint.Align.CENTER);
        tittlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        tittlePaint.setTextSize(70);
        canvas.drawText("Tenakata University Admission", pagewidth / 2, 250, tittlePaint);


        canvas.drawBitmap(mBitmap, 50, 350, myPaint);
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        myPaint.setTextSize(40f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Student Name: " + mname, 20, 590, myPaint);
        canvas.drawText("Student Age: " + mage, 20, 640, myPaint);
        canvas.drawText("Student Gender: " + mgender, 20, 690, myPaint);
        canvas.drawText("Student Marital Status: " + mmarital, 20, 740, myPaint);
        canvas.drawText("Student Height: " + mheight, 20, 790, myPaint);
        canvas.drawText("Student Country: " + mCountry, 20, 840, myPaint);
        canvas.drawText("Student Result: " + mresult, 20, 890, myPaint);
        canvas.drawText("Admissibility: " + madmissibility, 20, 940, myPaint);


        myPdfDocument.finishPage(myPage);

        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/Tenakata_Admission.pdf";
        File myFile = new File(myFilePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
        } catch (Exception e) {
            e.printStackTrace();
        }

        myPdfDocument.close();
    }

    public void showDetails(View view) {
        Intent intent = new Intent(this, StudentDetails.class);
        startActivity(intent);
    }

}

