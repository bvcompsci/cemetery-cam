package bvu.edu.camapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CameraActivity extends AppCompatActivity implements LocationListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private LocationManager locationManager;
    private Location lastKnownLocation;
    private String[] personInfo;
    private String person_img_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // LOCATION GPS SERVICE
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);

        personInfo = getIntent().getStringArrayExtra("PERSON");
        final Integer person_id = Integer.parseInt(personInfo[0]);
        person_img_name = personInfo[1].replaceAll(" ", "") + "_" + personInfo[0];

        Button btn = (Button) findViewById(R.id.upload);
        Button save_btn = (Button)findViewById(R.id.save_for_later);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getImageFile(person_img_name);
                singleUpload(getImageFile(person_img_name));
            }
        });

        save_btn.setOnClickListener(saveLaterListener);
    }

    View.OnClickListener saveLaterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = Integer.parseInt(personInfo[0]);
            String latval = String.valueOf(lastKnownLocation.getLatitude());
            String lngval = String.valueOf(lastKnownLocation.getLongitude());
            Person newPerson = new Person();
            newPerson.setId(id);
            newPerson.setHeadstone(person_img_name);
            newPerson.setLat(latval);
            newPerson.setLng(lngval);
            JsonWrite.WriteToFile(v.getContext(), newPerson);
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            ImageView holder = (ImageView) findViewById(R.id.imageHolder);
            holder.setImageBitmap(image);
            File file = createImageFile(person_img_name);
            ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,outputBytes);
            try {
                FileOutputStream output = new FileOutputStream(file);
                output.write(outputBytes.toByteArray());
                output.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void singleUpload(File imagepathFile){
        CemeteryAPI cemeteryService = CemeteryService.getCemeteryService();
        String latval = String.valueOf(lastKnownLocation.getLatitude());
        String lngval = String.valueOf(lastKnownLocation.getLongitude());
        RequestBody lat = RequestBody.create(MediaType.parse("form-data"),latval);
        RequestBody lng = RequestBody.create(MediaType.parse("form-data"),lngval);
        RequestBody id = RequestBody.create(MediaType.parse("form-data"), personInfo[0]);
        RequestBody imageFile  = RequestBody.create(MediaType.parse("multipart/form-data"), imagepathFile);
        MultipartBody.Part image = MultipartBody.Part.createFormData("file",imagepathFile.getName(), imageFile);
        Call<ResponseBody> call = cemeteryService.updateBurial(id, lng, lat, image);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading ...");
        dialog.show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.hide();
                Toast.makeText(getBaseContext(), "Uploaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.hide();
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File createImageFile(String person_name){
        // Create an image file name
        String imageFileName = person_name;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName + ".jpg");
        if(image.exists()){
            try {
                image.delete();
                image.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }
    private File getImageFile(String person_name){
        String imageFileName = person_name;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, imageFileName + ".jpg");
    }

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLocation = location;
        TextView lngHolder = (TextView) findViewById(R.id.lng);
        TextView latHolder = (TextView) findViewById(R.id.lat);
        lngHolder.setText(String.valueOf(location.getLongitude()));
        latHolder.setText(String.valueOf(location.getLatitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "GPS is on", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        startActivity(intent);
        Toast.makeText(this, "GPS is turned off", Toast.LENGTH_SHORT).show();
    }
}
