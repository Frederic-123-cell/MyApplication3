@echo off
title Build and Deploy App

echo ==========================================
echo 构建并部署应用
echo ==========================================

echo.
echo 步骤 1: 返回项目根目录
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"
if %errorlevel% == 0 (
    echo 成功进入项目目录
) else (
    echo 进入项目目录失败
    goto end
)

echo.
echo 步骤 2: 清理项目
echo ------------------------
call gradlew clean
if %errorlevel% == 0 (
    echo 项目清理成功
) else (
    echo 项目清理失败
    goto end
)

echo.
echo 步骤 3: 编译Debug版本
echo ------------------------
call gradlew assembleDebug
if %errorlevel% == 0 (
    echo Debug版本编译成功
) else (
    echo Debug版本编译失败
    goto end
)

echo.
echo 步骤 4: 检查APK文件
echo ------------------------
if exist app\build\outputs\apk\debug\app-debug.apk (
    echo APK文件存在: app\build\outputs\apk\debug\app-debug.apk
) else (
    echo APK文件不存在，请检查构建过程
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
echo 卸载完成（如果应用未安装则会显示错误，这是正常的）

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
echo 步骤 10: 显示应用进程信息
echo ------------------------
adb shell ps | findstr com.example.myapplication

:end
echo.
echo 脚本执行完成
echo.
echo 按任意键退出...
pause >nul