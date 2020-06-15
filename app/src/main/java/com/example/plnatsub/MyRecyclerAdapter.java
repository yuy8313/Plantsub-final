package com.example.plnatsub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private final List<CardItem> mDataList;
    private MyRecyclerViewClickListener mListener;
    public MyRecyclerAdapter(List<CardItem> dataList) {
        mDataList = dataList;
    }

    // 뷰 홀더를 생성하는 부분. 레이아웃을 만드는 부분
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_book, parent, false);
        return new ViewHolder(view);
    }

    // 뷰 홀더에 데이터를 설정하는 부분
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CardItem item = mDataList.get(position);

        holder.image.setImageBitmap(item.getImg_flower());

        holder.contents.setText(item.getContents());


        // 클릭 이벤트
        if (mListener != null) {
            // 현재 위치
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(holder.getAdapterPosition());
                }
            });
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onShareButtonClicked(holder.getAdapterPosition());
                }
            });
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onLearnMoreButtonClicked(holder.getAdapterPosition());
                }
            });
        }
    }

    // 아이템의 수
    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    // 각각의 아이템의 레퍼런스를 저장할 뷰 홀더 클래스
    // 반드시 RecyclerView.ViewHolder를 상속해야 함
    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView contents;
        ImageView image;
        Button share;
        Button more;

        public ViewHolder(View itemView) {
            super(itemView);
            contents = (TextView) itemView.findViewById(R.id.contents_txt);
            image = (ImageView) itemView.findViewById(R.id.book_image);
            share = (Button) itemView.findViewById(R.id.info_button);
            more = (Button) itemView.findViewById(R.id.del_button);

        }
    }

    public void setOnClickListener(MyRecyclerViewClickListener listener) {
        mListener = listener;
    }

    public interface MyRecyclerViewClickListener {
        // 아이템 전체 부분의 클릭
        void onItemClicked(int position);

        // Share 버튼 클릭
        void onShareButtonClicked(int position);

        // Learn More 버튼 클릭
        void onLearnMoreButtonClicked(int position);
    }

    public void removeItem(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataList.size());
    }

    public void addItem(int position, CardItem item) {
        mDataList.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mDataList.size());
    }
}
