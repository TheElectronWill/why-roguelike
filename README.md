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

And then **open a new Terminal** and run the client:
```bash
java -jar client-assembly-0.1.0-SNAPSHOT.jar localhost 4242
```

You can replace `localhost` by the server's address if it is on a remote machine.

### Logs
The server displays its log in the console that started it.

The client cannot do this, since it's displaying the game. It writes its log to the file `why-client.log`.
You can run `tail +1f why-client.log` to get a real-time view of this file.

## Playing
You move with the arrows.
Nothing else for now :-)

## Warning: experimental Scala version
Because it's fun to learn new things (and because I'm a contributor),
this project doesn't use Scala 2.x but Dotty, an experimental research project
that will become Scala 3, the next major version of scala.

Please see [the Dotty website](https://dotty.epfl.ch) for more information.
