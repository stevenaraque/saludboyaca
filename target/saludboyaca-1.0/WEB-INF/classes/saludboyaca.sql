-- ============================================
-- TABLA 1: usuarios (personal clinico)
-- ============================================
CREATE TABLE usuarios (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nombres         VARCHAR(80)  NOT NULL,
    apellidos       VARCHAR(80)  NOT NULL,
    documento       VARCHAR(20)  NOT NULL UNIQUE,
    email           VARCHAR(100) NOT NULL UNIQUE,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    rol             ENUM('MEDICO','RECEPCIONISTA','ENFERMERO') NOT NULL,
    especialidad    VARCHAR(80),
    lang_preferido  VARCHAR(5)   DEFAULT 'es',
    activo          TINYINT(1)   DEFAULT 1
);

-- ============================================
-- TABLA 2: pacientes
-- ============================================
CREATE TABLE pacientes (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    nombres          VARCHAR(80)  NOT NULL,
    apellidos        VARCHAR(80)  NOT NULL,
    documento        VARCHAR(20)  NOT NULL UNIQUE,
    fecha_nacimiento DATE         NOT NULL,
    telefono         VARCHAR(20),
    email            VARCHAR(100),
    eps              VARCHAR(80)  NOT NULL,
    vereda_barrio    VARCHAR(80)
);

-- ============================================
-- TABLA 3: especialidades
-- ============================================
CREATE TABLE especialidades (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(80)  NOT NULL,
    descripcion VARCHAR(200)
);

-- ============================================
-- TABLA 4: horarios (disponibilidad por medico)
-- ============================================
CREATE TABLE horarios (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    id_medico   INT NOT NULL,
    dia_semana  TINYINT NOT NULL COMMENT '1=Lun 2=Mar 3=Mie 4=Jue 5=Vie',
    hora_inicio TIME NOT NULL,
    hora_fin    TIME NOT NULL,
    max_citas   INT DEFAULT 10,
    FOREIGN KEY (id_medico) REFERENCES usuarios(id)
);

-- ============================================
-- TABLA 5: citas (entidad central)
-- ============================================
CREATE TABLE citas (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente      INT NOT NULL,
    id_medico        INT NOT NULL,
    id_especialidad  INT NOT NULL,
    fecha_cita       DATE NOT NULL,
    hora_cita        TIME NOT NULL,
    motivo           VARCHAR(300),
    estado           ENUM('PROGRAMADA','CONFIRMADA','ATENDIDA','CANCELADA') DEFAULT 'PROGRAMADA',
    observaciones    VARCHAR(500),
    fecha_registro   DATETIME DEFAULT CURRENT_TIMESTAMP,
    id_registrado_por INT,
    FOREIGN KEY (id_paciente)     REFERENCES pacientes(id),
    FOREIGN KEY (id_medico)       REFERENCES usuarios(id),
    FOREIGN KEY (id_especialidad) REFERENCES especialidades(id)
);

-- ============================================
-- TABLA 6: otp_tokens (seguridad)
-- ============================================
CREATE TABLE otp_tokens (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario  INT NOT NULL,
    codigo      VARCHAR(6)   NOT NULL,
    fecha_gen   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    expira_en   DATETIME     NOT NULL,
    usado       TINYINT(1)   DEFAULT 0,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- ============================================
-- TABLA 7: log_accesos (auditoria)
-- ============================================
CREATE TABLE log_accesos (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario  INT,
    username    VARCHAR(50),
    accion      VARCHAR(50)  NOT NULL,
    ip          VARCHAR(45),
    resultado   ENUM('EXITO','FALLO') NOT NULL,
    fecha       DATETIME     DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- DATOS DE PRUEBA REALES DE PAIPA, BOYACA
-- ============================================

-- Especialidades medicas
INSERT INTO especialidades (nombre, descripcion) VALUES
('Medicina General', 'Atencion primaria de salud'),
('Odontologia', 'Salud oral y dental'),
('Pediatria', 'Atencion medica infantil'),
('Ginecologia', 'Salud de la mujer'),
('Optometria', 'Salud visual');

-- Usuarios del sistema (personal clinico)
INSERT INTO usuarios (nombres, apellidos, documento, email, username, password, rol, especialidad) VALUES
('Carlos Ernesto', 'Pedraza Rondon', '1052345678', 'cpedraza@saludboyaca.gov.co', 'cpedraza', 'admin123', 'MEDICO', 'Medicina General'),
('Maria Eugenia', 'Suarez Cely', '1052345679', 'msuarez@saludboyaca.gov.co', 'msuarez', 'enfermero1', 'ENFERMERO', NULL),
('Jorge Hernando', 'Baez Morales', '1052345680', 'jbaez@saludboyaca.gov.co', 'jbaez', 'recep123', 'RECEPCIONISTA', NULL);

-- Pacientes de Paipa
INSERT INTO pacientes (nombres, apellidos, documento, fecha_nacimiento, telefono, email, eps, vereda_barrio) VALUES
('Ana Lucia', 'Gutierrez Paez', '1052345681', '1985-03-15', '3123456789', 'agutierrez@gmail.com', 'Sanitas', 'Centro'),
('Luis Fernando', 'Rojas Casas', '1052345682', '1990-07-22', '3109876543', 'lrojas@gmail.com', 'Compensar', 'El Rosal'),
('Diana Marcela', 'Arias Bernal', '1052345683', '1978-11-08', '3154567890', 'darias@gmail.com', 'Nueva EPS', 'La Cumbre'),
('Pedro Jose', 'Martinez Vega', '1052345684', '1995-01-30', '3187654321', 'pmartinez@gmail.com', 'Sura', 'San Jose');

-- Horarios del medico Carlos Pedraza
INSERT INTO horarios (id_medico, dia_semana, hora_inicio, hora_fin, max_citas) VALUES
(1, 1, '08:00:00', '12:00:00', 8),
(1, 1, '14:00:00', '17:00:00', 6),
(1, 2, '08:00:00', '12:00:00', 8),
(1, 2, '14:00:00', '17:00:00', 6),
(1, 3, '08:00:00', '12:00:00', 8),
(1, 3, '14:00:00', '17:00:00', 6),
(1, 4, '08:00:00', '12:00:00', 8),
(1, 4, '14:00:00', '17:00:00', 6),
(1, 5, '08:00:00', '12:00:00', 8),
(1, 5, '14:00:00', '16:00:00', 4);