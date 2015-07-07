package com.ionis.igem.app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.ionis.igem.app.R;
import com.ionis.igem.app.utils.ViewUtils;

public class WhatActivity extends AppCompatActivity {

    @InjectView(R.id.link)
    protected TextView linkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what);
        ButterKnife.inject(this);
        ViewUtils.textUnderline(linkView);

    }

    @OnClick(R.id.link)
    protected void onClickLink() {
        Intent iWebsite = new Intent(Intent.ACTION_VIEW);
        iWebsite.setData(Uri.parse(getResources().getString(R.string.what_url)));
        startActivity(iWebsite);
    }

}
