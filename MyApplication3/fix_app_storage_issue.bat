@echo off
title Fix App Storage Issue

echo ==========================================
echo 解决应用存储清除问题
echo ==========================================

echo.
echo 步骤 1: 重启ADB服务
echo ------------------------
cd /d "E:\Program Files\Android\Sdk\platform-tools"
adb kill-server
timeout /t 2 /nobreak >nul
adb start-server

echo.
echo 步骤 2: 检查设备连接
echo ------------------------
echo 已连接的设备:
adb devices

echo.
echo 步骤 3: 强制停止应用
echo ------------------------
adb shell am force-stop com.example.myapplication
if %errorlevel% == 0 (
    echo 应用已强制停止
) else (
    echo 强制停止应用失败，但继续执行下一步
)

echo.
echo 步骤 4: 清除应用数据
echo ------------------------
adb shell pm clear com.example.myapplication
if %errorlevel% == 0 (
    echo 应用数据已清除
) else (
    echo 清除应用数据失败，尝试替代方法
    echo 尝试卸载并重新安装应用...
    adb uninstall com.example.myapplication
)

echo.
echo 步骤 5: 重新安装应用
echo ------------------------
echo 请在Android Studio中重新运行应用

echo.
echo 完成！如果问题仍然存在，请尝试以下操作：
echo 1. 在手机上手动清除应用数据：
echo    设置 ^> 应用管理 ^> 我的应用 ^> 存储 ^> 清除数据
echo 2. 重启手机后重试
echo 3. 检查USB调试设置是否正确开启
echo.
echo 按任意键退出...
pause >nul