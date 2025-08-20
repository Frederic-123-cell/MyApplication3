@echo off
title Check App Status

echo ==========================================
echo 检查应用运行状态
echo ==========================================

echo.
echo 步骤 1: 检查ADB服务
echo ------------------------
cd /d "E:\Program Files\Android\Sdk\platform-tools"
adb start-server >nul 2>&1

echo.
echo 步骤 2: 检查设备连接
echo ------------------------
echo 已连接的设备:
adb devices
echo.

echo.
echo 步骤 3: 检查应用安装状态
echo ------------------------
adb shell pm list packages | findstr com.example.myapplication
if %errorlevel% == 0 (
    echo 应用已安装
) else (
    echo 应用未安装
)

echo.
echo 步骤 4: 检查应用进程状态
echo ------------------------
adb shell ps | findstr com.example.myapplication
if %errorlevel% == 0 (
    echo 应用正在运行
) else (
    echo 应用未运行
)

echo.
echo 步骤 5: 检查应用存储使用情况
echo ------------------------
adb shell dumpsys diskstats | findstr com.example.myapplication

echo.
echo 步骤 6: 检查应用权限状态
echo ------------------------
adb shell dumpsys package com.example.myapplication | findstr permission

echo.
echo 步骤 7: 获取应用详细信息
echo ------------------------
adb shell dumpsys package com.example.myapplication | findstr -A 10 "Package \[com.example.myapplication\]"

echo.
echo 按任意键退出...
pause >nul