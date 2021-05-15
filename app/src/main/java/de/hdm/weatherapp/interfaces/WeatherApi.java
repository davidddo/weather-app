package de.hdm.weatherapp.interfaces;



import de.hdm.weatherapp.network.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("forecast?lang=de&units=metric")
    Call<ApiResponse> getSearchResult(@Query("id") String id, @Query("APPID") String APPID);
}
