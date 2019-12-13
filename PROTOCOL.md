# WHY 🌳 NETWORK PROTOCOL

## Data types

### Primitives
| type | size (bytes) | comments |
|------|--------------|----------|
| Bool   | 1 | 0 or 1
| Byte   | 1 | [-128, +127]
| UByte  | 1 | [0, 255]
| Short  | 2 | [-32768, +32767]
| UShort | 2 | [0, 65535]
| Int    | 4 | [0, 2147483647 ~= 2x10^9]
| Varint | 1+| variable-length integer

### Structures
**String** : UTF-8 string
- length: Varint
- bytes: Array[Byte] of UTF-8 characters

**Vec2i** : 2D integer coordinates
- x: UShort
- y: UShort

### Color
There are three possible formats: standard 4-bit color, extended 8-bit color, true 24-bit RGB color.

**Standard**
- value: Byte = `color.ordinal | isBright << 3`

Note that `value` only uses the lower 4 bits in standard format.

**Extended**
- marker: Byte = `0xf0`
- value: UByte

The marker indicates to the receiver that the extended format is used instead of the standard one.

### ColorSetting
Sets the background and/or the foreground color (or nothing if colorFlag is 0).
- colorFlag: Byte = `changeForeground << 1 | changeBackground`
- foreground: EncodedColor (present only if `changeForeground == 1`)
- background: EncodedColor (present only if `changeBackground == 1`)

How to decode:
```scala
val changeForeground = (colorFlag & 2) > 0
val changeBackground = (colorFlag & 1) == 1
if changeForeground then
  val newForeground = decodeColor()
  // ...
if changeBackground then
  val newBackground = decodeColor()
  // ...
```
**True**
- marker: Byte = `0xff`
- red: UByte
- green: UByte
- blue: UByte

The marker indicates to the receiver that the true 24-bit format is used instead of the standard one.

### Type IDs

- **Tile type**: Varint
- **Entity type**: Varint

IDs are varint to allow for more than 255 types at a small cost.

## Packet format

- packet length (including id): UShort
- packet id: UByte
- content: * (depends on the id, see below)

## Session

1. C->S: connection request (id: 0)
2. S->C: connection response (id: 0)
*3. S->C: [OPTIONAL] ids registration (id: 1)*
3. S->C: terrain data (id: 2)
4. game commands

## Client to Server
### 0: Connection Request
- clientVersion: String
- username: String

## Server to Client
### 0: Connection Response
- accepted: Boolean
- serverVersion: String
- message: String

### 1: IDs registration
- tileCount: Varint
- tiles: Array of tuples `(tileId: Varint, character: UShort)`
- entityCount: Varint
- entities: Array of tuples `(entityId: Varint, character: UShort)`

## 2: Terrain data
- width: UShort
- height: UShort
- tileIds: Array[Varint]

## 3: Entity spawn
- entityId: UShort
- entityType: Vec2i

## 4: Entity delete
- entityId: UShort

## 5: Entity move
- entityId: UShort
- newPosition: Vec2i

## 6: Set entity appearance
- entityId: UShort
- character: UShort
- color: ColorSetting

## 7: Update tile
- position: Vec2i
- tileType: Varint

## 8: Set tile appearance
- position: Vec2i
- character: UShort
- color: ColorSetting
