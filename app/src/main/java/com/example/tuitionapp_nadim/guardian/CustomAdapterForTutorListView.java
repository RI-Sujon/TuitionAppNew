package com.example.tuitionapp_nadim.guardian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tuitionapp_nadim.R;
import com.example.tuitionapp_nadim.candidate_tutor.CandidateTutorInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapterForTutorListView extends BaseAdapter {
    private Context context;

    private ArrayList<CandidateTutorInfo> candidateTutorInfoArrayList ;
    private String viewFlag ;

    public CustomAdapterForTutorListView(Context context, ArrayList<CandidateTutorInfo> candidateTutorInfoArrayList, String viewFlag) {
        this.context = context ;
        this.candidateTutorInfoArrayList = candidateTutorInfoArrayList ;
        this.viewFlag = viewFlag ;
    }

    @Override
    public int getCount() {
        return candidateTutorInfoArrayList.size() ;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_adapter_tutor_list_view, null);
            holder.layout = convertView.findViewById(R.id.tutor_searching_result);
            holder.imageView = convertView.findViewById(R.id.profile_pic);
            holder.name = convertView.findViewById(R.id.name);
            holder.institute = convertView.findViewById(R.id.institute);
            holder.department = convertView.findViewById(R.id.subject);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag() ;
        }

        if(candidateTutorInfoArrayList.get(position).getProfilePictureUri()!=null){
            if(!candidateTutorInfoArrayList.get(position).getProfilePictureUri().equals("")){
                Picasso.get().load(candidateTutorInfoArrayList.get(position).getProfilePictureUri()).into(holder.imageView) ;
            }
        }
        else{
            if(candidateTutorInfoArrayList.get(position).getGender().equals("MALE")){
                holder.imageView.setImageResource(R.drawable.male_pic);
            }
            else{
                holder.imageView.setImageResource(R.drawable.female_pic);
            }
        }
        holder.name.setText(candidateTutorInfoArrayList.get(position).getUserName());
        holder.institute.setText(candidateTutorInfoArrayList.get(position).getEdu_instituteName());
        holder.layout.setVisibility(View.VISIBLE);

        if(viewFlag.equals("groupTutor")||viewFlag.equals("admin")){
            holder.layout.setBackgroundResource(R.drawable.border4);
            holder.layout.setPadding(0,8,0,8);
        }

        /*if(departmentList.get(position).charAt(0)!='-'||departmentList.get(position).charAt(0)!='!'){
            holder.department.setText("Studies " + departmentList.get(position));
        }
        else{
            holder.department.setVisibility(View.GONE);
        }*/
        return convertView ;
    }

    class ViewHolder {
        LinearLayout layout ;
        ImageView imageView ;
        TextView  name ;
        TextView institute ;
        TextView department ;
    }
}
