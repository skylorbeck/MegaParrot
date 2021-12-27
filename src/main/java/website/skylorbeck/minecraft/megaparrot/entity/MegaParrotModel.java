package website.skylorbeck.minecraft.megaparrot.entity;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import website.skylorbeck.minecraft.megaparrot.Declarar;

public class MegaParrotModel extends AnimatedGeoModel<MegaParrotEntity> {
    @Override
    public Identifier getModelLocation(MegaParrotEntity object) {
        return Declarar.getMegaParrotId("geo/mega_parrot.geo.json");
    }

    @Override
    public Identifier getTextureLocation(MegaParrotEntity object) {
        return Declarar.getMegaParrotId("textures/entity/red_parrot.png");
    }

    @Override
    public Identifier getAnimationFileLocation(MegaParrotEntity animatable) {
        return Declarar.getMegaParrotId("animations/mega_parrot.animation.json");
    }

    @Override
    public void setLivingAnimations(MegaParrotEntity entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
