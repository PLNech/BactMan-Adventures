package fr.plnech.igem.glossary;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import fr.plnech.igem.R;

import java.util.*;

/**
 * Created by Paul-Louis Nech on 15/09/2015.
 */
public class EntryListAdapter extends ArrayAdapter<Entry> implements SectionIndexer {
    public static final String TAG = "EntryAdapter";
    public List<Entry> entries;
    HashMap<String, Integer> mapIndex;
    String[] sections;

    public EntryListAdapter(Context context, List<Entry> pEntries) {
        super(context, R.layout.glossary_item, R.id.txt_title, pEntries);
        entries = pEntries;
        mapIndex = new LinkedHashMap<>();

        for (int i = 0; i < entries.size(); i++) {
            Entry e = entries.get(i);
            String initial = e.getName().substring(0, 1).toUpperCase(Locale.US);

            mapIndex.put(initial, i);
        }

        Set<String> sectionLetters = mapIndex.keySet();
        ArrayList<String> sectionList = new ArrayList<>(sectionLetters);

        Log.d("sectionList", sectionList.toString());
        Collections.sort(sectionList);
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);
    }

    @Override
    public int getPositionForSection(int section) {
        Log.d(TAG, "getPositionForSection - " + section);
        return mapIndex.get(sections[section]);
    }

    @Override
    public int getSectionForPosition(int position) {
        Log.d("TAG", "getSectionForPosition - " + position);
        return 0;
    }

    @Override
    public Object[] getSections() {
        return sections;
    }
}
