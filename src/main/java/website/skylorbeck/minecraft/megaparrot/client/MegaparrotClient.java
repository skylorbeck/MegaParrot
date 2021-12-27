package website.skylorbeck.minecraft.megaparrot.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import website.skylorbeck.minecraft.megaparrot.Declarar;
import website.skylorbeck.minecraft.megaparrot.entity.MegaParrotRenderer;

@Environment(EnvType.CLIENT)
public class MegaparrotClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(Declarar.MEGA_PARROT_ENTITY_TYPE,
                MegaParrotRenderer::new);
    }
}
