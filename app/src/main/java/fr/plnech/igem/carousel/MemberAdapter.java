/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

package fr.plnech.igem.carousel;

import android.content.Context;
import fr.rolandl.carousel.CarouselAdapter;
import fr.rolandl.carousel.CarouselItem;

import java.util.List;

public class MemberAdapter extends CarouselAdapter<TeamMember> {
    public MemberAdapter(Context context, List<TeamMember> items) {
        super(context, items);
    }

    @Override
    public CarouselItem<TeamMember> getCarouselItem(Context context) {
        return new MemberItem(context);
    }
}
