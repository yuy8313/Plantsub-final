package com.example.plnatsub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResult extends AppCompatActivity {

    private TextView result1, result2, result1_percent, result2_percent;
    private MyAPI mMyAPI;

    private final  String TAG = getClass().getSimpleName();

    private final String BASE_URL = "http://ac6dc08d6af5.ngrok.io"; //url주소
    Button first_detail_btn,second_detail_btn;
    ImageView flower_img1, flower_img2, flower_img3;
    private String android_id;
    private String formatDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        flower_img1 = (ImageView) findViewById(R.id.flower_img1);
        flower_img2 = (ImageView) findViewById(R.id.flower_img2);
        //flower_img3 = (ImageView) findViewById(R.id.flower_img3);

        result1 = (TextView)findViewById(R.id.result1);
        result1_percent = (TextView)findViewById(R.id.result1_percent);

        result2 = (TextView)findViewById(R.id.result2);
        result2_percent = (TextView)findViewById(R.id.result2_percent);

        first_detail_btn = (Button) findViewById(R.id.first_detail_btn);
        second_detail_btn = (Button) findViewById(R.id.second_detail_btn);

        Intent intent = getIntent(); //인텐트를 가져옴

        String plant_name_list1 = intent.getExtras().getString("frist_txt");
        result1.setText(plant_name_list1);
        String plant_percent_list1 = intent.getExtras().getString("first_percent_txt");
        result1_percent.setText(plant_percent_list1);

        String plant_name_list2 = intent.getExtras().getString("second_txt");
        result2.setText(plant_name_list2);
        String plant_percent_list2 = intent.getExtras().getString("second_percent_txt");
        result2_percent.setText(plant_percent_list2);

        final String search_my_plant_images = intent.getExtras().getString("my_plant_images");

        String first_img_txt = intent.getExtras().getString("first_img_txt");
        String second_img_txt = intent.getExtras().getString("second_img_txt");

        Picasso.get().load(first_img_txt).into(flower_img1);
        Picasso.get().load(second_img_txt).into(flower_img2);

        final String one = plant_name_list1; // 첫번째 버튼에 해당
        final String two = plant_name_list2; // 두번째 버튼에 해당

        first_detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), PlantDetail.class);

                Call<List<AccountItem>> plantconCall = mMyAPI.get_plant_con(one);
                plantconCall.enqueue(new Callback<List<AccountItem>>() {
                    @Override
                    public void onResponse(Call<List<AccountItem>> call, Response<List<AccountItem>> response) {
                        if(response.isSuccessful()){
                            List<AccountItem> versionList =response.body();
                            Log.d(TAG,response.body().toString());
                            String name_txt = "";
                            String flower_txt = "";
                            String content_txt = "";
                            String img_txt = "";

                            for(AccountItem accountItem:versionList){
                                Log.d(TAG,"ㅅ"+one);
                                Log.d(TAG,"ㅎ"+accountItem.getName());

                                name_txt +=""+ accountItem.getName();
                                flower_txt +=" 꽃말: "+accountItem.getFlower();
                                content_txt +=" 꽃 내용: "+accountItem.getContent();
                                img_txt ="http://ac6dc08d6af5.ngrok.io"+accountItem.getImage();  //url주소

                            }


                            intent.putExtra("name_txt",name_txt);
                            intent.putExtra("flower_txt",flower_txt);
                            intent.putExtra("content_txt",content_txt);
                            intent.putExtra("img_txt",img_txt);

                            intent.putExtra("search_my_plant_images",search_my_plant_images);
                            startActivity(intent);

                        }else{
                            int StatusCode =response.code();
                            Log.d(TAG,"dd아"+StatusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AccountItem>> call, Throwable t) {
                        Log.d(TAG,"실패"+t.getMessage());
                    }
                });
//                            startActivity(intent);
            }
        });

        second_detail_btn.setOnClickListener(new View.OnClickListener() {  //버튼 클릭시 2등인 정확도 정보나옴
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), PlantDetail.class);

                Call<List<AccountItem>> plantconCall = mMyAPI.get_plant_con(two);
                plantconCall.enqueue(new Callback<List<AccountItem>>() {
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
                                img_txt ="http://ac6dc08d6af5.ngrok.io"+accountItem.getImage();   //url주소
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
