package com.example.tuitionapp_surji.guardian;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.tuition_post.TuitionPostViewSinglePageActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapterForGuardianHomePage1 extends RecyclerView.Adapter<RecyclerAdapterForGuardianHomePage1.FeaturedViewHolder> {

    private ArrayList<CandidateTutorInfo> candidateTutorInfoArrayList ;
    private ArrayList<String> tutorUidArrayList ;
    private ViewGroup parent ;

    public RecyclerAdapterForGuardianHomePage1(ArrayList<CandidateTutorInfo> candidateTutorInfoArrayList, ArrayList<String> tutorUidArrayList) {
        this.candidateTutorInfoArrayList = candidateTutorInfoArrayList ;
        this.tutorUidArrayList = tutorUidArrayList ;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent ;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_card_for_guardian_home_page, parent, false) ;

        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view) ;
        return featuredViewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, final int position) {
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

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), VerifiedTutorProfileActivity.class);
                intent.putExtra("user", "guardian") ;
                intent.putExtra("tutorUid",tutorUidArrayList.get(position));
                intent.putExtra("userEmail", candidateTutorInfoArrayList.get(position).getEmailPK()) ;
                intent.putExtra("context", "homepage") ;
                parent.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
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


