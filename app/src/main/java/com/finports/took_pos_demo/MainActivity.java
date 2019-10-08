package com.finports.took_pos_demo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<RecyclerItem> recyclerItemsList = new ArrayList<>();
        recyclerItemsList.add(
                new RecyclerItem("설화수 예서스킨커버 21호 은은한 색 SPF25", 2, "100,908원"));
        recyclerItemsList.add(
                new RecyclerItem("설화수 전설마스크 80ml", 3, "533,376원"));

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(recyclerItemsList);

        Button insertListBtn = (Button) findViewById(R.id.plusProduct);
        insertListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.recyclerview_item, null, false);
            }
        });

        mRecyclerView.setAdapter(recyclerAdapter);
    }
}
