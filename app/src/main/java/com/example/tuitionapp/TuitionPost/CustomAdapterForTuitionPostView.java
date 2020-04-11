package com.example.tuitionapp.TuitionPost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tuitionapp.MessageBox.MessageBoxInfo;
import com.example.tuitionapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class CustomAdapterForTuitionPostView extends BaseAdapter {

    private Context context ;
    private ArrayList<TuitionPostInfo> tuitionPostInfo ;
    private String userEmail ;

    private DatabaseReference myRefMessageBox;

    public CustomAdapterForTuitionPostView(Context context, ArrayList<TuitionPostInfo> tuitionPostInfo, String userEmail) {
        this.context = context;
        this.tuitionPostInfo = tuitionPostInfo;
        this.userEmail = userEmail ;

        myRefMessageBox = FirebaseDatabase.getInstance().getReference("MessageBox") ;
    }

    @Override
    public int getCount() {
        return tuitionPostInfo.size();
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
        ViewHolder holder ;
        if(convertView==null){
            holder = new ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_custom_adapter_list_view, null);
            holder.layout = convertView.findViewById(R.id.tuitionPostLayout) ;
            holder.responseButton = convertView.findViewById(R.id.responseButton) ;
            holder.postTextView = convertView.findViewById(R.id.postTextView) ;
            holder.layout.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag() ;
        }

        System.out.println(tuitionPostInfo.get(position).toString());
        holder.postTextView.setText(tuitionPostInfo.get(position).toString());

        holder.responseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBoxInfo messageBoxInfo = new MessageBoxInfo(tuitionPostInfo.get(position).getGuardianMobileNumberFK(), userEmail, false, true) ;
                myRefMessageBox.push().setValue(messageBoxInfo) ;
            }
        });


        return convertView;
    }

    class ViewHolder{
        LinearLayout layout ;
        TextView postTextView ;
        Button responseButton ;
    }
}
