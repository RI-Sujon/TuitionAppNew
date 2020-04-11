package com.example.tuitionapp.Group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp.Batch.BatchHomePageActivity;
import com.example.tuitionapp.R;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorHomePageActivity;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupHomePageActivity extends AppCompatActivity {

    private DatabaseReference myRefGroupInfo ;
    private String user, userEmail, groupID ;
    private ArrayList<String>userInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_home_page);
        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        user = intent.getStringExtra("user") ;
        userEmail = intent.getStringExtra("userEmail") ;

        myRefGroupInfo = FirebaseDatabase.getInstance().getReference("Group") ;

        myRefGroupInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int flag = 0 ;
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    GroupInfo groupInfo = dS1.getValue(GroupInfo.class) ;
                    if(groupInfo.groupAdminEmail.equals(userEmail)){
                        groupID = dS1.getKey() ;
                        groupHomePage(groupInfo);
                        flag = 1 ;
                        break ;
                    }
                }
                if(flag == 0){
                    goToGroupCreation();
                }
                myRefGroupInfo.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void groupHomePage(GroupInfo groupInfo){
        LinearLayout layout = findViewById(R.id.groupHomePage) ;
        layout.setVisibility(View.VISIBLE);
        TextView groupNameTextView = findViewById(R.id.groupNameTextView) ;
        TextView fullAddressTextView = findViewById(R.id.fullAddressTextView) ;

        groupNameTextView.setText(groupInfo.groupName);
        fullAddressTextView.setText(groupInfo.fullAddress + ", " + groupInfo.address);
    }

    public void goToGroupCreation(){
        LinearLayout groupCreationLayout = findViewById(R.id.groupCreation) ;
        final Spinner addressSpinner = findViewById(R.id.groupAddressSpinner) ;
        Button createGroupButton = findViewById(R.id.createGroupButton) ;

        groupCreationLayout.setVisibility(View.VISIBLE);

        final String[] addressString = new String[1];

        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (addressSpinner.getSelectedItemPosition() == 0) {
                }
                else {
                    addressString[0] = addressSpinner.getSelectedItem().toString().trim();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupCreation(addressString[0]);
            }
        });


    }

    public void groupCreation(String addressString){
        EditText groupNameEditText = findViewById(R.id.groupName) ;
        EditText fullAddressEditText = findViewById(R.id.groupFullAddress) ;

        String groupName, fullAddress ;

        groupName = groupNameEditText.getText().toString().trim() ;
        fullAddress = fullAddressEditText.getText().toString().trim() ;

        if(!groupName.equals("")&&!addressString.equals("")&&!fullAddress.equals("")){
            GroupInfo groupInfo = new GroupInfo(groupName,addressString,fullAddress,userEmail) ;
            myRefGroupInfo.push().setValue(groupInfo) ;
            Toast.makeText(getApplicationContext(), "Group Successfully Created", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,GroupHomePageActivity.class);
            intent.putStringArrayListExtra("userInfo",userInfo) ;
            intent.putExtra("userEmail",userEmail) ;
            intent.putExtra("user" , "user") ;
            startActivity(intent);
            finish();
        }
        else{

        }
    }

    public void goToBatchManagement(View view){
        Intent intent = new Intent(this, BatchHomePageActivity.class) ;
        intent.putExtra("user",user) ;
        intent.putExtra("groupID" , groupID) ;
        intent.putStringArrayListExtra("userInfo",userInfo) ;
        startActivity(intent);
        finish();
    }

    public void goToBackPageActivity(View view){
        if(user.equals("user")){
            Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
            intent.putStringArrayListExtra("userInfo",userInfo) ;
            startActivity(intent);
            finish();
        }
    }
}
