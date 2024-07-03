--tabla Banco
INSERT INTO banco (id, nombre, pais) VALUES (1, 'Banco BCP', 'Peru');

--tabla Cliente
INSERT INTO cliente (dni, nombre,apellido, email, telefono)VALUES ("733912311", 'Juan','Pérez', 'juan.perez@correo.com', '123456789');
INSERT INTO cliente (dni, nombre,apellido, email, telefono)VALUES ("733912312", 'Ana', 'Gómez','ana.gomez@correo.com', '234567890');
INSERT INTO cliente (dni, nombre,apellido, email, telefono)VALUES ("733912313", 'Carlos','López', 'carlos.lopez@correo.com', '345678901');
INSERT INTO cliente (dni, nombre,apellido, email, telefono)VALUES ("733912314", 'María','Fernández', 'maria.fernandez@correo.com', '456789012');
INSERT INTO cliente (dni, nombre,apellido, email, telefono)VALUES ("733912315", 'Pedro','Sánchez', 'pedro.sanchez@correo.com', '567890123');
INSERT INTO cliente (dni, nombre,apellido, email, telefono)VALUES ("733912316", 'Laura','Martínez', 'laura.martinez@correo.com', '678901234');

--tabla Cuenta
-- Creación de cuentas para clientes
INSERT INTO cuenta (numero_cuenta, saldo, password, cliente_id, banco_id) VALUES('10001111AAA', 1000.0, 'password1', 1, 1); -- Cuenta para Juan Pérez en Banco 1
INSERT INTO cuenta (numero_cuenta, saldo, password, cliente_id, banco_id) VALUES('20002222BBB', 1500.0, 'password2', 2, 1); -- Cuenta para Ana Gómez en Banco 1
INSERT INTO cuenta (numero_cuenta, saldo, password, cliente_id, banco_id) VALUES('30003333CCC', 800.0, 'password3', 3, 1);-- Cuenta para Carlos López en Banco 1
INSERT INTO cuenta (numero_cuenta, saldo, password, cliente_id, banco_id) VALUES('40004444DDD', 2000.0, 'password4', 4, 1); -- Cuenta para María Fernández en Banco 1
INSERT INTO cuenta (numero_cuenta, saldo, password, cliente_id, banco_id) VALUES('50005555EEE', 500.0, 'password5', 5, 1); -- Cuenta para Pedro Sánchez en Banco 1
INSERT INTO cuenta (numero_cuenta, saldo, password, cliente_id, banco_id) VALUES('60006666FFF', 1200.0, 'password6', 6, 1); -- Cuenta para Laura Martínez en Banco 1


--tabla Transaccion

