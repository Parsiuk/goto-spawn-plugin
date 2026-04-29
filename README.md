# GotoSpawn

A simple Paper plugin that lets players teleport between world spawn and their bed using the `/goto` command. Teleportation is restricted — players must be within 16 blocks of the destination anchor point (bed for `/goto spawn`, world spawn for `/goto bed`).

## Usage

| Command | Description |
|---|---|
| `/goto spawn` | Teleport to world spawn (must be within 16 blocks of your bed) |
| `/goto bed` | Teleport to your bed (must be within 16 blocks of world spawn) |

Players must have a bed or respawn point set for either subcommand to work.

## Requirements

- Paper 1.21+
- Java 21+

## Building

```sh
./gradlew clean build
```

The compiled JAR will be in `build/libs/`. Copy it to your server's `plugins/` directory and restart.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes and test on a Paper server
4. Submit a pull request
