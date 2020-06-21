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
import android.widget.TextView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.guardian.GuardianHomePageActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorHomePageActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    String tutorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_message);

        Intent intent = getIntent() ;
        checkUser = intent.getStringExtra("user");

        //Toolbar toolbar=findViewById(R.id.toolbar_message);
       // setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle(" ");

        profile_image=findViewById(R.id.profile_image);
        //username=findViewById(R.id.username_message);

        tutorInfo = new CandidateTutorInfo();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("MessageBox");//.child(firebaseUser.getUid());
        candidateTutorReference = FirebaseDatabase.getInstance().getReference("CandidateTutor");



        if(checkUser.equals("guardian"))
        {
            System.out.println("Guardian ID ======================================"+firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot1)
                {
                    for(DataSnapshot snapshot:dataSnapshot1.getChildren()){
                        System.out.println("kaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                        System.out.println();
                        MessageBoxInfo user=snapshot.getValue(MessageBoxInfo.class);
                        if(user.getGuardianUid().equals(firebaseUser.getUid())){
                            System.out.println("getTutorId ============================= "+user.getTutorUid());
                            messageBoxUser = user;
                            System.out.println("AAAAAAAAAAAAAAAAAAAAAAA ==== "+messageBoxUser.getTutorEmail());
                            break;
                        }


                    }

                    final String tutorEmail = messageBoxUser.getTutorEmail();

                    candidateTutorReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                CandidateTutorInfo candidateTutorInfo = snapshot.getValue(CandidateTutorInfo.class);
                                if(candidateTutorInfo.getEmailPK().equals(tutorEmail)){
                                    System.out.println("Email =============================" +candidateTutorInfo.getEmailPK());
                                    tutorInfo = candidateTutorInfo;
                                    System.out.println("Name =============="+tutorInfo.getUserName());
                                    tutorName = tutorInfo.getUserName();
                                    System.out.println("Name =============="+tutorName);

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

                    final String tutorEmail = messageBoxUser.getTutorEmail();

                    candidateTutorReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                CandidateTutorInfo candidateTutorInfo = snapshot.getValue(CandidateTutorInfo.class);
                                if(candidateTutorInfo.getEmailPK().equals(tutorEmail)){
                                    System.out.println("Email =============================" +candidateTutorInfo.getEmailPK());
                                    System.out.println("Email =============================" +tutorEmail);
                                    tutorInfo = candidateTutorInfo;
                                    System.out.println("Name =============="+tutorInfo.getUserName());
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
                    Intent intent = new Intent(MainMessageActivity.this, VerifiedTutorHomePageActivity.class);
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                    startActivity(intent);
                    finish();
                }
                else if(checkUser.equals("guardian")){
                    Intent intent = new Intent(MainMessageActivity.this, GuardianHomePageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ViewPager viewPager =findViewById(R.id.view_pager_message);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter( getSupportFragmentManager(),2);

        viewPagerAdapter.addFragment(new UsersFragment(checkUser,tutorName),"Users");

        viewPager.setAdapter(viewPagerAdapter);

       // tabLayout.setupWithViewPager(viewPager);
        //toolbar.setupWithViewPager(viewPager);


    }

    public void backFromMessageBox(View view){

        if(checkUser.equals("tutor")){

            Intent intent1 = getIntent() ;
            userInfo = intent1.getStringArrayListExtra("userInfo") ;
            Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
            intent.putStringArrayListExtra("userInfo", userInfo) ;
            startActivity(intent);
            finish();
        }
        else if(checkUser.equals("guardian")){
            Intent intent = new Intent(this, GuardianHomePageActivity.class);
            startActivity(intent);
            finish();
        }


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

}
