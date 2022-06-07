package website.skylorbeck.minecraft.megaparrot.mixin;

import net.minecraft.entity.passive.AbstractHorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractHorseEntity.class)
public interface HorseBaseEntityAccessor {
    @Invoker("setEating")
    void invokeSetEating();
}
