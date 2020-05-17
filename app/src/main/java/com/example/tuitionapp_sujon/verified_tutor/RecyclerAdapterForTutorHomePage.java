package com.example.tuitionapp_sujon.verified_tutor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionapp_sujon.R;
import com.example.tuitionapp_sujon.tuition_post.TuitionPostInfo;

import java.util.ArrayList;

public class RecyclerAdapterForTutorHomePage extends RecyclerView.Adapter<RecyclerAdapterForTutorHomePage.FeaturedViewHolder> {

    ArrayList<TuitionPostInfo> tuitionPostInfoArrayList ;

    public RecyclerAdapterForTutorHomePage(ArrayList<TuitionPostInfo> tuitionPostInfoArrayList) {
        this.tuitionPostInfoArrayList = tuitionPostInfoArrayList;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_card_for_verified_tutor_home_page, parent, false) ;
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view) ;
        return featuredViewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        holder.title.setText(tuitionPostInfoArrayList.get(position).getPostTitle());
        holder.class_name.setText(tuitionPostInfoArrayList.get(position).getStudentClass());
        holder.subject.setText(tuitionPostInfoArrayList.get(position).getStudentSubjectList());
        if (!tuitionPostInfoArrayList.get(position).getStudentFullAddress().equals("")){
            holder.address.setText(tuitionPostInfoArrayList.get(position).getStudentFullAddress() + ", " + tuitionPostInfoArrayList.get(position).getStudentAreaAddress());
        }
        else {
            holder.address.setText(tuitionPostInfoArrayList.get(position).getStudentAreaAddress());

        }
    }


    @Override
    public int getItemCount() {
        return tuitionPostInfoArrayList.size();
    }


    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        TextView title ;
        TextView class_name ;
        TextView subject;
        TextView address ;


        public FeaturedViewHolder(@NonNull View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.postTitle) ;
            class_name = itemView.findViewById(R.id.class_name) ;
            subject = itemView.findViewById(R.id.subject) ;
            address = itemView.findViewById(R.id.address) ;
        }
    }
}
