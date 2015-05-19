package indystructural;

import org.dynalang.dynalink.CallSiteDescriptor;

public class SizedChainedCallSite extends org.dynalang.dynalink.ChainedCallSite {
    private int maxChainLength;

    /**
     * Creates a new chained call site.
     *
     * @param descriptor the descriptor for the call site.
     */
    public SizedChainedCallSite(CallSiteDescriptor descriptor, int maxChainLength) {
        super(descriptor);
        this.maxChainLength = maxChainLength;
    }

    @Override
    protected int getMaxChainLength() {
        return maxChainLength;
    }
}
