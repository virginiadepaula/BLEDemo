package com.enautec.ble.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enautec.ble.R;
import com.enautec.ble.adapter.AdapterTagScan;
import com.enautecble.model.TagEnautec;

import java.util.ArrayList;

public class ActivityTagScan extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterTagScan adapterTagScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_scan);

        ArrayList<TagEnautec> tagEnautecList = (ArrayList<TagEnautec>) getIntent().getSerializableExtra("TAG");
        recyclerView = findViewById(R.id.recyclerTag);
        adapterTagScan = new AdapterTagScan(tagEnautecList,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterTagScan);

    }

}