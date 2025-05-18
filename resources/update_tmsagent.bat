@echo off
:: -----------------------------
:: Check for Admin Privileges
:: -----------------------------
>nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"
if '%errorlevel%' NEQ '0' (
    echo [INFO] Requesting administrative privileges...
    powershell -Command "Start-Process '%~f0' -Verb RunAs"
    exit /b
)

setlocal

echo [INFO] Running with administrator privileges...

:: -----------------------------
:: Define paths and constants
:: -----------------------------
set TARGET_DIR=C:\
set FILE_NAME=TMSAgent.jar
set DOWNLOAD_URL=https://github.com/mehedi8gb/TMSAgent/releases/latest/download/TMSAgent.jar
set TEMP_FILE=%TEMP%\%FILE_NAME%

:: -----------------------------
:: Download latest JAR
:: -----------------------------
echo [INFO] Downloading latest TMSAgent.jar...
curl -L -o "%TEMP_FILE%" "%DOWNLOAD_URL%"
IF %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Failed to download the file.
    exit /b 1
)

:: -----------------------------
:: Replace the existing JAR
:: -----------------------------
echo [INFO] Replacing old file at %TARGET_DIR%%FILE_NAME%
copy /Y "%TEMP_FILE%" "%TARGET_DIR%%FILE_NAME%"
IF %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Failed to replace the file.
    exit /b 2
)

echo [SUCCESS] TMSAgent.jar has been updated successfully.
exit /b 0
