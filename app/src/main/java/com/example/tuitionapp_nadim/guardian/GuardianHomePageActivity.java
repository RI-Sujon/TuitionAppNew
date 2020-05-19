package com.example.tuitionapp_nadim.guardian;

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
import android.widget.TextView;

import com.example.tuitionapp_nadim.R;
import com.example.tuitionapp_nadim.admin.ApproveAndBlockInfo;
import com.example.tuitionapp_nadim.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_nadim.group.GroupInfo;
import com.example.tuitionapp_nadim.message_box.MainMessageActivity;
import com.example.tuitionapp_nadim.system.HomePageActivity;
import com.example.tuitionapp_nadim.tuition_post.TuitionPostViewActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class GuardianHomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference myRefVerifiedTutor, myRefCandidateTutor, myRefApproveAndBlock ;

    private DrawerLayout drawerLayout ;
    private NavigationView navigationView ;
    private RecyclerView recyclerView, recyclerView2 ;
    private View view ;

    private ArrayList<ApproveAndBlockInfo> approveAndBlockInfoList ;
    private ArrayList<CandidateTutorInfo> candidateTutorInfoArrayList ;
    private ArrayList<String> tutorUidArrayList;

    private ArrayList<GroupInfo> groupInfoList ;

    private ArrayList<String> approveAndBlockTutorUidList ;
    private ArrayList<String> groupNameList ;
    private ArrayList<String> groupIDList ;

    private Map<String,String> profilePicUriListMap ;

    private TextView guardianMobileNo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_home_page);

        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout) ;
        navigationView = findViewById(R.id.navigation_view) ;
        recyclerView = findViewById(R.id.recycler_view) ;
        recyclerView2 = findViewById(R.id.recycler_view2) ;

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close) ;
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        recyclerViewOperation2();

        view = navigationView.getHeaderView(0) ;

        guardianMobileNo = (TextView)view.findViewById(R.id.email) ;
        guardianMobileNo.setText(mAuth.getCurrentUser().getPhoneNumber());

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.view_profile).setVisible(false) ;
        menu.findItem(R.id.calender).setVisible(false) ;
        menu.findItem(R.id.reminder).setVisible(false) ;
        menu.findItem(R.id.notes).setVisible(false) ;


    }

    @Override
    protected void onStart() {
        super.onStart();

        approveAndBlockTutorUidList = new ArrayList<>() ;
        tutorUidArrayList = new ArrayList<>();
        approveAndBlockInfoList = new ArrayList<>() ;
        candidateTutorInfoArrayList = new ArrayList<>() ;

        groupInfoList = new ArrayList<>() ;
        groupNameList = new ArrayList<>() ;
        groupIDList = new ArrayList<>() ;

        myRefApproveAndBlock = FirebaseDatabase.getInstance().getReference("ApproveAndBlock") ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor") ;

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
                                if(approveAndBlockTutorUidList.get(i).equals(dS1.getKey()) && approveAndBlockInfoList.get(i).getStatus().equals("running")){
                                    candidateTutorInfoArrayList.add(candidateTutorInfo) ;
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
    }

    private void recyclerViewOperation() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        RecyclerAdapterForGuardianHomePage1 adapter = new RecyclerAdapterForGuardianHomePage1(candidateTutorInfoArrayList) ;
        recyclerView.setAdapter(adapter);
    }

    private void recyclerViewOperation2() {
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        RecyclerAdapterForGuardianHomePage2 adapter = new RecyclerAdapterForGuardianHomePage2(approveAndBlockTutorUidList,approveAndBlockTutorUidList,approveAndBlockTutorUidList) ;
        recyclerView2.setAdapter(adapter);
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
        finish();
    }

    public void signOut() {
        mAuth.signOut();

        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }


    public void openDrawerOperation(View view){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out:
                signOut();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
