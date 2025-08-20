@echo off
title Check App Logs

echo ==========================================
echo 查看应用错误日志
echo ==========================================

cd /d "E:\Program Files\Android\Sdk\platform-tools"

echo.
echo 清理旧日志...
adb logcat -c

echo.
echo 等待10秒收集新日志...
timeout 10 >nul

echo.
echo 收集错误日志...
adb logcat *:E

echo.
echo 按任意键退出...
pause >nul