package fr.plnech.igem.ui;

import fr.plnech.igem.R;
import fr.plnech.igem.carousel.TeamMember;

import java.util.List;

public class SponsorsActivity extends CarouselActivity {

    @Override
    public int getTitleResId() {
        return R.string.title_activity_sponsors;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_sponsors;
    }

    @Override
    protected void loadData(List<TeamMember> sponsors) {
        addSponsor(sponsors, new TeamMember("E Art-Sup", "sponsor_e_artsup"));
        addSponsor(sponsors, new TeamMember("Sup BioTech", "sponsor_supbiotech"));
        addSponsor(sponsors, new TeamMember("EPITA", "sponsor_epita"));
        addSponsor(sponsors, new TeamMember("Fluigent", "sponsor_fluigent"));
        addSponsor(sponsors, new TeamMember("Le BioClub", "sponsor_bioclub"));
        addSponsor(sponsors, new TeamMember("MicroFactory", "sponsor_microfactory"));
        addSponsor(sponsors, new TeamMember("Embassy of France in the United States", null, "sponsor_embassy",
                "Embassy of France in the United States\n\nOffice for Science and Technology"));
        addSponsor(sponsors, new TeamMember("BIOSS", "sponsor_bioss"));
        addSponsor(sponsors, new TeamMember("Glowee", "sponsor_glowee"));
        addSponsor(sponsors, new TeamMember("KickStarter", "sponsor_kickstarter"));
        addSponsor(sponsors, new TeamMember("New England BioLabs", "sponsor_neb"));
        addSponsor(sponsors, new TeamMember("QIAGEN", "sponsor_qiagen"));
        addSponsor(sponsors, new TeamMember("UNI Freiburg", "sponsor_uni"));
    }

    private void addSponsor(List<TeamMember> sponsors, TeamMember member) {
        member.setTransparent(true);
        sponsors.add(member);
    }

}
