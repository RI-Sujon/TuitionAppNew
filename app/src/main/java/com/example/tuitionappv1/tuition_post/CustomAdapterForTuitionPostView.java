package com.example.tuitionappv1.tuition_post;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.tuitionappv1.message_box.MessageBoxInfo;
import com.example.tuitionappv1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomAdapterForTuitionPostView extends BaseAdapter {

    private Context context ;
    private ArrayList<TuitionPostInfo> tuitionPostInfo ;
    private String userEmail ;
    private  String tutorUid ;
    MessageBoxInfo messageBoxInfo;

    private DatabaseReference myRefMessageBox;

    public CustomAdapterForTuitionPostView(Context context, ArrayList<TuitionPostInfo> tuitionPostInfo, String userEmail, String tutorUid) {
        this.context = context;
        this.tuitionPostInfo = tuitionPostInfo;
        this.userEmail = userEmail ;
        this.tutorUid = tutorUid;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder ;
        if(convertView==null){
            holder = new ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_custom_adapter_list_view, null);
            holder.layout = convertView.findViewById(R.id.tuitionPostLayout) ;
            holder.responseButton = convertView.findViewById(R.id.responseButton) ;
            holder.postTextView = convertView.findViewById(R.id.postTextView) ;
            holder.postTime = convertView.findViewById(R.id.postTime) ;
            holder.postDate = convertView.findViewById(R.id.postDate) ;
            holder.layout.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        }

        else{
            holder = (ViewHolder) convertView.getTag() ;
        }

        holder.postTextView.setText(tuitionPostInfo.get(position).toString());
        holder.postTime.setText("TIME: " + tuitionPostInfo.get(position).getPostTime());
        holder.postDate.setText("DATE: " + tuitionPostInfo.get(position).getPostDate());

        holder.responseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 messageBoxInfo = new MessageBoxInfo(tuitionPostInfo.get(position).getGuardianMobileNumberFK(),
                        tuitionPostInfo.get(position).getGuardianUidFK(), userEmail, tutorUid, false, true) ;

            /*   myRefMessageBox.orderByChild("tutorUid").equalTo(tutorUid).addChildEventListener(new ChildEventListener()
               {
                   private int flag =0;
                   @Override
                   public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                       myRefMessageBox.orderByChild("guardianUidFK").equalTo(tuitionPostInfo.get(position).getGuardianUidFK()).addChildEventListener(new ChildEventListener() {
                           @Override
                           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                               MessageBoxInfo messageBoxInfo2 = dataSnapshot.getValue(MessageBoxInfo.class);
                               if(messageBoxInfo2.)

                           }

                           @Override
                           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                           }

                           @Override
                           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                           }

                           @Override
                           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                   }

                   @Override
                   public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                   }

                   @Override
                   public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                   }

                   @Override
                   public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
*/

                myRefMessageBox.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int flag = 0;
                                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                {
                                    MessageBoxInfo messageBoxInfo1 = snapshot.getValue(MessageBoxInfo.class);
                                    if(messageBoxInfo1.getGuardianUid().equals(tuitionPostInfo.get(position).getGuardianUidFK())
                                            && messageBoxInfo1.getTutorUid().equals(tutorUid)){
                                        flag=1;
                                    }

                                }

                        if(flag == 0)
                            myRefMessageBox.push().setValue(messageBoxInfo) ;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                holder.responseButton.setBackgroundColor(Color.GRAY);
                holder.responseButton.setEnabled(false);
            }
        });


        return convertView;
    }

    class ViewHolder{
        LinearLayout layout ;
        TextView postTextView ;
        TextView postTime ;
        TextView postDate ;
        Button responseButton ;
    }
}
