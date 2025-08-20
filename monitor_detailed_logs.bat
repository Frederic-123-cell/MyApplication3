@echo off
title Monitor Detailed Logs

echo ==========================================
echo 监控应用详细日志
echo ==========================================

echo.
echo 清除现有日志并开始监控...
echo 按 Ctrl+C 停止监控
echo ====================

cd /d "E:\Program Files\Android\Sdk\platform-tools"
adb logcat -c
adb logcat | findstr -i "com.example.myapplication\|texttospeech\|tts\|FATAL\|ERROR\|CRASH\|Exception"

echo.
echo 按任意键退出...
pause >nul