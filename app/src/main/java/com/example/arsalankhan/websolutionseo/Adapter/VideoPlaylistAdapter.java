package com.example.arsalankhan.websolutionseo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arsalankhan.websolutionseo.R;
import com.example.arsalankhan.websolutionseo.SingleVideoActivity;
import com.example.arsalankhan.websolutionseo.helper.PlaylistHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Arsalan khan on 8/2/2017.
 */

public class VideoPlaylistAdapter extends RecyclerView.Adapter<VideoPlaylistAdapter.MyViewHolder> {

    private ArrayList<PlaylistHelper> videoPlaylistArraylist=new ArrayList<>();
    private Context context;
    public VideoPlaylistAdapter(Context context,ArrayList<PlaylistHelper> arrayList){

        videoPlaylistArraylist=arrayList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_video_row,parent,false);

        return new MyViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PlaylistHelper helper= videoPlaylistArraylist.get(position);


        Picasso.with(context).load(helper.getThumbnail()).placeholder(R.drawable.placeholder).into(holder.thumnailImage);
        holder.logoImage.setImageResource(R.drawable.logo);
        holder.videoTitle.setText(helper.getTitle());
        holder.channelTitle.setText(helper.getChannelTitle());
    }

    @Override
    public int getItemCount() {
        return videoPlaylistArraylist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView thumnailImage;
        private ImageView logoImage;
        private TextView videoTitle;
        private TextView channelTitle;
         Context context;
        public MyViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context=context;

            thumnailImage=itemView.findViewById(R.id.thumbnail_Imageview);
            logoImage=itemView.findViewById(R.id.logo_ImageView);
            videoTitle=itemView.findViewById(R.id.tv_VideoTitle);
            channelTitle=itemView.findViewById(R.id.tv_channelTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String VideoId=videoPlaylistArraylist.get(getAdapterPosition()).getVideoId();

                    Intent singleVideoIntent=new Intent(context, SingleVideoActivity.class);

                    singleVideoIntent.putExtra("videoId",VideoId);
                    singleVideoIntent.putExtra("videoTitle",videoPlaylistArraylist.get(getAdapterPosition()).getTitle());
                    singleVideoIntent.putExtra("channelTitle",videoPlaylistArraylist.get(getAdapterPosition()).getChannelTitle());
                    context.startActivity(singleVideoIntent);
                }
            });
        }
    }
}


