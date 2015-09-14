package fr.plnech.igem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.plnech.igem.R;
import fr.plnech.igem.game.LandscapeGameActivity;
import fr.plnech.igem.game.PortraitGameActivity;

public class GameMenuActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @Override
    int getTitleResId() {
        return R.string.title_activity_game_menu;
    }

    @Override
    int getLayoutResId() {
        return R.layout.activity_game_menu;
    }

    @OnClick(R.id.button_game_new)
    protected void onClickNew() {
        startActivity(new Intent(this, LandscapeGameActivity.class));
    }

    @OnClick(R.id.button_game_levels)
    protected void onClickLevels() {
        startActivity(new Intent(this, PortraitGameActivity.class));
    }

    @OnClick(R.id.button_game_settings)
    protected void onClickSettings() {
        Toast.makeText(GameMenuActivity.this, "En travaux!", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_game_help)
    protected void onClickHelp() {
        Toast.makeText(GameMenuActivity.this, "Need help?", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this, HelpActivity.class));
//        TODO: Consider a help page
    }

}
