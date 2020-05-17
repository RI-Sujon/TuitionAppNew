package com.example.tuitionapp_sujon.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.tuitionapp_sujon.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapterForCandidateTutorApproval extends BaseAdapter {

    private Context context ;
    private ArrayList<String> name ;
    private ArrayList<String> email ;
    private DatabaseReference myRefTutorApprove ;

    public CustomAdapterForCandidateTutorApproval(Context context, ArrayList<String> name, ArrayList<String> email) {
        this.context = context;
        this.name = name;
        this.email = email;
        myRefTutorApprove = FirebaseDatabase.getInstance().getReference("Approve") ;
    }

    @Override
    public int getCount() {
        return name.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder ;

        if(convertView==null){
            holder = new ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_adapter_tutor_list_view, null);
            holder.nameTextView = convertView.findViewById(R.id.name);
            holder.emailTextView = convertView.findViewById(R.id.institute);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag() ;
        }


        holder.nameTextView.setText(name.get(position));
        holder.emailTextView.setText(email.get(position));

        holder.nameTextView.setVisibility(View.VISIBLE);
        holder.emailTextView.setVisibility(View.VISIBLE);
        /*holder.button.setVisibility(View.VISIBLE);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.button.setText("Approved");
                holder.button.setEnabled(false);
                ApproveInfo approveInfo = new ApproveInfo("tuitionapsspl02@gmail.com", email.get(position)) ;
                approveInfo.setApprove(true);
                myRefTutorApprove.push().setValue(approveInfo) ;
            }
        });*/
        return convertView;
    }

    class ViewHolder{
        Button button ;
        TextView nameTextView ;
        TextView emailTextView ;
    }

}
