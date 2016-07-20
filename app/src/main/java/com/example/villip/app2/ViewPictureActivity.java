package com.example.villip.app2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewPictureActivity extends AppCompatActivity {

    ImageView imageView;

    private String Link;
    private static int status;
    private String Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_picture);
        imageView = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        Link = intent.getStringExtra("linkApp1");

        //Для тестирования ссылок. Чтобы не вводить длинные ссылки вручную с App2, можно вводить a, b или c
        if(Link.equals("a")){
            Link = "http://www.jivosite.ru/assets/images/icons/play.png";
        } else if(Link.equals("b")){
            Link = "http://affiche.ru/wall/raznocvetnye_bukvy_wallpapers.jpg";
        } else if(Link.equals("c")){
            Link = "http://www.onlineobuchenie.ru/wp-content/uploads/2015/01/Onlayn.jpg";
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Time = sdf.format(c.getTime());

        if ( !isOnline() ){
            status = 2;
            onWriteData();

            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
        else {
            Callback callBack = new Callback() {
                @Override
                public void onSuccess() {
                    status = 1;
                    onWriteData();

                    Toast.makeText(getApplicationContext(), "Картинка загрузилась", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError() {
                    status = 3;
                    onWriteData();

                    Toast.makeText(getApplicationContext(), "Не корректная ссылка", Toast.LENGTH_LONG).show();
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


    void onWriteData() {
        ContentValues values = new ContentValues();

        values.put(LinksContract.LINK, Link);
        values.put(LinksContract.STATUS, status);
        values.put(LinksContract.TIME, Time);

        getContentResolver().insert(LinksContract.CONTENT_URI, values);
    }
}

