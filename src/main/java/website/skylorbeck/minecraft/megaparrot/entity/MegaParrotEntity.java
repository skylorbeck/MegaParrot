package website.skylorbeck.minecraft.megaparrot.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class MegaParrotEntity extends HorseBaseEntity implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public MegaParrotEntity(EntityType<? extends HorseBaseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "locomotion_controller", 0, this::locomotion_predicate));
        animationData.addAnimationController(new AnimationController(this, "aux_controller", 0, this::aux_predicate));
    }

    private <E extends IAnimatable> PlayState locomotion_predicate(AnimationEvent<E> event)
    {
        if (event.isMoving())
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mega_parrot.walk", true));
        else
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mega_parrot.idle", true));
        return PlayState.CONTINUE;
    }
    private <E extends IAnimatable> PlayState aux_predicate(AnimationEvent<E> event)
    {
        if (this.world.random.nextFloat()<=0.01f)
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mega_parrot.wing_flutter", false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
