# Android签名配置说明

## 📝 签名参数详解

在Android应用构建中，这些参数的作用：

```kotlin
storeFile = file("../rndemo-key.keystore")  // 密钥库文件路径
storePassword = "password"                       // 密钥库访问密码
keyAlias = "my-key-alias"                       // 密钥别名（标识符）
keyPassword = "password"                        // 具体密钥的密码
```

### 🔑 参数含义：

| 参数 | 说明 | 作用 |
|------|------|------|
| **storeFile** | 密钥库文件路径 | 指向存储数字证书的.keystore文件 |
| **storePassword** | 密钥库密码 | 打开.keystore文件所需的密码 |
| **keyAlias** | 密钥别名 | 一个密钥库可以包含多个密钥，这是特定密钥的标识符 |
| **keyPassword** | 密钥密码 | 使用特定密钥时需要的密码 |

## 🔒 为什么需要签名？

1. **应用完整性验证** - 确保APK没有被篡改
2. **开发者身份验证** - 证明应用来自特定开发者
3. **应用商店要求** - Google Play等平台强制要求
4. **更新一致性** - 确保应用更新来自同一开发者

## 🆚 原始配置 vs 改进配置

### ❌ 原始配置（不安全）
```kotlin
storePassword = "password"             // 硬编码密码
keyAlias = "my-key-alias"             // 硬编码别名
keyPassword = "password"              // 硬编码密码
```

**问题：**
- 密码直接写在代码中
- 提交到Git会泄露密码
- 使用弱密码"password"
- 团队协作困难

### ✅ 改进配置（安全）
```kotlin
val keystorePropertiesFile = rootProject.file("keystore.properties")
if (keystorePropertiesFile.exists()) {
    val keystoreProperties = Properties()
    keystoreProperties.load(keystorePropertiesFile.inputStream())
    
    storeFile = file("../${keystoreProperties["MYAPP_UPLOAD_STORE_FILE"]}")
    storePassword = keystoreProperties["MYAPP_UPLOAD_STORE_PASSWORD"]?.toString()
    keyAlias = keystoreProperties["MYAPP_UPLOAD_KEY_ALIAS"]?.toString()
    keyPassword = keystoreProperties["MYAPP_UPLOAD_KEY_PASSWORD"]?.toString()
}
```

**优势：**
- 敏感信息存储在外部文件
- keystore.properties不会提交到Git
- 支持强密码
- 便于团队协作和CI/CD

## 📁 文件结构

```
android/
├── keystore.properties          # 签名配置（不提交到Git）
├── my-release-key.keystore     # 密钥文件（不提交到Git）
├── .gitignore                  # 忽略敏感文件
└── app/
    └── build.gradle.kts        # 构建配置
```

## 🛠️ 使用流程

### 1. 生成密钥和配置
```bash
./generate-keystore.sh
```
这会创建：
- `my-release-key.keystore` - 密钥文件
- `keystore.properties` - 配置文件

### 2. 构建Release版本
```bash
./gradlew assembleRelease    # 构建APK
./gradlew bundleRelease     # 构建AAB
```

### 3. 验证签名
```bash
# 查看APK签名信息
keytool -printcert -jarfile app-release.apk

# 验证AAB签名
java -jar bundletool-all.jar verify --bundle=app-release.aab
```

## ⚠️ 安全最佳实践

1. **密码管理**
   - 使用强密码（至少8位，包含数字、字母、特殊字符）
   - storePassword和keyPassword可以相同
   - 妥善保管密码，建议使用密码管理器

2. **文件管理**
   - 将keystore.properties添加到.gitignore
   - 备份密钥文件到安全位置
   - 定期验证密钥文件完整性

3. **团队协作**
   - 每个开发者单独创建keystore.properties
   - 通过安全渠道分享密钥信息
   - 生产环境使用独立的签名密钥

4. **CI/CD配置**
   - 使用环境变量存储敏感信息
   - 加密存储密钥文件
   - 限制密钥访问权限

## 🔍 常见问题

**Q: 忘记密码怎么办？**
A: 密钥密码无法找回，需要重新生成密钥。已发布应用无法更新，需要新的包名重新发布。

**Q: 密钥文件损坏怎么办？**
A: 需要重新生成密钥，已发布应用无法更新。

**Q: 可以修改密钥信息吗？**
A: 密钥别名、密码等可以在有效期内修改，但会影响已签名应用。

**Q: 一个密钥可以签名多个应用吗？**
A: 可以，但建议每个应用使用独立密钥以提高安全性。
