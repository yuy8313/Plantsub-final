package com.example.plnatsub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.plnatsub.R.drawable.btn_carmer;

public class SearchResult extends AppCompatActivity {

    private TextView result1, result2, result1_percent, result2_percent;
    private MyAPI mMyAPI;

    private final  String TAG = getClass().getSimpleName();

    private final String BASE_URL = "http://36c5fcc3ab6e.ngrok.io"; //url주소


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        LinearLayout layout_result1 = (LinearLayout) findViewById(R.id.layout_result1);
        LinearLayout layout_result2 = (LinearLayout) findViewById(R.id.layout_result2);
        LinearLayout layout_result3 = (LinearLayout) findViewById(R.id.layout_result3);

        LinearLayout layout_result1_text = (LinearLayout) findViewById(R.id.layout_result1_text);
        LinearLayout layout_result1_img = (LinearLayout) findViewById(R.id.layout_result1_img);
        LinearLayout layout_result1_btn = (LinearLayout) findViewById(R.id.layout_result1_btn);

        LinearLayout layout_result2_text = (LinearLayout) findViewById(R.id.layout_result2_text);
        LinearLayout layout_result2_img = (LinearLayout) findViewById(R.id.layout_result2_img);
        LinearLayout layout_result2_btn = (LinearLayout) findViewById(R.id.layout_result2_btn);

        LinearLayout layout_result3_text = (LinearLayout) findViewById(R.id.layout_result3_text);
        LinearLayout layout_result3_img = (LinearLayout) findViewById(R.id.layout_result3_img);
        LinearLayout layout_result3_btn = (LinearLayout) findViewById(R.id.layout_result3_btn);

        Intent intent = getIntent(); //인텐트를 가져옴


        final String search_my_plant_images = intent.getExtras().getString("my_plant_images");

            String first_img_txt = intent.getExtras().getString("first_img_txt");
            String plant_first_name = intent.getExtras().getString("frist_txt");
            String plant_first_percent = intent.getExtras().getString("first_percent_txt");

            Typeface typeface_tv = Typeface.createFromAsset(getAssets(), "maplestory_light.ttf");  //제목 텍스트 폰트

        LinearLayout.LayoutParams params_btn_info = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        if (Float.valueOf(plant_first_percent)> 35.0 ) {


            ImageView flower_img1 = new ImageView(getApplicationContext());
            Picasso.get().load(first_img_txt).resize(400, 400).into(flower_img1);
            layout_result1_img.addView(flower_img1);

            TextView tv_first_name = new TextView(getApplicationContext());

            LinearLayout.LayoutParams params_text = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params_text.setMargins(40,0,0,0);

            tv_first_name.setTypeface(typeface_tv);

            tv_first_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35); // 텍스트 크기
            tv_first_name.setTextColor(Color.BLACK);
            tv_first_name.setText(plant_first_name);
            layout_result1_text.addView(tv_first_name, params_text);

            TextView tv_first_percent = new TextView(getApplicationContext());

            tv_first_percent.setTypeface(typeface_tv);

