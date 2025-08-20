@echo off
title Clean and Rebuild Project

echo ==========================================
echo 清理和重建项目
echo ==========================================

echo.
echo 步骤 1: 停止ADB服务
echo ------------------------
cd /d "E:\Program Files\Android\Sdk\platform-tools"
adb kill-server
if %errorlevel% == 0 (
    echo ADB服务已停止
) else (
    echo 停止ADB服务失败
)

echo.
echo 步骤 2: 启动ADB服务
echo ------------------------
adb start-server
if %errorlevel% == 0 (
    echo ADB服务已启动
) else (
    echo 启动ADB服务失败
)

echo.
echo 步骤 3: 强制停止应用
echo ------------------------
adb shell am force-stop com.example.myapplication
if %errorlevel% == 0 (
    echo 应用已强制停止
) else (
    echo 强制停止应用失败
)

echo.
echo 步骤 4: 清理项目
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"
call gradlew.bat clean
if %errorlevel% == 0 (
    echo 项目已清理
) else (
    echo 项目清理失败
)

echo.
echo 步骤 5: 重建项目
echo ------------------------
call gradlew.bat build
if %errorlevel% == 0 (
    echo 项目已重建
) else (
    echo 项目重建失败
)

echo.
echo 步骤 6: 断开调试器连接
echo ------------------------
adb shell am clear-debug-app
if %errorlevel% == 0 (
    echo 调试器连接已断开
) else (
    echo 断开调试器连接失败
)

echo.
echo 完成！现在请在Android Studio中点击绿色的Run按钮运行应用。
echo 按任意键退出...
pause >nul