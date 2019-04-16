package com.example.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photoapp.Helper.BitmapHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class postGet extends AppCompatActivity {

    private TextView textErrResult;
    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private ImageView img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_get);

        img1 = findViewById(R.id.imageView2);
        img1.setImageBitmap(BitmapHelper.getInstance().getBitmap());

        textErrResult = findViewById(R.id.text_err_result);
        textViewResult = findViewById(R.id.text_view_result);
        //img1 = findViewById(R.id.img1);
        //bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.heh);
        bitmap1 = BitmapHelper.getInstance().getBitmap();

        // prev url - http://192.168.0.101:5000/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.101:5000/")
                .client(okHttpClient)

                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //getData();
        //createData();

    }

    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    public void putRequest(View view) {

        createData();

    }

    public void getRequest(View view) {

        getData();

    }

    public void initiateRequest(View view) {

        initiateProcessing();

    }

    private void getData() {

        Call<List<Data>> call = jsonPlaceHolderApi.getData();

        call.enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
                if (!response.isSuccessful()) {
                    textErrResult.setText("Code: " + response.code());
                    return;
                }

                List<Data> datas = response.body();

                for (Data data : datas) {

                    bitmap2 = ImageUtil.convert(data.getValue());
                    img1.setImageBitmap(bitmap2);
                    //String content = "";
                    //content += "value: " + data.getValue() + "\n\n";

                    //textViewResult.append(content);

                }

            }

            @Override
            public void onFailure(Call<List<Data>> call, Throwable t) {
                textErrResult.setText(t.getMessage());

            }
        });
    }


    private void createData() {

        String base64String = ImageUtil.convert(bitmap1);
        // textViewResult.setText(base64String);

        Data data = new Data("value", base64String);

        Call<Data> call = jsonPlaceHolderApi.createData(data);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {

                if (!response.isSuccessful()) {
                    textErrResult.setText("Code: " + response.code());
                    return;
                }

//                Data dataResponse = response.body();
//
//                String content = "";
//                content += "Code: " + response.code() + "\n";
//                content += "idIm: " + dataResponse.getIdIm() + "\n";
//                content += "value: " + dataResponse.getValue() + "\n\n";
//
//                textViewResult.setText(content);


            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

                textErrResult.setText(t.getMessage());

            }
        });

    }


    private void initiateProcessing(){

        Data data = new Data("value", "photo");

        Call<Data> call = jsonPlaceHolderApi.initiateProcessing(data);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (!response.isSuccessful()) {
                    textErrResult.setText("Code: " + response.code());
                    return;
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

                textErrResult.setText(t.getMessage());

            }
        });


    }

    public void sendBack(View view) {

        Bitmap bitmap = ((BitmapDrawable)img1.getDrawable()).getBitmap();

        BitmapHelper.getInstance().setBitmap(bitmap);

        if (BitmapHelper.getInstance().getBitmap() == null) {

            Toast.makeText(this, "Bitmap is null", Toast.LENGTH_LONG).show();

        } else {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }

    }






}
