@echo off
echo 🏢 Starting Employee Database Management System...
echo.

REM Check if compiled classes exist
if not exist "target\classes\com\employee\EmployeeApp.class" (
    echo ❌ Application not compiled yet!
    echo 📋 Please run build.bat first to compile the application.
    pause
    exit /b 1
)

REM Check if MySQL connector exists
if not exist "lib\mysql-connector-j-8.0.33.jar" (
    echo ❌ MySQL connector not found!
    echo 📋 Please run build.bat first to download dependencies.
    pause
    exit /b 1
)

REM Check if .env file exists
if not exist ".env" (
    echo ⚠️  .env file not found!
    echo 📋 Please copy .env.example to .env and update with your database credentials.
    echo.
    echo 🔧 Quick setup:
    echo    1. copy .env.example .env
    echo    2. Edit .env file with your MySQL password
    echo    3. Run this script again
    pause
    exit /b 1
)

echo 🚀 Running application...
echo.
java -cp "target\classes;lib\mysql-connector-j-8.0.33.jar" com.employee.EmployeeApp

echo.
echo 👋 Application finished.
pause