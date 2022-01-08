package pl.lijek.veinminer.mixin;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lijek.veinminer.VeinMinerClient;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "method_1554", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBlendFunc(II)V", shift = At.Shift.BEFORE))
    private void onDrawBoxBounds(PlayerBase arg, HitResult hit, int i, ItemInstance arg2, float f, CallbackInfo ci){
        VeinMinerClient.setAndRender(hit, f);
    }
}
