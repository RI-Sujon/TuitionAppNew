package com.example.tuitionappv2.message_box;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionappv2.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private  String checkUser;



    public UserAdapter(Context mContext, List<User> mUsers, String checkUser) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.checkUser = checkUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final User user=mUsers.get(position);

        if(checkUser.equals("guardian")){
            holder.username.setText(user.getTutorEmail());
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }

        else if(checkUser.equals("tutor")){
            holder.username.setText(user.getGuardianMobileNumber());
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }


        /*
        if(user.getGuardianMobileNumber() != ""){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }

        else{
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);

                if(checkUser.equals("guardian")){
                    intent.putExtra("userId", user.getTutorUid());
                    intent.putExtra("user", checkUser);
                }

                else if(checkUser.equals("tutor")){
                    intent.putExtra("userId", user.getGuardianUid());
                    intent.putExtra("user", checkUser);

                }
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);

            username=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);
        }
    }


}
