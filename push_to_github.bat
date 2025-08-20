@echo off
echo 正在将项目推送到GitHub...

echo 添加所有文件到Git
git add .

echo 创建提交
git commit -m "Update project files"

echo 推送到GitHub
git push -u origin main

echo 推送完成！
pause