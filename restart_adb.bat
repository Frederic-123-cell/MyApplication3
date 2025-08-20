@echo off
title 重启ADB服务

echo ==========================================
echo 重启ADB服务工具
echo ==========================================

echo.
echo 步骤 1: 定位ADB路径
echo ------------------------
set ADB_PATH="E:\Program Files\Android\Sdk\platform-tools\adb.exe"
if exist %ADB_PATH% (
    echo 找到ADB: %ADB_PATH%
) else (
    echo 未找到ADB，请检查Android SDK路径
    pause
    exit /b 1
)

echo.
echo 步骤 2: 停止ADB服务
echo ------------------------
echo 正在停止ADB服务...
%ADB_PATH% kill-server
if %errorlevel% == 0 (
    echo ADB服务已停止
) else (
    echo 停止ADB服务失败
)

echo.
echo 步骤 3: 启动ADB服务
echo ------------------------
echo 正在启动ADB服务...
%ADB_PATH% start-server
if %errorlevel% == 0 (
    echo ADB服务已启动
) else (
    echo 启动ADB服务失败
)

echo.
echo 步骤 4: 检查连接设备
echo ------------------------
echo 已连接的设备:
%ADB_PATH% devices

echo.
echo 重启完成！
echo 现在可以尝试重新安装应用
pause