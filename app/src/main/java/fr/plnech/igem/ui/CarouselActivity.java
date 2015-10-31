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
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.plnech.igem.R;
import fr.plnech.igem.carousel.MemberAdapter;
import fr.plnech.igem.carousel.TeamMember;
import fr.plnech.igem.ui.model.DetailActivity;
import fr.rolandl.carousel.Carousel;
import fr.rolandl.carousel.CarouselAdapter;
import fr.rolandl.carousel.CarouselBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class CarouselActivity extends DetailActivity {
    @InjectView(R.id.carousel)
    Carousel carousel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        carousel = (Carousel) findViewById(R.id.carousel);

        final List<TeamMember> dataList = new ArrayList<>();
        loadData(dataList);
        final CarouselAdapter adapter = new MemberAdapter(this, dataList);
        final CarouselBaseAdapter.OnItemClickListener listener = getOnItemClickListener(dataList);
        carousel.setOnItemClickListener(listener);
        carousel.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @NonNull
    CarouselBaseAdapter.OnItemClickListener getOnItemClickListener(final List<TeamMember> dataList) {
        return new CarouselBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CarouselBaseAdapter<?> carouselBaseAdapter, View view, int position, long id) {
                final TeamMember member = dataList.get(position);
                final String quote = member.getQuote();
                Toast.makeText(getApplicationContext(), quote != null ? quote : member.getName(), Toast.LENGTH_SHORT).show();
                carousel.scrollToChild(position);
            }
        };
    }

    protected abstract void loadData(List<TeamMember> sponsors);
}
