package website.skylorbeck.minecraft.megaparrot;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.GeckoLib;

public class MegaParrot implements ModInitializer {
    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        FabricDefaultAttributeRegistry.register(Declarar.MEGA_PARROT_ENTITY_TYPE,
                MobEntity.createMobAttributes()
                        .add(EntityAttributes.HORSE_JUMP_STRENGTH)
                        .add(EntityAttributes.GENERIC_MAX_HEALTH, 53.0)
                        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.325f)
        );
        BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.PARROT), SpawnGroup.CREATURE,Declarar.MEGA_PARROT_ENTITY_TYPE,50,2,10);
        Registry.register(Registry.ITEM,Declarar.getMegaParrotId("spawn_egg"),Declarar.MEGA_PARROT_EGG);
    }
}
