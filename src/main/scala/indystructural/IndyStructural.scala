package indystructural

import java.lang.invoke.{MethodHandle, CallSite, MethodType, MethodHandles}

import org.dynalang.dynalink._
import org.dynalang.dynalink.linker.GuardedInvocation
import org.dynalang.dynalink.support.{CallSiteDescriptorFactory, AbstractCallSiteDescriptor}

object Test {
  def main(args: Array[String]): Unit = {
    run()
  }
  

  def run(): Unit = {
    val lookup = MethodHandles.lookup()

    {
      val callSite = link(lookup, "(Ljava/lang/Object;)V", "close")
      List(new C1, new C2, new C3, new C3, new C5).foreach (c =>
        callSite.bindTo(c).invokeWithArguments()
      )
      callSite.bindTo(new C1).invokeWithArguments()
      callSite.bindTo(new C1).invokeWithArguments()
      callSite.bindTo(new C2).invokeWithArguments()
      callSite.bindTo(new C1).invokeWithArguments()
    }

    {
      val callSite = link(lookup, "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;", "s")
      println(callSite.bindTo(new V).invokeWithArguments("a"))
      println(callSite.bindTo(new W).invokeWithArguments("b"))
    }
  }

  def link(lookup: MethodHandles.Lookup, desc: String, methodName: String): MethodHandle = {
    val loader = lookup.lookupClass().getClassLoader
    val linker = new DynamicLinkerFactory().createLinker()
    val methodType = MethodType.fromMethodDescriptorString(desc, loader)
    val descriptor: CallSiteDescriptor = CallSiteDescriptorFactory.create(lookup, "dyn:callMethod:" + methodName, methodType)
    val cacheSize = 3
    val chained = new ChainedCallSite(descriptor) {

      override def getMaxChainLength: Int = cacheSize

      override def relink(guardedInvocation: GuardedInvocation, fallback: MethodHandle): Unit = {
        println(s"relinking $methodName:$desc")
        super.relink(guardedInvocation, fallback)
      }
    }
    linker.link(chained).dynamicInvoker()
  }
}

class C1 { def close() = println("C1") }
class C2 { def close() = println("C2") }
class C3 { def close() = println("C3") }
class C4 { def close() = println("C4") }
class C5 { def close() = println("C5") }

class V { def s(a: String): Object = "V_" + a }
class W { def s(a: String): Object = "W_" + a }
