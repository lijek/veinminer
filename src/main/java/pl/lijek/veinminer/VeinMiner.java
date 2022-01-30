package pl.lijek.veinminer;

import blue.endless.jankson.Comment;
import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.glasslauncher.mods.api.gcapi.api.MultiplayerSynced;
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

    @GConfig(value = "config", visibleName = "Vein miner config")
    public static final Config config = new Config();

    public static Identifier breakWithVeinMinePacket = Identifier.of(MODID, "breakWithVeinMiner");

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

    public static class Config {
        @MultiplayerSynced
        @ConfigName("Damage tool")
        @Comment("Whether damage the tool from vein mined blocks.")
        public Boolean damageTool = true;
        @MultiplayerSynced
        @ConfigName("Maximum distance")
        @Comment("Distance from first mined block to the furthest block.")
        public Integer maxDistance = 5;
        @MultiplayerSynced
        @ConfigName("Maximum count of blocks to mine")
        @Comment("If you set more than 525 (default) it could be laggy.")
        public Integer maxBlocksToMine = 525;
        /*@ConfigName("Last mining mode ID")
        @Comment("This saves last selected mining mode, on server it won't do anything")
        public Integer lastMode = VeinMinerMode.NORMAL.toInt();
        todo make this last mode thing work
        */
    }
}
