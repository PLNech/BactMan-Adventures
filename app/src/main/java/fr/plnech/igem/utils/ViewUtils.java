package fr.plnech.igem.utils;

import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.text.style.UpdateAppearance;
import android.widget.TextView;

/**
 * Created by PLN on 07/07/2015.
 */
public class ViewUtils {
    public static void textUnderline(TextView view) {
        UnderlineSpan sUnder = new UnderlineSpan();
        textTransform(view, sUnder);
    }

    public static void textStrike(TextView view) {
        StrikethroughSpan sStrike = new StrikethroughSpan();
        textTransform(view, sStrike);
    }

    private static void textTransform(TextView view, UpdateAppearance appearance) {
        SpannableString viewText = new SpannableString(view.getText());
        viewText.setSpan(appearance, 0, viewText.length(), 0);
        view.setText(viewText);
    }
}
