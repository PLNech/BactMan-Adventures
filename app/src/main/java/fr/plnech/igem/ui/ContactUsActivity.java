package fr.plnech.igem.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.plnech.igem.R;
import fr.plnech.igem.ui.model.DetailActivity;
import fr.plnech.igem.utils.DevUtils;

public class ContactUsActivity extends DetailActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.icon_email)
    protected void onClickEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        Uri data = Uri.parse("mailto:"
                + getResources().getString(R.string.contact_us_email_val));
        intent.setData(data);
        startActivity(Intent.createChooser(intent, "Send email"));
    }

    @OnClick(R.id.icon_facebook)
    protected void onClickFacebook() {
        DevUtils.openFacebookLink(getResources().getString(R.string.url_contact_facebook), this);
    }

    @OnClick(R.id.icon_twitter)
    protected void onClickTwitter() {
        DevUtils.openLink(getResources().getString(R.string.url_contact_twitter), this);
    }

    @OnClick(R.id.icon_youtube)
    protected void onClickYoutube() {
        DevUtils.openLink(getResources().getString(R.string.url_contact_youtube), this);
    }

    @Override
    public int getTitleResId() {
        return R.string.title_activity_contact_us;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_contact_us;
    }

}
