package website.skylorbeck.minecraft.megaparrot;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import software.bernie.geckolib3.GeckoLib;

import static website.skylorbeck.minecraft.megaparrot.Declarar.config;

public class MegaParrot implements ModInitializer {
    @Override
    public void onInitialize() {
        GeckoLib.initialize();

        ConfigHolder<ParrotConfig> configHolder = AutoConfig.register(ParrotConfig.class, GsonConfigSerializer::new);
        config = configHolder.getConfig();
        configHolder.registerSaveListener((manager, data) -> {
            config = data;
            return ActionResult.SUCCESS;
        });

        FabricDefaultAttributeRegistry.register(Declarar.MEGA_PARROT_ENTITY_TYPE,
                MobEntity.createMobAttributes()
                        .add(EntityAttributes.HORSE_JUMP_STRENGTH)
                        .add(EntityAttributes.GENERIC_MAX_HEALTH, 53.0)
                        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.325f)
        );
        if (config.spawnStuff.jungle){
            BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.JUNGLE),SpawnGroup.CREATURE,Declarar.MEGA_PARROT_ENTITY_TYPE,config.spawnStuff.spawnWeightA,config.minGroupSize, config.maxGroupSize);
        }

        if (config.spawnStuff.taiga){
            BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.TAIGA),SpawnGroup.CREATURE,Declarar.MEGA_PARROT_ENTITY_TYPE,config.spawnStuff.spawnWeightB,config.minGroupSize, config.maxGroupSize);
        }

        if (config.spawnStuff.desert){
            BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.DESERT),SpawnGroup.CREATURE,Declarar.MEGA_PARROT_ENTITY_TYPE,config.spawnStuff.spawnWeightC,config.minGroupSize, config.maxGroupSize);
        }

        if (config.spawnStuff.plains){
            BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.PLAINS),SpawnGroup.CREATURE,Declarar.MEGA_PARROT_ENTITY_TYPE,config.spawnStuff.spawnWeightD,config.minGroupSize, config.maxGroupSize);
        }

        if (config.spawnStuff.mesa){
            BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.MESA),SpawnGroup.CREATURE,Declarar.MEGA_PARROT_ENTITY_TYPE,config.spawnStuff.spawnWeightE,config.minGroupSize, config.maxGroupSize);
        }

        if (config.spawnStuff.icy){
            BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.ICY),SpawnGroup.CREATURE,Declarar.MEGA_PARROT_ENTITY_TYPE,config.spawnStuff.spawnWeightF,config.minGroupSize, config.maxGroupSize);
        }

        Registry.register(Registry.ITEM,Declarar.getMegaParrotId("spawn_egg"),Declarar.MEGA_PARROT_EGG);
        Registry.register(Registry.ITEM,Declarar.getMegaParrotId("parrot_meat"),Declarar.PARROT_MEAT);
        Registry.register(Registry.ITEM,Declarar.getMegaParrotId("cooked_parrot_meat"),Declarar.COOKED_PARROT_MEAT);
        Registry.register(Registry.ITEM,Declarar.getMegaParrotId("bird_whistle"),Declarar.BIRD_WHISTLE);
        Registry.register(Registry.ITEM,Declarar.getMegaParrotId("mega_feather"),Declarar.MEGA_FEATHER);
    }
}
//todo
// special plant?