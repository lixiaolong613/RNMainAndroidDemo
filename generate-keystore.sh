#!/bin/bash

# 生成Android签名密钥脚本
# 使用方法: chmod +x generate-keystore.sh && ./generate-keystore.sh

echo "=== 生成Android签名密钥 ==="
echo ""

# 检查是否已存在密钥文件
if [ -f "my-release-key.keystore" ]; then
    echo "⚠️  密钥文件 my-release-key.keystore 已存在！"
    echo "如果要重新生成，请先删除现有文件:"
    echo "rm my-release-key.keystore"
    echo "rm keystore.properties"
    exit 1
fi

echo "开始生成签名密钥..."
echo ""

# 获取用户输入
read -s -p "请输入密钥库密码: " STORE_PASSWORD
echo ""
read -s -p "请再次输入密钥库密码: " STORE_PASSWORD_CONFIRM
echo ""

if [ "$STORE_PASSWORD" != "$STORE_PASSWORD_CONFIRM" ]; then
    echo "❌ 密码不匹配！"
    exit 1
fi

read -s -p "请输入密钥密码 (建议与密钥库密码相同): " KEY_PASSWORD
echo ""

read -p "请输入密钥别名 [默认: my-key-alias]: " KEY_ALIAS
KEY_ALIAS=${KEY_ALIAS:-my-key-alias}

echo ""
echo "将要创建密钥，请输入证书信息:"
read -p "您的姓名 [CN]: " CN
read -p "组织单位 [OU]: " OU
read -p "组织 [O]: " O
read -p "城市 [L]: " L
read -p "省份 [ST]: " ST
read -p "国家代码 [C, 如CN]: " C

# 构建DN字符串
DN="CN=${CN:-Unknown}, OU=${OU:-Unknown}, O=${O:-Unknown}, L=${L:-Unknown}, ST=${ST:-Unknown}, C=${C:-CN}"

echo ""
echo "正在生成密钥..."

# 生成密钥
keytool -genkey -v \
    -keystore rndemo-key.keystore \
    -alias "$KEY_ALIAS" \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000 \
    -storepass "$STORE_PASSWORD" \
    -keypass "$KEY_PASSWORD" \
    -dname "$DN"

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 签名密钥生成成功！"
    echo "文件位置: $(pwd)/my-release-key.keystore"

    # 创建keystore.properties文件
    echo ""
    echo "正在创建配置文件 keystore.properties..."

    cat > keystore.properties << EOF
# Android签名配置文件
# 注意：不要将此文件提交到版本控制系统！

# 密钥库配置
MYAPP_UPLOAD_STORE_FILE=my-release-key.keystore
MYAPP_UPLOAD_KEY_ALIAS=$KEY_ALIAS
MYAPP_UPLOAD_STORE_PASSWORD=$STORE_PASSWORD
MYAPP_UPLOAD_KEY_PASSWORD=$KEY_PASSWORD

# 生成时间: $(date)
# 证书信息: $DN
EOF

    echo "✅ 配置文件创建成功！"
    echo ""
    echo "📁 生成的文件："
    echo "  - my-release-key.keystore (密钥文件)"
    echo "  - keystore.properties (配置文件)"
    echo ""
    echo "⚠️  重要提醒："
    echo "1. 请妥善保管密钥文件和密码"
    echo "2. 不要将这些文件提交到版本控制系统"
    echo "3. 建议备份密钥文件到安全位置"
    echo "4. 丢失密钥将无法更新已发布的应用"
    echo ""
    echo "🚀 现在可以构建Release版本:"
    echo "./gradlew assembleRelease"
    echo "./gradlew bundleRelease"
else
    echo "❌ 密钥生成失败！"
    echo "请检查是否安装了Java keytool工具"
fi
