@echo off
:: 设置窗口标题
title Android App Clean Reinstall Tool

:: 初始化
setlocal EnableDelayedExpansion
set APP_PACKAGE=com.example.myapplication
set SDK_PATH="E:\Program Files\Android\Sdk\platform-tools"
set PROJECT_PATH="E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"
set LOG_FILE=reinstall_log.txt

:: 创建日志文件
echo %date% %time% - 开始执行清理重装流程 > %LOG_FILE%

:: 清屏
cls

:: 进度条函数
:progressBar
set "pb=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>"
echo.
echo [!pb:~0,%1!] %2%%
goto :eof

:: 显示标题
echo ==========================================
echo Android 应用完整清理和重装工具
echo ==========================================
call :progressBar 0 0
echo 正在初始化...
timeout /t 1 >nul

:: 检查ADB
echo.
echo 检查ADB环境...
%SDK_PATH%\adb.exe version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: ADB未找到，请检查SDK路径是否正确
    echo %date% %time% - ADB未找到，终止流程 >> %LOG_FILE%
    exit /b 1
)

:: 停止ADB服务
call :progressBar 5 5
echo 正在停止ADB服务...
%SDK_PATH%\adb.exe kill-server >> %LOG_FILE% 2>&1
if %errorlevel% == 0 (
    echo ADB服务已停止
) else (
    echo 警告: 停止ADB服务失败
)
timeout /t 1 >nul

:: 启动ADB服务
call :progressBar 10 10
echo 正在启动ADB服务...
%SDK_PATH%\adb.exe start-server >> %LOG_FILE% 2>&1
if %errorlevel% == 0 (
    echo ADB服务已启动
) else (
    echo 警告: 启动ADB服务失败
)
timeout /t 1 >nul

:: 检查设备连接
call :progressBar 15 15
echo 正在检查设备连接...
echo 已连接的设备:
%SDK_PATH%\adb.exe devices >> %LOG_FILE%
%SDK_PATH%\adb.exe devices | findstr /r /c:"\<device\>" | find /v "List" >nul
if %errorlevel% neq 0 (
    echo 错误: 未检测到连接的设备
    echo %date% %time% - 未检测到设备，终止流程 >> %LOG_FILE%
    exit /b 1
)
timeout /t 1 >nul

:: 强制停止应用
call :progressBar 20 20
echo 正在强制停止应用...
%SDK_PATH%\adb.exe shell am force-stop %APP_PACKAGE% >> %LOG_FILE% 2>&1
if %errorlevel% == 0 (
    echo 应用已强制停止
) else (
    echo 警告: 强制停止应用失败
)
timeout /t 1 >nul

:: 清除应用数据
call :progressBar 25 25
echo 正在清除应用数据...
%SDK_PATH%\adb.exe shell pm clear %APP_PACKAGE% >> %LOG_FILE% 2>&1
if %errorlevel% == 0 (
    echo 应用数据已清除
) else (
    echo 警告: 清除应用数据失败
)
timeout /t 1 >nul

:: 卸载应用
call :progressBar 30 30
echo 正在卸载应用...
%SDK_PATH%\adb.exe uninstall %APP_PACKAGE% >> %LOG_FILE% 2>&1
if %errorlevel% == 0 (
    echo 应用已卸载
) else (
    echo 信息: 应用可能未安装
)
timeout /t 1 >nul

:: 返回项目目录
call :progressBar 35 35
echo 正在切换到项目目录...
cd /d %PROJECT_PATH% >> %LOG_FILE% 2>&1
if %errorlevel% == 0 (
    echo 已切换到项目目录
) else (
    echo 错误: 无法切换到项目目录
    echo %date% %time% - 切换目录失败，终止流程 >> %LOG_FILE%
    exit /b 1
)
timeout /t 1 >nul

:: 清理构建目录
call :progressBar 40 40
echo 正在清理构建目录...
if exist app\build (
    echo 删除构建目录...
    rd /s /q app\build >> %LOG_FILE% 2>&1
    echo 构建目录已删除
) else (
    echo 构建目录不存在
)
timeout /t 1 >nul

:: 项目清理
call :progressBar 45 45
echo 正在清理项目...
call gradlew.bat clean >> %LOG_FILE% 2>&1
if %errorlevel% == 0 (
    echo 项目清理成功
) else (
    echo 错误: 项目清理失败
    echo %date% %time% - 项目清理失败，终止流程 >> %LOG_FILE%
    exit /b 1
)
timeout /t 1 >nul

:: 同步构建
call :progressBar 55 55
echo 正在同步构建项目...
call gradlew.bat build >> %LOG_FILE% 2>&1
if %errorlevel% == 0 (
    echo 项目同步构建成功
) else (
    echo 错误: 项目同步构建失败
    echo %date% %time% - 项目同步构建失败，终止流程 >> %LOG_FILE%
    exit /b 1
)
timeout /t 1 >nul

:: 安装应用
call :progressBar 70 70
echo 正在安装应用...
%SDK_PATH%\adb.exe install app-release.apk >> %LOG_FILE% 2>&1
if %errorlevel% == 0 (
    echo 应用安装成功
) else (
    echo 错误: 应用安装失败
    echo %date% %time% - 应用安装失败，终止流程 >> %LOG_FILE%
    exit /b 1
)
timeout /t 1 >nul

:: 启动应用
call :progressBar 85 85
echo 正在启动应用...
%SDK_PATH%\adb.exe shell am start -n %APP_PACKAGE%/.MainActivity >> %LOG_FILE% 2>&1
if %errorlevel% == 0 (
    echo 应用启动成功
) else (
    echo 警告: 应用启动失败
)
timeout /t 1 >nul

:: 完成
cls
call :progressBar 100 100
echo.
echo ==========================================
echo 清理和重装流程已完成!
echo ==========================================
echo.
echo 重要信息:
echo 1. 应用已成功安装并尝试启动
echo 2. 详细日志已保存到: %cd%\%LOG_FILE%
echo 3. 如果遇到问题，请尝试:
echo    - 重启设备
echo    - 重新连接USB线
echo    - 在开发者选项中重新启用USB调试
echo.
echo 按任意键退出...
pause >nul