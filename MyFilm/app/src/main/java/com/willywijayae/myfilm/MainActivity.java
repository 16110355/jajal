package com.willywijayae.myfilm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button buttonMove = findViewById(R.id.btnLihat);
        buttonMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Sinopsis.class);
                startActivity(intent);

            }
        });

        final TextView tvNama = findViewById(R.id.tvNama);
        final TextView tvSinopsis = findViewById(R.id.tvSinopsis);
        final ImageButton img = findViewById(R.id.imgButton);

        String url = "https://api.themoviedb.org/3/movie/now_playing?" +
                "api_key=32f7f07283696dba81c173ea5b35c87d";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(),
                        "Tidak dapat terhubung server", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                try {
                    JSONObject objData = new JSONObject(responseData);
                    final JSONArray arrayNama = objData.getJSONArray("results");
                    final JSONObject objNama = new JSONObject(arrayNama.get(0).toString());

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                tvNama.setText(objNama.get("title").toString());
                                tvSinopsis.setText(objNama.get("overview").toString());

                                String urlIcon = "http://image.tmdb.org/t/p/w185"+
                                        objNama.get("poster_path");
                                Glide.with(MainActivity.this)
                                        .load(urlIcon)
                                        .into(img);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }
}
