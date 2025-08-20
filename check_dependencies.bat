@echo off
title Check Dependencies

echo ==========================================
echo 检查项目依赖项
echo ==========================================

echo.
echo 步骤 1: 返回项目目录
echo ------------------------
cd /d "E:\Users\ALIENWARE\AndroidStudioProjects\MyApplication3"

echo.
echo 步骤 2: 检查依赖项树
echo ------------------------
echo 生成依赖项树...
call gradlew.bat app:dependencies > dependencies.txt
if %errorlevel% == 0 (
    echo 依赖项树已生成到 dependencies.txt
) else (
    echo 生成依赖项树失败
)

echo.
echo 步骤 3: 检查可能的冲突
echo ------------------------
echo 检查Guava相关依赖项...
call gradlew.bat app:dependencies | findstr -i "guava\|listenablefuture\|concurrent"
echo.

echo.
echo 步骤 4: 检查AndroidX相关依赖项
echo ------------------------
echo 检查AndroidX相关依赖项...
call gradlew.bat app:dependencies | findstr -i "androidx"
echo.

echo.
echo 依赖项检查完成
echo 详细信息请查看 dependencies.txt 文件

echo.
echo 按任意键退出...
pause >nul