# ReusableVaults

A Paper plugin for Minecraft **26.2** that makes Trial Chambers vaults **infinitely reusable**.
Use a Trial Key (or Ominous Trial Key) on a vault as many times as you like, with no "already opened" lockout.

## How it works
Vanilla vaults remember which players already looted them. This plugin clears that
memory for the player just before and just after a key is used, so the vault always accepts the key.
Keys are still consumed on each use.

## Features
- Works on normal and ominous vaults
- No config, no commands, no permissions
- Lightweight: a single event listener

## Installation
1. Download the latest jar from [Releases](../../releases) (or the Actions artifacts).
2. Drop it into your server's `plugins/` folder.
3. Restart the server.

## Building
Requires Java 25 and Gradle:

```bash
gradle build
```

The jar appears in `build/libs/`.

> If the build fails on the `paper-api` version, check the [Paper docs](https://docs.papermc.io)
> and update it in `build.gradle.kts`. Also adjust `api-version` in `plugin.yml` if needed.

## Releasing
Push a tag like `v1.0.0` and GitHub Actions will build the jar and attach it to a release:

```bash
git tag v1.0.0 && git push origin v1.0.0
```

## License
MIT
