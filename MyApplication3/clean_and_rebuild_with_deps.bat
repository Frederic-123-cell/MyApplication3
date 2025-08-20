@echo off
title Clean and Rebuild with Dependencies

echo ==========================================
echo 清理并重建项目（包含依赖项修复）
echo ==========================================

echo.
echo 步骤 1: 返回项目目录
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"

echo.
echo 步骤 2: 停止ADB服务
echo ------------------------
cd /d "E:\Program Files\Android\Sdk\platform-tools"
adb kill-server
timeout /t 3 /nobreak >nul

echo.
echo 步骤 3: 清理项目构建目录
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"
if exist app\build (
    echo 删除构建目录...
    rd /s /q app\build
    echo 构建目录已删除
) else (
    echo 构建目录不存在
)

echo.
echo 步骤 4: 清理项目
echo ------------------------
call gradlew.bat clean
if %errorlevel% == 0 (
    echo 项目已清理
) else (
    echo 项目清理失败
)

echo.
echo 步骤 5: 同步依赖项
echo ------------------------
call gradlew.bat --refresh-dependencies
if %errorlevel% == 0 (
    echo 依赖项同步成功
) else (
    echo 依赖项同步失败
)

echo.
echo 步骤 6: 重新构建项目
echo ------------------------
call gradlew.bat build
if %errorlevel% == 0 (
    echo 项目构建成功
) else (
    echo 项目构建失败
    echo.
    echo 请检查错误信息并修复问题
    goto end
)

echo.
echo 步骤 7: 启动ADB服务
echo ------------------------
cd /d "E:\Program Files\Android\Sdk\platform-tools"
adb start-server

echo.
echo 步骤 8: 检查设备连接
echo ------------------------
echo 已连接的设备:
adb devices

echo.
echo 步骤 9: 卸载现有应用
echo ------------------------
adb uninstall com.example.myapplication
echo 卸载完成（如果应用未安装则会显示错误，这是正常的）

echo.
echo 完成！
echo 现在请在Android Studio中:
echo 1. 点击 Run (^>_) 按钮（而不是Debug按钮）
echo 2. 或者在Run Configuration中取消勾选"Debug application on start"
echo 3. 然后重新运行应用

:end
echo.
echo 按任意键退出...
pause >nul