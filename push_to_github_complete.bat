@echo off
setlocal enabledelayedexpansion

echo 正在将项目推送到GitHub...
echo.

echo 步骤1: 检查Git状态
git status
echo.

echo 步骤2: 添加所有文件到Git
git add .
echo.

echo 步骤3: 创建提交
git commit -m "Initial commit with all project files"
if %errorlevel% neq 0 (
    echo 提交失败，可能没有更改需要提交
)
echo.

echo 步骤4: 检查远程仓库
git remote -v
echo.

echo 步骤5: 添加远程仓库（如果尚未设置）
git remote get-url origin >nul 2>&1
if %errorlevel% equ 1 (
    echo 远程仓库未设置，正在添加...
    git remote add origin https://github.com/Frederic-123-cell/MyApplication3.git
) else (
    echo 远程仓库已存在
)
echo.

echo 步骤6: 检查分支名称
git branch
echo.

echo 步骤7: 创建并切换到main分支（如果需要）
git branch -M main 2>nul
echo.

echo 步骤8: 推送到GitHub
echo 尝试推送到远程仓库...
git push -u origin main
if %errorlevel% neq 0 (
    echo 推送失败，尝试强制推送...
    git push -u origin main --force
    if !errorlevel! neq 0 (
        echo 强制推送也失败了，请手动检查问题
        echo 可能需要先执行: git pull origin main --allow-unrelated-histories
    )
)
echo.

echo 推送过程完成！
echo 如果推送成功，您的代码现在应该在GitHub上了。
echo.
pause