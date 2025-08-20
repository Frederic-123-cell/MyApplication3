科大讯飞SDK集成说明：

1. 请从科大讯飞开放平台下载Android SDK：
   - 访问官网：https://www.xfyun.cn/
   - 注册并创建应用获取APPID
   - 下载Android SDK

2. 将以下文件放入此目录：
   - Msc.jar (科大讯飞核心库)
   - 将此文件添加到此目录

3. 在app/build.gradle.kts中已配置：
   implementation(files("libs/Msc.jar"))

4. 替换代码中的APPID：
   - VoiceControlActivity.java中的IFLYTEK_APP_ID
   - MyApplication.java中的IFLYTEK_APP_ID
   - 使用您在科大讯飞平台申请的实际APPID替换"your_iflytek_app_id_here"

5. 添加.so文件：
   - 在src/main目录下创建jniLibs文件夹
   - 将下载的SDK中的armeabi、armeabi-v7a、arm64-v8a等架构的.so文件分别放入对应的子目录中

6. 当前项目功能：
   - 支持Android内置语音识别和科大讯飞语音识别双引擎切换
   - 在语音控制界面点击"切换语音引擎"按钮可在两种引擎间切换
   - 科大讯飞在中文识别方面通常有更好的准确率

7. 编译错误解决：
   - 当前项目因为缺少科大讯飞SDK文件而存在编译错误
   - 添加SDK文件后错误将自动解决
   - 如果不需要科大讯飞功能，可以移除相关代码