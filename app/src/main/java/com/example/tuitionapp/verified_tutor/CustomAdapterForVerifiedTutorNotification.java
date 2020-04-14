package com.example.tuitionapp.verified_tutor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.tuitionapp.candidate_tutor.ReferInfo;
import com.example.tuitionapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class CustomAdapterForVerifiedTutorNotification extends BaseAdapter {

    private ArrayList<String> email;
    private DatabaseReference myRefRefer = FirebaseDatabase.getInstance().getReference("Refer");
    private Map<String,ReferInfo> map  ;
    private Context context;
    private String userEmail;

    public CustomAdapterForVerifiedTutorNotification(Context context, ArrayList<String> email, Map<String,ReferInfo> map, String userEmail) {
        this.email = email;
        this.context = context;
        this.userEmail = userEmail;
        this.map = map ;
    }

    @Override
    public int getCount() {
        return email.size();
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
        if (convertView == null) {
            holder = new ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_custom_adapter_list_view, null);
            holder.emailTextView = convertView.findViewById(R.id.emailTextView);
            holder.button = convertView.findViewById(R.id.approveButton);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag() ;
        }

        holder.emailTextView.setText("Do You Know this Email: \"" + email.get(position) + "\"");
        holder.emailTextView.setVisibility(View.VISIBLE);
        holder.button.setVisibility(View.VISIBLE);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.button.setText("Approved");
                holder.button.setEnabled(false);

                for(Map.Entry m:map.entrySet()){
                    ReferInfo info = (ReferInfo) m.getValue() ;

                    if (info.getCandidateTutorEmail().equals(email.get(position)) && info.getVerifiedTutorEmail().equals(userEmail)) {
                        if (info.isReferApprove() != true) {
                            info.setReferApprove(true);
                            myRefRefer.child(m.getKey().toString()).setValue(info);
                        }
                    }

                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView emailTextView ;
        Button button ;

    }
}



