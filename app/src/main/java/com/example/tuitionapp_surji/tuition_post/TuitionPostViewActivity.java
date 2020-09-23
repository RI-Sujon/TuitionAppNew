package com.example.tuitionapp_surji.tuition_post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.group.GroupCreationActivity;
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
    private DatabaseReference myRefTuitionPost, myRefResponsePost;
    private ArrayList<ArrayList<TuitionPostInfo>> filteredTuitionPost ;
    private ArrayList<ArrayList<String>> filteredTuitionPostUid ;
    private CustomAdapterForTuitionPostView adapter ;

    private LinearLayout filterByLayout ;
    private TextView filterList ;
    private String filterListString ;

    private EditText searchBar ;

    private ArrayList<String> responseTuitionPostList ;
    private int [] responsePostArray ;

    private PopupMenu popup ;
    private Menu menu ;

    int filterCount = 1 ;

    private ListView listView ;
    private ImageButton filterButton ;
    private TextView createNewPostButton ;

    private ArrayList<String> userInfo ;
    private String user, tutorEmail = "", tutorUid = "", guardianProfilePicUri , tutorApprovalStatus;

    private List<String> areaList, classList, groupList, mediumList ;
    private int areaFlag = 1, classFlag = 1, groupFlag = 1, mediumFlag = 1 ;
    private String reverseGender = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuition_post_view);

        listView = findViewById(R.id.tuitionPostList) ;
        filterByLayout = findViewById(R.id.filtered_option_layout) ;
        filterList = findViewById(R.id.filter_list) ;
        searchBar = findViewById(R.id.search_bar) ;

        responseTuitionPostList = new ArrayList<>() ;

        filteredTuitionPost = new ArrayList<>(5) ;
        for(int i=0 ; i<5 ; i++){
            filteredTuitionPost.add(new ArrayList()) ;
        }

        filteredTuitionPostUid = new ArrayList<>(5) ;
        for(int i=0 ; i<5 ; i++){
            filteredTuitionPostUid.add(new ArrayList()) ;
        }

        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;

        myRefTuitionPost = FirebaseDatabase.getInstance().getReference("TuitionPost") ;
        myRefResponsePost = FirebaseDatabase.getInstance().getReference("ResponsePost") ;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        areaList = Arrays.asList(getResources().getStringArray(R.array.areaAddress_array)) ;
        classList = Arrays.asList(getResources().getStringArray(R.array.preferredClass_array_bangla_medium)) ;
        groupList = Arrays.asList(getResources().getStringArray(R.array.group_array)) ;
        mediumList = Arrays.asList(getResources().getStringArray(R.array.medium_array)) ;

        if(user.equals("tutor")){
            filterButton = findViewById(R.id.filter_option) ;
            filterButton.setVisibility(View.VISIBLE);
            filterByLayout.setVisibility(View.VISIBLE);
            userInfo = intent.getStringArrayListExtra("userInfo") ;
            tutorEmail = userInfo.get(2) ;
            tutorUid = userInfo.get(3) ;
            tutorApprovalStatus = intent.getStringExtra("tutorApprovalStatus") ;

            if(userInfo.get(4).equals("MALE")){
                reverseGender = "Only Female" ;
            }else if(userInfo.get(4).equals("FEMALE")){
                reverseGender = "Only Male" ;
            }

            filterListString = "GENDER: " + userInfo.get(4) ;
            filterList.setText(filterListString);

            myRefResponsePost = myRefResponsePost.child(userInfo.get(3)) ;

            if(!tutorApprovalStatus.equals("running")){
                takeActionForNonApprovalTutor() ;
            }

            myRefResponsePost.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dS1 : dataSnapshot.getChildren()){
                        ResponsePost responsePost = dS1.getValue(ResponsePost.class) ;
                        responseTuitionPostList.add(responsePost.getPostUid()) ;
                    }

                    myRefResponsePost.removeEventListener(this);
                    filterByGender(reverseGender);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }) ;
        }

        if(user.equals("guardian")){
            guardianProfilePicUri = intent.getStringExtra("guardianProfilePicUri") ;
            createNewPostButton = findViewById(R.id.create_new_post) ;
            createNewPostButton.setVisibility(View.VISIBLE);
            filterByGuardian(firebaseUser.getUid());
        }

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!searchBar.getText().toString().equals("")) {
                    searchOperation(searchBar.getText().toString());
                }
                searchBar.setText("");
                return false;
            }
        });
    }

    public void viewTuitionPost(){
        isPostResponseGenerator(0);

        adapter  = new CustomAdapterForTuitionPostView(this,filteredTuitionPost.get(0), userInfo, filteredTuitionPostUid.get(0), user, responsePostArray) ;
        listView.setAdapter(adapter);
    }

    public void takeActionForNonApprovalTutor(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this) ;
        dialog.setMessage("Please Wait for Admin Approval.") ;

        dialog.setCancelable(false) ;

        dialog.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToBackPageActivity(null);
            }
        }) ;

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    dialog.dismiss();
                    goToBackPageActivity(null);
                }
                return true;
            }
        }) ;

        dialog.show();

    }

    public void isPostResponseGenerator(int index){
        responsePostArray = new int[filteredTuitionPostUid.get(index).size()] ;

        for (int i=0 ; i<responsePostArray.length ; i++){
            responsePostArray[i] = 0 ;
        }

        for(int i=0 ; i<filteredTuitionPostUid.get(index).size() ; i++){
            for(int j=0 ; j<responseTuitionPostList.size() ; j++){
                if(filteredTuitionPostUid.get(index).get(i).equals(responseTuitionPostList.get(j))){
                    responsePostArray[i] = 1 ;
                    break ;
                }
            }
        }
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
        Intent intent = new Intent(this, TuitionPostCreationActivity.class);
        intent.putExtra("type","newPost");
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
        if(mediumFlag == -1){
            menu.removeItem(R.id.medium);
        }

        if(areaFlag==1 && classFlag==1 && groupFlag==1 && mediumFlag==1){
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
                        Toast.makeText(TuitionPostViewActivity.this, "Clicked popup menu item " + item.getTitle(), Toast.LENGTH_SHORT).show() ;
                    }
                }

                for(int i=0 ; i<classList.size() ; i++){
                    if(item.getTitle().equals(classList.get(i))&&!item.getTitle().equals("CLASS")){
                        filterByClass(classList.get(i)) ;
                        Toast.makeText(TuitionPostViewActivity.this, "Clicked popup menu item " + item.getTitle(), Toast.LENGTH_SHORT).show() ;
                    }
                }

                for(int i=0 ; i<groupList.size() ; i++){
                    if(item.getTitle().equals(groupList.get(i))&&!item.getTitle().equals("GROUP")){
                        filterByGroup(groupList.get(i)) ;
                        Toast.makeText(TuitionPostViewActivity.this, "Clicked popup menu item " + item.getTitle(), Toast.LENGTH_SHORT).show() ;
                    }
                }

                for(int i=0 ; i<mediumList.size() ; i++){
                    if(item.getTitle().equals(mediumList.get(i))&&!item.getTitle().equals("MEDIUM")){
                        filterByMedium(mediumList.get(i)) ;
                        Toast.makeText(TuitionPostViewActivity.this, "Clicked popup menu item " + item.getTitle(), Toast.LENGTH_SHORT).show() ;
                    }
                }

                popup.dismiss();
                return true ;
            }
        });

        popup.show();
    }

    public void searchOperation(String textString){
        String [] textArray = textString.split(" ") ;

        for(String text: textArray){
            if(text.toUpperCase().contains("CLASS")||text.toUpperCase().contains("GROUP")||text.toUpperCase().contains("SUBJECT")||text.toUpperCase().contains("MEDIUM")){
                continue;
            }

            if(text.toUpperCase().contains("BANGLA")) {

            }else if(text.toUpperCase().contains("ENGLISH")){

            }

            if(text.toUpperCase().contains("SCIENCE")) {
                filterByGroup("Science");
                continue;
            }
            else if(text.toUpperCase().contains("COMMERCE")){
                filterByGroup("Commerce");
                continue;
            }
            else if(text.toUpperCase().contains("Arts")){
                filterByGroup("Arts");
                continue;
            }

            for(int i=10 ; i>=1 ;i--){
                if(text.contains(String.valueOf(i))){
                    filterByClass("CLASS "+ i);
                    break;
                }
            }
            if(text.contains("11")||text.toUpperCase().contains("INTER")){
                filterByClass("INTER FIRST YEAR");
                continue;
            }
            else if(text.contains("12")||text.toUpperCase().contains("INTER")){
                filterByClass("INTER SECOND YEAR");
                continue;
            }
            else if(text.toUpperCase().contains("HONS")||text.toUpperCase().contains("HONOURS")||text.toUpperCase().contains("VARSITY")||text.toUpperCase().contains("UNIVERSITY")){
                //filterByClass("");
            }

            for(int i=0 ; i<areaList.size() ; i++){
                if(text.toUpperCase().equals(areaList.get(i).toUpperCase())) {
                    filterByArea(areaList.get(i));
                }
            }
        }
    }

    public void refreshActivity(){
        Intent intent = new Intent(this, TuitionPostViewActivity.class) ;
        intent.putExtra("user" , user) ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    public void filterByGuardian(final String guardianUid){
        final ArrayList<TuitionPostInfo> helpArrayList = new ArrayList<>() ;
        final ArrayList<String> helpArrayList2 = new ArrayList<>() ;

        myRefTuitionPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    TuitionPostInfo tuitionPostInfo = dS1.getValue(TuitionPostInfo.class) ;

                    if(tuitionPostInfo.getGuardianUidFK().equals(guardianUid)){
                        helpArrayList.add(tuitionPostInfo);
                        helpArrayList2.add(dS1.getKey()) ;
                    }
                }

                myRefTuitionPost.removeEventListener(this);
                TuitionPostInfo tuitionPostInfo = new TuitionPostInfo() ;
                filteredTuitionPost.get(0).add(tuitionPostInfo) ;
                filteredTuitionPostUid.get(0).add("") ;

                for(int i=helpArrayList.size()-1 ; i >= 0 ; i--){
                    filteredTuitionPost.get(0).add(helpArrayList.get(i)) ;
                    filteredTuitionPostUid.get(0).add(helpArrayList2.get(i));
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
        final ArrayList<String> helpArrayList2 = new ArrayList<>() ;

        myRefTuitionPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    TuitionPostInfo tuitionPostInfo = dS1.getValue(TuitionPostInfo.class) ;
                    if(!tuitionPostInfo.getTutorGenderPreference().equals(gender) && tuitionPostInfo.getAvailability().equals("Available")){
                        helpArrayList.add(tuitionPostInfo);
                        helpArrayList2.add(dS1.getKey()) ;
                    }
                }

                for(int i=helpArrayList.size()-1 ; i>=0 ; i--){
                    filteredTuitionPost.get(0).add(helpArrayList.get(i)) ;
                    filteredTuitionPostUid.get(0).add(helpArrayList2.get(i)) ;
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
        for(int i=0 ; i<filteredTuitionPost.get(filterCount-1).size(); i++){
            if(filteredTuitionPost.get(filterCount-1).get(i).getStudentAreaAddress().equals(area)){
                filteredTuitionPost.get(filterCount).add(filteredTuitionPost.get(filterCount-1).get(i)) ;
                filteredTuitionPostUid.get(filterCount).add(filteredTuitionPostUid.get(filterCount-1).get(i)) ;
            }
        }

        isPostResponseGenerator(filterCount);

        adapter.setListData(filteredTuitionPost.get(filterCount), filteredTuitionPostUid.get(filterCount), responsePostArray);
        adapter.notifyDataSetChanged();
        filterCount++ ;

        filterListString = filterListString + ", " + "AREA: " + area ;
        filterList.setText(filterListString);

        areaFlag = -1 ;
    }

    public void filterByGroup(String group){
        for(int i=0 ; i<filteredTuitionPost.get(filterCount-1).size(); i++){
            if(filteredTuitionPost.get(filterCount-1).get(i).getStudentGroup().equals(group)){
                filteredTuitionPost.get(filterCount).add(filteredTuitionPost.get(filterCount-1).get(i)) ;
                filteredTuitionPostUid.get(filterCount).add(filteredTuitionPostUid.get(filterCount-1).get(i)) ;
            }
        }

        isPostResponseGenerator(filterCount);

        adapter.setListData(filteredTuitionPost.get(filterCount), filteredTuitionPostUid.get(filterCount), responsePostArray);
        adapter.notifyDataSetChanged();
        filterCount++ ;

        filterListString = filterListString + ", " + "GROUP: " + group ;
        filterList.setText(filterListString);

        groupFlag = -1 ;
    }

    public void filterByMedium(String medium){
        for(int i=0 ; i<filteredTuitionPost.get(filterCount-1).size(); i++){
            if(filteredTuitionPost.get(filterCount-1).get(i).getStudentMedium().equals(medium)){
                filteredTuitionPost.get(filterCount).add(filteredTuitionPost.get(filterCount-1).get(i)) ;
                filteredTuitionPostUid.get(filterCount).add(filteredTuitionPostUid.get(filterCount-1).get(i)) ;
            }
        }

        isPostResponseGenerator(filterCount);

        adapter.setListData(filteredTuitionPost.get(filterCount), filteredTuitionPostUid.get(filterCount), responsePostArray);
        adapter.notifyDataSetChanged();
        filterCount++ ;

        filterListString = filterListString + ", " + "MEDIUM: " + medium ;
        filterList.setText(filterListString);

        mediumFlag = -1 ;
    }

    public void filterByClass(String class_name){
        for(int i=0 ; i<filteredTuitionPost.get(filterCount-1).size(); i++){
            if(filteredTuitionPost.get(filterCount-1).get(i).getStudentClass().equals(class_name)){
                filteredTuitionPost.get(filterCount).add(filteredTuitionPost.get(filterCount-1).get(i)) ;
                filteredTuitionPostUid.get(filterCount).add(filteredTuitionPostUid.get(filterCount-1).get(i)) ;
            }
        }

        isPostResponseGenerator(filterCount);

        adapter.setListData(filteredTuitionPost.get(filterCount), filteredTuitionPostUid.get(filterCount), responsePostArray);
        adapter.notifyDataSetChanged();
        filterCount++ ;

        filterListString = filterListString + ", " + "CLASS: " + class_name ;
        filterList.setText(filterListString);

        classFlag = -1 ;
    }
}
