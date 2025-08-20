@echo off
echo 准备将项目推送到GitHub...

echo.
echo 步骤1: 检查Git状态
git status

echo.
echo 步骤2: 添加所有文件
git add .

echo.
echo 步骤3: 创建初始提交（如果尚未提交）
git commit -m "Initial commit"

echo.
echo 步骤4: 设置远程仓库
git remote remove origin 2>nul
git remote add origin https://github.com/Frederic-123-cell/MyApplication3.git

echo.
echo 步骤5: 创建main分支
git branch -M main

echo.
echo 步骤6: 推送到GitHub
echo 正在推送代码到GitHub，请稍候...
git push -u origin main --force

echo.
echo 推送完成！
echo 您的代码现在应该已经在GitHub仓库中了。
echo 仓库地址：https://github.com/Frederic-123-cell/MyApplication3
pause