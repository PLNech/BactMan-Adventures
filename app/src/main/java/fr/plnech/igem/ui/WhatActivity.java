/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.ui;

import android.os.Bundle;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fr.plnech.igem.R;
import fr.plnech.igem.ui.model.DetailActivity;
import fr.plnech.igem.utils.DevUtils;
import fr.plnech.igem.utils.ViewUtils;

public class WhatActivity extends DetailActivity {

    @Override
    public int getTitleResId() {
        return R.string.title_activity_what;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_what;
    }

    @InjectView(R.id.link)
    protected TextView linkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        ViewUtils.textUnderline(linkView);
    }

    @OnClick(R.id.link)
    protected void onClickLink() {
        DevUtils.openLink(getResources().getString(R.string.what_url), this);
    }

}
