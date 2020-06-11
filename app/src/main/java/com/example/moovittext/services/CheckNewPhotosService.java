package com.example.moovittext.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.moovittext.R;
import com.example.moovittext.activities.MainActivity;
import com.example.moovittext.jsonmodels.FlickerMain;
import com.example.moovittext.retrofit.FlickerRetrofitAPI;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CheckNewPhotosService extends Service {

    public static final String serviceName = "com.example.moovittext.service.CheckNewPhotosService";
    private static final String TAG = "CheckNewPhotosService";
    private String searchWord;
    private Context context;
    private FlickerMain lastPhotos;
    Handler handler = new Handler();
    private Runnable mRunnable;

    public CheckNewPhotosService() {
        context = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"YAY!");

        return null;

    }

    @Override
    public void onCreate() {

        Log.d(TAG,"YAY!");

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Bundle extras = intent.getExtras();
        Gson gson = new Gson();
        String test = (String) extras.get("lastBatchOfPhotos");
        lastPhotos = gson.fromJson((String) extras.get("lastBatchOfPhotos"),FlickerMain.class);
        searchWord = intent.getStringExtra("currentSearchTerm");
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"YAY!");


                //Start retrofit
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.flickr.com/services/rest/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                FlickerRetrofitAPI flickerRetrofitAPI = retrofit.create(FlickerRetrofitAPI.class);
                flickerRetrofitAPI.getPhotosWithSearchOnPage(1,searchWord).enqueue(new Callback<FlickerMain>() {
                    @Override
                    public void onResponse(Call<FlickerMain> call, Response<FlickerMain> response) {
                        int notification_id = 1;
                        notification_id++;
                        Intent intent = new Intent(CheckNewPhotosService.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(CheckNewPhotosService.this, 0, intent, 0);

                        if(hasNewPhotos(response.body(),lastPhotos)){
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(CheckNewPhotosService.this, "test")
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setContentTitle("New Photos")
                                    .setContentText("You have new photos!")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    // Set the intent that will fire when the user taps the notification
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                            notificationManager.notify(notification_id, builder.build());
                        }
                    }

                    @Override
                    public void onFailure(Call<FlickerMain> call, Throwable t) {
                        Log.e(TAG,t.getMessage());
                    }
                });
                handler.postDelayed(this,  1000 * 60 * 15 ); //now is every 15 minutes
            }
        };
        handler.postDelayed(mRunnable, 0);

        return START_STICKY;
    }

    private boolean hasNewPhotos(FlickerMain body, FlickerMain lastPhotos) {
        return !body.getPhotos().getPhoto().equals(lastPhotos.getPhotos().getPhoto());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("test", name, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onDestroy() {
        handler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
}

