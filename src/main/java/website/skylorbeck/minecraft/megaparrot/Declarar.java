package website.skylorbeck.minecraft.megaparrot;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.item.EggItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.megaparrot.entity.MegaParrotEntity;

import java.awt.*;

public class Declarar {
    public static String MODID = "megaparrot";
    public static Identifier getMegaParrotId(String path){return new Identifier(MODID,path);}
    private static final Identifier MEGA_PARROT_ID = getMegaParrotId("megaparrot");
    public static final EntityType<MegaParrotEntity> MEGA_PARROT_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE,MEGA_PARROT_ID, EntityType.Builder.create(MegaParrotEntity::new, SpawnGroup.CREATURE).setDimensions(1.3964844f, 1.6f).maxTrackingRange(10).build(MEGA_PARROT_ID.toString()));
    public static final Item MEGA_PARROT_EGG = new SpawnEggItem(MEGA_PARROT_ENTITY_TYPE, 11141120,5592575,new FabricItemSettings().group(ItemGroup.MISC));
}
