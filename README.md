# WHY 🌳
**Welcome to the Holy Yggdrasil**

You wake up in what looks like a dungeon.
You aren't hurt\*, but you feel like this isn't where you should be...
WHY are you there?

\*: at least, not yet...


## Compiling
This is an SBT project, but you don't need to install sbt on your machine.
To compile, simply run
```bash
./sbt compile
```

You can also run sbt's shell with `./sbt` and then execute commands like
`compile`, `test`, etc.

## Running the game
First, you'll need to build the client and server jar files.
```bash
./sbt assembly
```
The `assembly` command produces:
- `server/target/scala-0.21/server-assembly-0.1.0-SNAPSHOT.jar`
- `client-ascii/target/scala-0.21/client-assembly-0.1.0-SNAPSHOT.jar`

You can now run the server:
```bash
java -jar server-assembly-0.1.0-SNAPSHOT.jar
```

And in another processus the client:
```bash
java -jar client-assembly-0.1.0-SNAPSHOT.jar localhost 4242
```

## Warning: experimental Scala version
Because it's fun to learn new things (and because I'm a contributor),
this project doesn't use Scala 2.x but Dotty, an experimental research project
that will become Scala 3, the next major version of scala.

Please see [the Dotty website](https://dotty.epfl.ch) for more information.

## TMP: ANSI ESCAPE SEQUENCES
https://linux.die.net/man/4/console_codes
https://unix.stackexchange.com/questions/306716/meaning-of-e0-in-ps1-in-bashrc
https://shiroyasha.svbtle.com/escape-sequences-a-quick-guide-1

