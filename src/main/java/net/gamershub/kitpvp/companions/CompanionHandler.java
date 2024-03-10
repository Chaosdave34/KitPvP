package net.gamershub.kitpvp.companions;

import net.gamershub.kitpvp.companions.impl.AllayCompanion;
import net.gamershub.kitpvp.companions.impl.ZoglinCompanion;

public class CompanionHandler{
    public static Companion ALLAY;
    public static Companion ZOGLIN;

    public CompanionHandler() {
        ALLAY = registerCompanion(new AllayCompanion());
        ZOGLIN = registerCompanion(new ZoglinCompanion());
    }

    private Companion registerCompanion(Companion companion) {
        return companion;
    }
}
