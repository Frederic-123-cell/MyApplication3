# My Application - 多功能AI助手应用

这是一个为视障用户设计的多功能AI助手应用，包含以下主要功能：

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
- 文本阅读功能
- 文本转语音(TTS)朗读
- 自动翻页功能

### 5. 语音控制 (VoiceControlActivity)
- 语音识别和语音合成
- 集成豆包AI进行对话
- 使用Android系统内置语音识别

### 6. 紧急求助 (EmergencyActivity)
- 快速拨打紧急电话
- 发送位置信息给紧急联系人
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

1. **确保所有视图组件正确初始化**：
   - 检查布局文件中的组件ID是否与代码中的ID匹配
   - 确保在访问视图组件前调用setContentView()

2. **检查权限配置**：
   - 确保AndroidManifest.xml中声明了所有必要的权限
   - 检查运行时权限请求是否正确处理

3. **验证依赖库配置**：
   - 确保build.gradle.kts中添加了所有必要的依赖
   - 检查libs.versions.toml中的版本配置是否正确

4. **检查Activity声明**：
   - 确保AndroidManifest.xml中声明了所有Activity
   - 检查Activity的intent-filter配置是否正确


## 语音识别

应用使用Android系统内置的语音识别功能，适用于大多数Android设备。

注意：之前曾尝试集成科大讯飞语音识别功能，但由于缺少必要的SDK文件导致构建错误，现已移除该功能。如果需要重新启用，请参考libs目录下的说明。

## 集成指南

### 基础配置
1. 确保所有API密钥已正确配置
2. 检查网络权限和其他必要权限

### 科大讯飞SDK集成步骤
1. 从科大讯飞开放平台(https://www.xfyun.cn/)注册账号并创建应用
2. 下载Android SDK
3. 将Msc.jar文件放入app/libs目录
4. 将.so文件放入src/main/jniLibs对应架构目录
5. 在代码中替换IFLYTEK_APP_ID为您申请的APPID

## 注意事项

- 应用为视障用户优化，所有操作均支持语音提示
- 建议在安静环境下使用语音功能以获得最佳识别效果
- 部分功能需要网络连接
- 使用前请确保已授予必要的权限（相机、录音、存储等）

## 技术栈

- Android SDK
- Java
- Retrofit for API calls
- Gson for JSON parsing
- OkHttp for HTTP requests
- 声网(Agora)SDK for video calling（需要额外集成）
- Google Generative AI SDK for AI features（需要额外集成）
- 通义千问API
- 豆包AI API

# 项目已重置

本项目已被清空，为重新开始做准备。
