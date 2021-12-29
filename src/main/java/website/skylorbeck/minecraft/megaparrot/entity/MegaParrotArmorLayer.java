package website.skylorbeck.minecraft.megaparrot.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.DyeableHorseArmorItem;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import website.skylorbeck.minecraft.megaparrot.Declarar;

import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("rawtypes")
public class MegaParrotArmorLayer extends GeoLayerRenderer {
    private static final Identifier MODEL =  Declarar.getMegaParrotId("geo/mega_parrot.geo.json");

    @SuppressWarnings("unchecked")
    public MegaParrotArmorLayer(IGeoRenderer<?> entityRendererIn) {
        super(entityRendererIn);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, Entity megaParrotEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        MegaParrotEntity megaParrot = (MegaParrotEntity) megaParrotEntity;
        ItemStack armorItemStack = megaParrot.getArmorType();
        if (!megaParrot.hasArmorInSlot() || !megaParrot.isHorseArmor(armorItemStack)) {
            return;
        }
        HorseArmorItem horseArmorItem = (HorseArmorItem)armorItemStack.getItem();
        RenderLayer cameo= RenderLayer.getArmorCutoutNoCull(Declarar.getMegaParrotId("textures/entity/armor/"+ horseArmorItem.getEntityTexture().getPath().split("horse_armor_")[1]));
        float r = 1f;
        float g = 1f;
        float b = 1f;
        if (horseArmorItem instanceof DyeableHorseArmorItem) {
            int m = ((DyeableHorseArmorItem)horseArmorItem).getColor(armorItemStack);
            r = (float)(m >> 16 & 0xFF) / 255.0f;
            g = (float)(m >> 8 & 0xFF) / 255.0f;
            b = (float)(m & 0xFF) / 255.0f;
        }
        matrixStackIn.push();
        //Move or scale the model as you see fit
        matrixStackIn.scale(1.0f, 1.0f, 1.0f);
        matrixStackIn.translate(0.0d, 0.0d, 0.0d);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), megaParrotEntity, partialTicks, cameo, matrixStackIn, bufferIn,
                bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.DEFAULT_UV, r, g, b, 1f);
        matrixStackIn.pop();
    }
}