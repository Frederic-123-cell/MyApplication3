@echo off
title Test All Functionalities

echo ==========================================
echo 测试所有功能
echo ==========================================

echo.
echo 步骤 1: 返回项目目录
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"

echo.
echo 步骤 2: 清理项目
echo ------------------------
call gradlew.bat clean
if %errorlevel% == 0 (
    echo 项目清理成功
) else (
    echo 项目清理失败
    goto end
)

echo.
echo 步骤 3: 编译项目
echo ------------------------
call gradlew.bat build
if %errorlevel% == 0 (
    echo 项目编译成功
) else (
    echo 项目编译失败
    goto end
)

echo.
echo 步骤 4: 检查APK是否存在
echo ------------------------
if exist app\build\outputs\apk\debug\app-debug.apk (
    echo APK文件存在
) else (
    echo APK文件不存在
    goto end
)

echo.
echo 步骤 5: 重启ADB服务
echo ------------------------
cd /d "E:\Program Files\Android\Sdk\platform-tools"
adb kill-server
timeout /t 3 /nobreak >nul
adb start-server

echo.
echo 步骤 6: 检查设备连接
echo ------------------------
echo 已连接的设备:
adb devices

echo.
echo 步骤 7: 卸载现有应用
echo ------------------------
adb uninstall com.example.myapplication
echo 卸载完成

echo.
echo 步骤 8: 安装新应用
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"
adb install app\build\outputs\apk\debug\app-debug.apk
if %errorlevel% == 0 (
    echo 应用安装成功
) else (
    echo 应用安装失败
    goto end
)

echo.
echo 步骤 9: 启动应用
echo ------------------------
adb shell am start -n com.example.myapplication/.MainActivity
if %errorlevel% == 0 (
    echo 应用启动成功
) else (
    echo 应用启动失败
)

echo.
echo 步骤 10: 等待应用启动
echo ------------------------
timeout /t 5 /nobreak >nul

echo.
echo 测试完成！
echo 现在可以在手机上测试以下功能：
echo ========================
echo 1. 主界面功能:
echo    - 单击各个按钮听取功能说明（TTS功能）
echo    - 长按按钮进入相应功能模块
echo.
echo 2. 拍照识物功能:
echo    - 点击"拍照识别"按钮
echo    - 选择图片进行识别
echo.
echo 3. 典籍阅读功能:
echo    - 跳转到在线阅读网站
echo.
echo 4. 艺术鉴赏功能:
echo    - 浏览预设的艺术作品介绍
echo    - 使用"上一幅"/"下一幅"按钮切换作品
echo.
echo 5. 紧急求助功能:
echo    - 设置紧急联系人
echo    - 模拟摔倒检测
echo    - 测试紧急呼叫流程

:end
echo.
echo 按任意键退出...
pause >nul