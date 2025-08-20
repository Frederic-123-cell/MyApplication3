@echo off
title Fix Waiting for Debugger Issue

echo ==========================================
echo 修复 "Waiting for Debugger" 问题工具
echo ==========================================

echo.
echo 步骤 1: 停止ADB服务
echo ------------------------
"E:\Program Files\Android\Sdk\platform-tools\adb.exe" kill-server
if %errorlevel% == 0 (
    echo ADB服务已停止
) else (
    echo 停止ADB服务失败
)

echo.
echo 步骤 2: 启动ADB服务
echo ------------------------
"E:\Program Files\Android\Sdk\platform-tools\adb.exe" start-server
if %errorlevel% == 0 (
    echo ADB服务已启动
) else (
    echo 启动ADB服务失败
)

echo.
echo 步骤 3: 检查设备连接
echo ------------------------
echo 已连接的设备:
"E:\Program Files\Android\Sdk\platform-tools\adb.exe" devices
echo.

echo 步骤 4: 强制停止应用
echo ------------------------
"E:\Program Files\Android\Sdk\platform-tools\adb.exe" shell am force-stop com.example.myapplication
if %errorlevel% == 0 (
    echo 应用已强制停止
) else (
    echo 强制停止应用失败
)

echo.
echo 步骤 5: 清除应用数据
echo ------------------------
"E:\Program Files\Android\Sdk\platform-tools\adb.exe" shell pm clear com.example.myapplication
if %errorlevel% == 0 (
    echo 应用数据已清除
) else (
    echo 清除应用数据失败
)

echo.
echo 步骤 6: 卸载应用
echo ------------------------
"E:\Program Files\Android\Sdk\platform-tools\adb.exe" uninstall com.example.myapplication
if %errorlevel% == 0 (
    echo 应用已卸载
) else (
    echo 应用卸载失败或应用未安装
)

echo.
echo 修复完成！
echo 现在请在Android Studio中：
echo 1. 选择 Build ^> Clean Project
echo 2. 选择 Build ^> Rebuild Project
echo 3. 点击绿色的Run按钮（而不是Debug按钮）运行应用
echo.
echo 按任意键退出...
pause >nul