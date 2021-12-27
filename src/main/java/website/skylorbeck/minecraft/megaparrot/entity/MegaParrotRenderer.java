package website.skylorbeck.minecraft.megaparrot.entity;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MegaParrotRenderer extends GeoEntityRenderer<MegaParrotEntity>
{
    public MegaParrotRenderer(EntityRendererFactory.Context renderManager)
    {
        super(renderManager, new MegaParrotModel());
        this.shadowRadius = 0.7F;
    }
}