package au.id.ah.cli

import org.junit.contrib.java.lang.system.ExpectedSystemExit
import org.junit.{Rule, Test}
import org.junit.Assert.{assertEquals, assertTrue}

class MainSpec {
  val _exit: ExpectedSystemExit = ExpectedSystemExit.none()

  @Rule
  def exit: ExpectedSystemExit = _exit

  @Test
  def doesNotExitOnRightX(): Unit = {
    fakeMainRunner(_ => Right(1))(Array.empty[String])
  }

  @Test
  def exitsWithCodeNForLeftN(): Unit = {
    val n = 7
    exit.expectSystemExitWithStatus(n)
    fakeMainRunner(_ => Left(n))(Array.empty[String])
  }

  @Test
  def passesArgsToCallee(): Unit = {
    val inArgs = Array("a", "b", "3")
    var argCountPassed = 0
    def callee: Array[String] => Either[Int, Int] =
      args => {
        argCountPassed = args.length
        Right(args.length)
      }
    Main.main(callee)(inArgs)
    assertEquals(
      inArgs.length,
      argCountPassed
    )
  }

  @Test
  def acceptsParameterFreeCallee(): Unit = {
    var called = false

    def run: Any => Either[Int, Int] = _ => {
      called = true
      Right(1)
    }

    def main: Array[String] => Unit = _ => {
      Main.main(run)()
    }

    main(Array("testing", "1", "2", "3"))
    assertTrue(called)
  }

  def fakeMainRunner(executee: Array[String] => Either[Int, Int]): Array[String] => Unit = Main
    .main(executee)
}
