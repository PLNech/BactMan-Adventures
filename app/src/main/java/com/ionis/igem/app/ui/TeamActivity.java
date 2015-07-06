package com.ionis.igem.app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ionis.igem.app.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.firstImage)
    protected void onClickGoulwen() {
        Toast.makeText(getApplicationContext(), "You just clicked on Goulwen's avatar!", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.secondImage)
    protected void onClickJohanna() {
        Toast.makeText(getApplicationContext(), "You just clicked on Johanna's avatar!", Toast.LENGTH_SHORT).show();
    }

}
