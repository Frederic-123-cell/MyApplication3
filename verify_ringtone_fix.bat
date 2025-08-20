@echo off
title Verify Ringtone Fix

echo ==========================================
echo 验证铃声修复
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
echo 步骤 3: 编译EmergencyActivity
echo ------------------------
if exist gradlew.bat (
    call gradlew.bat compileDebugJavaWithJavac --console=plain
    if %errorlevel% == 0 (
        echo EmergencyActivity编译成功
    ) else (
        echo EmergencyActivity编译失败
        goto end
    )
) else (
    echo 未找到gradlew.bat文件
    goto end
)

echo.
echo 步骤 4: 编译整个项目
echo ------------------------
if exist gradlew.bat (
    call gradlew.bat compileDebugJavaWithJavac --console=plain
    if %errorlevel% == 0 (
        echo 项目编译成功
    ) else (
        echo 项目编译失败
        goto end
    )
) else (
    echo 未找到gradlew.bat文件
    goto end
)

echo.
echo 步骤 5: 构建Debug APK
echo ------------------------
if exist gradlew.bat (
    call gradlew.bat assembleDebug --console=plain
    if %errorlevel% == 0 (
        echo Debug APK构建成功
        echo APK位置: app\build\outputs\apk\debug\app-debug.apk
    ) else (
        echo Debug APK构建失败
        goto end
    )
) else (
    echo 未找到gradlew.bat文件
    goto end
)

echo.
echo 所有编译和构建步骤都成功完成！铃声修复验证通过。

:end
echo.
echo 按任意键退出...
pause >nul