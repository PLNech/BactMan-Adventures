package fr.plnech.igem.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.TextView;
import butterknife.InjectView;
import fr.plnech.igem.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.plnech.igem.ui.model.DetailActivity;
import fr.plnech.igem.utils.ViewUtils;

public class ContactUsActivity extends DetailActivity {

    @InjectView(R.id.link)
    protected TextView linkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        ViewUtils.textUnderline(linkView);
    }

    @OnClick(R.id.link)
    protected void onClickEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        Uri data = Uri.parse("mailto:"
                + R.string.contact_us_email_val);
        intent.setData(data);
        startActivity(Intent.createChooser(intent, "Send email"));
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
