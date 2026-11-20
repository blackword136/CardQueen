# 卡牌皇后（Text）

一款 Android 平台的卡牌对战类单机/联机游戏，使用 Java 开发。

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
