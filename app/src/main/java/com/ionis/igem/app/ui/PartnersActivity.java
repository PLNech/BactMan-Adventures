package com.ionis.igem.app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ionis.igem.app.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PartnersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);
        ButterKnife.inject(this);
    }

}
