import java.io.{ByteArrayOutputStream, PrintStream}
import java.nio.charset.StandardCharsets

import au.id.ah.cli.CommandLineTools._
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CommandLineToolsSpec extends Specification {
  "sanityCheckArgs" should {
    "throw a RuntimeException if the checkExpr is false" in {
      sanityCheckArgs(
        Array("one"))(
        _.length == 2)(
        "This example requires two arguments but only one was provided", this.getClass)
        .should(throwA[RuntimeException])
    }
    "report the usage message if the check expression evaluates to false" in {
      val usageMessage = "This example emits a usage message"
      val buffer = new ByteArrayOutputStream()
      val usageErrorStream = new PrintStream(buffer)
      try {
        sanityCheckArgs(Array("one"))(_.length == 2)(usageMessage, this.getClass)(usageErrorStream)
      } catch {
        case x: RuntimeException => {}
      }
      buffer.toString(StandardCharsets.UTF_8).trim() shouldEqual usageMessage
    }
    "return gracefully without throwing any exception if the check expression evaluates to true" in {
      sanityCheckArgs(Array("one", "two"))(_.length == 2)("Usage: <one> <two>", this.getClass)
        .should(not(throwA[Throwable]))
    }
    "return gracefully without any printed warning if the check expression evaluates to true" in {
      val buffer = new ByteArrayOutputStream()
      val usageErrorStream = new PrintStream(buffer)
      sanityCheckArgs(Array("one", "two"))(_.length == 2)("Usage: <one> <two>", this.getClass)(
        usageErrorStream
      )
      buffer.size() shouldEqual 0
    }
  }
  "usage" should {
    "report the usage message to the specified PrintStream" in {
      val usageMessage = "This example emits a usage message"
      val buffer = new ByteArrayOutputStream()
      val usageErrorStream = new PrintStream(buffer)
      try {
        usage(usageMessage, this.getClass)(usageErrorStream)
      } catch {
        case x: RuntimeException => {}
      }
      buffer.toString(StandardCharsets.UTF_8).trim() shouldEqual usageMessage
    }
  }
}
