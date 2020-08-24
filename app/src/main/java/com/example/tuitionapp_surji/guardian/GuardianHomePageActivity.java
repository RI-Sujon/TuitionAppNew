package com.example.tuitionapp_surji.guardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.admin.ApproveAndBlockInfo;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.group.GroupInfo;
import com.example.tuitionapp_surji.message_box.MainMessageActivity;
import com.example.tuitionapp_surji.notification_pack.TokenInfo;
import com.example.tuitionapp_surji.starting.HomePageActivity;
import com.example.tuitionapp_surji.tuition_post.TuitionPostViewActivity;
import com.example.tuitionapp_surji.notification_pack.NotificationViewActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class GuardianHomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference myRefVerifiedTutor, myRefCandidateTutor, myRefApproveAndBlock, myRefGroupInfo ;

    private DrawerLayout drawerLayout ;
    private NavigationView navigationView ;
    private RecyclerView recyclerView, recyclerView2 ;
    private View view ;

    private ArrayList<ApproveAndBlockInfo> approveAndBlockInfoList ;
    private ArrayList<CandidateTutorInfo> candidateTutorInfoArrayList ;
    private ArrayList<String> tutorUidArrayList;

    private ArrayList<GroupInfo> groupInfoArrayList ;

    private ArrayList<String> approveAndBlockTutorUidList ;
    private ArrayList<String> groupNameList ;
    private ArrayList<String> groupInfoUidList ;

    private DatabaseReference myRefGuardian ;
    private DatabaseReference reference;

    private TextView guardianMobileNo, guardianName ;
    private ImageView profilePic ;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_home_page);

        String refreshToken = FirebaseInstanceId.getInstance().getToken() ;
        TokenInfo token = new TokenInfo(refreshToken) ;
        FirebaseDatabase.getInstance().getReference("Notification").child("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser().getUid();
        myRefGuardian = FirebaseDatabase.getInstance().getReference("Guardian").child(mAuth.getCurrentUser().getUid()) ;

        drawerLayout = findViewById(R.id.drawer_layout) ;
        navigationView = findViewById(R.id.navigation_view) ;
        recyclerView = findViewById(R.id.recycler_view) ;
        recyclerView2 = findViewById(R.id.recycler_view2) ;

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close) ;
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        view = navigationView.getHeaderView(0) ;

        guardianName = view.findViewById(R.id.name);
        guardianMobileNo = view.findViewById(R.id.email);
        profilePic = view.findViewById(R.id.profile_image);

        guardianMobileNo.setText(mAuth.getCurrentUser().getPhoneNumber());

        myRefGuardian.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GuardianInfo guardianInfo = dataSnapshot.getValue(GuardianInfo.class) ;
                guardianName.setText(guardianInfo.getName());

                if(!guardianInfo.getProfilePicUri().equals("1")){
                    Picasso.get().load(guardianInfo.getProfilePicUri()).into(profilePic) ;
                }

                else
                    profilePic.setImageResource(R.drawable.man);


                myRefGuardian.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;


        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.view_profile).setVisible(false) ;
        menu.findItem(R.id.calenderMenuId).setVisible(false) ;
        menu.findItem(R.id.notes).setVisible(false) ;

    }

    @Override
    protected void onStart() {
        super.onStart();

        approveAndBlockTutorUidList = new ArrayList<>() ;
        tutorUidArrayList = new ArrayList<>();
        approveAndBlockInfoList = new ArrayList<>() ;
        candidateTutorInfoArrayList = new ArrayList<>() ;

        groupInfoArrayList = new ArrayList<>() ;
        groupNameList = new ArrayList<>() ;
        groupInfoUidList = new ArrayList<>() ;

        myRefApproveAndBlock = FirebaseDatabase.getInstance().getReference("ApproveAndBlock") ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor") ;
        myRefGroupInfo = FirebaseDatabase.getInstance().getReference("Group") ;

        myRefApproveAndBlock.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0 ;
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    ApproveAndBlockInfo approveAndBlockInfo = dS1.getValue(ApproveAndBlockInfo.class) ;

                    approveAndBlockInfoList.add(approveAndBlockInfo) ;
                    approveAndBlockTutorUidList.add(dS1.getKey()) ;

                    if(i>=20) break;
                    i++ ;
                }

                myRefCandidateTutor.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int j = 0 ;
                        for(DataSnapshot dS1: dataSnapshot.getChildren()){
                            CandidateTutorInfo candidateTutorInfo = dS1.getValue(CandidateTutorInfo.class) ;
                            for(int i=0 ; i<approveAndBlockTutorUidList.size(); i++){
                                if(approveAndBlockTutorUidList.get(i).equals(dS1.getKey()) && approveAndBlockInfoList.get(i).getStatus().equals("running") && candidateTutorInfo.isTutorAvailable()){
                                    candidateTutorInfoArrayList.add(candidateTutorInfo) ;
                                    tutorUidArrayList.add(dS1.getKey()) ;
                                }
                            }

                            if(j>=20) break;
                            j++ ;
                        }

                        recyclerViewOperation();

                        myRefCandidateTutor.removeEventListener(this);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });

                myRefApproveAndBlock.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        myRefGroupInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    GroupInfo groupInfo = dS1.getValue(GroupInfo.class) ;
                    groupInfoArrayList.add(groupInfo) ;
                    groupInfoUidList.add(dS1.getKey()) ;
                    recyclerViewOperation2();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;
    }

    private void recyclerViewOperation() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        RecyclerAdapterForGuardianHomePage1 adapter = new RecyclerAdapterForGuardianHomePage1(candidateTutorInfoArrayList, tutorUidArrayList) ;
        recyclerView.setAdapter(adapter);
    }

    private void recyclerViewOperation2() {
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        RecyclerAdapterForGuardianHomePage2 adapter2 = new RecyclerAdapterForGuardianHomePage2(groupInfoArrayList, groupInfoUidList) ;
        recyclerView2.setAdapter(adapter2);
    }


    public void goToGuardianTuitionPostView(View view) {
        Intent intent = new Intent(this, TuitionPostViewActivity.class);
        intent.putExtra("user", "guardian") ;
        startActivity(intent);
        finish();
    }

    public void goToMessageBox(View view){
        Intent intent = new Intent(this, MainMessageActivity.class);
        intent.putExtra("user","guardian") ;
        startActivity(intent);
        finish();
    }

    public void goToGuardianTutorProfileViewActivity(View view){
        Intent intent = new Intent(this, ViewingSearchingTutorProfileActivity.class) ;
        intent.putExtra("user","guardian") ;
        startActivity(intent);
        //finish();
    }

    public void goToVerifiedTutorNotificationActivity(View view){
        Intent intent = new Intent(this, NotificationViewActivity.class) ;
        intent.putExtra("user", "guardian") ;
        startActivity(intent);
        //finish();
    }

    public void goToGuardianProfileViewActivity(){
        Intent intent = new Intent(this, GuardianInformationViewActivity.class) ;
        intent.putExtra("user", "guardian") ;
        startActivity(intent);
    }

    public void signOut() {
        mAuth.signOut();

        Intent intent = new Intent(GuardianHomePageActivity.this, HomePageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(intent);
        finish();
    }

    private void status(String status){
         reference = FirebaseDatabase.getInstance().getReference("Guardian").child(user);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", status);
            reference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    public void openDrawerOperation(View view){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
            //Intent intent = new Intent(Intent.ACTION_MAIN) ;
            //intent.addCategory(Intent.CATEGORY_HOME);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out:
                signOut();
                break;

            case R.id.edit_profile:
                goToGuardianProfileViewActivity();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
