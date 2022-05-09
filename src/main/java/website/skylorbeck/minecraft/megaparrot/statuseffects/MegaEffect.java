package website.skylorbeck.minecraft.megaparrot.statuseffects;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.passive.ParrotEntity;
import website.skylorbeck.minecraft.megaparrot.Declarar;
import website.skylorbeck.minecraft.megaparrot.MegaParrot;
import website.skylorbeck.minecraft.megaparrot.entity.MegaParrotEntity;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MegaEffect extends StatusEffect {
    public MegaEffect() {
        super(StatusEffectCategory.BENEFICIAL, 16773073);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
            return duration <=10;
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getType().equals(EntityType.PARROT)){
            ParrotEntity parrot = (ParrotEntity) entity;
            MegaParrotEntity megaParrot = Declarar.MEGA_PARROT_ENTITY_TYPE.create(entity.world);
            assert megaParrot != null;
            megaParrot.setPos(parrot.getX(),parrot.getY(),parrot.getZ());
            megaParrot.setVariant(parrot.getVariant());
            if (parrot.getCustomName()!=null) {
                megaParrot.setCustomName(parrot.getCustomName());
            }
            parrot.world.spawnEntity(megaParrot);
            parrot.discard();
        }
        super.applyUpdateEffect(entity, amplifier);
    }
}
