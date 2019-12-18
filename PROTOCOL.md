# WHY 🌳 NETWORK PROTOCOL

## Introduction

I didn't want to take care of packet ordering and loss, and I didn't need a precisely controlled performance, therefore I've based my protocol on TCP.


## Data types

### Primitives

| type | size (bytes) | comments |
| ---- | ------------ | -------- |
| Bool   | 1 | 0 or 1 |
| Byte   | 1 | [-128, +127] |
| UByte  | 1 | [0, 255] |
| Short  | 2 | [-32768, +32767] |
| UShort | 2 | [0, 65535] |
| Int    | 4 | [-2147483648, 2147483647 ≈ 2x10^9] |
| Varint | 1+| variable-length integer, 1 byte for [0, 127] |


### Structures
#### UTF-8 String
- length: Varint
- bytes: Array[Byte] of UTF-8 characters

Strings are length-prefixed for easier decoding.

#### Vec2i - 2D Vector
- x: Short
- y: Short

Coordinates are positive (because the terminal window works like this) but vectors can be negative.

### Color
There are three possible formats: standard 4-bit color, extended 8-bit color, true 24-bit RGB color.

#### Standard Color

- value: Byte = `color.ordinal | isBright << 3`

Note that `value` only uses the lower 4 bits in standard format. `color.ordinal` returns the index (0-based) of the color in following Scala3 enum:
```scala
enum StandardColor {
  case Black, Red, Green, Yellow, Blue, Magenta, Cyan, White
}
```
That is, `Black` has ordinal 0 and `White` has ordinal 7.

#### Extended Color

- marker: Byte = `0xf0`
- value: UByte

The marker indicates to the receiver that the extended format is used instead of the standard one.

#### True Color

- marker: Byte = `0xff`
- red: UByte
- green: UByte
- blue: UByte

The marker indicates to the receiver that the true 24-bit format is used instead of the standard one.

#### How to decode
```scala
val firstByte = readByte()
(firstByte & 0xf0) match
  case 0 =>
    val isBright = firstByte & 8 // 0b1000
    val colorId  = firstByte & 7 // 0b0111
  case 0xf0 =>
    val extendedColorId = readUnsignedByte()
  case 0xff =>
    val red = readUnsignedByte()
    val green = readUnsignedByte()
    val blue = readUnsignedByte()
```


### ColorSetting
Sets the background and/or the foreground color (or nothing if colorFlag is 0).
- colorFlag: Byte = `changeForeground << 1 | changeBackground`
- foreground: EncodedColor (present only if `changeForeground == 1`)
- background: EncodedColor (present only if `changeBackground == 1`)

How to decode:
```scala
val changeForeground = (colorFlag & 2) == 1
val changeBackground = (colorFlag & 1) == 1
if changeForeground then
  val newForeground = decodeColor()
  // ...
if changeBackground then
  val newBackground = decodeColor()
  // ...
```


### Type IDs

- **Tile type**: Varint
- **Entity type**: Varint

IDs are varints to allow for more than 255 types at small cost.

## Packet format

- packet length (including id): UShort
- packet id: UByte
- content: 0+ (depends on the id, see below)

## Session opening
The client first sends a connection request to the server. After a positive response, the server sends some essential data. The game then goes on until one of the party disconnects.

1. C->S: connection request (id: 0)
2. S->C: connection response (id: 0)
3. S->C: ids registration (id: 1)
4. S->C: terrain data (id: 2)
5. *game continues*

## Session closing
Both the client and the server can initiate the end of the session, by sending packet 255 "Disconnect". A (possibly empty) message can be provided, e.g. to explain the reason of the disconnection.

There is no response to this packet, the other party simply takes note of the disconnection and might perform some internal modification/cleanup.

## Client to Server
### 0: Connection Request
- clientVersion: String (current version is `0.0.1-alpha`)
- username: String

### 1: Player move

- destination: Vec2i

### 255: Disconnect

- error: Boolean
- message: String

## Server to Client
### 0: Connection Response
- accepted: Boolean (true if disconnects because an error occurred)
- serverVersion: String (current version is `0.0.1-alpha`)
- message: String

If `accepted` is false then the server will close the connection without sending a 255 "Disconnect" packet.

### 1: IDs registration
- tileCount: Varint
- tiles: Array of tuples `(id: Varint, name: String, char: UShort)`
- entityCount: Varint
- entities: Array of tuples `(id: Varint, name: String, char: UShort)`

### 2: Terrain data
- levelId: Short
- width: Short, positive (so between 0 and 32767)
- height: Short, positive (so between 0 and 32767)
- tiles: Array[Varint] (tiles' ids)
- playerPosition: Vec2i

### 3: Entity spawn
- entityId: UShort
- entityType: Vec2i

### 4: Entity delete
- entityId: UShort

### 5: Entity move
- entityId: UShort
- newPosition: Vec2i

### 6: Entity appearance
- entityId: UShort
- character: UShort
- color: ColorSetting

### 7: Tile Update
- position: Vec2i
- tileType: Varint

### 8: Tile appearance
- position: Vec2i
- character: UShort
- color: ColorSetting

### 9: Area update

- from: Vec2i (upper left corner of the area)
- to: Vec2i (lower right corner of the area)
- tiles: Array[Varint] (tiles' ids, line by line)

The `tiles` array contains `(to - from).x + (to - from).y` elements.

### 254: Warning

- message: String

This message is sent when the server receives an erroneous packet that does not put the whole session in danger (e.g. a nonsensical request, such as moving the player across a wall). It indicates that the client has made a mistake, but keeps the session going to avoid disturbing the player too much.

The client is free to report the problem (which is likely a bug) to the user.

### 255: Disconnect

- error: Boolean (true if disconnects because an error occurred)
- message: String
