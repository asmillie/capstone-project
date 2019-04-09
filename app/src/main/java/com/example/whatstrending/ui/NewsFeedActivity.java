package com.example.whatstrending.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.whatstrending.R;
import com.example.whatstrending.data.AppRepository;

public class NewsFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppRepository.getInstance(this);
    }
}