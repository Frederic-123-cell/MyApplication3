@echo off
title Rebuild Project

echo ==========================================
echo 重新构建项目
echo ==========================================

echo.
echo 步骤 1: 清理项目
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"
call gradlew.bat clean
if %errorlevel% == 0 (
    echo 项目清理完成
) else (
    echo 项目清理失败
    goto end
)

echo.
echo 步骤 2: 重新构建项目
echo ------------------------
call gradlew.bat build
if %errorlevel% == 0 (
    echo 项目构建成功！
    echo.
    echo 现在可以在Android Studio中运行应用了。
) else (
    echo 项目构建失败，请检查错误信息
)

:end
echo.
echo 按任意键退出...
pause >nul