package fr.plnech.igem.carousel;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import fr.plnech.igem.R;
import fr.rolandl.carousel.CarouselItem;

/**
 * Created by PLNech on 15/09/2015.
 */
public class MemberItem extends CarouselItem<TeamMember> {
    private final Context context;
    private TextView name;
    private ImageView avatar;

    public MemberItem(Context context) {
        super(context, R.layout.member_item);
        this.context = context;
    }

    @Override
    public void extractView(View view) {
        avatar = (ImageView) view.findViewById(R.id.avatar);
        name = (TextView) view.findViewById(R.id.name);
    }

    @Override
    public void update(TeamMember teamMember) {
        avatar.setImageResource(getResources().getIdentifier(teamMember.getAvatarResName(), "drawable", context.getPackageName()));
        if (teamMember.isTransparent()) {
            avatar.setBackgroundColor(getResources().getColor(android.R.color.white));
        }

        name.setText(teamMember.getName());
    }
}
