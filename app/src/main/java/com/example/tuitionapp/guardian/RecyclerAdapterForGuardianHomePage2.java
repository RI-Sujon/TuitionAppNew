package com.example.tuitionapp.guardian;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionapp.R;

import java.util.ArrayList;

public class RecyclerAdapterForGuardianHomePage2 extends RecyclerView.Adapter<RecyclerAdapterForGuardianHomePage2.FeaturedViewHolder> {

    ArrayList<String> nameList ;
    ArrayList<String> instituteList ;
    ArrayList<String> profilePicUriList ;

    public RecyclerAdapterForGuardianHomePage2(ArrayList<String> nameList, ArrayList<String> instituteList, ArrayList<String> profilePicUriList) {
        this.nameList = nameList;
        this.instituteList = instituteList;
        this.profilePicUriList = profilePicUriList;
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
        //holder.name.setText(nameList.get(position));
        //holder.instituteName.setText(instituteList.get(position));
        //holder.profilePicUri.setImageResource(profilePicUriList.get(position));
    }


    @Override
    public int getItemCount() {
        return 5;
        //return nameList.size();
    }


    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        LinearLayout layout ;
        TextView name ;
        TextView instituteName ;
        ImageView profilePicUri;


        public FeaturedViewHolder(@NonNull View itemView){
            super(itemView);

            layout = itemView.findViewById(R.id.group_layout) ;
            name = itemView.findViewById(R.id.name) ;
            instituteName = itemView.findViewById(R.id.institute_name) ;
            profilePicUri = itemView.findViewById(R.id.profile_image) ;
        }
    }
}


