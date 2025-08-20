@echo off
title Check Compilation

echo ==========================================
echo 检查代码编译问题
echo ==========================================

cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"

echo.
echo 正在清理项目...
call gradlew.bat clean

echo.
echo 正在编译Debug版本...
call gradlew.bat compileDebugJavaWithJavac

if %errorlevel% == 0 (
    echo.
    echo 编译成功!
) else (
    echo.
    echo 编译失败，请检查以上错误信息
)

echo.
echo 按任意键退出...
pause >nul