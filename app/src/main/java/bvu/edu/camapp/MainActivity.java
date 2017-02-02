package bvu.edu.camapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private List<Person> mPeople = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RecyclerView recyclerView_results = (RecyclerView)findViewById(R.id.results);
        recyclerView_results.setLayoutManager(new LinearLayoutManager(this));

        Button search_btn = (Button)findViewById(R.id.searchbtn);
        final EditText first_name = (EditText) findViewById(R.id.first_name);
        final EditText last_name = (EditText) findViewById(R.id.last_name);



        CemeteryAPI cemeteryService = CemeteryService.getCemeteryService();
        Call<ArrayList<Person>> call = cemeteryService.getBurials();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Connecting to server");
        dialog.show();
        final ResultAdapter resultAdapter = new ResultAdapter(new ArrayList<Person>());
        recyclerView_results.setAdapter(resultAdapter);
        call.enqueue(new Callback<ArrayList<Person>>() {
            @Override
            public void onResponse(Call<ArrayList<Person>> call, Response<ArrayList<Person>> response) {
                dialog.hide();
                dialog.cancel();
                mPeople.addAll(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Person>> call, Throwable t) {
                dialog.hide();
                dialog.cancel();
                Toast.makeText(getBaseContext(), "Error connecting", Toast.LENGTH_LONG).show();
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                List<Person> results = new ArrayList<Person>();
                String firstname = first_name.getText().toString().trim().toLowerCase();
                String lastname = last_name.getText().toString().trim().toLowerCase();
                if(firstname.length() > 0 & lastname.length() > 0){
                    for(Person p: mPeople) {
                        if(p.getFirstName().toLowerCase().contains(firstname)
                                & p.getLastName().toLowerCase().contains(lastname))
                           results.add(p);
                    }
                }
                else if(firstname.length() > 0) {
                    for (Person p : mPeople) {
                        if (p.getFirstName().toLowerCase().contains(firstname))
                            results.add(p);
                    }
                }
                else if(lastname.length() > 0) {
                    for (Person p : mPeople) {
                        if (p.getLastName().toLowerCase().contains(lastname))
                            results.add(p);
                    }
                }
                else{
                    Toast.makeText(v.getContext(), "You need to enter a name", Toast.LENGTH_LONG).show();
                }
                resultAdapter.setDataset(results);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.bulk_upload:
                if(checkUploadFile()){ // if upload file exists
                    multiUpload();
                }else{
                    Toast.makeText(getBaseContext(), "Upload file is not found", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void closeKeyboard(){
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean checkUploadFile(){
        if(!isExternalStorageWritable())
            return false;
        File  file = new File(this.getExternalFilesDir(null), "out.json");
        return file.exists();
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private void multiUpload(){
        // Read in the file and store every object in a list
        ArrayList<Person> people = JsonRead.readFromFile(this);
        Log.d("DEUBG", "" + people.size());
        for(Person person: people){
            Log.d("DEBUG", person.getHeadstone());
            File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), person.getHeadstone() + ".jpg");
            singleUpload(file, person.getLat(), person.getLng(), String.valueOf(person.getId()));
        }
    }
    private void singleUpload(File imagepathFile, String latval, String lngval, final String person_id){
        CemeteryAPI cemeteryService = CemeteryService.getCemeteryService();
        RequestBody lat = RequestBody.create(MediaType.parse("form-data"),latval);
        RequestBody lng = RequestBody.create(MediaType.parse("form-data"),lngval);
        RequestBody id = RequestBody.create(MediaType.parse("form-data"), person_id);
        RequestBody imageFile  = RequestBody.create(MediaType.parse("multipart/form-data"), imagepathFile);
        MultipartBody.Part image = MultipartBody.Part.createFormData("file",imagepathFile.getName(), imageFile);
        Call<ResponseBody> call = cemeteryService.updateBurial(id, lng, lat, image);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getBaseContext(), String.format("Uploaded id: " + person_id), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), String.format("Failed to upload id: " + person_id), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
