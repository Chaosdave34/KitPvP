package io.github.chaosdave34.kitpvp.abilities;

import lombok.Getter;

@Getter
public enum AbilityType {
    RIGHT_CLICK("RIGHT CLICK"),
    LEFT_CLICK("LEFT CLICK"),
    SNEAK_RIGHT_CLICK("SNEAK RIGHT CLICK"),
    SNEAK_LEFT_CLICK("SNEAK LEFT CLICK"),
    SNEAK("SNEAK");

    private final String displayName;

    AbilityType(String name) {this.displayName = name;}
}
