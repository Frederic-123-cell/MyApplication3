@echo off
title Full Clean and Rebuild

echo ==========================================
echo 完全清理和重建项目
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
echo 步骤 3: 检查设备连接
echo ------------------------
echo 已连接的设备:
adb devices

echo.
echo 步骤 4: 强制停止应用
echo ------------------------
adb shell am force-stop com.example.myapplication
if %errorlevel% == 0 (
    echo 应用已强制停止
) else (
    echo 强制停止应用失败
)

echo.
echo 步骤 5: 清除应用数据
echo ------------------------
adb shell pm clear com.example.myapplication
if %errorlevel% == 0 (
    echo 应用数据已清除
) else (
    echo 清除应用数据失败
)

echo.
echo 步骤 6: 卸载应用
echo ------------------------
adb uninstall com.example.myapplication
if %errorlevel% == 0 (
    echo 应用已卸载
) else (
    echo 应用卸载失败或应用未安装
)

echo.
echo 步骤 7: 返回项目目录
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"

echo.
echo 步骤 8: 清理项目构建目录
echo ------------------------
if exist app\build (
    echo 删除构建目录...
    rd /s /q app\build
    echo 构建目录已删除
) else (
    echo 构建目录不存在
)

echo.
echo 步骤 9: 清理项目
echo ------------------------
call gradlew.bat clean
if %errorlevel% == 0 (
    echo 项目已清理
) else (
    echo 项目清理失败
)

echo.
echo 步骤 10: 重新构建项目
echo ------------------------
call gradlew.bat build
if %errorlevel% == 0 (
    echo 项目构建成功
    echo.
    echo 现在可以在Android Studio中运行应用了！
) else (
    echo 项目构建失败
    echo.
    echo 请检查错误信息并尝试修复问题
)

echo.
echo 按任意键退出...
pause >nul