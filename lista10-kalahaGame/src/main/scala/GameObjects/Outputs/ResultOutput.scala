package GameObjects.Outputs

class ResultOutput extends Output {
  override def printGame(): Unit = ()

  override def putMessage(message: String): Unit = println(message)
}
