# VersionsGen

A tiny little tool to generate JSON for Cosmic Reach versions. Prints JSON like this to stdout:
```json
{
    "id": "0.3.14",
    "type": "pre_alpha",
    "releaseTime": 1735430400,
    "client": {
        "url": "https://raw.githubusercontent.com/CRModders/CosmicArchive/main/versions/pre-alpha/0.3.14/client/Cosmic-Reach-0.3.14.jar",
        "sha256": "70915270401bf4ada7e3f6b909eebef397a0d896ed192029ec1fecd98eed54d6",
        "size": 53409134
    },
    "server": {
        "url": "https://raw.githubusercontent.com/CRModders/CosmicArchive/main/versions/pre-alpha/0.3.14/server/Cosmic-Reach-Server-0.3.14.jar",
        "sha256": "0a7feeaee8277b04af6042a43dade220f8d0706542eaf76151e04bbe70bb3777",
        "size": 13068507
    }
}
```

## Usage
```shell
gradlew shadowJar
java -jar build/libs/versionsgen-0.1.1-all.jar file --type pre_alpha --version 0.3.14 --client <CLIENT_JAR_PATH> --server <SERVER_JAR_PATH>
```
See more in program usage
