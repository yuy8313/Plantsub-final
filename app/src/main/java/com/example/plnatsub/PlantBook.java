package com.example.plnatsub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlantBook  extends AppCompatActivity {

    private final String BASE_URL = "http://85b809a8712a.ngrok.io";


    private MyAPI mMyAPI;
    private final  String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_book);

        final TextView book_txt = (TextView) findViewById(R.id.book_txt);
        final ImageView book_image = (ImageView) findViewById(R.id.book_image);




        initMyAPI(BASE_URL);
        String android_id = android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Call<List<AccountItem>> versionCall = mMyAPI.get_book_list(""+android_id);
        versionCall.enqueue(new Callback<List<AccountItem>>() {
            @Override
            public void onResponse(Call<List<AccountItem>> call, Response<List<AccountItem>> response) {
                if(response.isSuccessful()){
                    List<AccountItem> versionList =response.body();
                    Log.d(TAG,response.body().toString());
                    String book_list_text = "";
                    String book_list_image = "";

                    for(AccountItem accountItem:versionList){
//                        book_list_text +=""+accountItem.getName()+""+accountItem.getFlower()+""+accountItem.getContent()+"\n\n";
                        book_list_text +=""+accountItem.getName()+"\n\n";
                        book_txt.setText(book_list_text);

                        book_list_image += accountItem.getImage()+"@";
                        Log.d(TAG,"아나"+book_list_image);
                        String test_split[] = book_list_image.split("@");


                        for(int i=0; i<test_split.length; i++) {

                            Log.d(TAG, "테스트" + test_split[i]);

                            Picasso.get().load(test_split[i]).into(book_image);  // 이게 주소불러와서 이미지뷰 저장 피카소 시발
                        }

                    }

                }else{
                    int StatusCode =response.code();
                    Log.d(TAG,"dd아"+StatusCode);
                }
            }

            @Override
            public void onFailure(Call<List<AccountItem>> call, Throwable t) {

            }
        });
    }


    public void initMyAPI(String baseUrl){

        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        mMyAPI = retrofit.create(MyAPI.class);
    }
}
