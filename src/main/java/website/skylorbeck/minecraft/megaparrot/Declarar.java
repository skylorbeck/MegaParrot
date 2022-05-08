package website.skylorbeck.minecraft.megaparrot;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.megaparrot.entity.MegaParrotEntity;

public class Declarar {
    public static String MODID = "megaparrot";
    public static ParrotConfig config = new ParrotConfig();

    public static Identifier getMegaParrotId(String path) {
        return new Identifier(MODID, path);
    }

    private static final Identifier MEGA_PARROT_ID = getMegaParrotId("megaparrot");
    public static final EntityType<MegaParrotEntity> MEGA_PARROT_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, MEGA_PARROT_ID,
            EntityType.Builder.create(MegaParrotEntity::new, SpawnGroup.CREATURE)
                    .setDimensions(1.3964844f, 1.6f)
                    .maxTrackingRange(10)
                    .build(MEGA_PARROT_ID.toString()));
    public static final Item MEGA_PARROT_EGG = new SpawnEggItem(MEGA_PARROT_ENTITY_TYPE, 11141120, 5592575, new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item PARROT_MEAT = new Item(new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.CHICKEN));
    public static final Item COOKED_PARROT_MEAT = new Item(new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.COOKED_CHICKEN));
}