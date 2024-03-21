package net.gamershub.kitpvp.companions;

import net.gamershub.kitpvp.companions.impl.AllayCompanion;
import net.gamershub.kitpvp.companions.impl.ZombifiedPiglinCompanion;

public class CompanionHandler{
    public static Companion ALLAY;
    public static Companion ZOMBIFIED_PIGLIN;

    public CompanionHandler() {
        ALLAY = registerCompanion(new AllayCompanion());
        ZOMBIFIED_PIGLIN = registerCompanion(new ZombifiedPiglinCompanion());
    }

    private Companion registerCompanion(Companion companion) {
        return companion;
    }
}
