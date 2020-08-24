package com.example.tuitionapp_surji.guardian;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.group.GroupHomePageActivity;
import com.example.tuitionapp_surji.group.GroupInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapterForGuardianHomePage2 extends RecyclerView.Adapter<RecyclerAdapterForGuardianHomePage2.FeaturedViewHolder> {

    private ArrayList<GroupInfo> groupInfoArrayList ;
    private ArrayList<String> groupUidList ;
    private ViewGroup parent ;

    public RecyclerAdapterForGuardianHomePage2(ArrayList<GroupInfo> groupInfoArrayList, ArrayList<String> groupUidList) {
        this.groupInfoArrayList = groupInfoArrayList ;
        this.groupUidList = groupUidList ;
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
        holder.groupName.setText(groupInfoArrayList.get(position).getGroupName());
        holder.classRange.setText(groupInfoArrayList.get(position).getClassRange());
        holder.location.setText(groupInfoArrayList.get(position).getFullAddress() + "\n" + groupInfoArrayList.get(position).getAddress());

        if(groupInfoArrayList.get(position).getGroupImageUri().equals("")){
            holder.groupImage.setImageResource(R.drawable.create_group_icon);
        }
        else {
            Picasso.get().load(groupInfoArrayList.get(position).getGroupImageUri()).into(holder.groupImage);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext() , GroupHomePageActivity.class);
                intent.putExtra("user", "guardian") ;
                intent.putExtra("groupID", groupUidList.get(position)) ;
                intent.putExtra("context", "homepage") ;
                parent.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return groupInfoArrayList.size();
    }


    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        LinearLayout layout ;
        TextView groupName ;
        TextView classRange ;
        TextView location ;
        ImageView groupImage;


        public FeaturedViewHolder(@NonNull View itemView){
            super(itemView);

            layout = itemView.findViewById(R.id.group_layout) ;
            groupName = itemView.findViewById(R.id.group_name) ;
            classRange = itemView.findViewById(R.id.class_range) ;
            location = itemView.findViewById(R.id.location) ;
            groupImage = itemView.findViewById(R.id.group_image) ;
        }
    }
}


