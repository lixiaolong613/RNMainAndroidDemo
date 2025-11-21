#!/bin/bash

# Android项目构建脚本
# 使用方法: chmod +x build-scripts.sh && ./build-scripts.sh

echo "=== Android项目构建脚本 ==="
echo "当前目录: $(pwd)"
echo "日期: $(date)"
echo ""

# 检查Java环境
echo "检查Java环境..."
java -version
echo ""

# 检查Gradle Wrapper
echo "检查Gradle Wrapper..."
ls -la gradlew
echo ""

# 1. 构建Debug版本APK
echo "=== 构建Debug版本APK ==="
./gradlew assembleDebug
echo "Debug APK构建完成！"
echo ""

# 2. 构建Release版本APK
echo "=== 构建Release版本APK ==="
echo "注意: 需要先生成签名密钥文件"
 ./gradlew assembleRelease

# 3. 构建Debug版本AAB
echo "=== 构建Debug版本AAB ==="
./gradlew bundleDebug
echo "Debug AAB构建完成！"
echo ""

# 4. 构建Release版本AAB
echo "=== 构建Release版本AAB ==="
echo "注意: 需要先生成签名密钥文件"
 ./gradlew bundleRelease

echo "=== 构建结果位置 ==="
echo "APK文件位置:"
find app/build/outputs/apk -name "*.apk" -type f 2>/dev/null || echo "暂无APK文件"
echo ""
echo "AAB文件位置:"
find app/build/outputs/bundle -name "*.aab" -type f 2>/dev/null || echo "暂无AAB文件"
echo ""

echo "=== 可用的Gradle任务 ==="
echo "Debug构建:"
echo "  ./gradlew assembleDebug        # 构建Debug APK"
echo "  ./gradlew bundleDebug          # 构建Debug AAB"
echo ""
echo "Release构建 (需要签名配置):"
echo "  ./gradlew assembleRelease      # 构建Release APK"
echo "  ./gradlew bundleRelease        # 构建Release AAB"
echo ""
echo "所有变体:"
echo "  ./gradlew assemble             # 构建所有APK"
echo "  ./gradlew bundle               # 构建所有AAB"
echo ""
echo "清理:"
echo "  ./gradlew clean                # 清理构建文件"
echo ""
