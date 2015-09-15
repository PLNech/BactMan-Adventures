package fr.plnech.igem.ui;

import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.plnech.igem.R;
import fr.plnech.igem.ui.model.MenuActivity;

public class UsActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @Override
    public int getTitleResId() {
        return R.string.title_activity_us;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_us;
    }

    @OnClick(R.id.button_us_presentation)
    protected void onClickPresentation() {
        startActivity(new Intent(this, PresentationActivity.class));
    }

    @OnClick(R.id.button_us_team)
    protected void onClickTeam() {
        startActivity(new Intent(this, TeamActivity.class));
    }

    @OnClick(R.id.button_us_sponsors)
    protected void onClickSponsors() {
        startActivity(new Intent(this, SponsorsActivity.class));
    }

    @OnClick(R.id.button_us_contact)
    protected void onClickContactUs() {
        startActivity(new Intent(this, ContactUsActivity.class));
    }

}
