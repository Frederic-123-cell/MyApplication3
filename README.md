# 盲人辅助应用 - 多功能AI助手应用

这是一个专为视障用户设计的多功能AI助手应用，旨在提供更便捷的生活辅助。应用名为"盲人辅助应用"，包含物体识别、艺术品欣赏、图书阅读、语音控制和紧急求助等核心功能。

## 核心功能

### 1. 主界面 (MainActivity)
- 应用入口，提供导航到各个功能模块
- 支持语音导航和触觉反馈

### 2. 物体识别 (ObjectRecognitionActivity)
- 使用摄像头拍照识别物体
- 集成通义千问VL API进行图像识别
- 语音播报识别结果

### 3. 艺术品欣赏 (ArtAppreciationActivity)
- 浏览和欣赏艺术品
- 提供艺术品描述和艺术家信息
- 语音播报功能

### 4. 图书阅读 (BookReadingActivity)
-接入网站


### 5. 紧急求助 (EmergencyActivity)
- 快速拨打紧急电话
- 摔倒检测
- 语音报警功能

## 解决应用闪退和编译错误问题

我们已经解决了应用的以下问题：

1. **应用闪退问题**：
   - 修复了MainActivity中视图组件初始化的问题
   - 添加了空值检查和异常处理机制
   - 简化了事件监听器以提高稳定性

2. **编译错误问题**：
   - 修复了重复定义的checkPermissions()方法
   - 补充了缺失的import语句
   - 修复了RecognitionListener相关的方法覆盖问题
   - 解决了所有找不到符号的错误

现在应用应该能够正常启动和运行，不再出现闪退或编译错误。

## 运行/部署指南

### 环境要求
- Android Studio (推荐最新版本)
- Android SDK API 34 (Android 14)
- Java 17
- 最低支持Android 5.0 (API 21)

### 导入项目
1. 打开Android Studio
2. 选择"Open an existing Android Studio project"
3. 导航到项目目录并选择根文件夹
4. 等待Gradle同步完成

### 构建APK
文件里已有现成的.apk文件如果不成功请继续下列操作
-----------------------------------------------------------------------------------------------------------
1. 在Android Studio中，选择菜单栏的 Build → Build Bundle(s) / APK(s) → Build APK(s)
2. 等待构建完成
3. 点击"locate"可查看生成的APK文件

或者使用命令行构建:
```bash
# Windows
gradlew assembleDebug

# macOS/Linux
./gradlew assembleDebug
```

构建好的APK文件位置：
```
app/build/outputs/apk/debug/app-debug.apk
```

### 安装应用
1. 将生成的APK文件传输到Android设备
2. 在设备上打开文件管理器，找到APK文件
3. 点击APK文件开始安装
4. 根据提示授予必要的权限

## API密钥配置

本应用需要配置以下API密钥才能正常使用相关功能：

1. 通义千问VL API密钥（用于物体识别功能）


在`local.properties`文件中添加以下配置：
```properties
dashscope.api.key=your_dashscope_api_key

```

## 权限说明

应用需要以下权限才能正常运行：
- INTERNET：网络访问权限，用于API调用
- CAMERA：相机权限，用于物体识别拍照
- RECORD_AUDIO：录音权限，用于语音识别
- CALL_PHONE：电话权限，用于紧急求助功能
- ACCESS_FINE_LOCATION：精确定位权限，用于紧急求助发送位置
- ACCESS_COARSE_LOCATION：粗略定位权限，用于紧急求助发送位置
- 需要访问设备的陀螺仪，指南针，海拔气压针，水平仪，运动与健康等
## 项目结构说明

```
app/
├── src/main/java/              # Java源代码
├── src/main/res/               # 资源文件
├── src/main/AndroidManifest.xml # 应用配置文件
├── build.gradle.kts           # 构建配置文件
└── libs/                      # 第三方库文件
```

## 技术栈

- Android SDK (API 34)
- Java
- Retrofit for API calls
- Gson for JSON parsing

## 注意事项

- 应用为视障用户优化，所有操作均支持语音提示
- 建议在安静环境下使用语音功能以获得最佳识别效果
- 部分功能需要网络连接
- 使用前请确保已授予必要的权限（相机、录音、存储等）

## 可执行文件获取

应用的可执行APK文件位于仓库的Release区域，或者可以按照上述构建指南自行构建。

构建好的APK文件位置：
```
app/build/outputs/apk/debug/app-debug.apk
```
