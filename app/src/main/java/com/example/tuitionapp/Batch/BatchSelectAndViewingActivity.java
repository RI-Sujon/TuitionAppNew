package com.example.tuitionapp.Batch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.tuitionapp.R;

import java.util.ArrayList;

public class BatchSelectAndViewingActivity extends AppCompatActivity {

    private ArrayList<BatchInfo> batchInfoArrayList ;

    private CustomAdapterForSelectAndViewBatch adapter ;

    private ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_select_and_viewing);


    }

    public void goToBatchListView(){

    }
}
