package fr.plnech.igem.ui;

import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.plnech.igem.R;
import fr.plnech.igem.ui.model.MenuActivity;

public class iGEMActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @Override
    public int getTitleResId() {
        return R.string.title_activity_igem;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_igem;
    }

    @OnClick(R.id.button_igem_what)
    protected void onClickWhat() { startActivity(new Intent(this, WhatActivity.class)); }

    @OnClick(R.id.button_igem_biology)
    protected void onClickBio() {
        startActivity(new Intent(this, SynBioActivity.class));
    }

    @OnClick(R.id.button_igem_biobricks)
    protected void onClickBiobrick() {
        startActivity(new Intent(this, BiobrickActivity.class));
    }

    @OnClick(R.id.button_igem_partners)
    protected void onClickPartners() {
        startActivity(new Intent(this, PartnersActivity.class));
    }


}
