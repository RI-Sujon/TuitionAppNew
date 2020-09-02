package com.example.tuitionapp_surji.message_box;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionapp_surji.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int IMAGE_TYPE_RIGHT = 2;
    public static final int IMAGE_TYPE_LEFT = 3;

    private Context mContext;
    private List<Chat> mChat;
    private String imageUri,gender=" ",checkUser;
    private ArrayList<String> userInfo;
    private String guardianMobileNumber, tutorEmail,userId;
    private FirebaseUser fuser;


    public MessageAdapter(Context mContext, List<Chat> mChat, String imageUri, String gender, String checkUser, ArrayList<String> userInfo, String userId, String guardianMobileNumber, String tutorEmail){//, String imageurl) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageUri = imageUri;
        this.gender = gender;
        this.userInfo = userInfo;
        this.checkUser = checkUser;
        this.guardianMobileNumber = guardianMobileNumber;
        this.tutorEmail = tutorEmail;
        this.userId =userId;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if( viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);

            return new MessageAdapter.ViewHolder(view);
        }

        else if(viewType == MSG_TYPE_LEFT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);

            return new MessageAdapter.ViewHolder(view);
        }

        else if(viewType == IMAGE_TYPE_LEFT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.image_item_left, parent, false);

            return new MessageAdapter.ViewHolder(view);
        }

        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.image_item_right, parent, false);

            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, int position)
    {
        final Chat chat = mChat.get(position);

        if(imageUri!=null){
          Picasso.get().load(imageUri).into(holder.profile_image_messenger);
        }



        else if(imageUri==null && gender!=null)
        {
            if(gender.equals("MALE")){
                holder.profile_image_messenger.setImageResource(R.drawable.male_pic);

            }

            else {
                holder.profile_image_messenger.setImageResource(R.drawable.female_pic);
            }
        }


        if(chat.getMessage_type().equals("image")){
            //System.out.println("URI ==================="+chat.getMessage());
            if(chat.getMessage()!=null){

                Picasso.get().load(chat.getMessage()).into(holder.show_image);
                holder.show_message.setVisibility(View.GONE);
                holder.show_date.setVisibility(View.GONE);
                holder.txt_seen.setVisibility(View.GONE);
                holder.profile_image_messenger.setVisibility(View.GONE);

                holder.show_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(mContext, "Image will be shown", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext,ImageLargeViewActivity.class);
                        intent.putExtra("imageUri",chat.getMessage());

                        if(checkUser.equals("guardian")){
                            intent.putExtra("userId", userId);
                            intent.putExtra("tutorEmail",tutorEmail);
                            intent.putExtra("user", checkUser);
                        }

                        else if(checkUser.equals("tutor")){
                            intent.putExtra("userId", userId);
                            intent.putExtra("mobileNumber", guardianMobileNumber);
                            intent.putStringArrayListExtra("userInfo", userInfo) ;
                            intent.putExtra("user", checkUser);

                        }
                        /*intent.putExtra("user",checkUser);
                        intent.putStringArrayListExtra("userInfo", userInfo) ;*/
                        mContext.startActivity(intent);
                    }
                });
            }
        }

        else if(chat.getMessage_type().equals("text")){
            holder.show_image.setVisibility(View.GONE);
            holder.show_message.setText(chat.getMessage());


            if(position==mChat.size()-1){
                if(chat.getIsSeen().equals("yes")){
                    holder.txt_seen.setText("Seen");
                }

                else {
                    holder.txt_seen.setText("Delivered");
                }
            }

            else
            {
                holder.txt_seen.setVisibility(View.GONE);
            }

            holder.show_date.setVisibility(View.GONE);




            holder.show_message.setOnClickListener(new View.OnClickListener() {

                int counter = 0;

                @Override
                public void onClick(View v)
                {
                    //  System.out.println("SEEN ============== "+ chat1.isSeen());

                    counter++;
                    holder.show_date.setText(chat.getMessageDate()+", "+chat.getMessage_time());

                    if(counter==1){

                        if(chat.getIsSeen().equals("yes"))
                            holder.txt_seen.setText("Seen");

                        else
                            holder.txt_seen.setText("Delivered");

                        holder.txt_seen.setVisibility(View.VISIBLE);
                        holder.show_date.setVisibility(View.VISIBLE);
                    }

                    else if(counter%2==0){
                        holder.txt_seen.setVisibility(View.GONE);
                        holder.show_date.setVisibility(View.GONE);
                    }

                    else if(counter%2!=0)
                    {
                        if(chat.getIsSeen().equals("yes"))
                            holder.txt_seen.setText("Seen");

                        else
                            holder.txt_seen.setText("Delivered");

                        holder.txt_seen.setVisibility(View.VISIBLE);
                        holder.show_date.setVisibility(View.VISIBLE);

                    }

                    //System.out.println("Counter ============  "+counter);

                }
            });



        }



/*
        if(position==mChat.size()-1){
            if(chat.getIsSeen().equals("yes")){
                holder.txt_seen.setText("Seen");
            }

            else {
                holder.txt_seen.setText("Delivered");
            }
        }

        else
        {
            holder.txt_seen.setVisibility(View.GONE);
        }

        holder.show_date.setVisibility(View.GONE);




        holder.show_message.setOnClickListener(new View.OnClickListener() {

            int counter = 0;

            @Override
            public void onClick(View v)
            {
              //  System.out.println("SEEN ============== "+ chat1.isSeen());

                counter++;
                holder.show_date.setText(chat.getMessageDate()+", "+chat.getMessage_time());

                if(counter==1){

                    if(chat.getIsSeen().equals("yes"))
                        holder.txt_seen.setText("Seen");

                    else
                        holder.txt_seen.setText("Delivered");

                    holder.txt_seen.setVisibility(View.VISIBLE);
                    holder.show_date.setVisibility(View.VISIBLE);
                }

               else if(counter%2==0){
                    holder.txt_seen.setVisibility(View.GONE);
                    holder.show_date.setVisibility(View.GONE);
                }

                else if(counter%2!=0)
                {
                    if(chat.getIsSeen().equals("yes"))
                        holder.txt_seen.setText("Seen");

                    else
                        holder.txt_seen.setText("Delivered");

                    holder.txt_seen.setVisibility(View.VISIBLE);
                    holder.show_date.setVisibility(View.VISIBLE);

                }

                //System.out.println("Counter ============  "+counter);

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView show_message;
        public TextView show_date;
        public TextView txt_seen;
        public ImageView profile_image_messenger;
        public ImageView show_image;


        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            show_date = itemView.findViewById(R.id.messageDate);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            profile_image_messenger = itemView.findViewById(R.id.profile_image_messenger);
            show_image = itemView.findViewById(R.id.show_image);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
      fuser = FirebaseAuth.getInstance().getCurrentUser();

      if(mChat.get(position).getMessage_type().equals("text")){
          if(mChat.get(position).getSender().equals(fuser.getUid())){
              return MSG_TYPE_RIGHT;
          }

          else {
              return MSG_TYPE_LEFT;
          }
      }

      else{
          if(mChat.get(position).getSender().equals(fuser.getUid())){
              return IMAGE_TYPE_RIGHT;
          }

          else {
              return IMAGE_TYPE_LEFT;
          }
      }

    }
}