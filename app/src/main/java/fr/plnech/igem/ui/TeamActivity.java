package fr.plnech.igem.ui;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import com.crashlytics.android.answers.Answers;
import fr.plnech.igem.R;
import fr.plnech.igem.carousel.TeamMember;
import fr.plnech.igem.game.model.MemberEvent;
import fr.plnech.igem.utils.DevUtils;
import fr.rolandl.carousel.CarouselBaseAdapter;

import java.util.List;

public class TeamActivity extends CarouselActivity {

    @Override
    public int getTitleResId() {
        return R.string.title_activity_team;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_team;
    }

    @Override
    protected void loadData(List<TeamMember> teamMembers) {
        teamMembers.add(new TeamMember("Paul-Louis", "Nech", "avatar_pl", "A human is a system for converting dust billions of years ago " +
                "into dust billions of years from now via a roundabout process which involves checking email a lot.\n\n" +
                "[Randall Munroe - xkcd]", "https://fr.linkedin.com/in/plnech", "https://twitter.com/PaulLouisNech"));
        teamMembers.add(new TeamMember("Armelle", "Louisy", "avatar_armelle", "Determination, availability, discipline and success is within reach.\n\n" +
                "[Philip Roth]", "https://fr.linkedin.com/pub/armelle-louisy/69/b24/983"));
        teamMembers.add(new TeamMember("Gilles", "Defrel", "avatar_gilles", "Intelligence is not what you know but what you do when you don't know.\n\n" +
                "[Jean Piaget]", "https://www.linkedin.com/pub/gilles-defrel/90/939/a32"));
        teamMembers.add(new TeamMember("Hugo", "Cremaschi", "avatar_hugo", "The advantage of being smart is that you can always make a fool,\n" +
                "While the opposite is impossible.\n\n" +
                "[Woody Allen]", "https://www.linkedin.com/pub/hugo-cremaschi/a4/b0a/b7a"));
        teamMembers.add(new TeamMember("Johanna", "Chesnel", "avatar_johanna", "You have to learn the rules of the game.\n" +
                "And then you have to play better than anyone else.\n\n" +
                "[Albert Einstein]", "https://www.linkedin.com/pub/johanna-chesnel/76/53a/a"));
        teamMembers.add(new TeamMember("Lionel", "Chesnais", "avatar_lionel", null, "https://www.linkedin.com/pub/lionel-chesnais/90/954/98a"));
        teamMembers.add(new TeamMember("Marine", "Mohmmed", "avatar_marine", "An essential aspect of creativity is not being afraid to fail.\n\n" +
                "[Edwin Land]", "https://www.linkedin.com/pub/marine-mohmmed/62/116/72a"));
        teamMembers.add(new TeamMember("Marwa", "Zerhouni", "avatar_marwa", "The best way to predict the future is to create it.\n\n" +
                "[Abraham Lincoln]", "https://www.linkedin.com/pub/marwa-zerhouni/96/718/643/en"));
        teamMembers.add(new TeamMember("Matthieu", "Da Costa", "avatar_matt", "If you can't explain it simply, you don't understand it well enough.\n" +
                "[Albert Einstein]", "https://www.linkedin.com/pub/matthieu-da-costa/"));
        teamMembers.add(new TeamMember("Nicolas", "Cornille", "avatar_nico", "Satisfaction lies in the effort, not in the attainment.\n" +
                "Full effort is full victory.\n\n" +
                "[Mahatma Gandhi]", "https://www.linkedin.com/pub/nicolas-cornille/98/58/251"));
        teamMembers.add(new TeamMember("Goulwen", "Mintec", "avatar_goulwen", null, "https://www.linkedin.com/pub/goulwen-mintec/88/a6a/4ab"));
        teamMembers.add(new TeamMember("Pauline", "Trébulle", "avatar_pauline", "Study the science of art. Study the art of science.\n" +
                "Develop your senses - learn how to see. Realize that everything connects to everything else.\n\n" +
                "[Leonardo da Vinci]", "https://www.linkedin.com/pub/pauline-tr%C3%A9bulle/79/499/20b"));
    }

    @NonNull
    @Override
    protected CarouselBaseAdapter.OnItemClickListener getOnItemClickListener(final List<TeamMember> dataList) {
        return new CarouselBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CarouselBaseAdapter<?> carouselBaseAdapter, View view, int position, long id) {
                final TeamMember member = dataList.get(position);
                carousel.scrollToChild(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(TeamActivity.this);
                builder.setTitle(member.getName() + " " + member.getLastName())
                        .setMessage(member.getQuote())
                        .setCancelable(false)
                        .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Answers.getInstance().logCustom(new MemberEvent(member, MemberEvent.ACTION_CLOSE));
                            }
                        })
                        .setPositiveButton("LinkedIn", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Answers.getInstance().logCustom(new MemberEvent(member, MemberEvent.ACTION_LINKEDIN));
                                DevUtils.openLink(member.getUrlLinkedin(), TeamActivity.this);
                            }
                        });
                final String urlTwitter = member.getUrlTwitter();
                if (urlTwitter != null) {
                    builder.setNegativeButton("Twitter", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Answers.getInstance().logCustom(new MemberEvent(member, MemberEvent.ACTION_TWITTER));
                            DevUtils.openLink(urlTwitter, TeamActivity.this);
                        }
                    });
                }
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
    }
}
