# Android构建和安装指南

本项目包含了完整的Android构建脚本和工具，帮助你轻松打包和安装应用。

## 🚀 快速开始

### 1. 给脚本添加执行权限
```bash
chmod +x *.sh
```

### 2. 构建Debug版本（推荐先试这个）
```bash
./build-scripts.sh
```

### 3. 生成Release签名密钥（仅第一次需要）
```bash
./generate-keystore.sh
```

### 4. 安装AAB文件到手机
```bash
./install-aab.sh app/build/outputs/bundle/defaultDebug/app-default-debug.aab
```

## 📦 构建命令详解

### Debug版本构建
```bash
# 构建Debug APK
./gradlew assembleDebug

# 构建Debug AAB
./gradlew bundleDebug

# 同时构建两个flavor的Debug版本
./gradlew assembleDefaultDebug assembleGpayDebug
```

### Release版本构建
```bash
# 首先生成签名密钥（仅需一次）
./generate-keystore.sh

# 构建Release APK
./gradlew assembleRelease

# 构建Release AAB
./gradlew bundleRelease
```

### 清理构建文件
```bash
./gradlew clean
```

## 📱 应用变体说明

项目配置了两个应用变体：

| 变体 | 包名 | 应用名 | 图标 |
|------|------|--------|------|
| **default** | `com.example.rndemo` | RnDemo | `@mipmap/ic_launcher` |
| **gpay** | `com.example.rndemo.gpay` | RnDemo GPay | `@mipmap/ic_launcher_gpay` |

每个变体都有Debug和Release两个构建类型。

## 🔧 Debug vs Release

| 特性 | Debug | Release |
|------|-------|---------|
| **网络安全** | 允许HTTP | 仅HTTPS |
| **签名** | Debug签名 | Release签名 |
| **调试** | 可调试 | 生产优化 |
| **安装** | 直接安装 | 需要签名 |

## 📁 输出文件位置

### APK文件
```
app/build/outputs/apk/
├── default/
│   ├── debug/app-default-debug.apk
│   └── release/app-default-release.apk
└── gpay/
    ├── debug/app-gpay-debug.apk
    └── release/app-gpay-release.apk
```

### AAB文件
```
app/build/outputs/bundle/
├── defaultDebug/app-default-debug.aab
├── defaultRelease/app-default-release.aab
├── gpayDebug/app-gpay-debug.aab
└── gpayRelease/app-gpay-release.aab
```

## 🔑 签名配置

Release版本需要签名配置，当前配置：
- **密钥文件**: `my-release-key.keystore`
- **密钥别名**: `my-key-alias`
- **密码**: 在生成时设置

⚠️ **重要提醒**:
- 不要将密钥文件提交到Git
- 密钥文件和密码要妥善保管
- 丢失密钥将无法更新已发布的应用

## 📲 AAB安装方法

### 方法1: 使用脚本（推荐）
```bash
./install-aab.sh path/to/your/app.aab
```

### 方法2: 使用bundletool
```bash
# 下载bundletool
curl -L "https://github.com/google/bundletool/releases/latest/download/bundletool-all.jar" -o bundletool-all.jar

# 生成APKs
java -jar bundletool-all.jar build-apks --bundle=app.aab --output=app.apks

# 安装到设备
java -jar bundletool-all.jar install-apks --apks=app.apks
```

### 方法3: 生成通用APK
```bash
java -jar bundletool-all.jar build-apks --bundle=app.aab --output=universal.apks --mode=universal
unzip universal.apks
adb install universal.apk
```

## 🛠️ 常见问题

### Q: 构建失败怎么办？
A: 
1. 检查Java环境: `java -version`
2. 清理构建: `./gradlew clean`
3. 检查网络连接（下载依赖）

### Q: 无法安装到手机？
A:
1. 检查USB调试是否开启
2. 检查设备连接: `adb devices`
3. 卸载旧版本再安装

### Q: Release签名错误？
A:
1. 确认密钥文件路径正确
2. 检查密码是否正确
3. 重新生成密钥文件

### Q: AAB无法安装？
A: AAB不能直接安装，需要:
1. 使用bundletool转换
2. 或使用脚本自动处理
3. 或生成通用APK

## 📝 版本信息

- **应用版本**: 1.0
- **构建工具**: Gradle 8.x
- **最小Android版本**: API 24 (Android 7.0)
- **目标Android版本**: API 35
- **编译SDK版本**: 35

## 🎯 最佳实践

1. **开发阶段**: 使用Debug APK，快速迭代测试
2. **内测阶段**: 使用Release APK，接近生产环境
3. **应用商店**: 使用Release AAB，文件更小，支持动态交付
4. **版本管理**: 每次发布前更新versionCode和versionName

---

如果遇到问题，请检查：
1. 网络连接是否正常
2. Java环境是否配置正确
3. Android SDK是否安装
4. 设备是否正确连接并开启USB调试
