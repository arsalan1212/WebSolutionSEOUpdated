package com.example.arsalankhan.websolutionseo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arsalankhan.websolutionseo.R;
import com.example.arsalankhan.websolutionseo.helper.CommentsHelper;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Arsalan khan on 8/3/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>{

    private ArrayList<CommentsHelper> arrayList =new ArrayList<>();
    private Context context;

    public CommentsAdapter(Context context, ArrayList<CommentsHelper> commentsHelperArrayList){
        this.context =  context;
        arrayList = commentsHelperArrayList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comment_row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CommentsHelper helper = arrayList.get(position);

        holder.DisplayNameView.setText(helper.getDisplayName());
        holder.CommentsTextView.setText(helper.getComment());


        // for getting date in this formate i.e. 3 months ago
        long now = System.currentTimeMillis();

        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
        String publishDate = helper.getPublishedAt();
        String[] DateArray = publishDate.split("T");
        String dateWithoutTime = DateArray[0];


         try {
             Date date =dateFormat.parse(dateWithoutTime);

             CharSequence publishedAt = DateUtils.getRelativeTimeSpanString(date.getTime(),now, DateUtils.DAY_IN_MILLIS);
             holder.publishAtView.setText(publishedAt+"");

        } catch (ParseException e) {
            e.printStackTrace();
        }


        Picasso.with(context).load(helper.getProfileImage()).placeholder(R.drawable.avatar_default).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView imageView;
        private TextView DisplayNameView,CommentsTextView, publishAtView;
        public MyViewHolder(View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.comment_author_image);
            DisplayNameView = itemView.findViewById(R.id.tv_commentAuthorName);
            CommentsTextView = itemView.findViewById(R.id.tv_commentText);
            publishAtView = itemView.findViewById(R.id.tv_comment_publishAt);

        }
    }
}
