package com.example.tuitionapp_surji.verified_tutor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.tuition_post.TuitionPostInfo;
import com.example.tuitionapp_surji.tuition_post.TuitionPostViewSinglePageActivity;

import java.util.ArrayList;

public class RecyclerAdapterForTutorHomePage extends RecyclerView.Adapter<RecyclerAdapterForTutorHomePage.FeaturedViewHolder> {

    private ArrayList<TuitionPostInfo> tuitionPostInfoArrayList ;
    private ArrayList<String> tutorInfo ;
    private ArrayList<String> tuitionPostInfoUid ;
    private int [] responsePostArray ;
    private int flag ;
    private ViewGroup parent ;


    public RecyclerAdapterForTutorHomePage(ArrayList<TuitionPostInfo> tuitionPostInfoArrayList, ArrayList<String> tutorInfo, ArrayList<String> tuitionPostInfoUid, int[] responsePostArray, int flag) {
        this.tuitionPostInfoArrayList = tuitionPostInfoArrayList;
        this.tutorInfo = tutorInfo;
        this.tuitionPostInfoUid = tuitionPostInfoUid;
        this.responsePostArray = responsePostArray;
        this.flag = flag;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent ;
        View view;
        if(flag==1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_card_for_verified_tutor_home_page, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_card_for_verified_tutor_home_page2, parent, false);
        }

        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view) ;
        return featuredViewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, final int position) {
        holder.title.setText(tuitionPostInfoArrayList.get(position).getPostTitle());
        holder.class_name.setText(tuitionPostInfoArrayList.get(position).getStudentClass());
        holder.subject.setText(tuitionPostInfoArrayList.get(position).getStudentSubjectList());
        if (!tuitionPostInfoArrayList.get(position).getStudentFullAddress().equals("")){
            holder.address.setText(tuitionPostInfoArrayList.get(position).getStudentFullAddress() + ", " + tuitionPostInfoArrayList.get(position).getStudentAreaAddress());
        }
        else {
            holder.address.setText(tuitionPostInfoArrayList.get(position).getStudentAreaAddress());
        }

        if(tuitionPostInfoArrayList.get(position).getStudentMedium().equals("English Medium")){
            holder.postImage.setImageResource(R.drawable.logo_english_medium);
        }
        else if(tuitionPostInfoArrayList.get(position).getStudentGroup().equals("Science")){
            holder.postImage.setImageResource(R.drawable.logo_science);
        }
        else if(tuitionPostInfoArrayList.get(position).getStudentGroup().equals("Commerce")){
            holder.postImage.setImageResource(R.drawable.logo_commerce);
        }
        else if(tuitionPostInfoArrayList.get(position).getStudentGroup().equals("Arts")){
            holder.postImage.setImageResource(R.drawable.logo_humanities);
        }
        else if(tuitionPostInfoArrayList.get(position).getStudentClass().equals("CLASS 8")){
            holder.postImage.setImageResource(R.drawable.logo_class8);
        }
        else if(tuitionPostInfoArrayList.get(position).getStudentClass().equals("CLASS 7")){
            holder.postImage.setImageResource(R.drawable.logo_class7);
        }
        else if(tuitionPostInfoArrayList.get(position).getStudentClass().equals("CLASS 6")){
            holder.postImage.setImageResource(R.drawable.logo_class6);
        }
        else if(tuitionPostInfoArrayList.get(position).getStudentClass().equals("CLASS 5")){
            holder.postImage.setImageResource(R.drawable.logo_class5);
        }
        else if(tuitionPostInfoArrayList.get(position).getStudentClass().equals("CLASS 4")|(tuitionPostInfoArrayList).get(position).getStudentClass().equals("CLASS 3")||
                (tuitionPostInfoArrayList).get(position).getStudentClass().equals("CLASS 2")|(tuitionPostInfoArrayList).get(position).getStudentClass().equals("CLASS 1")
                |(tuitionPostInfoArrayList).get(position).getStudentClass().equals("NURSERY")|(tuitionPostInfoArrayList).get(position).getStudentClass().equals("PLAY")){
            holder.postImage.setImageResource(R.drawable.logo_primary);
        }
        else {
            holder.postImage.setImageResource(R.drawable.logo_else_class);
        }

        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), TuitionPostViewSinglePageActivity.class);

                intent.putStringArrayListExtra("tutorInfo", tutorInfo) ;
                intent.putExtra("guardianUid", tuitionPostInfoArrayList.get(position).getGuardianUidFK()) ;
                intent.putExtra("user", "tutor") ;
                intent.putExtra("response", String.valueOf(responsePostArray[position])) ;
                intent.putExtra("tuitionPostUid", tuitionPostInfoUid.get(position)) ;

                intent.putExtra("postTitle", tuitionPostInfoArrayList.get(position).getPostTitle()) ;
                intent.putExtra("medium", tuitionPostInfoArrayList.get(position).getStudentMedium()) ;
                intent.putExtra("class_name", tuitionPostInfoArrayList.get(position).getStudentClass()) ;
                intent.putExtra("group", tuitionPostInfoArrayList.get(position).getStudentGroup()) ;
                intent.putExtra("subject", tuitionPostInfoArrayList.get(position).getStudentSubjectList()) ;
                intent.putExtra("studentInstituteName", tuitionPostInfoArrayList.get(position).getStudentInstitute()) ;
                intent.putExtra("address", tuitionPostInfoArrayList.get(position).getStudentFullAddress() +", " + tuitionPostInfoArrayList.get(position).getStudentAreaAddress()) ;
                intent.putExtra("contactNo", tuitionPostInfoArrayList.get(position).getStudentContactNo()) ;
                intent.putExtra("daysPerWeek", tuitionPostInfoArrayList.get(position).getDaysPerWeekOrMonth()) ;
                intent.putExtra("salary", tuitionPostInfoArrayList.get(position).getSalary()) ;
                intent.putExtra("extraInfo", tuitionPostInfoArrayList.get(position).getExtra()) ;
                intent.putExtra("postTime", tuitionPostInfoArrayList.get(position).getPostDate() + ", " + tuitionPostInfoArrayList.get(position).getPostTime()) ;
                intent.putExtra("tutorGenderPreferable", tuitionPostInfoArrayList.get(position).getTutorGenderPreference()) ;
                parent.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return tuitionPostInfoArrayList.size();
    }


    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout cardLayout ;
        TextView title ;
        TextView class_name ;
        TextView subject ;
        TextView address ;
        ImageView postImage ;

        public FeaturedViewHolder(@NonNull View itemView){
            super(itemView);

            cardLayout = itemView.findViewById(R.id.card) ;
            title = itemView.findViewById(R.id.postTitle) ;
            class_name = itemView.findViewById(R.id.class_name) ;
            subject = itemView.findViewById(R.id.subject) ;
            address = itemView.findViewById(R.id.address) ;
            postImage = itemView.findViewById(R.id.image) ;
        }
    }
}
