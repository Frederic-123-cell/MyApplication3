@echo off
title Simple Build Check

echo 正在检查项目构建...

cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"

echo 清理项目...
if exist app\build (
    rd /s /q app\build
)

echo 编译项目...
gradlew.bat build

if %errorlevel% == 0 (
    echo.
    echo 项目构建成功！
) else (
    echo.
    echo 项目构建失败，请检查错误信息。
)

echo.
echo 按任意键退出...
pause >nul