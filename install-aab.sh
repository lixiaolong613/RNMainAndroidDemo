#!/bin/bash

# AAB文件安装脚本
# 使用方法: chmod +x install-aab.sh && ./install-aab.sh [aab文件路径]

echo "=== AAB文件安装工具 ==="
echo ""

# 检查参数
if [ $# -eq 0 ]; then
    echo "使用方法: $0 <AAB文件路径>"
    echo "示例: $0 app/build/outputs/bundle/defaultDebug/app-default-debug.aab"
    echo ""
    echo "可用的AAB文件:"
    find app/build/outputs/bundle -name "*.aab" -type f 2>/dev/null || echo "暂无AAB文件"
    exit 1
fi

AAB_FILE="$1"

# 检查AAB文件是否存在
if [ ! -f "$AAB_FILE" ]; then
    echo "❌ 错误: 找不到AAB文件 $AAB_FILE"
    exit 1
fi

echo "AAB文件: $AAB_FILE"
echo ""

# 检查bundletool
BUNDLETOOL="bundletool-all.jar"
if [ ! -f "$BUNDLETOOL" ]; then
    echo "下载bundletool..."
    curl -L "https://github.com/google/bundletool/releases/latest/download/bundletool-all.jar" -o "$BUNDLETOOL"

    if [ $? -ne 0 ]; then
        echo "❌ bundletool下载失败！"
        echo "请手动下载: https://github.com/google/bundletool/releases/latest"
        exit 1
    fi
    echo "✅ bundletool下载完成"
fi

# 检查设备连接
echo "检查设备连接..."
adb devices
echo ""

# 生成APKs
echo "从AAB生成APKs..."
APKS_FILE="${AAB_FILE%.aab}.apks"
java -jar "$BUNDLETOOL" build-apks \
    --bundle="$AAB_FILE" \
    --output="$APKS_FILE"

if [ $? -ne 0 ]; then
    echo "❌ APKs生成失败！"
    exit 1
fi

echo "✅ APKs文件生成: $APKS_FILE"

# 安装到设备
echo "安装到设备..."
java -jar "$BUNDLETOOL" install-apks --apks="$APKS_FILE"

if [ $? -eq 0 ]; then
    echo "✅ 应用安装成功！"
else
    echo "❌ 应用安装失败！"
    echo ""
    echo "可能的原因:"
    echo "1. 设备未连接或未开启USB调试"
    echo "2. 需要先卸载旧版本应用"
    echo "3. 签名不匹配"
    echo ""
    echo "可以尝试:"
    echo "1. 检查设备连接: adb devices"
    echo "2. 卸载旧版本: adb uninstall com.example.rndemo"
    echo "3. 手动安装通用APK:"
    echo "   java -jar $BUNDLETOOL build-apks --bundle=$AAB_FILE --output=universal.apks --mode=universal"
    echo "   unzip universal.apks"
    echo "   adb install universal.apk"
fi
