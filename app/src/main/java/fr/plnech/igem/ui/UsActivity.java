package fr.plnech.igem.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.plnech.igem.R;

public class UsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_us);
        ButterKnife.inject(this);
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
