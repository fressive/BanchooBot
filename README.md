# BanchooBot
[![License](https://img.shields.io/badge/license-WTFPL-blue.svg)](https://raw.githubusercontent.com/1004121460/BanchooBot/master/LICENSE) [![Build status](https://ci.appveyor.com/api/projects/status/3drtvuj5mphatnuh?svg=true)](https://ci.appveyor.com/project/1004121460/banchoobot) [![Release](https://img.shields.io/github/release/1004121460/BanchooBot/all.svg)](https://github.com/1004121460/BanchooBot/releases)

N次重构后的BanchooBot

## Modules
| Module | Description |
| - | :-: |
| `/` | BanchooBot 主体 |
| `/http-server` | Http 服务器 |
| `/bot-frame` | Bot 框架 |

## Compiling
```bash
cd http-server
mvn clean compile install
cd ../bot-frame
mvn clean compile install
cd ..
mvn clean compile install
```

## License
- __BanchooBot__ WTFPL
