package io.github.chaosdave34.kitpvp.playerlist

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class PlayerListStaticEntry(player: Player, val component: Component, index: Int) : PlayerListEntry(player, index) {
    override fun getText(): Component = component

    override fun getTexture(): Pair<String, String> {
        return Pair(
            "ewogICJ0aW1lc3RhbXAiIDogMTcxOTM0OTM5OTU2MywKICAicHJvZmlsZUlkIiA6ICI1OGZmZWI5NTMxNGQ0ODcwYTQwYjVjYjQyZDRlYTU5OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTa2luREJuZXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E2Y2E0NWMxYjAyOTQxMDk3NWNjMGI3NjEyMTZjNGEwNTRlNzFhMWQxMjg1MWY5NDA0MjgxYTk2YTM0N2I3OSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
            "H7EXWxmsnLrc+jbTT0YRS1ppRptMSRKnlCG/hMtGS3cUqlMyXuXEuJ9GmV465w+G+KSro1pzS1om1uQmixiwjOHKmzD3oT+mhuCRDirb+/65zDMMqhLrhPW8VMCRgRKG4hE1ITxlZr3DVFXci5EL0TKH1lBgQDSYSGkvZ2TMrDc2bVXt8+sNpuJC//FRQJXvHs1a8CJATGlnOy+lp9GKbMdeRLkep2ttJX/GE+oIe0XZl/BM6s3ngBuux8yerYDhJTJjJOUvzb2pYPTuxTtsbfn/N7lncXjWMh9kn8+ZufeLmFYUah8WaO/8llF9TggarNpoy4CXnVlNNKLqjqAMjenfQ09SQyPtm8LQHD4kPkYK5xVqGGdISC/68CzNNjT+vCsFDBQYtHqncLkvqRNYy2qEKX3L8VOpRa2LKyzAoZXoBBE5jw5PHiFrCnaBMCyq8OjUAk5JYJIfrDE76YKZvRK/wosVFM/VmXO/HyfvteynzkBCusBY+LIMrN5bXXiWnaNAglKkxL/D49qasLma7/IGpre8g+6arkAqvfYR/qKKMCrmWe/HymxNpTkHNzlZcHUYMReIjblieg5gRGM0Ew41wC3ttljbGP7eEEz/RWhh/98geRsv2LoBNN3bV74NAETKH0FGsu6FyHfYPH+wO2xGfRi02SmBc7nqgl1m5Wo="
        )
    }
}