            tv_first_percent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30); // 텍스트 크기
            tv_first_percent.setTextColor(Color.BLACK);
            tv_first_percent.setText("일치율: " + plant_first_percent + "%");
            layout_result1_text.addView(tv_first_percent, params_text);


            final String one = plant_first_name; // 첫번째 버튼에 해당
            Button first_detail_btn = new Button(getApplicationContext());


            //params_btn_info1.gravity = Gravity.RIGHT;
            //params_btn_info1.setMargins(20, 80,20,0);

            Typeface typeface_btn = Typeface.createFromAsset(getAssets(), "maplestory_light.ttf"); //버튼 폰트
            first_detail_btn.setTypeface(typeface_btn);

            first_detail_btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15); // 텍스트 크기
            first_detail_btn.setBackgroundResource(btn_carmer);
            first_detail_btn.setText("자세히 보기"); //첫번째 버튼
            layout_result1_btn.addView(first_detail_btn, params_btn_info);

            Log.d(TAG, "에러냐" + plant_first_name);

            first_detail_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent(getApplicationContext(), PlantDetail.class);
                    Log.d(TAG, "진짜뭐냐" + one);
                    Call<List<AccountItem>> plantconCall = mMyAPI.get_plant_con(one);
                    plantconCall.enqueue(new Callback<List<AccountItem>>() {
                        @Override
                        public void onResponse(Call<List<AccountItem>> call, Response<List<AccountItem>> response) {
                            if (response.isSuccessful()) {
                                List<AccountItem> versionList = response.body();
                                Log.d(TAG, response.body().toString());
                                String name_txt = "";
                                String flower_txt = "";
                                String content_txt = "";
                                String img_txt = "";

                                for (AccountItem accountItem : versionList) {
                                    Log.d(TAG, "ㅅ" + one);
                                    Log.d(TAG, "ㅎ" + accountItem.getName());

                                    name_txt += "" + accountItem.getName();
                                    flower_txt += " 꽃말: " + accountItem.getFlower();
                                    content_txt += " 꽃 내용: " + accountItem.getContent();
                                    img_txt = "" + BASE_URL + accountItem.getImage();  //url주소

                                }


                                intent.putExtra("name_txt", name_txt);
                                intent.putExtra("flower_txt", flower_txt);
                                intent.putExtra("content_txt", content_txt);
                                intent.putExtra("img_txt", img_txt);

                                intent.putExtra("search_my_plant_images", search_my_plant_images);
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

        }else {

            TextView tv_first_name = new TextView(getApplicationContext());


            Typeface typeface_tv_f = Typeface.createFromAsset(getAssets(), "maplestory_light.ttf");  //제목 텍스트 폰트
            tv_first_name.setTypeface(typeface_tv_f);

            tv_first_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 45); // 텍스트 크기
            tv_first_name.setTextColor(Color.BLACK);
            tv_first_name.setText("꽃순이가 꽃을 찾지 못하였습니다.");
            layout_result1.addView(tv_first_name);

        }

        String plant_second_name = intent.getExtras().getString("second_txt");
        String plant_second_percent = intent.getExtras().getString("second_percent_txt");

        String plant_third_name = intent.getExtras().getString("third_txt");
        String plant_third_percent = intent.getExtras().getString("third_percent_txt");


        String second_img_txt = intent.getExtras().getString("second_img_txt");
        String third_img_txt = intent.getExtras().getString("third_img_txt");


        if (Float.valueOf(plant_second_percent)>= 35.0 ) {

            ImageView flower_img2 = new ImageView(getApplicationContext());
            Picasso.get().load(second_img_txt).resize(180, 180).into(flower_img2);
            layout_result2_img.addView(flower_img2);

            TextView tv_second_name = new TextView(getApplicationContext());

            tv_second_name.setTypeface(typeface_tv);

            tv_second_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30); // 텍스트 크기
            tv_second_name.setTextColor(Color.BLACK);
            tv_second_name.setText(plant_second_name);
            layout_result2_text.addView(tv_second_name);

            TextView tv_second_percent = new TextView(getApplicationContext());

            tv_second_percent.setTypeface(typeface_tv);

            tv_second_percent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20); // 텍스트 크기
            tv_second_percent.setTextColor(Color.BLACK);
            tv_second_percent.setText("일치율: "+plant_second_percent+"%");
            layout_result2_text.addView(tv_second_percent);




            final String two = plant_second_name; // 두번째 버튼에 해당

            Button second_detail_btn = new Button(getApplicationContext());


            second_detail_btn.setTypeface(typeface_tv);

            second_detail_btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15); // 텍스트 크기
            second_detail_btn.setBackgroundResource(btn_carmer);
            second_detail_btn.setText("자세히 보기");
            layout_result2_btn.addView(second_detail_btn, params_btn_info);

            second_detail_btn.setOnClickListener(new View.OnClickListener() {  //버튼 클릭시 2등인 정확도 정보나옴
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent(getApplicationContext(), PlantDetail.class);

                    Call<List<AccountItem>> plantconCall1 = mMyAPI.get_plant_con(two);
                    plantconCall1.enqueue(new Callback<List<AccountItem>>() {
                        @Override
                        public void onResponse(Call<List<AccountItem>> call, Response<List<AccountItem>> response) {
                            if(response.isSuccessful()){
                                List<AccountItem> versionList =response.body();
                                Log.d(TAG,response.body().toString());
                                String name_txt = "";
                                String flower_txt = "";
                                String img_txt = "";
                                String content_txt = "";
                                for(AccountItem accountItem:versionList){
                                    Log.d(TAG,"ㅅ"+two);
                                    Log.d(TAG,"ㅎ"+accountItem.getName());
//
                                    name_txt +=""+ accountItem.getName();
                                    flower_txt +=" 꽃말: "+accountItem.getFlower();
                                    img_txt =""+BASE_URL+accountItem.getImage();   //url주소
                                    content_txt +=" 꽃 내용: "+accountItem.getContent();
//
                                }
                                intent.putExtra("name_txt",name_txt);
                                intent.putExtra("flower_txt",flower_txt);
                                intent.putExtra("img_txt",img_txt);
                                intent.putExtra("content_txt",content_txt);

                                intent.putExtra("search_my_plant_images",search_my_plant_images);
                                startActivity(intent);
//                                intent.putExtra("detail_txt",detail2_txt);

                            }else{
                                int StatusCode =response.code();
                                Log.d(TAG,"dd아"+StatusCode);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<AccountItem>> call, Throwable t) {

                        }
                    });

//                            startActivity(intent);
                }
            });

        }
        if(Float.valueOf(plant_third_percent)>= 35.0){

            ImageView flower_img3 = new ImageView(getApplicationContext());
            Picasso.get().load(third_img_txt).resize(180, 180).into(flower_img3);
            layout_result3_img.addView(flower_img3);

            TextView tv_third_name = new TextView(getApplicationContext());

            tv_third_name.setTypeface(typeface_tv);

            tv_third_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30); // 텍스트 크기
            tv_third_name.setTextColor(Color.BLACK);
            tv_third_name.setText(plant_third_name);
            layout_result3_text.addView(tv_third_name);

            TextView tv_third_percent = new TextView(getApplicationContext());

            tv_third_percent.setTypeface(typeface_tv);

            tv_third_percent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20); // 텍스트 크기
            tv_third_percent.setTextColor(Color.BLACK);
            tv_third_percent.setText("일치율: "+plant_third_percent+"%");
            layout_result3_text.addView(tv_third_percent);



            final String three = plant_third_name;


            Button third_detail_btn = new Button(getApplicationContext());

            third_detail_btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15); // 텍스트 크기
            third_detail_btn.setBackgroundResource(btn_carmer);
            third_detail_btn.setText("자세히 보기");
            layout_result3_btn.addView(third_detail_btn, params_btn_info);



            third_detail_btn.setOnClickListener(new View.OnClickListener() {  //버튼 클릭시 2등인 정확도 정보나옴
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent(getApplicationContext(), PlantDetail.class);

                    Call<List<AccountItem>> plantconCall2 = mMyAPI.get_plant_con(three);
                    plantconCall2.enqueue(new Callback<List<AccountItem>>() {
                        @Override
                        public void onResponse(Call<List<AccountItem>> call, Response<List<AccountItem>> response) {
                            if(response.isSuccessful()){
                                List<AccountItem> versionList =response.body();
                                Log.d(TAG,response.body().toString());
                                String name_txt = "";
                                String flower_txt = "";
                                String img_txt = "";
                                String content_txt = "";
                                for(AccountItem accountItem:versionList){

                                    Log.d(TAG,"ㅎ"+accountItem.getName());
//
                                    name_txt +=""+ accountItem.getName();
                                    flower_txt +=" 꽃말: "+accountItem.getFlower();
                                    img_txt =""+BASE_URL+accountItem.getImage();   //url주소
                                    content_txt +=" 꽃 내용: "+accountItem.getContent();
//
                                }
                                intent.putExtra("name_txt",name_txt);
                                intent.putExtra("flower_txt",flower_txt);
                                intent.putExtra("img_txt",img_txt);
                                intent.putExtra("content_txt",content_txt);

                                intent.putExtra("search_my_plant_images",search_my_plant_images);
                                startActivity(intent);
//                                intent.putExtra("detail_txt",detail2_txt);

                            }else{
                                int StatusCode =response.code();
                                Log.d(TAG,"dd아"+StatusCode);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<AccountItem>> call, Throwable t) {

                        }
                    });

//                            startActivity(intent);
                }
            });



        } //if문
        initMyAPI(BASE_URL);
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