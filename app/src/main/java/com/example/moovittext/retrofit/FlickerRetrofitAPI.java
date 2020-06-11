package com.example.moovittext.retrofit;

import com.example.moovittext.jsonmodels.FlickerMain;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickerRetrofitAPI {


    @GET("?method=flickr.photos.getRecent&extras=url_s&api_key=aabca25d8cd75f676d3a74a72dcebf21&format=json&nojsoncallback=1&page=")
    Call<FlickerMain> getPhotosOnPage(@Query("page") int pageNumber);
    @GET("?method=flickr.photos.getRecent&extras=url_s&api_key=aabca25d8cd75f676d3a74a72dcebf21&format=json&nojsoncallback=1&page=&text=")
    Call<FlickerMain> getPhotosWithSearchOnPage(@Query("page") int pageNumber,
                                                @Query("text") String searchTerm);

}
