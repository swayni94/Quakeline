package com.example.quakeline.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.common.RestApi.IService;
import com.example.common.RestApi.Model.ResponseModel;
import com.example.common.RestApi.Model.Result;
import com.example.common.RestApi.RestServise;
import com.example.common.RestApi.db.AppDatabase;
import com.example.common.RestApi.db.QuakeDao;
import com.example.common.RestApi.db.entity.Quake;
import com.example.common.helper.CurrentLocationHelper;
import com.example.quakeline.R;
import com.example.quakeline.ViewPage.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuakelineMainService extends JobIntentService{
    private static final int JOB_ID = new Random().nextInt(9999-1000);
    private static final String TAG = JobService.class.getSimpleName();
    private QuakeDao quakeDao;

    public static void enqueueWork(Context context, Intent intent){
        enqueueWork(context, QuakelineMainService.class, JOB_ID, intent);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate is start");

        AppDatabase appDatabase = AppDatabase.getDatabase(getApplication());
        quakeDao = appDatabase.quakeDao();
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this::setData);
    }

    @Override
    public IBinder onBind(@NotNull Intent intent) {
        return null;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork() called with: intent = [" + intent + "]");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void setData(Location _location){
        IService service = RestServise.getClient().create(IService.class);

        service.getQuakes().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<ResponseModel> call , @NotNull Response<ResponseModel> response) {
                if (response.isSuccessful()){
                    notificationQuakeline("Background starting");
                    for (Result result : response.body().getResult()){
                        AppDatabase.databaseWriteExecutor.execute(()->{
                            Quake quake = new Quake(result.getHash(),result.getHash(),result.getMag(),result.getLng(),result.getLat(),result.getLokasyon(),result.getDepth(),
                                    result.getTitle(), result.getTimestamp(),result.getDateStamp(),result.getDate());
                            quakeDao.insert(quake);
                        });
                        if (CurrentLocationHelper.currentLocation(_location.getLatitude(),_location.getLongitude(), result.getLat(),result.getLng(),50))
                        {
                            String term = " "+result.getTitle() + " - " + result.getDate() + " - " + result.getMag();
                            notificationQuakeline(term);
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseModel> call , @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void notificationQuakeline(String quakelineNotificationDetail)
    {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Quekeline Service")
                .setContentText(quakelineNotificationDetail)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();

        notification.flags = Notification.FLAG_AUTO_CANCEL;

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        int i = new Random().nextInt(9999-1000);
        managerCompat.notify(i, notification);
    }

    public static final String CHANNEL_ID = "QuakelineServiceChannel";

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Quakeline Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(manager).createNotificationChannel(serviceChannel);
        }
    }
}