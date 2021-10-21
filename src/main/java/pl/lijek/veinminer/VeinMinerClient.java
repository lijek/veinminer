package pl.lijek.veinminer;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Vec3i;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.lwjgl.input.Keyboard;
import pl.lijek.veinminer.shapes.Shape;
import pl.lijek.veinminer.util.Util;

import java.util.List;

import static pl.lijek.veinminer.VeinMiner.breakWithVeinMinePacket;

public class VeinMinerClient {

    public static KeyBinding veinMineKey;
    public static KeyBinding sneakKey;
    public static VeinMinerMode veinMinerMode = VeinMinerMode.NORMAL;
    public static Level level;
    public static PlayerBase localPlayer;
    public static Shape shape;
    public static boolean veinMiningKeyPressed = false;
    public static boolean sneakKeyPressed = false;
    public static int timer;
    public static final int fadeTime = 150;

    public static void postMcInit() {
        Minecraft mc = ((Minecraft) FabricLoader.getInstance().getGameInstance());
        sneakKey = mc.options.sneakKey;
    }

    @EventListener
    public void registerKeyBindings(KeyBindingRegisterEvent event) {
        List<KeyBinding> list = event.keyBindings;
        list.add(veinMineKey = new KeyBinding(Identifier.of(VeinMiner.MODID, "veinMine").toString(), Keyboard.KEY_GRAVE));
    }

    @EventListener
    public void keyPressed(KeyStateChangedEvent event) {
        veinMiningKeyPressed = Keyboard.isKeyDown(veinMineKey.key);
        sneakKeyPressed = Keyboard.isKeyDown(sneakKey.key);
    }

    public static void changeMode(int i){
        if(veinMiningKeyPressed && sneakKeyPressed) {
            if (i > 0) {
                veinMinerMode = veinMinerMode.getPrevious();
            } else if (i < 0) {
                veinMinerMode = veinMinerMode.getNext();
            }

            timer = 200;
        }
    }

    public static void renderModeChange(Minecraft mc){
        if(mc.options.debugHud)
            return;
        if(timer <= 0 && !veinMiningKeyPressed)
            return;
        if(timer > 0){
            timer--;
        }

        int alpha = 255;
        if(timer <= fadeTime && timer > 0 && !veinMiningKeyPressed){
            double alphaMultiplier = timer / (fadeTime * 1.0D);
            alphaMultiplier = Math.abs(alphaMultiplier);
            alpha = (int) (255.0D * alphaMultiplier);
        }

        TextRenderer textRenderer = mc.textRenderer;
        textRenderer.drawTextWithShadow(Util.translate(veinMinerMode.getPrevious().getTranslationKey()), 2, 2, 0xdddddd + (alpha << 24));
        textRenderer.drawTextWithShadow(Util.translate(veinMinerMode.getTranslationKey()), 2, 11, 0xffffff + (alpha << 24));
        textRenderer.drawTextWithShadow(Util.translate(veinMinerMode.getNext().getTranslationKey()), 2, 20, 0xdddddd + (alpha << 24));
    }

    public static void switchLevel(Minecraft mc){
        level = mc.level;
        localPlayer = mc.player;
        if(shape != null)
            shape.level = level;
    }

    public static void changeShape(int x, int y, int z){
        if(shape == null)
            shape = veinMinerMode.getShape(level, new Vec3i(x, y, z));
        shape.reset(x, y, z);
    }

    public static void setAndRender(int x, int y, int z, float delta){
        if(!veinMiningKeyPressed)
            return;

        changeShape(x, y, z);

        double fixX = localPlayer.prevRenderX + (localPlayer.x - localPlayer.prevRenderX) * (double)delta;
        double fixY = localPlayer.prevRenderY + (localPlayer.y - localPlayer.prevRenderY) * (double)delta;
        double fixZ = localPlayer.prevRenderZ + (localPlayer.z - localPlayer.prevRenderZ) * (double)delta;

        shape.render(fixX, fixY, fixZ);
    }

    public static void sendVeinMinePacket(int x, int y, int z, int side){
        Message message = new Message(breakWithVeinMinePacket);
        message.ints = new int[]{x, y, z, side, veinMinerMode.toInt()};
        PacketHelper.send(message);
    }

}
