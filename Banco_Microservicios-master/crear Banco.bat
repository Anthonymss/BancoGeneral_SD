@echo off
echo.
echo ==========================================
echo     Configuracion del Proyecto Spring
echo ==========================================
echo.

set /p port="Ingrese el puerto del servidor (ej. 8081): "
set /p appName="Ingrese el nombre de la aplicacion Spring: "
set /p dbName="Ingrese el nombre de la base de datos MySQL: "
set /p bancoName="Ingrese el nombre del banco(sin espacios): "
set /p bancoPais="Ingrese el PAIS del banco para (sin espacios): "

set newFolderName=%appName%

xcopy /E /I /Y .\bank1 .\%newFolderName%

cd .\%newFolderName%

REM Actualizar el archivo application.properties en el nuevo proyecto
echo server.port=%port%> .\src\main\resources\application.properties
echo spring.application.name=%appName%>> .\src\main\resources\application.properties
echo spring.datasource.url=jdbc:mysql://localhost:3306/%dbName%>> .\src\main\resources\application.properties
echo eureka.client.service-url.defaultZone=http://localhost:8761/eureka/>> .\src\main\resources\application.properties
echo eureka.client.register-with-eureka=true>> .\src\main\resources\application.properties
echo eureka.client.fetch-registry=true>> .\src\main\resources\application.properties
echo spring.datasource.username=root>> .\src\main\resources\application.properties
echo spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect>> .\src\main\resources\application.properties
echo spring.datasource.password=>> .\src\main\resources\application.properties
echo spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver>> .\src\main\resources\application.properties
echo spring.jpa.hibernate.ddl-auto=create>> .\src\main\resources\application.properties


echo --tabla Banco > .\src\main\resources\import.sql
echo INSERT INTO banco (id, nombre, pais) VALUES (1, '%bancoName%', '%bancoPais%'); >> .\src\main\resources\import.sql

echo --tabla Cliente >> .\src\main\resources\import.sql
echo INSERT INTO cliente (dni, nombre,apellido, email, telefono)VALUES ("733912311", 'Juan','Pérez', 'juan.perez@correo.com', '123456789'); >> .\src\main\resources\import.sql
echo INSERT INTO cliente (dni, nombre,apellido, email, telefono)VALUES ("733912312", 'Ana', 'Gómez','ana.gomez@correo.com', '234567890'); >> .\src\main\resources\import.sql
echo INSERT INTO cliente (dni, nombre,apellido, email, telefono)VALUES ("733912313", 'Carlos','López', 'carlos.lopez@correo.com', '345678901'); >> .\src\main\resources\import.sql
echo INSERT INTO cliente (dni, nombre,apellido, email, telefono)VALUES ("733912314", 'María','Fernández', 'maria.fernandez@correo.com', '456789012'); >> .\src\main\resources\import.sql
echo INSERT INTO cliente (dni, nombre,apellido, email, telefono)VALUES ("733912315", 'Pedro','Sánchez', 'pedro.sanchez@correo.com', '567890123'); >> .\src\main\resources\import.sql
echo INSERT INTO cliente (dni, nombre,apellido, email, telefono)VALUES ("733912316", 'Laura','Martínez', 'laura.martinez@correo.com', '678901234'); >> .\src\main\resources\import.sql

echo --tabla Cuenta >> .\src\main\resources\import.sql
echo INSERT INTO cuenta (numero_cuenta, saldo, password, cliente_id, banco_id) VALUES('10001111AAA', 1000.0, 'password1', 1, 1); >> .\src\main\resources\import.sql
echo INSERT INTO cuenta (numero_cuenta, saldo, password, cliente_id, banco_id) VALUES('20002222BBB', 1500.0, 'password2', 2, 1); >> .\src\main\resources\import.sql
echo INSERT INTO cuenta (numero_cuenta, saldo, password, cliente_id, banco_id) VALUES('30003333CCC', 800.0, 'password3', 3, 1); >> .\src\main\resources\import.sql
echo INSERT INTO cuenta (numero_cuenta, saldo, password, cliente_id, banco_id) VALUES('40004444DDD', 2000.0, 'password4', 4, 1); >> .\src\main\resources\import.sql
echo INSERT INTO cuenta (numero_cuenta, saldo, password, cliente_id, banco_id) VALUES('50005555EEE', 500.0, 'password5', 5, 1); >> .\src\main\resources\import.sql
echo INSERT INTO cuenta (numero_cuenta, saldo, password, cliente_id, banco_id) VALUES('60006666FFF', 1200.0, 'password6', 6, 1); >> .\src\main\resources\import.sql


echo Configuración completada. Se genero su nuevo banco %appName%...
pause