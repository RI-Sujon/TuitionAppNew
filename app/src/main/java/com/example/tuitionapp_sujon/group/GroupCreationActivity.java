package com.example.tuitionapp_sujon.group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tuitionapp_sujon.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupCreationActivity extends AppCompatActivity {
    DatabaseReference myRefGroup ;

    private AutoCompleteTextView addressSpinner ;
    private Button createGroupButton ;
    private EditText groupNameEditText, fullAddressEditText ;
    private String user ;
    private ArrayList<String> userInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);

        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        user = intent.getStringExtra("user") ;

        myRefGroup = FirebaseDatabase.getInstance().getReference("Group") ;

        createGroupButton = findViewById(R.id.createGroupButton) ;
        addressSpinner = findViewById(R.id.groupAddressSpinner2) ;
        groupNameEditText = findViewById(R.id.groupName) ;
        fullAddressEditText = findViewById(R.id.detailsAddress) ;

        List areaAddress = new ArrayList(Arrays.asList(getResources().getStringArray(R.array.areaAddress_array))) ;
        areaAddress.remove(0) ;
        ArrayAdapter adapter = new ArrayAdapter(GroupCreationActivity.this,android.R.layout.simple_dropdown_item_1line,areaAddress);

        addressSpinner.setAdapter(adapter);
    }

    public void groupCreation(View view){
        String groupName, areaAddress, fullAddress ;

        areaAddress = addressSpinner.getText().toString().trim() ;
        groupName = groupNameEditText.getText().toString().trim() ;
        fullAddress = fullAddressEditText.getText().toString().trim() ;

        if(groupName.equals("")){
            groupNameEditText.setError("");
            return;
        }

        if(areaAddress.equals("")){
            addressSpinner.setError("");
            return;
        }

        if(fullAddress.equals("")){
            fullAddressEditText.setError("");
            return;
        }

        GroupInfo groupInfo = new GroupInfo(groupName,areaAddress,fullAddress,userInfo.get(3)) ;
        myRefGroup.push().setValue(groupInfo) ;
        Toast.makeText(getApplicationContext(), "Group Successfully Created", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,GroupHomePageActivity.class);
        intent.putStringArrayListExtra("userInfo",userInfo) ;
        intent.putExtra("user" , user) ;
        startActivity(intent);
        finish();

    }
}
