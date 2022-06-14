package website.skylorbeck.minecraft.megaparrot.mixin;

import net.minecraft.entity.passive.ParrotEntity;
import org.spongepowered.asm.mixin.Mixin;
import website.skylorbeck.minecraft.megaparrot.Declarar;
import website.skylorbeck.minecraft.megaparrot.entity.MegaParrotEntity;
import website.skylorbeck.minecraft.skylorlib.IMegable;

@Mixin(ParrotEntity.class)
public class ParrotMixin implements IMegable {
    @Override
    public void Megafy() {
        ParrotEntity parrot = (ParrotEntity) (Object)(this);
        MegaParrotEntity megaParrot = Declarar.MEGA_PARROT_ENTITY_TYPE.create(parrot.world);
        assert megaParrot != null;
        megaParrot.setPos(parrot.getX(), parrot.getY(), parrot.getZ());
        megaParrot.setVariant(parrot.getVariant());
        if (parrot.getCustomName() != null) {
            megaParrot.setCustomName(parrot.getCustomName());
        }
        parrot.world.spawnEntity(megaParrot);
        parrot.discard();
    }
}
