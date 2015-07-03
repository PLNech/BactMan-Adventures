package com.ionis.igem.app.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ionis.igem.app.R;
import com.ionis.igem.app.SynBioActivity;

public class iGEMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igem);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.button_igem_what)
    protected void onClickWhat() { startActivity(new Intent(this, WhatActivity.class)); }

    @OnClick(R.id.button_igem_biology)
    protected void onClickBio() {
        startActivity(new Intent(this, SynBioActivity.class));
    }


}
