@echo off
echo 🔨 Building Employee Database Application...
echo.

echo 📁 Creating directories...
mkdir lib 2>nul
mkdir target\classes 2>nul

echo 📦 Downloading MySQL Connector/J...
powershell -Command "if (-not (Test-Path 'lib\mysql-connector-j-8.0.33.jar')) { Write-Host 'Downloading MySQL connector...'; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar' -OutFile 'lib\mysql-connector-j-8.0.33.jar' }"

echo ⚙️ Compiling Java classes...
javac -cp "lib\mysql-connector-j-8.0.33.jar" -d "target\classes" src\main\java\com\employee\*.java

if %errorlevel% neq 0 (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

echo 📋 Copying resources...
xcopy src\main\resources target\classes\ /E /Y /Q 2>nul

echo ✅ Build completed successfully!
echo.
echo 🚀 To run the application:
echo    java -cp "target\classes;lib\mysql-connector-j-8.0.33.jar" com.employee.EmployeeApp
echo.
echo 📝 Make sure to:
echo    1. Update your .env file with database credentials
echo    2. Ensure MySQL server is running
echo    3. Run database\setup.sql to create the database
echo.
pause