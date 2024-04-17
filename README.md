# VersionsGen

A tiny little tool to generate JSON for Cosmic Reach versions. Prints JSON like this to stdout:
```json
{
    "id": "0.1.23",
    "type": "pre_alpha",
    "releaseTime": 1713139200,
    "url": "https://raw.githubusercontent.com/CRModders/CosmicArchive/main/Cosmic%20Reach-0.1.23.jar",
    "hash": "44cae6c35f484f018636db8bf77b9c51",
    "size": 23832099
}
```

## Usage
```shell
gradle shadowJar
java -jar build/libs/versionsgen-0.0.2-all.jar file --path <path to the cosmic reach jar>
```
See more in program usage