package website.skylorbeck.minecraft.megaparrot.entity;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import website.skylorbeck.minecraft.megaparrot.Declarar;

import java.util.Locale;

public class MegaParrotModel extends AnimatedGeoModel<MegaParrotEntity> {
    @Override
    public Identifier getModelLocation(MegaParrotEntity object) {
        return Declarar.getMegaParrotId("geo/mega_parrot.geo.json");
    }

    @Override
    public Identifier getTextureLocation(MegaParrotEntity object) {
        if (object.getCustomName() != null) {
            String name = object.getName().getString();
            if (name.equalsIgnoreCase("mordecai")){
                return Declarar.getMegaParrotId("textures/entity/mordecai.png");
            } else
            if (name.equalsIgnoreCase("CommissarGrey")){
                return Declarar.getMegaParrotId("textures/entity/phoenix.png");
            } else
            if (name.equalsIgnoreCase("Chone")){
                return Declarar.getMegaParrotId("textures/entity/chone.png");
            }
        }
        switch (object.getVariant()) {
            default -> {
                return Declarar.getMegaParrotId("textures/entity/red_parrot.png");
            }
            case 1 -> {
                return Declarar.getMegaParrotId("textures/entity/blue_parrot.png");
            }
            case 2 -> {
                return Declarar.getMegaParrotId("textures/entity/cyan_parrot.png");
            }
            case 3 -> {
                return Declarar.getMegaParrotId("textures/entity/green_parrot.png");
            }
            case 4 -> {
                return Declarar.getMegaParrotId("textures/entity/grey_parrot.png");
            }
        }
    }

    @Override
    public Identifier getAnimationFileLocation(MegaParrotEntity animatable) {
        return Declarar.getMegaParrotId("animations/mega_parrot.animation.json");
    }

    @SuppressWarnings({ "unchecked"})
    @Override
    public void setLivingAnimations(MegaParrotEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        IBone saddle = this.getAnimationProcessor().getBone("saddle");
        saddle.setHidden(!entity.isSaddled());

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
