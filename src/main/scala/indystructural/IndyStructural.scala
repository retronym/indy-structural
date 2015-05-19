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
      val invoker = Bootstrapper.bootstrap1(lookup, "dyn:callMethod:close", "(Ljava/lang/Object;)Ljava/lang/Object;").dynamicInvoker()
      List(new C1, new C2, new C3, new C3, new C5).foreach (c =>
        invoker.bindTo(c).invokeWithArguments()
      )
      invoker.bindTo(new C1).invokeWithArguments()
      invoker.bindTo(new C1).invokeWithArguments()
      invoker.bindTo(new C2).invokeWithArguments()
      invoker.bindTo(new C1).invokeWithArguments()
    }

    {
      val invoker = Bootstrapper.bootstrap1(lookup, "dyn:callMethod:s", "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;").dynamicInvoker()
      println(invoker.bindTo(new V).invokeWithArguments("v"))
      println(invoker.bindTo(new W).invokeWithArguments("w"))
      try invoker.bindTo(new X).invokeWithArguments("x") catch {
        case Ex =>
      }
      try invoker.bindTo(new Y).invokeWithArguments("y") catch {
        case Err =>
      }
    }
  }
}

class C1 { def close() = println("C1") }
class C2 { def close() = println("C2") }
class C3 { def close() = println("C3") }
class C4 { def close() = println("C4") }
class C5 { def close() = println("C5") }

class V { def s(a: String): Object = "V_" + a }
class W { def s(a: String): Object = "W_" + a }
class X { def s(a: String): Object = throw Ex }
class Y { def s(a: String): Object = throw Err }

object Err extends Error("!!")
object Ex extends Exception("!!")
