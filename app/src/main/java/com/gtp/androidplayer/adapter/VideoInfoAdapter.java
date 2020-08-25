package com.gtp.androidplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gtp.androidplayer.R;
import com.gtp.androidplayer.been.VideoInfo;

import java.util.List;


public class VideoInfoAdapter extends RecyclerView.Adapter<VideoInfoAdapter.MyViewHolder> {


    private List<VideoInfo> mData;
    private Context context;
    private LayoutInflater inflater;

    public VideoInfoAdapter(Context context, List<VideoInfo> mData) {
        this.context = context;
        this.mData = mData;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_obtain, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvName.setText(mData.get(position).getTitle());
        holder.tvTime.setText("时长" + mData.get(position).getTime());
        holder.tvWeight.setText("文件大小：" + mData.get(position).getSize());
        if (mData.get(position).getB() != null) {
            holder.ivImg.setImageBitmap(mData.get(position).getB());
        } else {
            holder.ivImg.setImageResource(R.drawable.music);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(mData.get(position), position + 1);

            }
        });
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {

        void onItemClick(VideoInfo videoInfo, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {

        this.listener = listener;
    }


    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvName;
        TextView tvTime;
        TextView tvWeight;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.item_tv_img);
            tvName = itemView.findViewById(R.id.item_tv_name);
            tvTime = itemView.findViewById(R.id.item_tv_time);
            tvWeight = itemView.findViewById(R.id.item_tv_weight);
        }
    }
}
