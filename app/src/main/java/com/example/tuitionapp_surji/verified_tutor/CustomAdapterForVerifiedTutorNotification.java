package com.example.tuitionapp_surji.verified_tutor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.candidate_tutor.ReferInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomAdapterForVerifiedTutorNotification extends BaseAdapter {

    private Context context;
    private ArrayList<NotificationInfo> notificationInfoArrayList ;
    private ArrayList<String> userInfo ;
    private ArrayList<String> notificationInfoUidList ;

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("E, dd MMM yyyy") ;
    private String today ;
    private String yesterday ;

    private DatabaseReference myRefRefer, myRefRefer2, myRefNotification, myRefNotification2 ;

    public CustomAdapterForVerifiedTutorNotification(Context context, ArrayList<NotificationInfo> notificationInfoArrayList, ArrayList<String> userInfo, ArrayList<String> notificationInfoUidList) {
        this.context = context ;
        this.notificationInfoArrayList = notificationInfoArrayList ;
        this.userInfo = userInfo ;
        this.notificationInfoUidList = notificationInfoUidList ;

        today = simpleDateFormat2.format(calendar.getTime());
        calendar.add(Calendar.DATE,-1);
        yesterday = simpleDateFormat2.format(calendar.getTime());

        myRefRefer = FirebaseDatabase.getInstance().getReference("Refer") ;
        myRefNotification = FirebaseDatabase.getInstance().getReference("Notification").child(userInfo.get(3)) ;
    }

    @Override
    public int getCount() {
        return notificationInfoArrayList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_adapter_notification_list_view, null);
            holder.message = convertView.findViewById(R.id.message);
            holder.buttonYes = convertView.findViewById(R.id.yes_button);
            holder.buttonNo = convertView.findViewById(R.id.no_button);
            holder.time = convertView.findViewById(R.id.time) ;

            convertView.setTag(holder);

        }
        else{
            holder = (ViewHolder) convertView.getTag() ;
        }

        String time = notificationInfoArrayList.get(position).getPostTime();
        String day = notificationInfoArrayList.get(position).getPostDate();

        if (day.equals(today)) {
            holder.time.setText("Today at " + time);
        } else if (day.equals(yesterday)) {
            holder.time.setText("Yesterday at " + time);
        } else {
            holder.time.setText(day + " at " + time);
        }

        if(notificationInfoArrayList.get(position).getMessage4()==null){
            holder.message.setText("Do you know " + notificationInfoArrayList.get(position).getMessage1() + " (email: " + notificationInfoArrayList.get(position).getMessage2() + ") ?");
            holder.buttonYes.setVisibility(View.VISIBLE);
            holder.buttonNo.setVisibility(View.VISIBLE);

            holder.buttonYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRefRefer2 = myRefRefer.child(notificationInfoArrayList.get(position).getMessage3()) ;
                    myRefNotification2 = myRefNotification.child(notificationInfoUidList.get(position)) ;

                    myRefRefer2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            myRefRefer2.removeEventListener(this);
                            for (DataSnapshot dS1: dataSnapshot.getChildren()){
                                ReferInfo referInfo = dS1.getValue(ReferInfo.class) ;
                                if(referInfo.getVerifiedTutorEmail().equals(userInfo.get(2))){
                                    System.out.println("------------------------------->" + dS1.toString());
                                    referInfo.setReferApprove("yes");
                                    myRefRefer2.child(dS1.getKey()).setValue(referInfo) ;

                                    NotificationInfo notificationInfo = notificationInfoArrayList.get(position) ;
                                    notificationInfo.setMessage4("yes");
                                    myRefNotification2.setValue(notificationInfo) ;
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }) ;
                }
            }) ;

            holder.buttonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRefRefer2 = myRefRefer.child(notificationInfoArrayList.get(position).getMessage3()) ;
                    myRefNotification2 = myRefNotification.child(notificationInfoUidList.get(position)) ;

                    myRefRefer2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            myRefRefer2.removeEventListener(this);
                            for (DataSnapshot dS1: dataSnapshot.getChildren()){
                                ReferInfo referInfo = dS1.getValue(ReferInfo.class) ;
                                if(referInfo.getVerifiedTutorEmail().equals(userInfo.get(2))){
                                    referInfo.setReferApprove("no");
                                    System.out.println("------------------------------->" + dS1.toString());
                                    myRefRefer2.child(dS1.getKey()).setValue(referInfo) ;

                                    NotificationInfo notificationInfo = notificationInfoArrayList.get(position) ;
                                    notificationInfo.setMessage4("no");
                                    myRefNotification2.setValue(notificationInfo) ;
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }) ;
                }
            }) ;

        }
        else if (notificationInfoArrayList.get(position).getMessage4().equals("yes")){
            holder.message.setText("Your friend " + notificationInfoArrayList.get(position).getMessage1() + " (email: " + notificationInfoArrayList.get(position).getMessage2() + ") now in Tuition App.");
        }
        else if (notificationInfoArrayList.get(position).getMessage4().equals("no")){
            holder.message.setText("An Unknown Person. (Name: " + notificationInfoArrayList.get(position).getMessage1() + ", Email: " + notificationInfoArrayList.get(position).getMessage2() + ")");
        }


        return convertView;
    }

    class ViewHolder {
        TextView message, time;
        Button buttonYes, buttonNo ;
    }
}



