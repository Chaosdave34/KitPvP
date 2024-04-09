package io.github.chaosdave34.kitpvp.challenges;

import io.github.chaosdave34.ghutils.Utils;
import io.github.chaosdave34.kitpvp.challenges.impl.TenKillsWithKit;
import io.github.chaosdave34.kitpvp.challenges.impl.TwentyFiveKillsChallenge;
import io.github.chaosdave34.kitpvp.kits.KitHandler;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class ChallengesHandler {
    private final Map<String, Challenge> challenges = new HashMap<>();

    public static Challenge TWENTY_FIVE_KILLS_CHALLENGE;
    public static Challenge TEN_KILLS_WITH_CLASSIC_KIT;
    public static Challenge TEN_KILLS_WITH_TANK_KIT;


    public ChallengesHandler() {
        TWENTY_FIVE_KILLS_CHALLENGE = registerChallenge(new TwentyFiveKillsChallenge());
        TEN_KILLS_WITH_CLASSIC_KIT = registerChallenge(new TenKillsWithKit(KitHandler.KNIGHT));
        TEN_KILLS_WITH_TANK_KIT = registerChallenge(new TenKillsWithKit(KitHandler.TANK));
    }

    private Challenge registerChallenge(Challenge challenge) {
        challenges.put(challenge.getId(), challenge);
        Utils.registerEvents(challenge);
        return challenge;
    }

    @Nullable
    public Challenge getChallenge(String id) {
        return challenges.get(id);
    }

    public List<Challenge> getThreeRandomChallenges() {
        List<Challenge> shuffledChallenges = new ArrayList<>(challenges.values());
        Collections.shuffle(shuffledChallenges);

        return shuffledChallenges.subList(0, 3);
    }

    public void resetProgress(Player p) {
        challenges.values().forEach(challenge -> challenge.resetProgress(p));
    }
}
