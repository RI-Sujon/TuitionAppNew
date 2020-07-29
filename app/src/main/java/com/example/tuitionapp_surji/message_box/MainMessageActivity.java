package com.example.tuitionapp_surji.message_box;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.guardian.GuardianHomePageActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorHomePageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainMessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference, candidateTutorReference;

    private String checkUser;
    ArrayList<String> userInfo ;
    CandidateTutorInfo tutorInfo;
    MessageBoxInfo messageBoxUser;

    TextView toolbar_name;

    String tutorName;
    String tutorEmail;

    ImageView chats_btn,request_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_message);

        toolbar_name = findViewById(R.id.toolbar_name);

        Intent intent = getIntent() ;
        checkUser = intent.getStringExtra("user");
        userInfo = intent.getStringArrayListExtra("userInfo") ;


        //Toolbar toolbar=findViewById(R.id.toolbar_message);
       // setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle(" ");

        profile_image=findViewById(R.id.profile_image);
        chats_btn = findViewById(R.id.chats_button);
        request_btn = findViewById(R.id.message_request_button);
        //username=findViewById(R.id.username_message);

        tutorInfo = new CandidateTutorInfo();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("MessageBox");//.child(firebaseUser.getUid());
        candidateTutorReference = FirebaseDatabase.getInstance().getReference("CandidateTutor");



        if(checkUser.equals("guardian"))
        {
           // System.out.println("Guardian ID ======================================"+firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot1)
                {
                    for(DataSnapshot snapshot:dataSnapshot1.getChildren()){
                 //       System.out.println("kaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                        System.out.println();
                        MessageBoxInfo user=snapshot.getValue(MessageBoxInfo.class);
                        if(user.getGuardianUid().equals(firebaseUser.getUid())){
                            System.out.println("getTutorId ============================= "+user.getTutorUid());
                            messageBoxUser = user;
                         //   System.out.println("AAAAAAAAAAAAAAAAAAAAAAA ==== "+messageBoxUser.getTutorEmail());
                            break;
                        }


                    }

                    if(messageBoxUser != null){
                          tutorEmail = messageBoxUser.getTutorEmail();
                    }

                    candidateTutorReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                CandidateTutorInfo candidateTutorInfo = snapshot.getValue(CandidateTutorInfo.class);
                                if(candidateTutorInfo.getEmailPK().equals(tutorEmail)){
                                //    System.out.println("Email =============================" +candidateTutorInfo.getEmailPK());
                                    tutorInfo = candidateTutorInfo;
                                  //  System.out.println("Name =============="+tutorInfo.getUserName());
                                    tutorName = tutorInfo.getUserName();
                                 // System.out.println("Name =============="+tutorName);

                                    break;
                                }


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

             /*   if(checkUser.equals("guardian")){
                    username.setText("Guardian");
                }

                else if(checkUser.equals("tutor")){
                    username.setText("Tutor");
                }

               if("default".equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }*/

               /* else{
                    Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_image);
                }*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//            username.setText("Guardian");
        }

        else{
            reference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        MessageBoxInfo user=snapshot.getValue(MessageBoxInfo.class);
                        if(user.getTutorUid().equals(firebaseUser.getUid())){
                            messageBoxUser = user;
                        }

                    }

                  //  final String tutorEmail = messageBoxUser.getTutorEmail();

                    candidateTutorReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                CandidateTutorInfo candidateTutorInfo = snapshot.getValue(CandidateTutorInfo.class);



                                if(candidateTutorInfo.getEmailPK().equals(firebaseUser.getEmail())){
                                  //  System.out.println("Email =============================" +candidateTutorInfo.getEmailPK());
                                  //  System.out.println("Email =============================" +tutorEmail);
                                    tutorInfo = candidateTutorInfo;
                                   // System.out.println("Name =============="+tutorInfo.getUserName());
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

             /*   if(checkUser.equals("guardian")){
                    username.setText("Guardian");
                }

                else if(checkUser.equals("tutor")){
                    username.setText("Tutor");
                }

               if("default".equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }*/

               /* else{
                    Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_image);
                }*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

          //  username.setText("Tutor");

        }

//        profile_image.setImageResource(R.mipmap.ic_launcher);


      //  TabLayout tabLayout=findViewById(R.id.tab_layout_message);
        Toolbar toolbar= findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUser.equals("tutor")){

                    Intent intent1 = getIntent() ;
                    userInfo = intent1.getStringArrayListExtra("userInfo") ;
                    Intent intent = new Intent(MainMessageActivity.this, VerifiedTutorHomePageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                    startActivity(intent);
                    //finish();
                }
                else if(checkUser.equals("guardian")){
                    Intent intent = new Intent(MainMessageActivity.this, GuardianHomePageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //finish();
                }
            }
        });

         final ViewPager viewPager =findViewById(R.id.view_pager_message);

         ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter( getSupportFragmentManager(),2);

         Fragment userFragment = new UsersFragment(checkUser,tutorName,userInfo);
         Fragment requestFragment = new MessageRequestsFragment(checkUser,tutorName,userInfo);


        viewPagerAdapter.addFragment(userFragment,"Chats");
        viewPagerAdapter.addFragment(requestFragment,"Requests");


        //  viewPagerAdapter.addFragment(new UsersFragment(checkUser,tutorName,userInfo),"Chats");
        // viewPagerAdapter.addFragment(new MessageRequestsFragment(),"Requests");
        // tabLayout.setupWithViewPager(viewPager);
        //toolbar.setupWithViewPager(viewPager);




        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(1);
            }
        });




        chats_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               viewPager.setCurrentItem(0);
            }
        });


        viewPager.setAdapter(viewPagerAdapter);

    }


    public class  ViewPagerAdapter  extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private  ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public  void  addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public CharSequence getPageTitle(int position){
            return titles.get(position);
        }
    }


    private void status(String status){

        if(checkUser.equals("tutor")){
            reference = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(firebaseUser.getUid());
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("status",status);
            reference.updateChildren(hashMap);
        }

        else if(checkUser.equals("guardian")){
            reference = FirebaseDatabase.getInstance().getReference("Guardian").child(firebaseUser.getUid());
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("status",status);
            reference.updateChildren(hashMap);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //reference.removeEventListener(seenListener);
        status("offline");

    }

}
