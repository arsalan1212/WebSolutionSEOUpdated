package com.example.arsalankhan.websolutionseo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arsalankhan.websolutionseo.R;
import com.example.arsalankhan.websolutionseo.helper.Messages;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Arsalan khan on 8/27/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ALIGN_LEFT_POSITION=0;
    public static final int ALIGN_RIGHT_POSITION=1;
    FirebaseAuth mAuth;

    ArrayList<Messages> messagesArrayList =new ArrayList<>();
    private Context context;
    public ChatAdapter(ArrayList arrayList,Context context){
        messagesArrayList = arrayList;
        mAuth = FirebaseAuth.getInstance();
        this.context = context;

    }

    @Override
    public int getItemViewType(int position) {

        if(mAuth != null && messagesArrayList.size()!=0){
            String currentUserId = mAuth.getCurrentUser().getUid();
            Messages messages = messagesArrayList.get(position);

            if(messages.getSenderId().equals(currentUserId)){

                return ALIGN_RIGHT_POSITION;
            }

            return ALIGN_LEFT_POSITION;
        }else{
            return 10;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == ALIGN_RIGHT_POSITION){

            View viewRight = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_single_row_right,parent,false);
            return new MyViewHolderRightBubble(viewRight);
        }
        else if(viewType == ALIGN_LEFT_POSITION){

            View viewLeft = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_single_row_left,parent,false);
            return new MyViewHolderLeftBubble(viewLeft);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Messages messages = messagesArrayList.get(position);

        if(holder instanceof MyViewHolderLeftBubble){

            MyViewHolderLeftBubble holderLeftBubble = (MyViewHolderLeftBubble) holder;
            holderLeftBubble.bubbleTextViewLeft.setText(messages.getMessage());
            holderLeftBubble.chatUserNameLeftSide.setText(messages.getDisplayName());

            Picasso.with(context).load(messages.getPhotoUrl()).placeholder(R.drawable.avatar_default)
                        .into(holderLeftBubble.chatUserProfileLeftSideImage);

        }

        else if(holder instanceof MyViewHolderRightBubble) {

            MyViewHolderRightBubble holderRightBubble = (MyViewHolderRightBubble) holder;
            holderRightBubble.bubbleTextViewRight.setText(messages.getMessage());

            holderRightBubble.chatUserNameRightSide.setText(messages.getDisplayName());


            Picasso.with(context).load(messages.getPhotoUrl()).placeholder(R.drawable.avatar_default)
                        .into(holderRightBubble.UserProfileRightSideImage);

        }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    public class MyViewHolderLeftBubble extends RecyclerView.ViewHolder{

        BubbleTextView bubbleTextViewLeft;
        CircleImageView chatUserProfileLeftSideImage;
        TextView chatUserNameLeftSide;
        public MyViewHolderLeftBubble(View itemView) {
            super(itemView);

            bubbleTextViewLeft =itemView.findViewById(R.id.messageBubbleLeft);
            chatUserProfileLeftSideImage = itemView.findViewById(R.id.chatUserProfileLeftSide);
            chatUserNameLeftSide = itemView.findViewById(R.id.chatUserDisplayNameLeft);
        }
    }

    public class MyViewHolderRightBubble extends RecyclerView.ViewHolder{

        BubbleTextView bubbleTextViewRight;
        CircleImageView UserProfileRightSideImage;
        TextView chatUserNameRightSide;
        public MyViewHolderRightBubble(View itemView) {
            super(itemView);

            bubbleTextViewRight = itemView.findViewById(R.id.messageBubbleRight);
            UserProfileRightSideImage = itemView.findViewById(R.id.chatUserProfileRightSide);
            chatUserNameRightSide = itemView.findViewById(R.id.chatUserDisplayNameRight);
        }
    }

}
