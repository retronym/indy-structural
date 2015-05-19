package indystructural;

import org.dynalang.dynalink.DynamicLinker;
import org.dynalang.dynalink.DynamicLinkerFactory;
import org.dynalang.dynalink.MonomorphicCallSite;
import org.dynalang.dynalink.support.CallSiteDescriptorFactory;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Bootstrapper {
    private static final DynamicLinker dynamicLinker = new DynamicLinkerFactory().createLinker();
    private static final int MAX_CHAIN_SIZE = 32;

    private Bootstrapper() {
    }

    public static CallSite bootstrap(MethodHandles.Lookup caller, String name, MethodType type) {
        return dynamicLinker.link(new SizedChainedCallSite(CallSiteDescriptorFactory.create(caller, name, type), MAX_CHAIN_SIZE));
    }

    public static CallSite bootstrap1(MethodHandles.Lookup caller, String name, String typeDescriptor) {
        return bootstrap(caller, name, MethodType.fromMethodDescriptorString(typeDescriptor, caller.lookupClass().getClassLoader()));
    }
}
