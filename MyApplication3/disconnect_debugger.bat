@echo off
title Disconnect Debugger

echo ==========================================
echo 断开调试器连接
echo ==========================================

echo.
echo 步骤 1: 重启ADB服务
echo ------------------------
cd /d "E:\Program Files\Android\Sdk\platform-tools"
adb kill-server
timeout /t 3 /nobreak >nul
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
    echo 强制停止应用失败
)

echo.
echo 步骤 4: 清除应用数据
echo ------------------------
adb shell pm clear com.example.myapplication
if %errorlevel% == 0 (
    echo 应用数据已清除
) else (
    echo 清除应用数据失败
)

echo.
echo 步骤 5: 卸载应用
echo ------------------------
adb uninstall com.example.myapplication
if %errorlevel% == 0 (
    echo 应用已卸载
) else (
    echo 应用卸载失败或应用未安装
)

echo.
echo 步骤 6: 重新安装应用（非调试模式）
echo ------------------------
echo 请在Android Studio中执行以下操作：
echo 1. 点击 Run (^>_) 按钮（而不是Debug按钮）
echo 2. 或者在Run Configuration中取消勾选"Debug application on start"
echo 3. 然后重新运行应用

echo.
echo OPPO手机特殊处理:
echo ==================
echo 1. 在手机上:
echo    - 打开 设置 ^> 其他设置 ^> 开发者选项
echo    - 关闭 "等待调试器" 选项
echo    - 确保开启了 "USB调试" 但关闭了 "USB调试(安全设置)"
echo 2. 如果仍然显示"waiting for debugger":
echo    - 强制关闭应用
echo    - 清除应用数据
echo    - 重新启动应用

echo.
echo 按任意键退出...
pause >nul