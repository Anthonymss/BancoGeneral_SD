@echo off

set "paises[usa]=America/New_York"
set "paises[uk]=Europe/London"
set "paises[japan]=Asia/Tokyo"
set "paises[australia]=Australia/Sydney"
set "paises[uae]=Asia/Dubai"
set "paises[india]=Asia/Kolkata"
set "paises[mexico]=America/Mexico_City"
set "paises[brazil]=America/Sao_Paulo"
set "paises[argentina]=America/Argentina/Buenos_Aires"
set "paises[canada]=America/Toronto"
set "paises[chile]=America/Santiago"
set "paises[colombia]=America/Bogota"
set "paises[peru]=America/Lima"
set "paises[spain]=Europe/Madrid"
set "paises[germany]=Europe/Berlin"
set "paises[france]=Europe/Paris"
set "paises[italy]=Europe/Rome"
set "paises[russia]=Europe/Moscow"
set "paises[china]=Asia/Shanghai"
set "paises[south_africa]=Africa/Johannesburg"
set "paises[nigeria]=Africa/Lagos"
set "paises[egypt]=Africa/Cairo"
set "paises[indonesia]=Asia/Jakarta"
set "paises[south_korea]=Asia/Seoul"
set "paises[saudi_arabia]=Asia/Riyadh"
set "paises[thailand]=Asia/Bangkok"
set "paises[singapore]=Asia/Singapore"
set "paises[new_zealand]=Pacific/Auckland"
set "paises[norway]=Europe/Oslo"
set "paises[sweden]=Europe/Stockholm"
set "paises[switzerland]=Europe/Zurich"
set "paises[netherlands]=Europe/Amsterdam"
set "paises[belgium]=Europe/Brussels"
set "paises[austria]=Europe/Vienna"
set "paises[portugal]=Europe/Lisbon"
set "paises[poland]=Europe/Warsaw"
set "paises[turkey]=Europe/Istanbul"
set "paises[greece]=Europe/Athens"
set "paises[israel]=Asia/Jerusalem"
set "paises[malaysia]=Asia/Kuala_Lumpur"
set "paises[philippines]=Asia/Manila"


echo.
echo ==========================================
echo     Configuracion del Proyecto Spring
echo ==========================================
echo.

set /p port="Ingrese el puerto del servidor (ej. 8081): "
set /p appName="Ingrese el nombre de la aplicacion Spring: "
set /p dbName="Ingrese el nombre de la base de datos MySQL: "
set /p bancoName="Ingrese el nombre del banco: "
set "bancoPais="
set "zonaHoraria="
:pedirPais
echo.
echo Paises disponibles:
echo ----------------------------------------
for /F "tokens=2 delims=[]=" %%a in ('set paises[') do (
    for /F "tokens=1,2 delims==" %%b in ("%%a=%paises[%%a]%") do (
        echo %%b   %%c
    )
)
echo ----------------------------------------

REM Pedir al usuario que ingrese el pais del banco
set /p bancoPais="Ingrese el PAIS del banco: "

REM Obtener la zona horaria correspondiente
for /F "tokens=2 delims==" %%a in ('set paises[%bancoPais%]') do set "zonaHoraria=%%a"

echo.

if not defined zonaHoraria (
    echo.
    echo El pais ingresado no es valido. Por favor, elija uno de la lista anterior.
    echo.
    goto pedirPais
)

set newFolderName=%appName%

xcopy /E /I /Y .\bank1 .\%newFolderName%

cd .\%newFolderName%

REM Actualizar el archivo application.properties en el nuevo proyecto
echo server.port=%port%> .\src\main\resources\application.properties
echo spring.application.name=%appName%>> .\src\main\resources\application.properties
echo spring.datasource.url=jdbc:mysql://localhost:3306/%dbName%>> .\src\main\resources\application.properties
echo eureka.client.service-url.defaultZone=https://eureka-server-45rg.onrender.com/eureka/>> .\src\main\resources\application.properties
echo eureka.client.register-with-eureka=true>> .\src\main\resources\application.properties
echo eureka.client.fetch-registry=true>> .\src\main\resources\application.properties
echo spring.datasource.username=root>> .\src\main\resources\application.properties
echo spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect>> .\src\main\resources\application.properties
echo spring.datasource.password=>> .\src\main\resources\application.properties
echo spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver>> .\src\main\resources\application.properties
echo spring.jpa.hibernate.ddl-auto=create>> .\src\main\resources\application.properties
echo spring.jpa.properties.hibernate.jdbc.time_zone=%zonaHoraria%>> .\src\main\resources\application.properties

echo --tabla Banco > .\src\main\resources\import.sql
echo INSERT INTO banco (id, nombre, pais) VALUES (1, '%bancoName%', '%bancoPais%'); >> .\src\main\resources\import.sql

echo.
echo Configuracion completada. Se genero su nuevo banco %appName%...
pause
