package website.skylorbeck.minecraft.megaparrot.mixin;

import net.minecraft.entity.passive.HorseBaseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HorseBaseEntity.class)
public interface HorseBaseEntityAccessor {
    @Invoker("setEating")
    void invokeSetEating();
}
