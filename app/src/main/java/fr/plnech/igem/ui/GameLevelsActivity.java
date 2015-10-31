/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fr.plnech.igem.R;
import fr.plnech.igem.game.model.BaseGame;
import fr.plnech.igem.ui.model.MenuActivity;

public class GameLevelsActivity extends MenuActivity {

    @InjectView(R.id.button_levels_bin)
    Button binButton;
    @InjectView(R.id.button_levels_picto)
    Button pictoButton;
    @InjectView(R.id.button_levels_piano)
    Button pianoButton;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        final boolean isLockedBin = setButtonStatus(BaseGame.ID_BIN, binButton);
        final boolean isLockedPicto = setButtonStatus(BaseGame.ID_PICTO, pictoButton);
        final boolean isLockedPiano = setButtonStatus(BaseGame.ID_PIANO, pianoButton);
        Boolean isAtLeastOneLocked = isLockedBin || isLockedPicto || isLockedPiano;
        if (isAtLeastOneLocked) {
            Toast.makeText(GameLevelsActivity.this, getString(R.string.msg_game_unlock), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean setButtonStatus(int gameId, Button button) {
        /**
         * Updates the given button according to the identified game's unlocked status
         * Returns true if the game is locked
         */
        final boolean gameIsLocked = !BaseGame.getUnlockedStatus(gameId, preferences);
        button.setEnabled(!gameIsLocked); //Button is disabled if game is locked
        Log.d("DEBUG", "setButtonStatus - game " + gameId + "isLocked: " + gameIsLocked);
        return gameIsLocked;
    }

    @Override
    public int getTitleResId() {
        return R.string.title_activity_game_levels;
    }

    @Override
    public int getLayoutResId() {
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
