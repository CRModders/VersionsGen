# VersionsGen

A tiny little tool to generate JSON for Cosmic Reach versions. Prints JSON like this to stdout:
```json
{
    "id": "0.1.23",
    "type": "pre_alpha",
    "releaseTime": 1713139200,
    "url": "https://raw.githubusercontent.com/CRModders/CosmicArchive/main/Cosmic%20Reach-0.1.23.jar",
    "hash": "fcc4df21e869c8014c4f12ab12b1172cc2957f9d819cc45d1517ccf7c7602bae",
    "size": 23832099
}
```

## Usage
```shell
gradlew shadowJar
java -jar build/libs/versionsgen-0.0.2-all.jar file --path <path to the cosmic reach jar>
```
See more in program usage