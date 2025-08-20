@echo off
title Simple Build

echo ==========================================
echo 简单构建脚本
echo ==========================================

echo.
echo 步骤 1: 返回项目目录
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"

echo.
echo 步骤 2: 清理项目
echo ------------------------
if exist gradlew.bat (
    call gradlew.bat clean
    if %errorlevel% == 0 (
        echo 项目清理成功
    ) else (
        echo 项目清理失败
        goto end
    )
) else (
    echo 未找到gradlew.bat文件
    goto end
)

echo.
echo 步骤 3: 编译项目
echo ------------------------
if exist gradlew.bat (
    call gradlew.bat build
    if %errorlevel% == 0 (
        echo 项目构建成功
    ) else (
        echo 项目构建失败
        goto end
    )
) else (
    echo 未找到gradlew.bat文件
    goto end
)

echo.
echo 构建完成！

:end
echo.
echo 按任意键退出...
pause >nul