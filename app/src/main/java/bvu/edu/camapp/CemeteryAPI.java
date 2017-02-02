package bvu.edu.camapp;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by loc on 10/2/16.
 */

public interface CemeteryAPI {
    @GET("api/search")
    Call<ArrayList<Person>> getBurials();

    @Multipart
    @POST("api/update-burial")
    Call<ResponseBody> updateBurial(@Part("id") RequestBody id, @Part("lng") RequestBody lng, @Part("lat") RequestBody lat,
                                    @Part MultipartBody.Part file);
}
