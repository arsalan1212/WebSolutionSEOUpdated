package com.example.arsalankhan.websolutionseo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arsalan khan on 8/27/2017.
 */

public class Chat {

    private static Chat mInstance;

    public static synchronized Chat getInstance(){

        if(mInstance == null){
            mInstance = new Chat();
        }

        return mInstance;
    }


    public void ChatWithUser(FirebaseAuth mAuth, String message) {


        if(mAuth != null){

            DatabaseReference mChatRef = FirebaseDatabase.getInstance().getReference().child("Chat");


            FirebaseUser mCurrentUser = mAuth.getCurrentUser();
            String Uid = mCurrentUser.getUid();


            Map chatMap = new HashMap();
            chatMap.put("senderId",Uid);
            chatMap.put("message",message);

            Map RefMap = new HashMap();
            RefMap.put(mChatRef.push().getKey()+"/",chatMap);

            mChatRef.updateChildren(RefMap);
        }


    }
}
