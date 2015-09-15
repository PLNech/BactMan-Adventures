package fr.plnech.igem.ui;

import fr.plnech.igem.R;
import fr.plnech.igem.ui.model.DetailActivity;

public class PresentationActivity extends DetailActivity {

    @Override
    public int getTitleResId() {
        return R.string.title_activity_presentation;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_presentation;
    }

}
