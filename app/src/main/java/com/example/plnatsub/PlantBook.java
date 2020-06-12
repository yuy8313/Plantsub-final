package com.example.plnatsub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlantBook  extends AppCompatActivity {

    private final String BASE_URL = "http://ac6dc08d6af5.ngrok.io"; //url 주소


    private MyAPI mMyAPI;
    private final  String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_book);

//        final TextView book_txt = (TextView) findViewById(R.id.book_txt);
//        final ImageView book_image = (ImageView) findViewById(R.id.book_image);




        initMyAPI(BASE_URL);
        final String android_id = android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Call<List<AccountItem>> versionCall = mMyAPI.get_book_list(""+android_id);
        versionCall.enqueue(new Callback<List<AccountItem>>() {
            @Override
            public void onResponse(Call<List<AccountItem>> call, Response<List<AccountItem>> response) {
                if(response.isSuccessful()){
                    List<AccountItem> versionList =response.body();
                    Log.d(TAG,response.body().toString());
                    String book_list_text = "";
                    String book_list_image = "";

                    String book_text_one = "";
                    String book_image_one = "";

                    String book_number_one = "";
                    for(AccountItem accountItem:versionList){
                        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
//                        book_list_text +=""+accountItem.getName()+""+accountItem.getFlower()+""+accountItem.getContent()+"\n\n";

                        book_text_one = ""+accountItem.getName();
                        TextView tv_one = new TextView(getApplicationContext());
                        tv_one.setText(book_text_one);          // 첫번째 꽃저장 이름
                        layout.addView(tv_one);


                        book_list_text += accountItem.getName()+"\n";

                        String book_text_last = book_list_text.substring(book_list_text.lastIndexOf("\n"));
                        TextView tv_last = new TextView(getApplicationContext());
                        tv_last.setText(book_text_last);
                        layout.addView(tv_last);


                        book_image_one = accountItem.getImage();
                        ImageView iv_one = new ImageView(getApplicationContext());
                        Picasso.get().load(book_image_one).into(iv_one);  // 첫번째 이미지
                        layout.addView(iv_one);

                        book_list_image += accountItem.getImage()+"@";

                        String book_image_last = book_list_image.substring(book_list_image.lastIndexOf("@"));
                        ImageView iv_last = new ImageView(getApplicationContext());
                        Picasso.get().load(book_image_last).into(iv_last);  // 2번째 부터 이미지

                        book_number_one = ""+accountItem.getId();
                        Button btn_one = new Button(getApplicationContext());
                        btn_one.setText("자세히 보기");
                        layout.addView(btn_one);

                        final String finalBook_number_one = book_number_one;
                        btn_one.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Intent intent = new Intent(getApplicationContext(), PlantBookDetail.class);
                                Call<List<AccountItem>> mydatailCall = mMyAPI.get_my_book_detail(android_id, finalBook_number_one);
                                mydatailCall.enqueue(new Callback<List<AccountItem>>() {
                                    @Override
                                    public void onResponse(Call<List<AccountItem>> call, Response<List<AccountItem>> response) {
                                        if (response.isSuccessful()) {
                                            List<AccountItem> versionList =response.body();
                                            String first_img_detail_txt = "";
                                            String first_name_datail_txt = "";
                                            String first_flower_detail_txt = "";
                                            String first_content_detail_txt = "";
                                            for(AccountItem accountItem:versionList){
                                                Log.d(TAG,"ㅎ"+accountItem.getName());

                                                first_img_detail_txt =""+accountItem.getImage();
                                                first_name_datail_txt = ""+accountItem.getName();
                                                first_flower_detail_txt = ""+accountItem.getFlower();
                                                first_content_detail_txt = ""+accountItem.getContent();
                                                Log.d(TAG,"뭐냐"+first_flower_detail_txt);
                                                Log.d(TAG,"뭐냐 이미지"+first_img_detail_txt);
                                            }
                                            intent.putExtra("first_img_detail_txt",first_img_detail_txt);
                                            intent.putExtra("first_name_datail_txt",first_name_datail_txt);
                                            intent.putExtra("first_flower_detail_txt",first_flower_detail_txt);
                                            intent.putExtra("first_content_detail_txt",first_content_detail_txt);

                                            startActivity(intent);
                                        } else {
                                            int StatusCode = response.code();
                                            Log.d(TAG, "dd아" + StatusCode);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<AccountItem>> call, Throwable t) {
                                        Log.d(TAG, "실패" + t.getMessage());
                                    }

                                });
                            }
                        });
                        layout.addView(iv_last);
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