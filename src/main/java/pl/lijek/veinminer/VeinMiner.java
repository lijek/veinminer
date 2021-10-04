package pl.lijek.veinminer;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.maths.Vec3i;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;

public class VeinMiner {
    @Entrypoint.ModID
    public static ModID MODID = Null.get();

    public static Identifier breakWithVeinMinePacket = Identifier.of(MODID, "breakWithVeinMiner");
    public static boolean damageTool = true;
    public static int maxDistanceNORMAL = 5;

    @EventListener
    public void listenToMessages(MessageListenerRegistryEvent event){
        event.registry.register(breakWithVeinMinePacket, (PlayerBase playerBase, Message msg) -> {
            //x, y, z, i1, blockID, blockMeta, mode.toInt(), matcherMode.toInt()
            if(playerBase == null) {
                System.out.println("PLAYER IS NULL!!!!");
                return;
            }
            int x = msg.ints[0];
            int y = msg.ints[1];
            int z = msg.ints[2];
            int side = msg.ints[3];
            int blockID = msg.ints[4];
            int blockMeta = msg.ints[5];
            BlockBreaker breaker = new BlockBreaker(new Vec3i(x, y, z), playerBase.level, playerBase, side, blockID, blockMeta);
            VeinMinerMode mode = VeinMinerMode.values()[msg.ints[6]];
            mode.mine(x, y, z, breaker);
            breaker.spawnDrop();
        });
    }
}
