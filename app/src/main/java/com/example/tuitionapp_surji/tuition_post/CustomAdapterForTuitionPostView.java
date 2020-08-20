package com.example.tuitionapp_surji.tuition_post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tuitionapp_surji.guardian.GuardianInformationViewActivity;
import com.example.tuitionapp_surji.message_box.MessageBoxInfo;
import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.notification_pack.SendNotification;
import com.example.tuitionapp_surji.notification_pack.NotificationInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomAdapterForTuitionPostView extends BaseAdapter {

    private Context context ;
    private ArrayList<TuitionPostInfo> tuitionPostInfo ;
    private ArrayList<String> tutorInfo ;
    private ArrayList<String> tuitionPostInfoUid ;
    private int [] responsePostArray ;
    private String userFlag ;
    private MessageBoxInfo messageBoxInfo;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("E, dd MMM yyyy") ;
    private String today ;
    private String yesterday ;

    private DatabaseReference myRefNotification, myRefTuitionPost, myRefResponsePost, myRefNotification2 ;

    public CustomAdapterForTuitionPostView(Context context, ArrayList<TuitionPostInfo> tuitionPostInfo, ArrayList<String> tutorInfo, ArrayList<String> tuitionPostInfoUid, String userFlag, int [] responsePostArray) {
        this.context = context;
        this.tuitionPostInfo = tuitionPostInfo;
        this.tutorInfo = tutorInfo  ;
        this.tuitionPostInfoUid = tuitionPostInfoUid;
        this.userFlag = userFlag ;
        this.responsePostArray = responsePostArray ;
        myRefNotification = FirebaseDatabase.getInstance().getReference("Notification").child("Guardian") ;

        if(userFlag.equals("tutor")) {
            myRefResponsePost = FirebaseDatabase.getInstance().getReference("ResponsePost").child(tutorInfo.get(3));
        }

        today = simpleDateFormat2.format(calendar.getTime());
        calendar.add(Calendar.DATE,-1);
        yesterday = simpleDateFormat2.format(calendar.getTime());

    }



    public void setListData(ArrayList<TuitionPostInfo> data){
        tuitionPostInfo = data ;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder ;
        if(convertView==null){
            holder = new ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_adapter_tuition_post_list_view, null);
            holder.layout = convertView.findViewById(R.id.tuitionPostLayout) ;
            holder.cardLayout = convertView.findViewById(R.id.cardLayout) ;
            holder.responseButton = convertView.findViewById(R.id.responseButton) ;
            holder.responseButtonLayout = convertView.findViewById(R.id.responseButtonLayout) ;
            holder.responseButtonPressedLayout = convertView.findViewById(R.id.responseButtonPressedLayout) ;

            holder.guardianPic = convertView.findViewById(R.id.guardianPic) ;
            holder.postImage = convertView.findViewById(R.id.image_view) ;
            holder.postTitle = convertView.findViewById(R.id.postTitle) ;
            holder.postTime = convertView.findViewById(R.id.postTime) ;
            holder.class_name = convertView.findViewById(R.id.class_name) ;
            holder.medium = convertView.findViewById(R.id.medium) ;
            holder.subject = convertView.findViewById(R.id.subject) ;
            holder.location = convertView.findViewById(R.id.location) ;
            holder.days = convertView.findViewById(R.id.days) ;
            holder.salary = convertView.findViewById(R.id.salary) ;
            holder.genderPreferable = convertView.findViewById(R.id.genderPreferable) ;
            holder.extra = convertView.findViewById(R.id.extraNotes) ;
            holder.availability = convertView.findViewById(R.id.availability) ;

            holder.mediumLayout = convertView.findViewById(R.id.mediumLayout) ;

            holder.setAvailabilityButton = convertView.findViewById(R.id.set_availability_button) ;
            holder.editPostButton = convertView.findViewById(R.id.edit_post_button) ;
            holder.layout2 = convertView.findViewById(R.id.create_new_post_layout) ;
            holder.createNewPostButton = convertView.findViewById(R.id.create_new_post) ;
            holder.availabilityLayout = convertView.findViewById(R.id.availability_layout) ;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag() ;
        }

        if(userFlag.equals("guardian") && tuitionPostInfo.get(position).getStudentClass()==null){
            holder.layout.setVisibility(View.GONE);
            holder.layout2.setVisibility(View.VISIBLE);
            holder.availability.setVisibility(View.VISIBLE);
            holder.createNewPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(parent.getContext(), TuitionPostCreationActivity.class) ;
                    intent.putExtra("type","newPost") ;
                    parent.getContext().startActivity(intent);
                }
            });
        }

        else {

            holder.layout.setVisibility(View.VISIBLE);
            holder.layout2.setVisibility(View.GONE);

            if (userFlag.equals("guardian")) {
                holder.availabilityLayout.setVisibility(View.VISIBLE);
                holder.setAvailabilityButton.setVisibility(View.VISIBLE);
                holder.editPostButton.setVisibility(View.VISIBLE);
                if(tuitionPostInfo.get(position).getAvailability()!=null){
                    holder.availability.setText(tuitionPostInfo.get(position).getAvailability());
                    if(tuitionPostInfo.get(position).getAvailability().equals("Available")){
                        holder.setAvailabilityButton.setText("Set \"NOT AVAILABLE\"");
                        holder.setAvailabilityButton.setTextColor(Color.RED);
                        holder.availability.setTextColor(Color.GREEN);
                    }
                    else {
                        holder.setAvailabilityButton.setText("Set \"AVAILABLE\"");
                        holder.setAvailabilityButton.setTextColor(Color.GREEN);
                        holder.availability.setTextColor(Color.RED );
                    }

                }
            } else {

                if(responsePostArray[position]==0){
                    holder.responseButtonLayout.setVisibility(View.VISIBLE);
                    holder.responseButtonPressedLayout.setVisibility(View.GONE);
                }
                else{
                    holder.responseButtonPressedLayout.setVisibility(View.VISIBLE);
                    holder.responseButtonLayout.setVisibility(View.GONE);
                }
            }

            if(tuitionPostInfo.get(position).getGuardianProfilePicUri()!=null){
                if(!tuitionPostInfo.get(position).getGuardianProfilePicUri().equals("")){
                    Picasso.get().load(tuitionPostInfo.get(position).getGuardianProfilePicUri()).into(holder.guardianPic);
                }
            }

            holder.postTitle.setText(tuitionPostInfo.get(position).getPostTitle());

            String time = tuitionPostInfo.get(position).getPostTime();
            String day = tuitionPostInfo.get(position).getPostDate();
            if (day.equals(today)) {
                holder.postTime.setText("Today at " + time);
            } else if (day.equals(yesterday)) {
                holder.postTime.setText("Yesterday at " + time);
            } else {
                holder.postTime.setText(day + " at " + time);
            }

            holder.class_name.setText(tuitionPostInfo.get(position).getStudentClass());

            String mediumString = tuitionPostInfo.get(position).getStudentMedium();
            if (!mediumString.equals("Bangla Medium")) {
                holder.medium.setText(mediumString);
            }
            else holder.mediumLayout.setVisibility(View.GONE);

            holder.subject.setText(tuitionPostInfo.get(position).getStudentSubjectList());

            if (!tuitionPostInfo.get(position).getStudentFullAddress().equals("")) {
                holder.location.setText(tuitionPostInfo.get(position).getStudentFullAddress() + ", " + tuitionPostInfo.get(position).getStudentAreaAddress());
            } else holder.location.setText(tuitionPostInfo.get(position).getStudentAreaAddress());
            holder.days.setText(tuitionPostInfo.get(position).getDaysPerWeekOrMonth());
            holder.salary.setText(tuitionPostInfo.get(position).getSalary());

            holder.genderPreferable.setVisibility(View.VISIBLE);
            holder.extra.setVisibility(View.VISIBLE);


            if (!tuitionPostInfo.get(position).getTutorGenderPreference().equals("Both Male And Female")) {
                holder.genderPreferable.setText("*Tutor Preferable: " + tuitionPostInfo.get(position).getTutorGenderPreference());
            } else if (tuitionPostInfo.get(position).getStudentContactNo().equals("")) {
                holder.genderPreferable.setVisibility(View.GONE);
            }

            if (!tuitionPostInfo.get(position).getExtra().equals("")) {
                holder.extra.setText("**" + tuitionPostInfo.get(position).getExtra());
            } else if (tuitionPostInfo.get(position).getExtra().equals(""))
                holder.extra.setVisibility(View.GONE);

            if(tuitionPostInfo.get(position).getStudentMedium().equals("English Medium")){
                holder.postImage.setImageResource(R.drawable.logo_english_medium);
            }
            else if(tuitionPostInfo.get(position).getStudentGroup().equals("Science")){
                holder.postImage.setImageResource(R.drawable.logo_science);
            }
            else if(tuitionPostInfo.get(position).getStudentGroup().equals("Commerce")){
                holder.postImage.setImageResource(R.drawable.logo_commerce);
            }
            else if(tuitionPostInfo.get(position).getStudentGroup().equals("Arts")){
                holder.postImage.setImageResource(R.drawable.logo_humanities);
            }
            else if(tuitionPostInfo.get(position).getStudentClass().equals("CLASS 8")){
                holder.postImage.setImageResource(R.drawable.logo_class8);
            }
            else if(tuitionPostInfo.get(position).getStudentClass().equals("CLASS 7")){
                holder.postImage.setImageResource(R.drawable.logo_class7);
            }
            else if(tuitionPostInfo.get(position).getStudentClass().equals("CLASS 6")){
                holder.postImage.setImageResource(R.drawable.logo_class6);
            }
            else if(tuitionPostInfo.get(position).getStudentClass().equals("CLASS 5")){
                holder.postImage.setImageResource(R.drawable.logo_class5);
            }
            else if(tuitionPostInfo.get(position).getStudentClass().equals("CLASS 4")||tuitionPostInfo.get(position).getStudentClass().equals("CLASS 3")||
                    tuitionPostInfo.get(position).getStudentClass().equals("CLASS 2")||tuitionPostInfo.get(position).getStudentClass().equals("CLASS 1")
                    ||tuitionPostInfo.get(position).getStudentClass().equals("NURSERY")||tuitionPostInfo.get(position).getStudentClass().equals("PLAY")){
                holder.postImage.setImageResource(R.drawable.logo_primary);
            }
            else {
                holder.postImage.setImageResource(R.drawable.logo_else_class);
            }

            holder.responseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRefNotification2 = myRefNotification.child(tuitionPostInfo.get(position).getGuardianUidFK()) ;
                    NotificationInfo notificationInfo = new NotificationInfo("response" , tutorInfo.get(0), tutorInfo.get(2), tutorInfo.get(3), tuitionPostInfoUid.get(position)) ;
                    myRefNotification2.push().setValue(notificationInfo) ;

                    ResponsePost responsePost = new ResponsePost(tuitionPostInfoUid.get(position)) ;
                    myRefResponsePost.push().setValue(responsePost) ;


                    holder.responseButton.setBackgroundColor(Color.GRAY);
                    holder.responseButtonLayout.setEnabled(false);

                    SendNotification sendNotification = new SendNotification(tuitionPostInfo.get(position).getGuardianUidFK(), "Response Post", "A tutor response to your post") ;
                    sendNotification.sendNotificationOperation();
                }
            });


            holder.setAvailabilityButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    myRefTuitionPost = FirebaseDatabase.getInstance().getReference("TuitionPost").child(tuitionPostInfoUid.get(position)) ;

                    myRefTuitionPost.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            TuitionPostInfo tuitionPostInfo = dataSnapshot.getValue(TuitionPostInfo.class);

                            if(tuitionPostInfo.getAvailability()==null){
                                tuitionPostInfo.setAvailability("Not Available");
                            }
                            else if(tuitionPostInfo.getAvailability().equals("Available")){
                                tuitionPostInfo.setAvailability("Not Available");
                            }
                            else if(tuitionPostInfo.getAvailability().equals("Not Available")){
                                tuitionPostInfo.setAvailability("Available");
                            }

                            myRefTuitionPost.setValue(tuitionPostInfo) ;

                            myRefTuitionPost.removeEventListener(this);

                            Intent intent = new Intent(parent.getContext(), TuitionPostViewActivity.class) ;
                            intent.putExtra("user","guardian") ;
                            parent.getContext().startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }) ;
                }
            });

            holder.editPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(parent.getContext(), TuitionPostCreationActivity.class) ;
                    intent.putExtra("type","editPost") ;
                    intent.putExtra("tuitionPostID" , tuitionPostInfoUid.get(position)) ;
                    parent.getContext().startActivity(intent) ;
                }
            });

        }

        if(userFlag.equals("tutor")){
            holder.guardianPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(parent.getContext(), GuardianInformationViewActivity.class) ;
                    intent.putExtra("guardianUid",tuitionPostInfo.get(position).getGuardianUidFK()) ;
                    intent.putExtra("user", "tutor") ;
                    intent.putStringArrayListExtra("tutorInfo", tutorInfo) ;
                    parent.getContext().startActivity(intent) ;
                }
            });
        }

        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), TuitionPostViewSinglePageActivity.class);

                intent.putStringArrayListExtra("tutorInfo", tutorInfo);
                intent.putExtra("guardianUid", tuitionPostInfo.get(position).getGuardianUidFK()) ;
                intent.putExtra("user", userFlag) ;
                intent.putExtra("response", String.valueOf(responsePostArray[position])) ;
                intent.putExtra("tuitionPostUid", tuitionPostInfoUid.get(position)) ;

                intent.putExtra("postTitle", tuitionPostInfo.get(position).getPostTitle()) ;
                intent.putExtra("medium", tuitionPostInfo.get(position).getStudentMedium()) ;
                intent.putExtra("class_name", tuitionPostInfo.get(position).getStudentClass()) ;
                intent.putExtra("group", tuitionPostInfo.get(position).getStudentGroup()) ;
                intent.putExtra("subject", tuitionPostInfo.get(position).getStudentSubjectList()) ;
                intent.putExtra("studentInstituteName", tuitionPostInfo.get(position).getStudentInstitute()) ;
                if(!tuitionPostInfo.get(position).getStudentFullAddress().equals("")){
                    intent.putExtra("address", tuitionPostInfo.get(position).getStudentFullAddress() +", " + tuitionPostInfo.get(position).getStudentAreaAddress()) ;
                }else{
                    intent.putExtra("address",  tuitionPostInfo.get(position).getStudentAreaAddress()) ;
                }
                intent.putExtra("genderPreferable", tuitionPostInfo.get(position).getStudentContactNo()) ;
                intent.putExtra("daysPerWeek", tuitionPostInfo.get(position).getDaysPerWeekOrMonth()) ;
                intent.putExtra("salary", tuitionPostInfo.get(position).getSalary()) ;
                intent.putExtra("extraInfo", tuitionPostInfo.get(position).getExtra()) ;
                intent.putExtra("postTime", tuitionPostInfo.get(position).getPostDate() + ", " + tuitionPostInfo.get(position).getPostTime()) ;
                intent.putExtra("tutorGenderPreferable", tuitionPostInfo.get(position).getTutorGenderPreference()) ;
                parent.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder{
        LinearLayout cardLayout ;
        ImageView guardianPic ;
        ImageView postImage ;
        LinearLayout layout ;
        TextView postTime ;
        TextView postTitle, class_name, medium, subject, location, days, salary, genderPreferable, extra, availability ;
        LinearLayout mediumLayout ;
        ImageButton responseButton ;
        RelativeLayout responseButtonLayout ;
        RelativeLayout responseButtonPressedLayout ;

        RelativeLayout layout2 ;
        ImageButton createNewPostButton ;
        Button setAvailabilityButton ;
        Button editPostButton ;
        LinearLayout availabilityLayout ;
    }
}
