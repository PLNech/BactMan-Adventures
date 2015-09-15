package fr.plnech.igem.ui;

import android.os.Bundle;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.plnech.igem.R;
import fr.plnech.igem.ui.model.DetailActivity;

public class TeamActivity extends DetailActivity {

    @Override
    public int getTitleResId() {
        return R.string.title_activity_team;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_team;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.firstImage)
    protected void onClickGoulwen() {
        Toast.makeText(getApplicationContext(), "You just clicked on Goulwen's avatar!", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.secondImage)
    protected void onClickJohanna() {
        Toast.makeText(getApplicationContext(), "You just clicked on Johanna's avatar!", Toast.LENGTH_SHORT).show();
    }

}
