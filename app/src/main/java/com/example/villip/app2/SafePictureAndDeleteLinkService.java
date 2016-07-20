package com.example.villip.app2;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class SafePictureAndDeleteLinkService extends Service {
    int ID;

    private Handler handler = new Handler();

    public SafePictureAndDeleteLinkService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        ID = intent.getIntExtra("PutID", 0);

        deleteLink();
        return super.onStartCommand(intent, flags, startId);
    }

    public void deleteLink() {
        new Thread(new Runnable() {
            public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                Uri uri = ContentUris.withAppendedId(LinksContract.CONTENT_URI, ID);
                int countDel = getContentResolver().delete(uri, null, null);

                message();
            }
        }).start();

        stopSelf();
    }

    private void message() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Ссылка была удалена", Toast.LENGTH_LONG).show();
            }
        });
    }
}
