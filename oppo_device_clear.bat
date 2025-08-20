@echo off
title OPPO Device Clear Storage

echo ==========================================
echo OPPO设备应用存储清理工具
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
echo 步骤 3: 尝试多种方法清除应用存储
echo ------------------------

echo.
echo 方法 1: 强制停止应用
echo --------
adb shell am force-stop com.example.myapplication
if %errorlevel% == 0 (
    echo 应用已强制停止
) else (
    echo 强制停止应用失败
)

echo.
echo 方法 2: 清除应用数据和缓存
echo --------
adb shell pm clear com.example.myapplication
if %errorlevel% == 0 (
    echo 应用数据和缓存已清除
) else (
    echo 清除应用数据和缓存失败
)

echo.
echo 方法 3: 重置应用偏好设置
echo --------
adb shell pm reset-permissions com.example.myapplication
if %errorlevel% == 0 (
    echo 应用权限已重置
) else (
    echo 重置应用权限失败
)

echo.
echo 方法 4: 卸载应用（保留数据）
echo --------
adb shell pm uninstall -k com.example.myapplication
if %errorlevel% == 0 (
    echo 应用已卸载（保留数据）
) else (
    echo 卸载应用失败
)

echo.
echo 步骤 4: 针对OPPO设备的特殊处理
echo ------------------------

echo.
echo OPPO设备特殊设置检查:
echo 1. 硞认已开启"USB调试"和"USB调试(安全设置)"
echo 2. 检查是否开启了"允许模拟位置"
echo 3. 确认"安装未知应用"权限已开启
echo 4. 检查是否启用了"开发者选项"中的高级调试选项

echo.
echo 步骤 5: 重启应用进程
echo ------------------------
adb shell am kill com.example.myapplication
if %errorlevel% == 0 (
    echo 应用进程已终止
) else (
    echo 终止应用进程失败
)

echo.
echo 建议的OPPO手机设置操作:
echo ========================
echo 1. 进入 设置 ^> 其他设置 ^> 开发者选项
echo 2. 确保开启了以下选项:
echo    - USB调试
echo    - USB调试(安全设置) 
echo    - 禁用权限监控
echo    - 禁用优化校验
echo 3. 进入 设置 ^> 应用管理 ^> MyApplication
echo 4. 点击 存储 ^> 清除数据 和 清除缓存
echo 5. 如果应用无法卸载，进入 应用管理 ^> 右上角菜单 ^> 显示系统程序
echo    查找并禁用"Installer"或"Package installer"相关的系统应用限制

echo.
echo 完成！如果问题仍然存在，请尝试:
echo 1. 重启手机
echo 2. 更换USB线或接口
echo 3. 更新ADB驱动
echo.
echo 按任意键退出...
pause >nul