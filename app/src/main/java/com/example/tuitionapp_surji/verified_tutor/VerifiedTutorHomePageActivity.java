package com.example.tuitionapp_surji.verified_tutor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.tuitionapp_surji.calendar.CalendarHomeActivity;
import com.example.tuitionapp_surji.demo_video.DemoVideoMainActivity;
import com.example.tuitionapp_surji.group.GroupCreationActivity;
import com.example.tuitionapp_surji.group.GroupHomePageActivity;
import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.group.GroupInfo;
import com.example.tuitionapp_surji.message_box.MainMessageActivity;
import com.example.tuitionapp_surji.note.Note_Home;
import com.example.tuitionapp_surji.notification_pack.TokenInfo;
import com.example.tuitionapp_surji.system.HomePageActivity;
import com.example.tuitionapp_surji.tuition_post.TuitionPostInfo;
import com.example.tuitionapp_surji.tuition_post.TuitionPostViewActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class VerifiedTutorHomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth ;
    private GoogleSignInClient mGoogleSignInClient ;
    private DatabaseReference myRefTuitionPost, myRefVerifiedTutor, myRefGroup ;
    private FirebaseUser user ;

    private VerifiedTutorInfo verifiedTutorInfo ;

    private ArrayList<String> userInfo ;
    private ArrayList<TuitionPostInfo> tuitionPostInfoArrayList1 ;
    private ArrayList<TuitionPostInfo> tuitionPostInfoArrayList2 ;
    private int count1 = 0, count2 = 0 ;

    private DrawerLayout drawerLayout ;
    private NavigationView navigationView ;
    private RecyclerView recyclerView, recyclerView2 ;

    private TextView nameTextView, emailTextView ;
    private ImageView profilePic ;
    private View view ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_verified_tutor_home_page);

        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        String refreshToken = FirebaseInstanceId.getInstance().getToken() ;
        TokenInfo token = new TokenInfo(refreshToken) ;
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);

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

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.edit_profile).setVisible(false);

        nameTextView = (TextView)view.findViewById(R.id.name) ;
        emailTextView = (TextView)view.findViewById(R.id.email) ;
        profilePic = (ImageView)view.findViewById(R.id.profile_image) ;

        nameTextView.setText(userInfo.get(0));
        emailTextView.setText(userInfo.get(2));

        if(!userInfo.get(1).equals("")){
            Picasso.get().load(userInfo.get(1)).into(profilePic) ;
        }

        mAuth = FirebaseAuth.getInstance() ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder().requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        user = mAuth.getCurrentUser() ;
        myRefTuitionPost = FirebaseDatabase.getInstance().getReference("TuitionPost");
        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor").child(user.getUid());
        myRefGroup = FirebaseDatabase.getInstance().getReference("Group");

        String gender = userInfo.get(4) ;

        tuitionPostInfoArrayList1 = new ArrayList<>() ;
        tuitionPostInfoArrayList2 = new ArrayList<>() ;

        if(gender.equals("MALE")){
            gender = "Only Male" ;
        }
        else gender = "Only Female" ;


        myRefVerifiedTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                verifiedTutorInfo = dataSnapshot.getValue(VerifiedTutorInfo.class) ;

                preparationForRecyclerView() ;
                myRefVerifiedTutor.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;
    }

    private void preparationForRecyclerView(){

        final String address = verifiedTutorInfo.getPreferredAreas() ;
        final String group = verifiedTutorInfo.getPreferredGroup() ;
        final String className = verifiedTutorInfo.getPreferredClasses() ;

        myRefTuitionPost.orderByChild("tutorGenderPreference").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TuitionPostInfo tuitionPostInfo = dataSnapshot.getValue(TuitionPostInfo.class) ;
                if((tuitionPostInfo.getStudentGroup().equals(group)||tuitionPostInfo.getStudentGroup().equals(""))){
                    tuitionPostInfoArrayList1.add(tuitionPostInfo) ;
                    count1++ ;
                }

                if(tuitionPostInfo.getStudentAreaAddress().equals(address) && (tuitionPostInfo.getStudentGroup().equals(group)||tuitionPostInfo.getStudentGroup().equals(""))){
                    tuitionPostInfoArrayList2.add(tuitionPostInfo) ;
                    count2++ ;
                }

                if(count2>=3){
                    recyclerViewOperation() ;
                    myRefTuitionPost.removeEventListener(this);
                }
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
        }) ;
    }

    private void recyclerViewOperation() {
        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        RecyclerAdapterForTutorHomePage adapter = new RecyclerAdapterForTutorHomePage(tuitionPostInfoArrayList1, userInfo,1) ;
        RecyclerAdapterForTutorHomePage adapter2 = new RecyclerAdapterForTutorHomePage(tuitionPostInfoArrayList2, userInfo,2) ;
        recyclerView.setAdapter(adapter2);
        recyclerView2.setAdapter(adapter);
    }

    public  void goToMessageBox(View view){
        Intent intent = new Intent(this, MainMessageActivity.class);
        intent.putExtra("user","tutor") ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    public void goToVerifiedTutorNotificationActivity(View view){
        Intent intent = new Intent(this, VerifiedTutorNotificationActivity.class) ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    public void goToVerifiedTutorGroupActivity(View view){

        myRefGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int flag = 0 ;
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    GroupInfo groupInfo = dS1.getValue(GroupInfo.class) ;
                    if(groupInfo.getGroupAdminUid().equals(userInfo.get(3))){
                        String groupID = dS1.getKey() ;

                        Intent intent = new Intent(VerifiedTutorHomePageActivity.this, GroupHomePageActivity.class) ;
                        intent.putExtra("user" , "tutor") ;
                        intent.putStringArrayListExtra("userInfo", userInfo) ;
                        intent.putExtra("groupID", groupID) ;
                        startActivity(intent);
                        //finish();

                        flag = 1 ;
                        break ;
                    }
                }
                if(flag == 0){
                    Intent intent = new Intent(VerifiedTutorHomePageActivity.this, GroupCreationActivity.class) ;
                    intent.putExtra("user" , "tutor") ;
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                    startActivity(intent);
                    finish();
                }
                myRefGroup.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void goToVerifiedTutorProfileActivity(){
        Intent intent = new Intent(this, VerifiedTutorProfileActivity.class) ;
        intent.putExtra("user" , "tutor") ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    public void goToDemoVideo(View view){
        Intent intent = new Intent(this, DemoVideoMainActivity.class) ;
        intent.putExtra("user" , "tutor") ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    public void goToVerifiedTutorViewPostActivity(View view){
        Intent intent = new Intent(this, TuitionPostViewActivity.class) ;
        intent.putExtra("user" , "tutor") ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        //finish();
    }

    public void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        Intent intent = new Intent(this, HomePageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(intent);
        //finish();
    }

    private void status(String status){


            DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(user.getUid());
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("status",status);
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

    public void calendarEvent(){
        //Intent intent = new Intent(this, CalendarSampleActivity.class);
        Intent intent = new Intent(this, CalendarHomeActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.view_profile:
                goToVerifiedTutorProfileActivity();
                break;

            case R.id.log_out:
                signOut();
                break;

            case  R.id.calenderMenuId:
                calendarEvent();
                break;

            case R.id.notes:
                addNote();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addNote() {
        Intent intent = new Intent(this, Note_Home.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
    }
}

