package fr.plnech.igem.game.model;

import com.crashlytics.android.answers.CustomEvent;
import fr.plnech.igem.carousel.TeamMember;

/**
 * Created by PLNech on 25/09/2015.
 */
public class MemberEvent extends CustomEvent{
    public static final int ACTION_CLOSE = 0;
    public static final int ACTION_LINKEDIN = 1;
    public static final int ACTION_TWITTER = 2;

    private static final String TAG = "GlossaryReadEvent";
    private static final String KEY_ACTION = "Action";

    public MemberEvent(TeamMember member, int action) {
        super("TeamView" + member.getName());
        putCustomAttribute(KEY_ACTION, getActionName(action));
    }

    private String getActionName(int action) {
        switch (action) {
            case 0:
                return "Close";
            case 1:
                return "LinkedIn";
            case 2:
                return "Twitter";
        }
        throw new IllegalArgumentException("No name for value: " + action);
    }

}
