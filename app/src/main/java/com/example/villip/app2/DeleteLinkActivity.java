package com.example.villip.app2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DeleteLinkActivity extends AppCompatActivity {

    ImageView imageView;

    String Link;
    int ID;
    int status;

    String DIR_SD = "BIGDIG/test/App2";
    String FILENAME_SD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_link);
        imageView = (ImageView) findViewById(R.id.imageView2);

        Intent intent = getIntent();
        Link = intent.getStringExtra("linkForDeleteApp1");
        ID = intent.getIntExtra("IDForDeleteApp1", 0);
        status = intent.getIntExtra("StatusForDeleteApp1", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (status == 1) {
            isStatus1();
        } else if (status == 2) {
            if(isOnline()) {
                isStatus2();
            }
        } else if (status == 3) {
            Toast.makeText(this, "Ссылка не корректна, статус не может измениться", Toast.LENGTH_LONG).show();
        }
    }


    private void isStatus1() {
        if (isOnline()) {
            Callback callBack = new Callback() {
                @Override
                public void onSuccess() {
                    writeFileSD();
                    onStartMyService();
                }

                @Override
                public void onError() {
                    Toast.makeText(getApplicationContext(), "Донт вори би хаппи )))", Toast.LENGTH_LONG).show();
                }
            };

            Picasso.with(this) //передаем контекст приложения
                    .load(Link) //адрес изображения
                    .into(imageView, callBack); //ссылка на ImageView

        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void isStatus2() {
        if (isOnline()) {
            Callback callBack = new Callback() {
                @Override
                public void onSuccess() {
                    updateStatus();
                }

                @Override
                public void onError() {
                    Toast.makeText(getApplicationContext(), "Донт вори би хаппи )))", Toast.LENGTH_LONG).show();
                }
            };

            Picasso.with(this) //передаем контекст приложения
                    .load(Link) //адрес изображения
                    .into(imageView, callBack); //ссылка на ImageView

        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void onStartMyService() {
        Intent intent = new Intent(this, SafePictureAndDeleteLinkService.class);
        intent.putExtra("PutID", ID);
        startService(intent);
    }


    void writeFileSD() {
        Uri uri = Uri.parse(Link);
        FILENAME_SD = uri.getLastPathSegment();

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }

        // получаем путь к SD
        File sdPath = new File(Environment.getExternalStorageDirectory().getPath());

        // добавляем свой каталог к пути
        sdPath = new File(sdPath + "/" + DIR_SD);
        sdPath.mkdirs();

        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);

        try {
            FileOutputStream ostream = new FileOutputStream(sdFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.close();

            if(sdFile.exists()) {
                Toast.makeText(this, "Картинка успешно сохранена по адресу - " + sdFile.toString(), Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateStatus() {
        ContentValues cv = new ContentValues();
        cv.put(LinksContract.STATUS, 1);
        Uri uri = ContentUris.withAppendedId(LinksContract.CONTENT_URI, ID);
        int cnt = getContentResolver().update(uri, cv, null, null);

        Toast.makeText(this, "Статус успешно изменился на ЗЕЛЁНЫЙ ", Toast.LENGTH_LONG).show();
    }
}