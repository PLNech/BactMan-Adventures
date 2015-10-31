/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/
package fr.plnech.igem.utils;

import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.text.style.UpdateAppearance;
import android.widget.TextView;

/**
 * Created by PLNech on 07/07/2015.
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
