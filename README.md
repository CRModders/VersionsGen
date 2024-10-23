# VersionsGen

A tiny little tool to generate JSON for Cosmic Reach versions. Prints JSON like this to stdout:
```json
{
    "id": "0.1.45",
    "type": "pre_alpha",
    "releaseTime": 1723408320,
    "url": "https://raw.githubusercontent.com/CRModders/CosmicArchive/main/versions/pre-alpha/Cosmic%20Reach-0.1.45.jar",
    "sha256": "a0aec07cb36ba05ab9a74b2812cc54a5c6ba556977634f8a5d1276ad0a3dd2d4",
    "size": 23986808
}
```

## Usage
```shell
gradlew shadowJar
java -jar build/libs/versionsgen-0.1.0-all.jar file --path <path to the cosmic reach jar>
```
See more in program usage