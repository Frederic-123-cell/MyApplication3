@echo off
title Check Build Errors

echo ==========================================
echo 检查构建错误
echo ==========================================

cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"

echo.
echo 正在清理项目...
call gradlew.bat clean

echo.
echo 正在构建项目...
call gradlew.bat build --info

echo.
echo 按任意键退出...
pause >nul