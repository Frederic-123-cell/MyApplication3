@echo off
echo Preparing to push project to GitHub...

echo.
echo Step 1: Check Git status
git status

echo.
echo Step 2: Add all files
git add .

echo.
echo Step 3: Create initial commit (if not already committed)
git commit -m "Initial commit"

echo.
echo Step 4: Set up remote repository
git remote remove origin 2>nul
git remote add origin https://github.com/Frederic-123-cell/MyApplication3.git

echo.
echo Step 5: Create main branch
git branch -M main

echo.
echo Step 6: Push to GitHub
echo Pushing code to GitHub, please wait...
git push -u origin main --force

echo.
echo Push completed!
echo Your code should now be on GitHub at:
echo https://github.com/Frederic-123-cell/MyApplication3
pause