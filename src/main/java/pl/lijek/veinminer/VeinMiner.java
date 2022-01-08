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
import org.apache.logging.log4j.Logger;
import pl.lijek.veinminer.shapes.Shape;

public class VeinMiner {

    @Entrypoint.Logger("VeinMiner")
    public static final Logger LOGGER = Null.get();

    @Entrypoint.ModID
    public static ModID MODID = Null.get();

    public static Identifier breakWithVeinMinePacket = Identifier.of(MODID, "breakWithVeinMiner");
    public static boolean damageTool = true;
    public static int maxDistance = 5;
    public static int maxBlocksToMine = 525;

    @EventListener
    public void listenToMessages(MessageListenerRegistryEvent event){
        event.registry.register(breakWithVeinMinePacket, VeinMiner::processVeinMinePacket);
    }

    public static void processVeinMinePacket(PlayerBase playerBase, Message msg){
        int x = msg.ints[0];
        int y = msg.ints[1];
        int z = msg.ints[2];
        int side = msg.ints[3];
        int playerFacing = msg.ints[5];
        VeinMinerMode mode = VeinMinerMode.values()[msg.ints[4]];
        Shape shape = mode.getShape(playerBase.level, new Vec3i(x, y, z), side, playerFacing);
        BlockBreaker blockBreaker = new BlockBreaker(playerBase, shape.level, shape.blocks);
        blockBreaker.breakBlocks();
        blockBreaker.spawnDrops();
    }
}
