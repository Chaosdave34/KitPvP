package io.github.chaosdave34.kitpvp.challenges;

import io.github.chaosdave34.kitpvp.Utils;
import io.github.chaosdave34.kitpvp.challenges.impl.TwentyFiveKillsChallenge;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class ChallengesHandler {
    private final Map<String, Challenge> challenges = new HashMap<>();

    public static Challenge TWENTY_FIVE_KILLS_CHALLENGE;


    public ChallengesHandler() {
        TWENTY_FIVE_KILLS_CHALLENGE = registerChallenge(new TwentyFiveKillsChallenge());
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
