package website.skylorbeck.minecraft.megaparrot;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.megaparrot.entity.MegaParrotEntity;

public class Declarar {
    public static String MODID = "megaparrot";
    public static Identifier getMegaParrotId(String path){return new Identifier(MODID,path);}
    private static final Identifier MEGA_PARROT_ID = getMegaParrotId("megaparrot");
    public static final EntityType<MegaParrotEntity> MEGA_PARROT_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE,MEGA_PARROT_ID, EntityType.Builder.create(MegaParrotEntity::new, SpawnGroup.CREATURE).setDimensions(1.3964844f, 1.6f).maxTrackingRange(10).build(MEGA_PARROT_ID.toString()));
}
