package fr.plnech.igem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.plnech.igem.R;
import fr.plnech.igem.game.model.BaseGame;
import fr.plnech.igem.ui.model.MenuActivity;

public class GameMenuActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @Override
    public int getTitleResId() {
        return R.string.title_activity_game_menu;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_game_menu;
    }

    @OnClick(R.id.button_game_new)
    protected void onClickNew() {
        final int lastUnlockedId = BaseGame.getLastUnlockedId(getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE));
        Toast.makeText(GameMenuActivity.this, "Last unlocked id: " + lastUnlockedId, Toast.LENGTH_SHORT).show();
        BaseGame.startGame(this, lastUnlockedId);
    }

    @OnClick(R.id.button_game_levels)
    protected void onClickLevels() {
        startActivity(new Intent(this, GameLevelsActivity.class));
    }

    @OnClick(R.id.button_game_glossary)
    protected void onClickGlossary() {
        startActivity(new Intent(this, GlossaryActivity.class));
    }

}
