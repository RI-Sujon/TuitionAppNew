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
import com.example.tuitionapp_surji.guardian.GuardianInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageRequestAdapter extends RecyclerView.Adapter<MessageRequestAdapter.RequestViewHolder> {

    private Context context;
    private List<MessageBoxInfo> mUsers;
    private  String checkUser;
    private String LastMessage;
    private boolean isChat;
    private ArrayList<String> userInfo ;
    private DatabaseReference candidateTutorReference, guardianReference; //= FirebaseDatabase.getInstance().getReference("CandidateTutor");
    private ArrayList<CandidateTutorInfo> imageUriStrings;


    public MessageRequestAdapter(Context context, List<MessageBoxInfo> mUsers, String checkUser, boolean isChat, ArrayList<String> userInfo) {
        this.context = context;
        this.mUsers = mUsers;
        this.checkUser = checkUser;
        this.isChat = isChat;
        this.userInfo = userInfo;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.message_request_item,parent,false);

        return new MessageRequestAdapter.RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestViewHolder holder, int position) {

        final MessageBoxInfo user = mUsers.get(position);
        candidateTutorReference = FirebaseDatabase.getInstance().getReference("CandidateTutor");
        guardianReference = FirebaseDatabase.getInstance().getReference("Guardian");

        if(checkUser.equals("guardian")){

            candidateTutorReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        CandidateTutorInfo candidateTutorInfo = snapshot.getValue(CandidateTutorInfo.class);
                        if(candidateTutorInfo.getEmailPK().equals(user.getTutorEmail()))
                        {
                            //System.out.println("User email ====================="+candidateTutorInfo.getEmailPK());
                           // System.out.println("Name  =======================  "+candidateTutorInfo.getUserName());
                            holder.requester_username.setText(candidateTutorInfo.getUserName());
                            holder.request_msg.setText(candidateTutorInfo.getUserName()+" sent you a message request.");

                            if(candidateTutorInfo.getGender().equals("MALE")){
                                if(candidateTutorInfo.getProfilePictureUri()!= null)
                                    Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(holder.requester_profile_image);
                                else
                                    holder.requester_profile_image.setImageResource(R.drawable.male_pic);
                            }

                            else if(candidateTutorInfo.getGender().equals("FEMALE")){
                                if(candidateTutorInfo.getProfilePictureUri()!= null)
                                    Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(holder.requester_profile_image);
                                else
                                    holder.requester_profile_image.setImageResource(R.drawable.female_pic);
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        else if(checkUser.equals("tutor")){

            guardianReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GuardianInfo guardianInfo = snapshot.getValue(GuardianInfo.class);
                        if (guardianInfo.getPhoneNumber().equals(user.getGuardianMobileNumber()))
                        {

                            holder.requester_username.setText(guardianInfo.getName());
                            holder.request_msg.setText("A guardian sent you a message request.");

                            if(!guardianInfo.getProfilePicUri().equals("1")){
                                Picasso.get().load(guardianInfo.getProfilePicUri()).into(holder.requester_profile_image);
                            }

                            else{
                                holder.requester_profile_image.setImageResource(R.drawable.man);
                            }

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

           /* holder.requester_username.setText(user.getGuardianMobileNumber());
            holder.request_msg.setText("A guardian sent you a message request.");
            holder.requester_profile_image.setImageResource(R.drawable.man)*/;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MessageRequestActivity.class);

                if(checkUser.equals("guardian")){
                    intent.putExtra("userId", user.getTutorUid());
                    intent.putExtra("tutorEmail",user.getTutorEmail());
                    intent.putExtra("user", checkUser);
                }

                else if(checkUser.equals("tutor")){
                    intent.putExtra("userId", user.getGuardianUid());
                    intent.putExtra("mobileNumber",user.getGuardianMobileNumber());
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                    intent.putExtra("user", checkUser);

                }
                context.startActivity(intent);

            }
        });


    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        public TextView requester_username;
        public TextView request_msg;
        public ImageView requester_profile_image;

        public RequestViewHolder(View view) {
            super(view);

            requester_username = view.findViewById(R.id.requester_username);
            request_msg = view.findViewById(R.id.request_msg);
            requester_profile_image = view.findViewById(R.id.requester_profile_image);
        }
    }
}
