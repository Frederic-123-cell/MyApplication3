@echo off
echo 正在尝试强制停止应用...
"E:\Program Files\Android\Sdk\platform-tools\adb.exe" shell am force-stop com.example.myapplication
if %errorlevel% == 0 (
    echo 应用已成功强制停止
) else (
    echo 强制停止应用失败，请检查设备连接和ADB设置
)

echo.
echo 正在尝试清除应用数据...
"E:\Program Files\Android\Sdk\platform-tools\adb.exe" shell pm clear com.example.myapplication
if %errorlevel% == 0 (
    echo 应用数据已成功清除
) else (
    echo 清除应用数据失败，请检查设备连接和ADB设置
)

echo.
echo 操作完成，按任意键退出...
pause >nul