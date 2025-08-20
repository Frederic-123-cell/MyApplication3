@echo off
title Quick Build Check

echo ==========================================
echo 快速构建检查
echo ==========================================

echo.
echo 步骤 1: 返回项目目录
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"

echo.
echo 步骤 2: 清理项目
echo ------------------------
call gradlew.bat clean
if %errorlevel% == 0 (
    echo 项目清理成功
) else (
    echo 项目清理失败
    goto end
)

echo.
echo 步骤 3: 编译Debug版本Java代码
echo ------------------------
call gradlew.bat compileDebugJavaWithJavac
if %errorlevel% == 0 (
    echo Java代码编译成功
) else (
    echo Java代码编译失败
    goto end
)

echo.
echo 步骤 4: 编译Release版本Java代码
echo ------------------------
call gradlew.bat compileReleaseJavaWithJavac
if %errorlevel% == 0 (
    echo Release版本Java代码编译成功
) else (
    echo Release版本Java代码编译失败
    goto end
)

echo.
echo 步骤 5: 构建APK
echo ------------------------
call gradlew.bat assembleDebug
if %errorlevel% == 0 (
    echo Debug APK构建成功
) else (
    echo Debug APK构建失败
    goto end
)

echo.
echo 所有构建步骤都成功完成！

:end
echo.
echo 按任意键退出...
pause >nul