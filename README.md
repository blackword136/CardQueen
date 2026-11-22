# 卡牌皇后（QueenX）

一款 Android 平台的卡牌对战类双人联机游戏，使用 Java 开发。

游戏音乐和图片由网络获取，如果侵权会删除
The game music and images are sourced from the internet, and will be deleted if they infringe on any rights.

## 功能

- 开始界面、模式选择、角色技能选择
- 卡牌对战核心逻辑（卡牌、装备、技能、伤害结算）
- 背景音乐与音效播放（`BgmService` + `res/raw` 音效资源）
- 局域网 TCP 联机对战（`TcpService` / `Tcpactivity`）

## 技术栈

- 语言：Java（JDK 11）
- 构建：Gradle Kotlin DSL + Android Gradle Plugin
- minSdk 24 / targetSdk 37
- 依赖：AndroidX（appcompat、constraintlayout、activity）、Material Components、Gson

## 目录结构

```
app/src/main/java/com/example/text/   核心代码（Activity、游戏逻辑、Service）
app/src/main/res/layout/              界面布局
app/src/main/res/raw/                 音效与语音资源
app/src/main/res/drawable/            角色与卡面图片
gradle/libs.versions.toml             依赖版本管理
```

## 编译与运行

用 Android Studio 打开本目录，等待 Gradle 同步完成后直接运行；或使用命令行：

```bash
# Windows
gradlew.bat assembleDebug

# macOS / Linux
./gradlew assembleDebug
```

生成的安装包位于 `app/build/outputs/apk/debug/app-debug.apk`。

> 首次编译前请确认 `local.properties` 中的 `sdk.dir` 指向本机 Android SDK 路径（该文件不会提交到仓库，需本地自行生成）。

## 版权与许可

- **源代码**：采用 [MIT License](LICENSE)，可自由使用、修改、分发，只需保留版权声明
- **游戏素材**：`app/src/main/res/raw` 的音效语音、`app/src/main/res/drawable` 的角色立绘与卡面均来自网络，版权归各自原作者所有，**不受 MIT License 覆盖**
- 本项目仅用于学习与交流，不作商业用途。若相关素材侵犯了你的权益，请提交 Issue，会尽快删除
