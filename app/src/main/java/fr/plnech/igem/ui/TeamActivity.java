package fr.plnech.igem.ui;

import fr.plnech.igem.R;
import fr.plnech.igem.carousel.TeamMember;

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
        teamMembers.add(new TeamMember("Paul-Louis", "avatar_pl", "√(♥) = ? | cos(♥) = ? | d♥/dt = ? | ⌹♥ = ?\n" +
                "My usual approach is useless here.\n\n" +
                "[Randall Munroe - xkcd]"));
        teamMembers.add(new TeamMember("Armelle", "avatar_armelle", "Determination, availability, discipline and success is within reach.\n\n" +
                "[Philip Roth]"));
        teamMembers.add(new TeamMember("Gilles", "avatar_gilles", "Intelligence is not what you know but what you do when you don't know.\n\n" +
                "[Jean Piaget]"));
        teamMembers.add(new TeamMember("Hugo", "avatar_hugo", "The advantage of being smart is that you can always make a fool,\n" +
                "While the opposite is impossible.\n\n" +
                "[Woody Allen]"));
        teamMembers.add(new TeamMember("Johanna", "avatar_johanna", "You have to learn the rules of the game.\n" +
                "And then you have to play better than anyone else.\n\n" +
                "[Albert Einstein]"));
        teamMembers.add(new TeamMember("Lionel", "avatar_lionel", null));
        teamMembers.add(new TeamMember("Marine", "avatar_marine", "An essential aspect of creativity is not being afraid to fail.\n\n" +
                "[Edwin Land]"));
        teamMembers.add(new TeamMember("Marwa", "avatar_marwa", null));
        teamMembers.add(new TeamMember("Matt", "avatar_matt", null));
        teamMembers.add(new TeamMember("Nico", "avatar_nico", "Satisfaction lies in the effort, not in the attainment.\n" +
                "Full effort is full victory.\n\n" +
                "[Mahatma Gandhi]"));
        teamMembers.add(new TeamMember("Samuel", "avatar_samuel", null));
        teamMembers.add(new TeamMember("Goulwen", "avatar_goulwen", null));
        teamMembers.add(new TeamMember("Pauline", "avatar_pauline", "Study the science of art. Study the art of science.\n" +
                "Develop your senses - learn how to see. Realize that everything connects to everything else.\n\n" +
                "[Leonardo da Vinci]"));
    }

}
