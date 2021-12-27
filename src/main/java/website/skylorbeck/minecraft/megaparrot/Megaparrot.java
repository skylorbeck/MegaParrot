package website.skylorbeck.minecraft.megaparrot;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import software.bernie.example.EntityUtils;
import software.bernie.geckolib3.GeckoLib;

public class Megaparrot implements ModInitializer {
    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        FabricDefaultAttributeRegistry.register(Declarar.MEGA_PARROT_ENTITY_TYPE,
                MobEntity.createMobAttributes()
                        .add(EntityAttributes.HORSE_JUMP_STRENGTH)
                        .add(EntityAttributes.GENERIC_MAX_HEALTH, 53.0)
                        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.325f)
        );
    }
}
