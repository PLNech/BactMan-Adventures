package com.ionis.igem.app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ionis.igem.app.R;

public class PartnersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.link)
    protected void onClickLink() {
        Intent iWebsite = new Intent(Intent.ACTION_VIEW);
        iWebsite.setData(Uri.parse(getResources().getString(R.string.what_url)));
        startActivity(iWebsite);
    }

}
