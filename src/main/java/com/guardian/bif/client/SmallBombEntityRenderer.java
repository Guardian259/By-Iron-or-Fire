package com.guardian.bif.client;


import com.guardian.bif.entity.BombEntity;
import com.guardian.bif.util.registries.server.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

//TODO: Implement the same entity rendering system as ProjectileEntityRenderer
public class SmallBombEntityRenderer extends EntityRenderer<BombEntity> {

    public static final ItemStack STACK = new ItemStack(ItemRegistry.SMALL_BOMB);

    public SmallBombEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public Identifier getTexture(BombEntity entity) {
        return null;
    }

    @Override
    public void render(BombEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                STACK,
                ModelTransformation.Mode.FIXED,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers
        );
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

}
