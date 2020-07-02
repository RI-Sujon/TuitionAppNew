package com.example.tuitionapp_surji.message_box;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private  String checkUser;
    CandidateTutorInfo tutorInfo;
    private String LastMessage;

    DatabaseReference candidateTutorReference; //= FirebaseDatabase.getInstance().getReference("CandidateTutor");
    ArrayList<CandidateTutorInfo> imageUriStrings;


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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
       final User user=mUsers.get(position);
       imageUriStrings = new ArrayList<>();

        candidateTutorReference = FirebaseDatabase.getInstance().getReference("CandidateTutor");


        if(checkUser.equals("guardian")){
            final String[] tutorName = new String[1];



            candidateTutorReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        CandidateTutorInfo candidateTutorInfo = snapshot.getValue(CandidateTutorInfo.class);
                        if(candidateTutorInfo.getEmailPK().equals(user.getTutorEmail()))
                        {
                            tutorInfo = candidateTutorInfo;
                            tutorName[0] = tutorInfo.getUserName();
                            imageUriStrings.add(candidateTutorInfo);
                            holder.username.setText(tutorName[0]);

                            if(candidateTutorInfo.getGender().equals("MALE")){
                                if(tutorInfo.getProfilePictureUri()!= null)
                                    Picasso.get().load(tutorInfo.getProfilePictureUri()).into(holder.profile_image);
                                else
                                    holder.profile_image.setImageResource(R.drawable.male_pic);
                            }

                            else if(candidateTutorInfo.getGender().equals("FEMALE")){
                                if(tutorInfo.getProfilePictureUri()!= null)
                                    Picasso.get().load(tutorInfo.getProfilePictureUri()).into(holder.profile_image);
                                else
                                    holder.profile_image.setImageResource(R.drawable.female_pic);
                            }



                            break;
                        }


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            lastMessageSetter(user.getTutorUid(), holder.last_msg);


            //holder.username.setText(user.getTutorEmail());
           // System.out.println("Before Set Name =============="+ imageUriStrings.get(0).getProfilePictureUri());
            //holder.username.setText(tutorName[0]);
            //holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }

        else if(checkUser.equals("tutor")){
            holder.username.setText(user.getGuardianMobileNumber());
            holder.profile_image.setImageResource(R.drawable.man);
            lastMessageSetter(user.getGuardianUid(), holder.last_msg);
            //holder.profile_image.setImageResource(R.mipmap.ic_launcher);
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
                    intent.putExtra("tutorEmail",user.getTutorEmail());
                    intent.putExtra("user", checkUser);
                }

                else if(checkUser.equals("tutor")){
                    intent.putExtra("userId", user.getGuardianUid());
                    intent.putExtra("mobileNumber",user.getGuardianMobileNumber());
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
        private  TextView last_msg;

        public ViewHolder(View itemView){
            super(itemView);

            username=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);
            last_msg=itemView.findViewById(R.id.last_msg);
        }
    }


    public  void lastMessageSetter(final String userId, final TextView last_msg){
        LastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if( (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId))
                            || chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid())){

                        LastMessage = chat.getMessage() + "  " + chat.getMessage_time();

                        lastMessageCondition(LastMessage,chat.getMessage_time(),last_msg,chat.getMessage_type(),userId,chat);
                    }

                }




               /* if(LastMessage!=null){

                    if(LastMessage.equals("default")){
                        last_msg.setText("No message");
                    }

                    else
                    {
                        if(LastMessage.length()>20){
                            String shortMessage = LastMessage.substring(0,20);
                            last_msg.setText(shortMessage+"... "+ chat.getMessage_time());

                        }
                        else
                            last_msg.setText(LastMessage);

                    }

                }

                else
                    last_msg.setText("No Message");


                LastMessage = "default";*/

                LastMessage = "default";
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void lastMessageCondition(String LastMessage, String message_time, TextView last_msg,  String message_type,String userId, Chat chat) {



        if(LastMessage!=null){

            if(LastMessage.equals("default")){
                last_msg.setText("No message");
            }

            else
            {
                if(LastMessage.length()>35){

                    if(message_type.equals("text")){
                        String shortMessage = LastMessage.substring(0,20);
                        last_msg.setText(shortMessage+"... "+ message_time);
                    }

                    else
                    {
                        if(chat.getSender().equals(userId)){
                            last_msg.setText("You have received a photo.  "+chat.getMessage_time());
                        }

                        else{
                            last_msg.setText("You have sent a photo.  "+chat.getMessage_time());
                        }

                    }


                }
                else
                    last_msg.setText(LastMessage);

            }

        }

        else
            last_msg.setText("No Message");



    }


}
