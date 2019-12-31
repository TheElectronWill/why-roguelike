package com.electronwill.why
package client

import ansi._
import gametype._
import console.{Symbol, ConsoleOutput, InputHandler}
import network.NetworkSystem
import protocol.client.PlayerMove

object Client {
  inline val VERSION = "1.0.0-beta1"
  inline val VIEW_PADDING = 4

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
    levelView =
      if level.width <= ConsoleOutput.width && level.height <= ConsoleOutput.height
        Box.positive(level.width, level.height)
      else
        Box.center(player.position, ConsoleOutput.width/2, ConsoleOutput.height/2)

    level.updatePlayerVision(player.position, Direction.LEFT)
    redrawView()
    setCursor(player.position + Direction.LEFT.vector)

  def moveView(d: Direction): Unit =
    levelView = d match
      case Direction.RIGHT =>
        levelView.shift(ConsoleOutput.width-2*VIEW_PADDING, 0)
      case Direction.LEFT =>
        levelView.shift(-ConsoleOutput.width+2*VIEW_PADDING, 0)
      case Direction.UP =>
        levelView.shift(0, -ConsoleOutput.height+2*VIEW_PADDING)
      case Direction.DOWN =>
        levelView.shift(0, ConsoleOutput.height-2*VIEW_PADDING)
    redrawView()

  def move(d: Direction): Boolean =
    val oldPosition = player.position
    val newPosition = oldPosition + d.vector
    if !level.terrain.isValid(newPosition) ||
        level.terrain(newPosition).isBlocking ||
        level.getEntity(newPosition).nonEmpty
      setCursor(newPosition) // make the cursor show where the player looks at
      false
    else
      level.moveEntity(player, newPosition)
      level.updatePlayerVision(newPosition, d)
      if  d == Direction.UP && newPosition.y - levelView.yMin < VIEW_PADDING ||
          d == Direction.DOWN && levelView.yMax - newPosition.y < VIEW_PADDING ||
          d == Direction.RIGHT && levelView.xMax - newPosition.x < VIEW_PADDING ||
          d == Direction.LEFT && newPosition.x - levelView.xMin < VIEW_PADDING
        moveView(d)
      else
        redrawView() // FIXME: I redraw the view to update the visibility of the terrain, but there is a better way
        // TODO optimize movement like before
        //val belowPlayer = level.getVisibleTile(oldPosition)
        //writeChar(oldPosition, belowPlayer.character)
        //writeChar(newPosition, player.tpe.character, player.customColor)
      setCursor(newPosition + d.vector) // make the cursor show where the player looks at
      network.send(PlayerMove(newPosition))
      true

  def redrawView(): Unit =
    ConsoleOutput.clear()
    Logger.info(s"Terminal size: ${ConsoleOutput.width} x ${ConsoleOutput.height}")
    Logger.info(s"Level size: (0, 0) to (${level.width}, ${level.height})")
    Logger.info(s"Player view: ${levelView.cornerMin} to ${levelView.cornerMax}")
    for
      y <- levelView.yMin to levelView.yMax
      x <- levelView.xMin to levelView.xMax
      if level.terrain.isValid(x, y)
    do
      val pos = Vec2i(x, y)
      val tile = level.getVisibleTile(pos)
      level.getEntity(pos) match
        case Some(entity) => writeChar(pos, Symbol.of(entity), entity.customColor)
        case None         => writeChar(pos, Symbol.of(tile, level.terrain.around(pos)))

  def writeChar(pos: Vec2i, character: Char, color: ColorSetting = ColorSetting(None,None)) =
    val posInView = pos - levelView.cornerMin
    ConsoleOutput.writeCharAt(posInView.y+1, posInView.x+1, character, color)

  def setCursor(pos: Vec2i) =
    val posInView = pos - levelView.cornerMin
    ConsoleOutput.moveCursor(posInView.y+1, posInView.x+1)

}
