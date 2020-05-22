package com.example.tuitionapp_surji.tuition_post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_surji.R;
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
import java.util.Arrays;
import java.util.List;

public class TuitionPostViewActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser ;
    private DatabaseReference myRefTuitionPost;
    private ArrayList<ArrayList<TuitionPostInfo>> filteredTuitionPost ;
    private CustomAdapterForTuitionPostView adapter ;

    private LinearLayout filterByLayout ;
    private TextView filterList ;
    private String filterListString ;

    private ArrayList<String> tuitionPostUidList ;

    private PopupMenu popup ;
    private Menu menu ;

    int filterCount = 1 ;

    private ListView listView ;
    private ImageButton filterButton ;
    private TextView createNewPostButton ;

    private ArrayList<String> userInfo ;
    private String user, tutorEmail = "", tutorUid = "" ;

    private List<String> areaList, classList, groupList ;

    private int areaFlag = 1, classFlag = 1, groupFlag = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuition_post_view);

        tuitionPostUidList = new ArrayList<>() ;

        listView = findViewById(R.id.tuitionPostList) ;
        filterByLayout = findViewById(R.id.filtered_option_layout) ;
        filterList = findViewById(R.id.filter_list) ;
        filteredTuitionPost = new ArrayList<>(10) ;

        for(int i=0 ; i<10 ; i++){
            filteredTuitionPost.add(new ArrayList()) ;
        }

        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;

        myRefTuitionPost = FirebaseDatabase.getInstance().getReference("TuitionPost") ;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(user.equals("tutor")){
            filterButton = findViewById(R.id.filter_option) ;
            filterButton.setVisibility(View.VISIBLE);
            filterByLayout.setVisibility(View.VISIBLE);
            userInfo = intent.getStringArrayListExtra("userInfo") ;
            tutorEmail = userInfo.get(2) ;
            tutorUid = userInfo.get(3) ;
            String reverseGender = "" ;

            if(userInfo.get(4).equals("MALE")){
                reverseGender = "Only Female" ;
            }else if(userInfo.get(4).equals("FEMALE")){
                reverseGender = "Only Male" ;
            }

            filterListString = "GENDER: " + userInfo.get(4) ;
            filterList.setText(filterListString);

            filterByGender(reverseGender);
            //filterByArea(userInfo.get((5)));
            //filterByGroup(userInfo.get((6)));
        }

        if(user.equals("guardian")){
            createNewPostButton = findViewById(R.id.create_new_post) ;
            createNewPostButton.setVisibility(View.VISIBLE);
            filterByGuardian(firebaseUser.getPhoneNumber());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        areaList = Arrays.asList(getResources().getStringArray(R.array.areaAddress_array)) ;
        classList = Arrays.asList(getResources().getStringArray(R.array.preferredClass_array_bangla_medium)) ;
        groupList = Arrays.asList(getResources().getStringArray(R.array.group_array)) ;
    }

    public void viewTuitionPost(){
        adapter  = new CustomAdapterForTuitionPostView(this,filteredTuitionPost.get(0), tutorEmail, tutorUid, tuitionPostUidList, user) ;
        listView.setAdapter(adapter);
    }

    public void goToBackPageActivity(View view){
        if(user.equals("tutor")){
            Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
            intent.putStringArrayListExtra("userInfo", userInfo) ;
            startActivity(intent);
            finish();
        }
        else if(user.equals("guardian")){
            Intent intent = new Intent(this, GuardianHomePageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void goToGuardianPostForTuitionActivity(View view) {
        Intent intent = new Intent(this, TuitionPostActivity.class);
        intent.putExtra("type","newPost") ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }

    public void onPopupFilterButtonClick(View view) {
        popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.filter_by_menu, popup.getMenu());

        menu = popup.getMenu() ;

        if(areaFlag == -1){
            menu.removeItem(R.id.area);
        }
        if(classFlag == -1){
            menu.removeItem(R.id.class_name);
        }
        if(groupFlag == -1){
            menu.removeItem(R.id.group);
        }

        if(areaFlag==1 && classFlag==1 && groupFlag==1){
            menu.findItem(R.id.clear).setVisible(false);
        }


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getTitle().equals("CLEAR ALL")){
                    refreshActivity();
                }

                for(int i=0 ; i<areaList.size() ; i++){
                    if(item.getTitle().equals(areaList.get(i))&&!item.getTitle().equals("AREA")){
                        filterByArea(areaList.get(i)) ;
                        Toast.makeText(TuitionPostViewActivity.this,
                                "Clicked popup menu item " + item.getTitle(),
                                Toast.LENGTH_SHORT).show() ;
                    }
                }

                for(int i=0 ; i<classList.size() ; i++){
                    if(item.getTitle().equals(classList.get(i))&&!item.getTitle().equals("CLASS")){
                        filterByClass(classList.get(i)) ;
                        Toast.makeText(TuitionPostViewActivity.this,
                                "Clicked popup menu item " + item.getTitle(),
                                Toast.LENGTH_SHORT).show() ;
                    }
                }

                for(int i=0 ; i<groupList.size() ; i++){
                    if(item.getTitle().equals(groupList.get(i))&&!item.getTitle().equals("GROUP")){
                        filterByGroup(groupList.get(i)) ;
                        Toast.makeText(TuitionPostViewActivity.this,
                                "Clicked popup menu item " + item.getTitle(),
                                Toast.LENGTH_SHORT).show() ;
                    }
                }

                popup.dismiss();
                return true ;
            }
        });

        popup.show();
    }

    public void refreshActivity(){
        Intent intent = new Intent(this, TuitionPostViewActivity.class) ;
        intent.putExtra("user" , user) ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    public void filterByGuardian(final String mobileNumber){
        final ArrayList<TuitionPostInfo> helpArrayList = new ArrayList<>() ;
        final ArrayList<String> helpArrayList2 = new ArrayList<>() ;

        myRefTuitionPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    TuitionPostInfo tuitionPostInfo = dS1.getValue(TuitionPostInfo.class) ;

                    if(tuitionPostInfo.getGuardianMobileNumberFK().equals(mobileNumber)){
                        helpArrayList.add(tuitionPostInfo);
                        helpArrayList2.add(dS1.getKey()) ;
                    }
                }

                myRefTuitionPost.removeEventListener(this);
                TuitionPostInfo tuitionPostInfo = new TuitionPostInfo() ;
                filteredTuitionPost.get(0).add(tuitionPostInfo) ;
                tuitionPostUidList.add("") ;


                for(int i=helpArrayList.size()-1 ; i>=0 ; i--){
                    filteredTuitionPost.get(0).add(helpArrayList.get(i)) ;
                    tuitionPostUidList.add(helpArrayList2.get(i));
                    System.out.println(helpArrayList.get(i).getPostTitle() + "  key:" + helpArrayList2.get(i));
                }
                helpArrayList.clear();
                viewTuitionPost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }) ;
    }

    public void filterByGender(final String gender){
        final ArrayList<TuitionPostInfo> helpArrayList = new ArrayList<>() ;

        myRefTuitionPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    TuitionPostInfo tuitionPostInfo = dS1.getValue(TuitionPostInfo.class) ;
                    if(!tuitionPostInfo.getTutorGenderPreference().equals(gender)){
                        helpArrayList.add(tuitionPostInfo);
                    }
                }

                for(int i=helpArrayList.size()-1 ; i>=0 ; i--){
                    filteredTuitionPost.get(0).add(helpArrayList.get(i)) ;
                }
                helpArrayList.clear();
                viewTuitionPost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;

    }

    public void filterByArea(String area){
        for(TuitionPostInfo tuitionPostInfo : filteredTuitionPost.get(filterCount-1)){
            if(tuitionPostInfo.getStudentAreaAddress().equals(area)){
                filteredTuitionPost.get(filterCount).add(tuitionPostInfo) ;
            }
        }
        adapter.setListData(filteredTuitionPost.get(filterCount));
        adapter.notifyDataSetChanged();
        filterCount++ ;

        filterListString = filterListString + ", " + "AREA: " + area ;
        filterList.setText(filterListString);

        areaFlag = -1 ;

    }

    public void filterByGroup(String group){
        for(TuitionPostInfo tuitionPostInfo : filteredTuitionPost.get(filterCount-1)){
            if(tuitionPostInfo.getStudentGroup().equals(group)){
                filteredTuitionPost.get(filterCount).add(tuitionPostInfo) ;
            }
        }
        adapter.setListData(filteredTuitionPost.get(filterCount));
        adapter.notifyDataSetChanged();
        filterCount++ ;

        filterListString = filterListString + ", " + "GROUP: " + group ;
        filterList.setText(filterListString);

        groupFlag = -1 ;
    }

    public void filterByClass(String class_name){

        for(TuitionPostInfo tuitionPostInfo : filteredTuitionPost.get(filterCount-1)){
            if(tuitionPostInfo.getStudentClass().equals(class_name)){
                filteredTuitionPost.get(filterCount).add(tuitionPostInfo) ;
            }
        }
        adapter.setListData(filteredTuitionPost.get(filterCount));
        adapter.notifyDataSetChanged();
        filterCount++ ;

        filterListString = filterListString + ", " + "CLASS: " + class_name ;
        filterList.setText(filterListString);

        classFlag = -1 ;
    }
}
