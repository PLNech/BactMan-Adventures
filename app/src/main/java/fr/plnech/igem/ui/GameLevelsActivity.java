package fr.plnech.igem.ui;

import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.plnech.igem.R;
import fr.plnech.igem.game.model.BaseGame;

public class GameLevelsActivity extends MenuActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

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
        BaseGame.startGame(this, BaseGame.ID_GUT);
    }

    @OnClick(R.id.button_levels_bin)
    protected void onClickBin() {
        BaseGame.startGame(this, BaseGame.ID_BIN);
    }

    @OnClick(R.id.button_levels_picto)
    protected void onClickPicto() {
        BaseGame.startGame(this, BaseGame.ID_PICTO);
    }

    @OnClick(R.id.button_levels_piano)
    protected void onClickPiano() {
        BaseGame.startGame(this, BaseGame.ID_PIANO);
    }
}
