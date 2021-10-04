package pl.lijek.veinminer.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.InGame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lijek.veinminer.VeinMinerClient;

@Mixin(InGame.class)
public class InGameMixin {

    @Shadow private Minecraft minecraft;

    @Inject(method = "renderHud", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V", shift = At.Shift.BEFORE))
    private void renderModeChange(float f, boolean flag, int i, int j, CallbackInfo ci){
        VeinMinerClient.renderModeChange(minecraft);
    }
}
