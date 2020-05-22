package com.example.tuitionapp_surji.guardian;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapterForGuardianHomePage1 extends RecyclerView.Adapter<RecyclerAdapterForGuardianHomePage1.FeaturedViewHolder> {

    private ArrayList<CandidateTutorInfo> candidateTutorInfoArrayList ;

    public RecyclerAdapterForGuardianHomePage1(ArrayList<CandidateTutorInfo> candidateTutorInfoArrayList) {
        this.candidateTutorInfoArrayList = candidateTutorInfoArrayList;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_card_for_guardian_home_page, parent, false) ;
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view) ;
        return featuredViewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        holder.layout.setVisibility(View.VISIBLE);
        holder.name.setText(candidateTutorInfoArrayList.get(position).getUserName());
        holder.instituteName.setText(candidateTutorInfoArrayList.get(position).getEdu_instituteName());

        if(candidateTutorInfoArrayList.get(position).getEdu_tutorSubject().charAt(0)!='-' && candidateTutorInfoArrayList.get(position).getEdu_tutorSubject().charAt(0)!='!'){
            holder.department.setText("Studies " + candidateTutorInfoArrayList.get(position).getEdu_tutorSubject() + " at");
        }
        else{
            holder.department.setText("Studies at");
        }

        if(candidateTutorInfoArrayList.get(position).getProfilePictureUri()!=null){
            if(!candidateTutorInfoArrayList.get(position).getProfilePictureUri().equals("")){
                Picasso.get().load(candidateTutorInfoArrayList.get(position).getProfilePictureUri()).into(holder.profilePic) ;
            }
        }
        else{
            if(candidateTutorInfoArrayList.get(position).getGender().equals("MALE")){
                holder.profilePic.setImageResource(R.drawable.male_pic);
            }
            else{
                holder.profilePic.setImageResource(R.drawable.female_pic);
            }
        }
    }


    @Override
    public int getItemCount() {
        //return 5;
        return candidateTutorInfoArrayList.size();
    }


    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        LinearLayout layout ;
        TextView name ;
        TextView instituteName ;
        TextView department ;
        ImageView profilePic;


        public FeaturedViewHolder(@NonNull View itemView){
            super(itemView);

            layout = itemView.findViewById(R.id.tutor_layout) ;
            name = itemView.findViewById(R.id.name) ;
            instituteName = itemView.findViewById(R.id.institute_name) ;
            department = itemView.findViewById(R.id.department) ;
            profilePic = itemView.findViewById(R.id.profile_image) ;
        }
    }
}


