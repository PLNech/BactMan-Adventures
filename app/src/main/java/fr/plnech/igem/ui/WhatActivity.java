package fr.plnech.igem.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fr.plnech.igem.R;
import fr.plnech.igem.ui.model.DetailActivity;
import fr.plnech.igem.utils.ViewUtils;

public class WhatActivity extends DetailActivity {

    @Override
    public int getTitleResId() {
        return R.string.title_activity_what;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_what;
    }

    @InjectView(R.id.link)
    protected TextView linkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
