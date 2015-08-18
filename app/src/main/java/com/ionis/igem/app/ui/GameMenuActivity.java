package com.ionis.igem.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ionis.igem.app.R;

public class GameMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.button_game_new)
    protected void onClickNew() {
        startActivity(new Intent(this, GameActivity.class));
    }

    @OnClick(R.id.button_game_levels)
    protected void onClickLevels() {
        startActivity(new Intent(this, GameLevelsActivity.class));
    }

    @OnClick(R.id.button_game_settings)
    protected void onClickSettings() {
        startActivity(new Intent(this, GameSettingsActivity.class));
    }

    @OnClick(R.id.button_game_help)
    protected void onClickHelp() {
        Toast.makeText(GameMenuActivity.this, "Need help?", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this, HelpActivity.class));
//        TODO: Consider a help page
    }

}
