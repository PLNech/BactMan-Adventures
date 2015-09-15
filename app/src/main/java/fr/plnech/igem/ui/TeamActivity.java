package fr.plnech.igem.ui;

import android.os.Bundle;
import android.view.Gravity;
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

public class TeamActivity extends DetailActivity {

    @InjectView(R.id.carousel)
    Carousel carousel;

    @Override
    public int getTitleResId() {
        return R.string.title_activity_team;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_team;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        carousel = (Carousel) findViewById(R.id.carousel);
        carousel.setGravity(Gravity.TOP);
        
        final List<TeamMember> teamMembers = new ArrayList<>();
        teamMembers.add(new TeamMember("Paul-Louis", "Yo!", "avatar_pl"));
        teamMembers.add(new TeamMember("Armelle", "Yo!", "avatar_armelle"));
        teamMembers.add(new TeamMember("Gilles", "Yo!", "avatar_gilles"));
        teamMembers.add(new TeamMember("Hugo", "Yo!", "avatar_hugo"));
        teamMembers.add(new TeamMember("Johanna", "Yo!", "avatar_johanna"));
        teamMembers.add(new TeamMember("Lionel", "Yo!", "avatar_lionel"));
        teamMembers.add(new TeamMember("Marine", "Yo!", "avatar_marine"));
        teamMembers.add(new TeamMember("Marwa", "Yo!", "avatar_marwa"));
        teamMembers.add(new TeamMember("Matt", "Yo!", "avatar_matt"));
        teamMembers.add(new TeamMember("Nico", "Yo!", "avatar_nico"));
        teamMembers.add(new TeamMember("Samuel", "Yo!", "avatar_samuel"));
        teamMembers.add(new TeamMember("Goulwen", "This sucks.", "avatar1"));//TODO: Use round one

        final CarouselAdapter adapter = new MemberAdapter(this, teamMembers);
        carousel.setOnItemClickListener(new CarouselBaseAdapter.OnItemClickListener()
        {

            @Override
            public void onItemClick(CarouselBaseAdapter<?> carouselBaseAdapter, View view, int position, long id)
            {
                Toast.makeText(getApplicationContext(), "You clicked on " + teamMembers.get(position).getName() + ".", Toast.LENGTH_SHORT).show();
                carousel.scrollToChild(position);
            }

        });
        carousel.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
