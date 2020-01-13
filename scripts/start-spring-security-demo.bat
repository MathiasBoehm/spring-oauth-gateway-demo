@ECHO Off
SET CURRENT_DIR=%~dp0

rem start cmd /k Call "%CURRENT_DIR%coded-gateway-start.bat"
start cmd /k Call "%CURRENT_DIR%oauth-server-start.bat"
start cmd /k Call "%CURRENT_DIR%order-service-start.bat"
start cmd /k Call "%CURRENT_DIR%person-service-start.bat"
rem start cmd /k Call "%CURRENT_DIR%spring-gateway-spring-security-start.bat"

Echo Applications started