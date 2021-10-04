package pl.lijek.veinminer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlockBreakAction {
    public final boolean skipPass;
    public final boolean cancel;

    public static final BlockBreakAction SKIP = new BlockBreakAction(true, false);
    public static final BlockBreakAction CANCEL = new BlockBreakAction(false, true);
    public static final BlockBreakAction PASS = new BlockBreakAction(false, false);
}
