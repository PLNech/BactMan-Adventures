package fr.plnech.igem.ui;

import android.content.Intent;
import butterknife.OnClick;
import fr.plnech.igem.R;

public class GameLevelsActivity extends MenuActivity {

    @Override
    int getTitleResId() {
        return R.string.title_activity_game_levels;
    }

    @Override
    int getLayoutResId() {
        return R.layout.activity_game_levels;
    }


    @OnClick(R.id.button_levels_gut)
    protected void onClickGut() {
        startActivity(new Intent(this, PresentationActivity.class));
    }

    @OnClick(R.id.button_levels_bin)
    protected void onClickBin() {
        startActivity(new Intent(this, TeamActivity.class));
    }

    @OnClick(R.id.button_levels_picto)
    protected void onClickPicto() {
        startActivity(new Intent(this, SponsorsActivity.class));
    }

    @OnClick(R.id.button_levels_piano)
    protected void onClickPiano() {
        startActivity(new Intent(this, ContactUsActivity.class));
    }


}
