package au.id.ah.cli

object Main {
  def main[T]: (T => Either[Int, Int]) => T => Unit =
    main0 => args => main0(args) match {
    case Left(nFailures) =>
      System.exit(nFailures)
    case Right(_) =>
  }
}
