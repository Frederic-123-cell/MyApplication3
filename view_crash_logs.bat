@echo off
title View Crash Logs

echo ==========================================
echo 查看应用崩溃日志
echo ==========================================

echo.
echo 步骤 1: 检查ADB服务
echo ------------------------
cd /d "E:\Program Files\Android\Sdk\platform-tools"
adb start-server >nul 2>&1

echo.
echo 步骤 2: 查看最近的崩溃日志
echo ------------------------
echo 最近的崩溃日志:
echo ====================
adb logcat -t 100 | findstr -i "FATAL\|crash\|exception\|com.example.myapplication"
echo.

echo.
echo 步骤 3: 实时监控崩溃日志
echo ------------------------
echo 按 Ctrl+C 停止监控
echo ====================
adb logcat | findstr -i "FATAL\|crash\|exception\|com.example.myapplication"

echo.
echo 按任意键退出...
pause >nul