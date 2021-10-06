package pl.lijek.veinminer.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlockAddAction {
    public final boolean skipPass;
    public final boolean cancel;

    public static final BlockAddAction SKIP = new BlockAddAction(true, false);
    public static final BlockAddAction CANCEL = new BlockAddAction(false, true);
    public static final BlockAddAction PASS = new BlockAddAction(false, false);
}
