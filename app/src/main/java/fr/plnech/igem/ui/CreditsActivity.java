package fr.plnech.igem.ui;

import android.os.Bundle;
import android.widget.ScrollView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fr.plnech.igem.R;
import fr.plnech.igem.ui.model.DetailActivity;
import fr.plnech.igem.utils.DevUtils;

public class CreditsActivity extends DetailActivity {

    @InjectView(R.id.scrollView)
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        scrollView.setVerticalScrollBarEnabled(true);
    }

    @OnClick(R.id.paragraphs_andengine)
    protected void onClickAndEngine() {
        DevUtils.openLink(getResources().getString(R.string.url_andengine), this);
    }

    @OnClick(R.id.paragraphs_butterknife)
    protected void onClickButterKnife() {
        DevUtils.openLink(getResources().getString(R.string.url_butterknife), this);
    }

    @OnClick(R.id.paragraphs_carousel)
    protected void onClickCarousel() {
        DevUtils.openLink(getResources().getString(R.string.url_carousel), this);
    }

    @OnClick(R.id.paragraphs_icons)
    protected void onClickIcons() {
        DevUtils.openLink(getResources().getString(R.string.url_icons), this);
    }

    @Override
    public int getTitleResId() {
        return R.string.title_activity_credits;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_credits;
    }
}
