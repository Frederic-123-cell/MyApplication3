@echo off
title Check Debug Status

echo ==========================================
echo 检查应用调试状态
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
echo 步骤 3: 检查应用调试状态
echo ------------------------
echo 应用调试信息:
adb shell dumpsys package com.example.myapplication | findstr -i "debuggable\|testOnly\|debug"
echo.

echo.
echo 步骤 4: 检查应用安装信息
echo ------------------------
echo 应用安装信息:
adb shell pm list packages -f | findstr com.example.myapplication
echo.

echo.
echo 步骤 5: 检查应用进程状态
echo ------------------------
echo 应用进程状态:
adb shell ps | findstr com.example.myapplication
echo.

echo.
echo 步骤 6: 检查应用权限
echo ------------------------
echo 应用权限信息:
adb shell dumpsys package com.example.myapplication | findstr -A 20 "requested permissions"
echo.

echo.
echo 建议操作:
echo ==========
echo 如果显示应用是调试模式或testOnly模式:
echo 1. 运行 disconnect_debugger.bat 脚本
echo 2. 运行 clean_rebuild_project.bat 脚本
echo 3. 在Android Studio中使用Run而不是Debug按钮
echo 4. 在OPPO手机上关闭"等待调试器"选项

echo.
echo 按任意键退出...
pause >nul