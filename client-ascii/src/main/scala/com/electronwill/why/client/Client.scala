package com.electronwill.why
package client

import gametype._

object Client {
  inline val VERSION = "1.0.0-beta1"
  inline val VIEW_PADDING = 2

  var username = "player"
  var level: ClientDungeonLevel = _
  var levelView: Box = _
  var player: ClientEntity = _

  private var networkSystem: NetworkSystem = _
  private var inputHandler: InputHandler = _

  def network = networkSystem // public getter

  def connect(serverAddress: String, serverPort: Int) =
    networkSystem = NetworkSystem(serverAddress, serverPort)
    networkSystem.start()

  def makeInteractive() =
    inputHandler = InputHandler()
    Thread(inputHandler).start()

  def initView() =
    levelView = Box.positive(level.width, level.height)//Box.center(player.position, TUI.width, TUI.height)
    redrawView()

  def moveView(d: Direction): Unit =
    levelView = d match
      case Direction.RIGHT =>
        levelView.shift(TUI.width-2*VIEW_PADDING, 0)
      case Direction.LEFT =>
        levelView.shift(-TUI.width+2*VIEW_PADDING, 0)
      case Direction.UP =>
        levelView.shift(0, -TUI.width+2*VIEW_PADDING)
      case Direction.DOWN =>
        levelView.shift(0, TUI.width-2*VIEW_PADDING)
    redrawView()

  def move(d: Direction): Boolean =
    val oldPosition = player.position
    val newPosition = oldPosition + d.vector
    if !level.terrain.isValid(newPosition) ||
        level.terrain(newPosition).isBlocking ||
        level.getEntity(newPosition).nonEmpty
      false
    else
      level.moveEntity(player, newPosition)
      if  d == Direction.UP && newPosition.y - levelView.yMin < VIEW_PADDING ||
          d == Direction.DOWN && levelView.yMax - newPosition.y < VIEW_PADDING ||
          d == Direction.RIGHT && levelView.xMax - newPosition.x < VIEW_PADDING ||
          d == Direction.LEFT && newPosition.x - levelView.xMin < VIEW_PADDING
        moveView(d)
      else
        val belowPlayer = level.terrain(oldPosition)
        TUI.write(oldPosition.x, oldPosition.y, belowPlayer.character)
        TUI.write(newPosition.x, newPosition.y, player.tpe.character, player.customColor)
      true

  def redrawView(): Unit =
    TUI.clear()
    Logger.info(s"Terminal size: ${TUI.width} x ${TUI.height}")
    Logger.info(s"Level size: (0, 0) to (${level.width}, ${level.height})")
    Logger.info(s"Player view: ${levelView.cornerMin} to ${levelView.cornerMax}")
    for
      y <- levelView.yMin to levelView.yMax
      x <- levelView.xMin to levelView.xMax
      if level.terrain.isValid(x, y)
    do
      val tile = level.terrain(x, y)
      val entity = level.getEntity(x, y)
      if entity.nonEmpty
        TUI.write(x, y, Symbol.of(entity.get), entity.get.customColor)
      else
        TUI.write(x, y, Symbol.of(tile, level.terrain.around(x, y)))

}
