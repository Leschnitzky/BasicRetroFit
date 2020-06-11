package com.example.moovittext.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.moovittext.adapters.EndlessRecyclerViewScrollListener;
import com.example.moovittext.adapters.MyAdapter;
import com.example.moovittext.R;
import com.example.moovittext.jsonmodels.FlickerMain;
import com.example.moovittext.jsonmodels.FlickerPhoto;
import com.example.moovittext.retrofit.FlickerRetrofitAPI;
import com.example.moovittext.services.CheckNewPhotosService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private Button mButton;
    private Button mStopServiceButton;
    private Intent serviceIntent = null;
    private EditText mEditText;
    private LinearLayoutManager layoutManager;
    private MyAdapter mAdapter;
    private FlickerRetrofitAPI flickerRetrofitAPI;
    private EndlessRecyclerViewScrollListener scrollListener;
    private List<FlickerPhoto> myDataset;
    private String currentSearchTerm = "";
    private FlickerMain lastFlickerPage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Recycler View Section
        RecyclerView recyclerView = findViewById(R.id.photo_recyclerview);
        int numberOfColumns = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MyAdapter(this, myDataset);
        recyclerView.setAdapter(mAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://api.flickr.com/services/rest/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
        flickerRetrofitAPI = retrofit.create(FlickerRetrofitAPI.class);

        flickerRetrofitAPI.getPhotosOnPage(1).enqueue(new Callback<FlickerMain>() {
            @Override
            public void onResponse(Call<FlickerMain> call, Response<FlickerMain> response) {
                myDataset = response.body().getPhotos().getPhoto();
                mAdapter.setData(myDataset);
            }

            @Override
            public void onFailure(Call<FlickerMain> call, Throwable t) {
                Log.e(TAG,t.getMessage());
            }
        });

        mEditText = findViewById(R.id.edit_text);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mEditText != null || mEditText.getText().toString().isEmpty()){
                    Context context = getApplicationContext();
                    SharedPreferences.Editor editor = context.getSharedPreferences(
                            getString(R.string.current_search_term), Context.MODE_PRIVATE).edit();
                    editor.putString(getResources().getString(R.string.current_search_term),mEditText.getText().toString());
                    editor.apply();
                    currentSearchTerm = mEditText.getText().toString();
                    flickerRetrofitAPI.getPhotosWithSearchOnPage(1,currentSearchTerm).enqueue(new Callback<FlickerMain>() {
                        @Override
                        public void onResponse(Call<FlickerMain> call, Response<FlickerMain> response) {
                            if(serviceIntent != null) {
                                stopService(serviceIntent);
                            }
                            serviceIntent = new Intent(context,CheckNewPhotosService.class);
                            Bundle bundle = new Bundle();
                            Gson gson = new Gson();
                            bundle.putString("lastBatchOfPhotos", gson.toJson(response.body()));
                            serviceIntent.putExtras(bundle);
                            serviceIntent.putExtra("currentSearchTerm",currentSearchTerm);
                            startService(serviceIntent);
                            myDataset = response.body().getPhotos().getPhoto();
                            mAdapter.setData(myDataset);
                        }

                        @Override
                        public void onFailure(Call<FlickerMain> call, Throwable t) {
                            Log.e(TAG,t.getMessage());
                        }
                    });
                }
            }
        });
        mStopServiceButton = findViewById(R.id.toggle_polling);
        mStopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serviceIntent != null) {
                    stopService(serviceIntent);
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.current_search_term), Context.MODE_PRIVATE);
        currentSearchTerm = sharedPref.getString(getResources().getString(R.string.current_search_term),"");
    }

    public void loadNextDataFromApi(int offset) {
        Log.d(TAG,"NEED NEW PHOTOS");
        //Check if current searchterm is not null, if it is, get data from other pages with the current searchTerm

        if(currentSearchTerm.isEmpty()){
            flickerRetrofitAPI.getPhotosOnPage(offset+1).enqueue(new Callback<FlickerMain>() {
                @Override
                public void onResponse(Call<FlickerMain> call, Response<FlickerMain> response) {
                    myDataset.addAll(response.body().getPhotos().getPhoto());
                    mAdapter.setData(myDataset);
                    Log.d(TAG,"ADAPTER UPDATED!");

                }

                @Override
                public void onFailure(Call<FlickerMain> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                }
            });
        } else {
            flickerRetrofitAPI.getPhotosWithSearchOnPage(offset+1,currentSearchTerm).enqueue(new Callback<FlickerMain>() {
                @Override
                public void onResponse(Call<FlickerMain> call, Response<FlickerMain> response) {
                    myDataset.addAll(response.body().getPhotos().getPhoto());
                    mAdapter.setData(myDataset);
                    Log.d(TAG,"ADAPTER UPDATED!");

                }

                @Override
                public void onFailure(Call<FlickerMain> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                }
            });
        }

    }

}
