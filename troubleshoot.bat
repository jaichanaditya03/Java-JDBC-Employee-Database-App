@echo off
echo 🔍 MySQL Connection Troubleshooter
echo ====================================
echo.
echo This will help you find the correct MySQL password.
echo.
echo 🧪 Testing common MySQL password combinations:
echo.

echo 📋 Please test these in MySQL Workbench:
echo.
echo 1. Try these common passwords:
echo    - Empty password (just press Enter)
echo    - root
echo    - admin  
echo    - password
echo    - Your Windows login password
echo.
echo 2. In MySQL Workbench:
echo    - Click on "Local instance MySQL80"
echo    - Try the passwords above
echo    - If successful, remember which one worked!
echo.
echo 3. Update your .env file:
echo    - Open .env file in VS Code
echo    - Change DB_PASSWORD=your_mysql_password_here
echo    - To DB_PASSWORD=the_working_password
echo.
echo 4. If nothing works, reset MySQL password:
echo    - Open MySQL Workbench as Administrator
echo    - Go to Server → Users and Privileges  
echo    - Select 'root' user and set new password
echo.
pause