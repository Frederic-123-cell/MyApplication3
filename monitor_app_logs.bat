@echo off
title Monitor App Logs

echo ==========================================
echo 监控应用运行日志
echo ==========================================

cd /d "E:\Program Files\Android\Sdk\platform-tools"

echo.
echo 正在监控应用日志，启动应用后查看日志输出...
echo 按 Ctrl+C 停止监控
echo.

adb logcat -c
adb logcat | findstr "com.example.myapplication\|AndroidRuntime\|FATAL\|ERROR"

pause