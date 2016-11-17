package bvu.edu.camapp;

import android.content.Context;
import android.os.Environment;
import android.util.JsonWriter;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by loc on 10/30/16.
 */

public class JsonWrite {

    public static void WriteToFile(Context context, Person newPerson){
        if(!isExternalStorageWritable()) {
            Toast.makeText(context, "No external storage or external device is locked", Toast.LENGTH_SHORT).show();
            return;
        }

        OutputStreamWriter outputStreamWriter = null;
        JsonWriter jsonWritter = null;
        ArrayList<Person> people = JsonRead.readFromFile(context); // if file doesn't exit, it will create one
        try {
            File file = new File(context.getExternalFilesDir(null), "out.json");
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            jsonWritter = new JsonWriter(outputStreamWriter);
            people.add(newPerson);
            jsonWritter.beginArray();
            for(Person person: people){
                writePerson(jsonWritter, person);
            }
            jsonWritter.endArray();
            jsonWritter.close();
            Toast.makeText(context, "saved!", Toast.LENGTH_SHORT).show();


        } catch (UnsupportedEncodingException e) {
            Toast.makeText(context, "Unsupported error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "File not found error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(context, "Error writing to file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private static void writePerson(JsonWriter writer, Person person) throws IOException {
        writer.beginObject();
        writer.name("id").value(person.getId());
        writer.name("lat").value(person.getLat());
        writer.name("lng").value(person.getLng());
        writer.name("img_url").value(person.getHeadstone());
        writer.endObject();
    }
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
