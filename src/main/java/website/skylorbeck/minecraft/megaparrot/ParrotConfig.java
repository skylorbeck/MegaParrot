package website.skylorbeck.minecraft.megaparrot;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "megaparrot")
public class ParrotConfig implements ConfigData {

    @ConfigEntry.BoundedDiscrete(min = 1, max = 4)
    public int minGroupSize = 2;
    @ConfigEntry.BoundedDiscrete(min = 4, max = 10)
    public int maxGroupSize = 4;

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public spawnStuff spawnStuff = new spawnStuff();

    static class spawnStuff {
        public boolean jungle = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 24)
        public int spawnWeightA = 24;

        public boolean taiga = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int spawnWeightB = 6;

        public boolean desert = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int spawnWeightC = 4;

        public boolean plains = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int spawnWeightD = 6;
    }
}
