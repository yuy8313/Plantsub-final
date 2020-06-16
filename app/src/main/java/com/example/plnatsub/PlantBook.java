package com.example.plnatsub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import  com.example.plnatsub.MyRecyclerAdapter;
import com.squareup.picasso.Target;

public class PlantBook  extends AppCompatActivity  {

    private final String BASE_URL = "http://655bd3efc4ec.ngrok.io";
    private static final String TAG = MainActivity.class.getSimpleName();
    private MyRecyclerAdapter mAdapter;
    private MyAPI mMyAPI;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_book_recycle);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_book);

        recyclerView.setHasFixedSize(false);

        // 레이아웃 매니저로 LinearLayoutManager를 설정
        //  LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        initMyAPI(BASE_URL);
        final String android_id = android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Intent intentL = new Intent(getApplicationContext(), Loading.class);
        startActivity(intentL); //로딩화면 출력


        // 표시할 임시 데이터
        final List<CardItem> dataList = new ArrayList<>();
        Call<List<AccountItem>> versionCall = mMyAPI.get_book_list("" + android_id);
        versionCall.enqueue(new Callback<List<AccountItem>>() {
            @Override
            public void onResponse(Call<List<AccountItem>> call, Response<List<AccountItem>> response) {
                if (response.isSuccessful()) {
                    List<AccountItem> versionList = response.body();
                    Log.d(TAG, response.body().toString());
                    String book_list_text = "";
                    String book_list_image = "";

                    String book_text_one = "";
                    String book_image_one = "";
                    for (AccountItem accountItem : versionList) {

                        book_text_one = "" + accountItem.getName();//1번째 이름
                        book_image_one = accountItem.getImage();//1번째 이미지

                        String book_number_one= accountItem.getId();
                        Log.d(TAG,"번호악"+book_number_one);
                        Log.d(TAG,"번호악111"+book_text_one);
                        final String finalBook_number_one = book_number_one;
                        final String finalBook_image_one = book_image_one;
                        Thread uThread = new Thread() {

                            @Override
                            public void run() {
                                try {
                                    //서버에 올려둔 이미지 URL
                                    URL url = new URL(finalBook_image_one);
                                    //Web에서 이미지 가져온 후 ImageView에 지정할 Bitmap 만들기
                    /* URLConnection 생성자가 protected로 선언되어 있으므로
                     개발자가 직접 HttpURLConnection 객체 생성 불가 */

                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    /* openConnection()메서드가 리턴하는 urlConnection 객체는
                    HttpURLConnection의 인스턴스가 될 수 있으므로 캐스팅해서 사용한다*/

                                    conn.setDoInput(true); //Server 통신에서 입력 가능한 상태로 만듦
                                    conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                                    InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 반환

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                        };
                        uThread.start(); // 작업 Thread 실행

                        try {
                            //메인 Thread는 별도의 작업을 완료할 때까지 대기한다!
                            //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다림
                            //join() 메서드는 InterruptedException을 발생시킨다.
                            uThread.join();

                            //작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                            //UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정

                        } catch (InterruptedException e) {
                            e.printStackTrace();

                        }
                        // 어댑터 설정
                        dataList.add(new CardItem(bitmap, book_text_one));
                        mAdapter = new MyRecyclerAdapter(dataList);
                        mAdapter.setOnClickListener(new MyRecyclerAdapter.MyRecyclerViewClickListener() {
                            @Override
                            public void onItemClicked(int position) {
                                Log.d(TAG, "onItemClicked: " + position);


                                final Intent intent = new Intent(getApplicationContext(), PlantBookDetail.class);
                                Call<List<AccountItem>> mydatailCall = mMyAPI.get_my_book_detail(android_id, finalBook_number_one);
                                mydatailCall.enqueue(new Callback<List<AccountItem>>() {
                                    @Override
                                    public void onResponse(Call<List<AccountItem>> call, Response<List<AccountItem>> response) {
                                        if (response.isSuccessful()) {
                                            Log.d(TAG,"번호"+finalBook_number_one);
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


                            @Override
                            public void onLearnMoreButtonClicked(int position) {   //삭제 버튼
                                Log.d(TAG, "onLearnMoreButtonClicked: " + position);

                                Call<AccountItem> mydeleteCall = mMyAPI.delete_book_list(finalBook_number_one);
                                mydeleteCall.enqueue(new Callback<AccountItem>() {
                                    @Override
                                    public void onResponse(Call<AccountItem> call, Response<AccountItem> response) {
                                        if (response.isSuccessful()) {
                                            AccountItem versionList =response.body();
                                            Log.d(TAG, "dd마마마ㅏㅏ아" + versionList);

                                        } else {
                                            int StatusCode = response.code();
                                            Log.d(TAG, "dd아" + StatusCode);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<AccountItem> call, Throwable t) {
                                        Log.d(TAG, "실패" + t.getMessage());
                                    }

                                });
                                // 아이템 삭제
                                mAdapter.removeItem(position);

                            }
                        });
                        recyclerView.setAdapter(mAdapter);
                    }
                } else {
                    int StatusCode = response.code();
                    Log.d(TAG, "dd아" + StatusCode);
                }
            }

            @Override
            public void onFailure(Call<List<AccountItem>> call, Throwable t) {

            }
        });


        // ItemAnimator
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        animator.setMoveDuration(1000);
        animator.setChangeDuration(1000);
        recyclerView.setItemAnimator(animator);

        // ItemDecoration
        DividerItemDecoration decoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
    }

    public void onBackPressed() {  //뒤로가기 버튼 이벤트
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    private void initMyAPI(String baseUrl) {
        Log.d(TAG, "initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        mMyAPI = retrofit.create(MyAPI.class);
    }



//    public static Bitmap getImageFromURL(final String imageURL) {
//
//    }
}








