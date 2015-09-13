package fr.plnech.igem.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.TextView;
import butterknife.InjectView;
import fr.plnech.igem.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.plnech.igem.utils.ViewUtils;

public class ContactUsActivity extends AppCompatActivity {

    @InjectView(R.id.link)
    protected TextView linkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
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

}
