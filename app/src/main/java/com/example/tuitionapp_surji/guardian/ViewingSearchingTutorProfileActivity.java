package com.example.tuitionapp_surji.guardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.tuitionapp_surji.admin.AdminHomePageActivity;
import com.example.tuitionapp_surji.admin.ApproveAndBlockInfo;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.group.GroupHomePageActivity;
import com.example.tuitionapp_surji.group.GroupInfo;
import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.starting.HomePageActivity;
import com.example.tuitionapp_surji.tuition_post.TuitionPostInfo;
import com.example.tuitionapp_surji.tuition_post.TuitionPostViewActivity;
import com.example.tuitionapp_surji.tuition_post.TuitionPostViewSinglePageActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorInfo;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorProfileActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewingSearchingTutorProfileActivity extends AppCompatActivity {

    private DatabaseReference myRefVerifiedTutor, myRefCandidateTutor, myRefGroup, myRefApproveAndBlock;

    private ArrayList<ApproveAndBlockInfo> approveAndBlockInfoList ;
    private ArrayList<ArrayList<GroupInfo>> filterGroupInfoList ;
    private ArrayList<String> approveAndBlockTutorUidList ;
    private ArrayList<ArrayList<String>> filterGroupIDList ;

    private CustomAdapterForTutorListView adapter ;
    private CustomAdapterForGroupListView adapter2 ;

    private String user;

    private ArrayList<ArrayList<CandidateTutorInfo>> filteredCandidateTutorList ;
    private ArrayList<ArrayList<String>> filteredCandidateTutorUidList ;
    private ArrayList<ArrayList<VerifiedTutorInfo>> filteredVerifiedTutorList ;
    private PopupMenu popup ;
    private Menu menu ;
    private int filterCount = 0, filterCount2 = 0 ;
    private List<String> areaList, classList, groupList, mediumList ;
    private int areaFlag = 1, classFlag = 1, groupFlag = 1, mediumFlag = 1 ;
    private TextView filterList ;
    private String filterListString ;
    private LinearLayout filterByLayout ;
    private EditText searchBar ;

    private ListView tutorListView ;
    private ListView groupListView ;
    private ViewFlipper viewFlipper ;
    private Button tutorListViewButton, groupListViewButton ;
    private MaterialButton tutorListViewButton2, groupListViewButton2 ;

    private String [] searchString ;
    private int searchIndex=0 ;
    private double x1, x2, y1, y2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_searching_tutor_profile);

        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;

        tutorListView = findViewById(R.id.verifiedTutorList) ;
        groupListView = findViewById(R.id.groupList) ;
        searchBar = findViewById(R.id.search_bar) ;
        viewFlipper = findViewById(R.id.viewFlipper) ;
        tutorListViewButton = findViewById(R.id.tutorListViewButton) ;
        tutorListViewButton2 = findViewById(R.id.tutor_list_button) ;
        groupListViewButton = findViewById(R.id.groupListViewButton) ;
        groupListViewButton2 = findViewById(R.id.group_list_button) ;

        filterByLayout = findViewById(R.id.filtered_option_layout) ;
        filterList = findViewById(R.id.filter_list) ;

        filteredCandidateTutorList = new ArrayList<>(8) ;
        for(int i=0 ; i<8 ; i++){
            filteredCandidateTutorList.add(new ArrayList()) ;
        }

        filteredCandidateTutorUidList = new ArrayList<>(8) ;
        for(int i=0 ; i<8 ; i++){
            filteredCandidateTutorUidList.add(new ArrayList()) ;
        }

        filteredVerifiedTutorList = new ArrayList<>(8) ;
        for(int i=0 ; i<8 ; i++){
            filteredVerifiedTutorList.add(new ArrayList()) ;
        }

        filterGroupInfoList = new ArrayList<>(4) ;
        for(int i=0 ; i<4 ; i++){
            filterGroupInfoList.add(new ArrayList()) ;
        }

        filterGroupIDList = new ArrayList<>(4) ;
        for(int i=0 ; i<4 ; i++){
            filterGroupIDList.add(new ArrayList()) ;
        }

        areaList = Arrays.asList(getResources().getStringArray(R.array.areaAddress_array)) ;
        classList = Arrays.asList(getResources().getStringArray(R.array.preferredClass_array_bangla_medium)) ;
        groupList = Arrays.asList(getResources().getStringArray(R.array.group_array)) ;
        mediumList = Arrays.asList(getResources().getStringArray(R.array.medium_array)) ;

        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor") ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        myRefGroup = FirebaseDatabase.getInstance().getReference("Group") ;
        myRefApproveAndBlock = FirebaseDatabase.getInstance().getReference("ApproveAndBlock") ;

        tutorListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String tutorUid = filteredCandidateTutorUidList.get(filterCount).get(position) ;
                String tutorEmail = filteredCandidateTutorList.get(filterCount).get(position).getEmailPK() ;
                goToSelectedVerifiedTutorProfile(tutorEmail,tutorUid) ;
            }
        });

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String groupID = filterGroupIDList.get(filterCount2).get(position) ;
                String adminEmail = filterGroupInfoList.get(filterCount2).get(position).getGroupAdminEmail() ;
                String groupAdminUid = filterGroupInfoList.get(filterCount2).get(position).getGroupAdminUid();
                goToSelectedGroup(groupID,groupAdminUid,adminEmail) ;
            }
        });

        tutorListViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(0);
                tutorListViewButton.setVisibility(View.GONE);
                tutorListViewButton2.setVisibility(View.VISIBLE);
                groupListViewButton.setVisibility(View.VISIBLE);
                groupListViewButton2.setVisibility(View.GONE);
            }
        });

        groupListViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(1);
                tutorListViewButton.setVisibility(View.VISIBLE);
                tutorListViewButton2.setVisibility(View.GONE);
                groupListViewButton.setVisibility(View.GONE);
                groupListViewButton2.setVisibility(View.VISIBLE);
            }
        });

        approveAndBlockTutorUidList = new ArrayList<>() ;
        approveAndBlockInfoList = new ArrayList<>() ;

        myRefApproveAndBlock.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    ApproveAndBlockInfo approveAndBlockInfo = dS1.getValue(ApproveAndBlockInfo.class) ;

                    if(approveAndBlockInfo.getStatus().equals("running")){
                        approveAndBlockTutorUidList.add(dS1.getKey()) ;
                        myRefApproveAndBlock.removeEventListener(this);
                    }
                }

                myRefCandidateTutor.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dS1: dataSnapshot.getChildren()){
                            for (String info : approveAndBlockTutorUidList) {
                                if (dS1.getKey().equals(info)){
                                    CandidateTutorInfo candidateTutorInfo = dS1.getValue(CandidateTutorInfo.class);
                                    if(user.equals("guardian") && !candidateTutorInfo.isTutorAvailable()){
                                        continue;
                                    }

                                    filteredCandidateTutorList.get(0).add(candidateTutorInfo) ;
                                    filteredCandidateTutorUidList.get(0).add(dS1.getKey()) ;
                                    break;
                                }
                            }
                        }

                        setVerifiedTutorListView();

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

        myRefGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1:dataSnapshot.getChildren()){
                    GroupInfo groupInfo = dS1.getValue(GroupInfo.class) ;
                    filterGroupInfoList.get(0).add(groupInfo) ;
                    filterGroupIDList.get(0).add(dS1.getKey()) ;
                }
                setGroupListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!searchBar.getText().toString().equals("")) {
                    searchString = new String[10] ;
                    searchIndex = 0 ;
                    if(groupFlag==-1||mediumFlag==-1||classFlag==-1||areaFlag==-1){
                        refreshActivity(1);
                    }

                    searchOperation(searchBar.getText().toString());
                }
                searchBar.setText("");
                return false;
            }
        });
    }

    public void searchOperation(String textString){
        if(textString!=null) {
            searchString = textString.split(" ");
        }
        for(int j=searchIndex ; j<searchString.length && j<8 ; j++){
            searchIndex++ ;
            if(searchString[j].toUpperCase().contains("CLASS")||searchString[j].toUpperCase().contains("GROUP")||searchString[j].toUpperCase().contains("SUBJECT")||searchString[j].toUpperCase().contains("MEDIUM")){
                continue;
            }

            if(searchString[j].toUpperCase().contains("BANGLA")) {
                if(areaFlag==1 && classFlag==1 && groupFlag==1 ){
                    firstFilter("Bangla Medium","medium");
                    return;
                }
                else{
                    filterByMedium("Bangla Medium");
                }
                continue;
            }
            else if(searchString[j].toUpperCase().contains("ENGLISH")){
                if(areaFlag==1 && classFlag==1 && groupFlag==1 ){
                    firstFilter("English Medium","medium");
                    return;
                }
                else{
                    filterByMedium("English Medium");
                }
                continue;
            }

            if(searchString[j].toUpperCase().contains("SCIENCE")) {
                if(areaFlag==1 && classFlag==1 && groupFlag==1 ){
                    firstFilter("Science","group");
                    return;
                }
                else{
                    filterByGroup("Science");
                }
                continue;
            }
            else if(searchString[j].toUpperCase().contains("COMMERCE")){
                if(areaFlag==1 && classFlag==1 && groupFlag==1 ){
                    firstFilter("Commerce","group");
                    return;
                }else {
                    filterByGroup("Commerce");
                }
                continue;
            }
            else if(searchString[j].toUpperCase().contains("Arts")){
                if(areaFlag==1 && classFlag==1 && groupFlag==1 ){
                    firstFilter("Arts","group");
                    return;
                }
                else {
                    filterByGroup("Arts");
                }
                continue;
            }

            for(int i=10 ; i>=1 ;i--){
                if(searchString[j].contains(String.valueOf(i))){
                    if(areaFlag==1 && classFlag==1 && groupFlag==1){
                        firstFilter("CLASS "+i,"class");
                        return;
                    }
                    else {
                        filterByClass("CLASS " + i);
                    }
                    break;
                }
            }
            if(searchString[j].contains("11")||searchString[j].toUpperCase().contains("INTER")){
                if(areaFlag==1 && classFlag==1 && groupFlag==1 ){
                    firstFilter("INTER FIRST YEAR","class");
                    return;
                }else {
                    filterByClass("INTER FIRST YEAR");
                }
                continue;
            }
            else if(searchString[j].contains("12")||searchString[j].toUpperCase().contains("INTER")){
                if(areaFlag==1 && classFlag==1 && groupFlag==1 ){
                    firstFilter("INTER SECOND YEAR","class");
                    return;
                }else {
                    filterByClass("INTER SECOND YEAR");
                }
                continue;
            }
            else if(searchString[j].toUpperCase().contains("HONS")||searchString[j].toUpperCase().contains("HONOURS")||searchString[j].toUpperCase().contains("VARSITY")||searchString[j].toUpperCase().contains("UNIVERSITY")){
                //filterByClass("");
            }

            for(int i=0 ; i<areaList.size() ; i++){
                if(searchString[j].toUpperCase().equals(areaList.get(i).toUpperCase())) {
                    if(areaFlag==1 && classFlag==1 && groupFlag==1 ){
                        firstFilter(areaList.get(i),"area");
                        return;
                    }
                    else{
                        filterByArea(areaList.get(i)) ;
                    }
                }
            }
        }
    }

    public void setVerifiedTutorListView(){
        adapter = new CustomAdapterForTutorListView(this, filteredCandidateTutorList.get(0), "guardianTutor");
        tutorListView.setAdapter(adapter);
    }

    public void setGroupListView(){
        adapter2 = new CustomAdapterForGroupListView(this,filterGroupInfoList.get(0));
        groupListView.setAdapter(adapter2);
    }

    public void backToHomePage(View view){
        if(user.equals("guardian")){
            finish();
        }
        else if(user.equals("admin")){
            Intent intent = new Intent(this, AdminHomePageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed(){
        backToHomePage(null);
    }

    public void goToSelectedVerifiedTutorProfile(String tutorEmail,String tutorUid){
        Intent intent = new Intent(this, VerifiedTutorProfileActivity.class);
        intent.putExtra("user", user) ;
        intent.putExtra("tutorUid",tutorUid);
        intent.putExtra("tutorEmail", tutorEmail) ;
        intent.putExtra("context", "guardian_view") ;
        startActivity(intent);
        finish();
    }

    public void goToSelectedGroup(String groupID, String tutorUid, String tutorEmail){
        Intent intent = new Intent(this, GroupHomePageActivity.class);
        intent.putExtra("user", user) ;
        intent.putExtra("tutorUid", tutorUid) ;
        intent.putExtra("tutorEmail", tutorEmail) ;
        intent.putExtra("groupID", groupID) ;
        startActivity(intent);
        finish();
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
                    refreshActivity(0);
                }

                for(int i=0 ; i<areaList.size() ; i++){
                    if(item.getTitle().equals(areaList.get(i))&&!item.getTitle().equals("AREA")){
                        if(areaFlag==1 && classFlag==1 && groupFlag==1 && mediumFlag==1){
                            firstFilter(areaList.get(i),"area");
                        }
                        else{
                            filterByArea(areaList.get(i)) ;
                        }
                        Toast.makeText(ViewingSearchingTutorProfileActivity.this, "Clicked popup menu item " + item.getTitle(), Toast.LENGTH_SHORT).show() ;
                    }
                }

                for(int i=0 ; i<classList.size() ; i++){
                    if(item.getTitle().equals(classList.get(i))&&!item.getTitle().equals("CLASS")){
                        if(areaFlag==1 && classFlag==1 && groupFlag==1 && mediumFlag==1 ){
                            firstFilter(classList.get(i),"class");
                        }
                        else{
                            filterByClass(classList.get(i)) ;
                        }
                        Toast.makeText(ViewingSearchingTutorProfileActivity.this, "Clicked popup menu item " + item.getTitle(), Toast.LENGTH_SHORT).show() ;
                    }
                }

                for(int i=0 ; i<groupList.size() ; i++){
                    if(item.getTitle().equals(groupList.get(i))&&!item.getTitle().equals("GROUP")){
                        if(areaFlag==1 && classFlag==1 && groupFlag==1 && mediumFlag==1){
                            firstFilter(groupList.get(i),"group");
                        }
                        else{
                            filterByGroup(groupList.get(i)) ;
                        }

                        Toast.makeText(ViewingSearchingTutorProfileActivity.this, "Clicked popup menu item " + item.getTitle(), Toast.LENGTH_SHORT).show() ;
                    }
                }
                for(int i=0 ; i<mediumList.size() ; i++){
                    if(item.getTitle().equals(mediumList.get(i))&&!item.getTitle().equals("MEDIUM")){
                        if(areaFlag==1 && classFlag==1 && groupFlag==1 && mediumFlag==1){
                            firstFilter(mediumList.get(i),"medium");
                        }
                        else{
                            filterByMedium(mediumList.get(i)) ;
                        }
                        Toast.makeText(ViewingSearchingTutorProfileActivity.this, "Clicked popup menu item " + item.getTitle(), Toast.LENGTH_SHORT).show() ;
                    }
                }

                popup.dismiss();
                return true ;
            }
        });

        popup.show();
    }

    public void firstFilter(final String data, final String flag){
        filterByLayout.setVisibility(View.VISIBLE);
        Query query = null;
        if(flag.equals("area")){
            groupFilterByArea(data);
            query = myRefVerifiedTutor.orderByChild("preferredAreas").equalTo(data) ;
            filterListString = "AREA: " + data ;
            areaFlag = -1 ;
        }
        else if(flag.equals("class")){
            query = myRefVerifiedTutor;
            filterListString = "CLASS: " + data ;
            classFlag = -1 ;
        }
        else if(flag.equals("group")){
            query = myRefVerifiedTutor.orderByChild("preferredGroup").equalTo(data) ;
            filterListString = "GROUP: " + data ;
            groupFlag = -1 ;
        }
        else if(flag.equals("medium")){
            query = myRefVerifiedTutor.orderByChild("preferredMediumOrVersion").equalTo(data) ;
            filterListString = "MEDIUM: " + data ;
            mediumFlag = -1 ;
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dS1: snapshot.getChildren()){
                    myRefVerifiedTutor.removeEventListener(this);
                    if(flag.equals("class")){
                        VerifiedTutorInfo verifiedTutorInfo = dS1.getValue(VerifiedTutorInfo.class) ;
                        if(!verifiedTutorInfo.getPreferredClasses().contains(data)){
                            continue;
                        }
                    }
                    for(int i=0; i<filteredCandidateTutorUidList.get(filterCount).size(); i++){
                        if(dS1.getKey().equals(filteredCandidateTutorUidList.get(filterCount).get(i))){
                            VerifiedTutorInfo verifiedTutorInfo = dS1.getValue(VerifiedTutorInfo.class) ;
                            filteredVerifiedTutorList.get(filterCount).add(verifiedTutorInfo) ;

                            filteredCandidateTutorList.get(filterCount+1).add(filteredCandidateTutorList.get(filterCount).get(i)) ;
                            filteredCandidateTutorUidList.get(filterCount+1).add(dS1.getKey()) ;
                            break ;
                        }
                    }
                }

                adapter.setListData(filteredCandidateTutorList.get(filterCount+1));
                adapter.notifyDataSetChanged();
                filterCount++ ;

                filterList.setText(filterListString);

                if(searchIndex!=0){
                    searchOperation(null);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void groupFilterByArea(String area){
        Query query = myRefGroup.orderByChild("address").equalTo(area) ;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dS1: snapshot.getChildren()){
                    myRefGroup.removeEventListener(this);
                    for(int i=0; i<filterGroupIDList.get(0).size() ; i++){
                        if(dS1.getKey().equals(filterGroupIDList.get(0).get(i))){
                            filterGroupInfoList.get(1).add(filterGroupInfoList.get(0).get(i)) ;
                            filterGroupIDList.get(1).add(dS1.getKey()) ;
                            break ;
                        }
                    }
                }

                adapter2.setListData(filterGroupInfoList.get(1)) ;
                adapter2.notifyDataSetChanged() ;
                filterCount2 = 1 ;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void filterByArea(String area){
        groupFilterByArea(area) ;

        if(areaFlag==-1){
            return;
        }
        for(int i=0 ; i<filteredVerifiedTutorList.get(filterCount-1).size() ; i++){
            if(filteredVerifiedTutorList.get(filterCount-1).get(i).getPreferredAreas().equals(area)){
                filteredVerifiedTutorList.get(filterCount).add(filteredVerifiedTutorList.get(filterCount-1).get(i)) ;
                filteredCandidateTutorList.get(filterCount+1).add(filteredCandidateTutorList.get(filterCount).get(i)) ;
                filteredCandidateTutorUidList.get(filterCount+1).add(filteredCandidateTutorUidList.get(filterCount).get(i)) ;
            }
        }

        adapter.setListData(filteredCandidateTutorList.get(filterCount+1));
        adapter.notifyDataSetChanged();
        filterCount++ ;

        filterListString = filterListString + ", " + "AREA: " + area ;
        filterList.setText(filterListString);

        areaFlag = -1 ;
    }

    public void filterByClass(String className){
        if(classFlag==-1){
            return ;
        }

        for(int i=0 ; i<filteredVerifiedTutorList.get(filterCount-1).size() ; i++){
            if(filteredVerifiedTutorList.get(filterCount-1).get(i).getPreferredClasses().contains(className)){
                filteredVerifiedTutorList.get(filterCount).add(filteredVerifiedTutorList.get(filterCount-1).get(i)) ;
                filteredCandidateTutorList.get(filterCount+1).add(filteredCandidateTutorList.get(filterCount).get(i)) ;
                filteredCandidateTutorUidList.get(filterCount+1).add(filteredCandidateTutorUidList.get(filterCount).get(i)) ;
            }
        }

        adapter.setListData(filteredCandidateTutorList.get(filterCount+1));
        adapter.notifyDataSetChanged() ;
        filterCount++ ;

        filterListString = filterListString + ", " + "CLASS: " + className ;
        filterList.setText(filterListString);

        classFlag = -1 ;
    }

    public void filterByGroup(String group){
        if(groupFlag==-1){
            return;
        }

        for(int i=0 ; i<filteredVerifiedTutorList.get(filterCount-1).size() ; i++){
            if(filteredVerifiedTutorList.get(filterCount-1).get(i).getPreferredGroup().equals(group)){
                filteredVerifiedTutorList.get(filterCount).add(filteredVerifiedTutorList.get(filterCount-1).get(i)) ;
                filteredCandidateTutorList.get(filterCount+1).add(filteredCandidateTutorList.get(filterCount).get(i)) ;
                filteredCandidateTutorUidList.get(filterCount+1).add(filteredCandidateTutorUidList.get(filterCount).get(i)) ;
            }
        }

        adapter.setListData(filteredCandidateTutorList.get(filterCount+1));
        adapter.notifyDataSetChanged();
        filterCount++ ;

        filterListString = filterListString + ", " + "GROUP: " + group ;
        filterList.setText(filterListString);

        groupFlag = -1 ;
    }

    public void filterByMedium(String medium){
        if(mediumFlag==-1){
            return;
        }
        for(int i=0 ; i<filteredVerifiedTutorList.get(filterCount-1).size() ; i++){
            if(filteredVerifiedTutorList.get(filterCount-1).get(i).getPreferredMediumOrVersion().equals(medium)){
                filteredVerifiedTutorList.get(filterCount).add(filteredVerifiedTutorList.get(filterCount-1).get(i)) ;
                filteredCandidateTutorList.get(filterCount+1).add(filteredCandidateTutorList.get(filterCount).get(i)) ;
                filteredCandidateTutorUidList.get(filterCount+1).add(filteredCandidateTutorUidList.get(filterCount).get(i)) ;
            }
        }

        adapter.setListData(filteredCandidateTutorList.get(filterCount+1));
        adapter.notifyDataSetChanged();
        filterCount++ ;

        filterListString = filterListString + ", " + "MEDIUM: " + medium ;
        filterList.setText(filterListString);

        mediumFlag = -1 ;
    }

    public void refreshActivity(int flag){
        for(int i=1 ; i<=filterCount ; i++){
            for(int j=filteredCandidateTutorUidList.get(i).size()-1; j>=0 ; j--){
                filteredCandidateTutorList.get(i).remove(j);
                filteredCandidateTutorUidList.get(i).remove(j);
            }
        }
        for(int i=0 ; i<filterCount ; i++){
            for(int j=filteredVerifiedTutorList.get(i).size()-1; j>=0 ; j--){
                filteredVerifiedTutorList.get(i).remove(j);
            }
        }

        for(int j=filterGroupInfoList.get(1).size()-1; j>=0 ; j--){
            filterGroupInfoList.get(1).remove(j);
            filterGroupIDList.get(1).remove(j);
        }
        filterCount2 = 0 ;

        filterCount = 0 ;
        areaFlag = 1 ;
        groupFlag = 1 ;
        mediumFlag = 1 ;
        classFlag = 1 ;

        filterList.setText("");

        if(flag!=1){
            filterByLayout.setVisibility(View.GONE);
            setVerifiedTutorListView();
            setGroupListView();
        }

    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

                if( x1<x2 || x1>x2 ){
                    if(viewFlipper.getDisplayedChild()==1){
                        viewFlipper.setDisplayedChild(0);
                        tutorListViewButton.setVisibility(View.GONE);
                        tutorListViewButton2.setVisibility(View.VISIBLE);
                        groupListViewButton.setVisibility(View.VISIBLE);
                        groupListViewButton2.setVisibility(View.GONE);
                    }
                    else if(viewFlipper.getDisplayedChild()==0){
                        viewFlipper.setDisplayedChild(1);
                        tutorListViewButton.setVisibility(View.VISIBLE);
                        tutorListViewButton2.setVisibility(View.GONE);
                        groupListViewButton.setVisibility(View.GONE);
                        groupListViewButton2.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
        return false;
    }

}

