# GotoSpawn

A simple Paper plugin that lets players teleport between world spawn and their bed using the `/goto` command. Teleportation is restricted — players must be within a configurable distance of the destination anchor point (bed for `/goto spawn`, world spawn for `/goto bed`).

## Usage

| Command | Description |
|---|---|
| `/goto spawn` | Teleport to world spawn (must be within N blocks of your bed) |
| `/goto bed` | Teleport to your bed (must be within N blocks of world spawn) |
| `/gotoreload` | Reload the plugin configuration (requires `goto.admin`) |

Players must have a bed or respawn point set for either subcommand to work.

## Requirements

- Paper 26.3+
- Java 25+

## Configuration

- File: `plugins/GotoSpawn/goto.conf` (YAML)
- Options:
	- `max-distance`: Maximum allowed distance in blocks (default: 16.0, min: 1, max: 48). Values outside this range are clamped.

Create the file on first run or edit and use `/gotoreload` to apply changes.

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
