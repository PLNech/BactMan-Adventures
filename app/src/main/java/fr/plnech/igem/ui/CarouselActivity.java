package fr.plnech.igem.ui;

import android.os.Bundle;
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

/**
 * Created by PLNech on 16/09/2015.
 */
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
        carousel.setOnItemClickListener(new CarouselBaseAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(CarouselBaseAdapter<?> carouselBaseAdapter, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), dataList.get(position).getQuote(), Toast.LENGTH_SHORT).show();
                carousel.scrollToChild(position);
            }

        });
        carousel.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    protected abstract void loadData(List<TeamMember> sponsors);
}
