package fr.plnech.igem.carousel;

import android.content.Context;
import fr.rolandl.carousel.CarouselAdapter;
import fr.rolandl.carousel.CarouselItem;

import java.util.List;

/**
 * Created by PLNech on 15/09/2015.
 */
public class MemberAdapter extends CarouselAdapter<TeamMember> {
    public MemberAdapter(Context context, List<TeamMember> items) {
        super(context, items);
    }

    @Override
    public CarouselItem<TeamMember> getCarouselItem(Context context) {
        return new MemberItem(context);
    }
}
