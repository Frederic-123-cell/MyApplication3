@echo off
title View App Logs

echo ==========================================
echo 查看应用日志
echo ==========================================

cd /d "E:\Program Files\Android\Sdk\platform-tools"

echo.
echo 清除现有日志...
adb logcat -c

echo.
echo 请启动应用，然后按任意键查看日志...
pause

echo.
echo 应用日志:
echo ====================
adb logcat -d | findstr "com.example.myapplication\|AndroidRuntime\|FATAL\|ERROR\|CRASH"

echo.
echo 按任意键退出...
pause >nul