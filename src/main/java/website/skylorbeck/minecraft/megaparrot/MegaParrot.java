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
import net.minecraft.world.biome.Biome;
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
        BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.JUNGLE,Biome.Category.TAIGA,Biome.Category.DESERT, Biome.Category.PLAINS, Biome.Category.MESA), SpawnGroup.CREATURE,Declarar.MEGA_PARROT_ENTITY_TYPE,50,2,10);
        Registry.register(Registry.ITEM,Declarar.getMegaParrotId("spawn_egg"),Declarar.MEGA_PARROT_EGG);
        Registry.register(Registry.ITEM,Declarar.getMegaParrotId("parrot_meat"),Declarar.PARROT_MEAT);
        Registry.register(Registry.ITEM,Declarar.getMegaParrotId("cooked_parrot_meat"),Declarar.COOKED_PARROT_MEAT);
    }
}
//todo
// special plant?