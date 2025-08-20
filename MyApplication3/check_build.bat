@echo off
title Check Build

echo ==========================================
echo 检查项目构建
echo ==========================================

echo.
echo 步骤 1: 返回项目目录
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"

echo.
echo 步骤 2: 编译Debug版本Java代码
echo ------------------------
call gradlew.bat compileDebugJavaWithJavac
if %errorlevel% == 0 (
    echo Java代码编译成功
) else (
    echo Java代码编译失败，请检查错误信息
)

echo.
echo 按任意键退出...
pause >nul