/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.ui;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.crashlytics.android.answers.Answers;
import fr.plnech.igem.R;
import fr.plnech.igem.game.model.GlossaryReadEvent;
import fr.plnech.igem.glossary.Entry;
import fr.plnech.igem.glossary.EntryListAdapter;
import fr.plnech.igem.ui.model.LoggedActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GlossaryActivity extends ListActivity {
    public static final String TAG = "GlossaryActivity";

    @InjectView(android.R.id.list)
    protected ListView glossaryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_glossary);
        Resources resources = getResources();

        final String contentName = resources.getString(R.string.title_activity_game_glossary);
        final String contentId = resources.getResourceName(R.layout.activity_game_glossary);
        LoggedActivity.logView(contentName, "DetailActivity", contentId, this);

        ButterKnife.inject(this);

        String[] entryTitles = resources.getStringArray(R.array.glossary_titles);
        String[] entryTexts = resources.getStringArray(R.array.glossary_texts);

        ArrayList<Entry> entries = buildList(entryTitles, entryTexts);
        Collections.sort(entries, new Comparator<Entry>() {
            @Override
            public int compare(Entry lhs, Entry rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        setListAdapter(new EntryListAdapter(this, entries));

        glossaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Entry item = (Entry) parent.getItemAtPosition(position);
                LoggedActivity.logView("Glossary: " + item.getName(), "GlossaryActivity", "glossary_" + item.getName().replace(" ", "_").toLowerCase(), GlossaryActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setTitle(item.getName())
                        .setMessage(item.getDescription())
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Answers.getInstance().logCustom(new GlossaryReadEvent(item.getName()));
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private ArrayList<Entry> buildList(String[] entryTitles, String[] entryTexts) {
        ArrayList<Entry> craftedList = new ArrayList<>();
        if (entryTexts.length != entryTitles.length) {
            throw new IllegalStateException("Texts and titles have different lengths!");
        }

        for (int i = 0; i < entryTexts.length; i++) {
            final Entry e = new Entry(entryTitles[i], entryTexts[i]);
            craftedList.add(e);
        }
        return craftedList;
    }
}
