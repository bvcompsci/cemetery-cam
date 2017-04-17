package bvu.edu.camapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by loc on 10/3/16.
 */

public class CemeteryService {
    public static final String BASE_URL = "http://cemetery-map.herokuapp.com";

    public static CemeteryAPI getCemeteryService() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(CemeteryAPI.class);
    }
}
