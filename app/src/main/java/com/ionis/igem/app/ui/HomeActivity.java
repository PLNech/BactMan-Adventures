package com.ionis.igem.app.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ionis.igem.app.R;

public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.button_home_new_game)
    protected void onClickNewGame() {
        Toast.makeText(getApplicationContext(), "You just clicked on new game!", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_home_team)
    protected void onClickTeam() {
        startActivity(new Intent(this, UsActivity.class));
    }

    @OnClick(R.id.button_home_iGEM)
    protected void onClickIGEM() {
        startActivity(new Intent(this, iGEMActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    // TODO: Discuss usefulness of OptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
