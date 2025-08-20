@echo off
title Clean and Build Project

echo ==========================================
echo 清理和构建项目
echo ==========================================

echo.
echo 步骤 1: 返回项目目录
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"

echo.
echo 步骤 2: 清理项目构建目录
echo ------------------------
if exist app\build (
    echo 删除构建目录...
    rd /s /q app\build
    echo 构建目录已删除
) else (
    echo 构建目录不存在
)

echo.
echo 步骤 3: 清理项目
echo ------------------------
call gradlew.bat clean
if %errorlevel% == 0 (
    echo 项目已清理
) else (
    echo 项目清理失败
)

echo.
echo 步骤 4: 重新构建项目
echo ------------------------
call gradlew.bat build
if %errorlevel% == 0 (
    echo 项目构建成功
    echo.
    echo 现在可以在Android Studio中运行应用了！
) else (
    echo 项目构建失败
    echo.
    echo 请检查错误信息并尝试修复问题
)

echo.
echo 按任意键退出...
pause >nul