package bvu.edu.camapp;

import android.content.Context;
import android.os.Environment;
import android.util.JsonReader;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by loc on 10/30/16.
 */

public class JsonRead {

    public static ArrayList<Person> readFromFile(Context context) {
        if (!isExternalStorageWritable()) {
            Toast.makeText(context, "Storage is not readable or it is locked", Toast.LENGTH_SHORT).show();
            return null;
        }
        ArrayList<Person> people = new ArrayList<>();
        InputStreamReader inputStreamReader = null;
        File file = new File(context.getExternalFilesDir(null), "out.json");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            JsonReader jsonReader = new JsonReader(inputStreamReader);
            jsonReader.beginArray();
            while(jsonReader.hasNext()){
                people.add(getPerson(jsonReader));
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(context, "Encoding error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return people;
    }
    public static void deleteFile(Context context){
        File file = new File(context.getExternalFilesDir(null), "out.json");
        if(file.exists()){
            file.delete();
        }
    }
    private static Person getPerson(JsonReader reader) throws IOException {
        Person person = new Person();
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if(name.equals("id")){
                person.setId(reader.nextInt());
            }
            else if(name.equals("lat")){
                person.setLat(reader.nextString());
            }
            else if(name.equals("lng")){
                person.setLng(reader.nextString());
            }
            else if(name.equals("img_url")){
                person.setHeadstone(reader.nextString());
            }
        }
        reader.endObject();
        return person;
    }
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
