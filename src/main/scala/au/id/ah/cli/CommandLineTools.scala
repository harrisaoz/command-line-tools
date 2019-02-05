package au.id.ah.cli

import java.io.PrintStream

object CommandLineTools {
  implicit val errorStream: PrintStream = System.err

  def sanityCheckArgs(args: Array[String])
                     (checkExpr: Array[String] => Boolean)
                     (usageMessage: String, mainClass: Class[_])
                     (implicit stream: PrintStream): Unit =
    assertOrElse(checkExpr(args))(usageMessage, mainClass)(stream)

  def assertOrElse(isUsageOk: Boolean)(message: String, mainClass: Class[_])(stream: PrintStream): Unit = {
    if (!isUsageOk)
      usage(message, mainClass)(stream)
  }

  def usage(message: String, mainClass: Class[_])(implicit stream: PrintStream): Unit = {
    val usageMessage = message format mainClass.getCanonicalName
    stream.println(usageMessage)
    throw new RuntimeException(usageMessage)
  }
}
