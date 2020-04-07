package com.example.tuitionapp.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.tuitionapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerAdapterForListView extends BaseAdapter {

    Context context ;
    String name[] ;
    String email[];
    Button button ;
    DatabaseReference myRefTutorApprove ;

    public CustomerAdapterForListView(Context context, String[] name, String[] email) {
        this.context = context;
        this.name = name;
        this.email = email;
        myRefTutorApprove = FirebaseDatabase.getInstance().getReference("Approve") ;
    }

    @Override
    public int getCount() {
        return name.length;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.activity_adminclass_tutor_list_view, null);
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView emailTextView = convertView.findViewById(R.id.emailTextView);
        nameTextView.setText(name[position]);
        emailTextView.setText(email[position]);
        button = convertView.findViewById(R.id.approveButton) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveInfo approveInfo = new ApproveInfo("tuitionapsspl02@gmail.com", email[position]) ;
                myRefTutorApprove.push().setValue(approveInfo) ;
                button.setText("Approved");
                button.setEnabled(false);
            }
        });
        return convertView;
    }
}
