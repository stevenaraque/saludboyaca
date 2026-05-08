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
-- ESPECIALIDADES
-- ============================================
INSERT INTO especialidades (id, nombre, descripcion) VALUES
(1, 'Medicina General',  'Atencion primaria de salud'),
(2, 'Odontologia',       'Salud oral y dental'),
(3, 'Pediatria',         'Atencion medica infantil'),
(4, 'Ginecologia',       'Salud de la mujer'),
(5, 'Optometria',        'Salud visual');

-- ============================================
-- USUARIOS (personal clinico)
-- ============================================
INSERT INTO usuarios (id, nombres, apellidos, documento, email, username, password, rol, especialidad) VALUES
(1, 'Carlos Ernesto',  'Pedraza Rondon',  '1052345678', 'cpedraza@saludboyaca.gov.co', 'cpedraza', 'admin123',    'MEDICO',         'Medicina General'),
(2, 'Laura Patricia',  'Montoya Ospina',  '1052345690', 'lmontoya@saludboyaca.gov.co', 'lmontoya', 'medico123',   'MEDICO',         'Odontologia'),
(3, 'Ricardo Andres',  'Vargas Pinzon',   '1052345691', 'rvargas@saludboyaca.gov.co',  'rvargas',  'medico456',   'MEDICO',         'Pediatria'),
(4, 'Maria Eugenia',   'Suarez Cely',     '1052345679', 'msuarez@saludboyaca.gov.co',  'msuarez',  'enfermero1',  'ENFERMERO',      NULL),
(5, 'Jorge Hernando',  'Baez Morales',    '1052345680', 'jbaez@saludboyaca.gov.co',    'jbaez',    'recep123',    'RECEPCIONISTA',  NULL);

-- ============================================
-- PACIENTES
-- ============================================
INSERT INTO pacientes (id, nombres, apellidos, documento, fecha_nacimiento, telefono, email, eps, vereda_barrio) VALUES
(1,  'Ana Lucia',      'Gutierrez Paez',    '1052345681', '1985-03-15', '3123456789', 'agutierrez@gmail.com',  'Sanitas',    'Centro'),
(2,  'Luis Fernando',  'Rojas Casas',       '1052345682', '1990-07-22', '3109876543', 'lrojas@gmail.com',      'Compensar',  'El Rosal'),
(3,  'Diana Marcela',  'Arias Bernal',      '1052345683', '1978-11-08', '3154567890', 'darias@gmail.com',      'Nueva EPS',  'La Cumbre'),
(4,  'Pedro Jose',     'Martinez Vega',     '1052345684', '1995-01-30', '3187654321', 'pmartinez@gmail.com',   'Sura',       'San Jose'),
(5,  'Claudia Milena', 'Beltran Torres',    '1052345685', '1982-06-10', '3201234567', 'cbeltran@gmail.com',    'Sanitas',    'Villa del Lago'),
(6,  'Hector Fabian',  'Chacon Rincon',     '1052345686', '1975-09-25', '3214567890', 'hchacon@gmail.com',     'Compensar',  'El Bosque'),
(7,  'Sandra Liliana', 'Mora Cifuentes',    '1052345687', '1993-12-03', '3167890123', 'smora@gmail.com',       'Nueva EPS',  'Centro'),
(8,  'Andres Felipe',  'Castro Medina',     '1052345688', '1988-04-18', '3178901234', 'acastro@gmail.com',     'Sura',       'La Roca'),
(9,  'Yolanda Esperanza','Pulido Garzon',   '1052345689', '1970-08-30', '3190123456', 'ypulido@gmail.com',     'Sanitas',    'El Prado'),
(10, 'Juan Sebastian', 'Herrera Acosta',    '1052345692', '2000-02-14', '3112345678', 'jherrera@gmail.com',    'Compensar',  'San Cayetano');

-- ============================================
-- HORARIOS
-- ============================================
INSERT INTO horarios (id_medico, dia_semana, hora_inicio, hora_fin, max_citas) VALUES
-- Carlos Pedraza (Medicina General)
(1, 1, '08:00:00', '12:00:00', 8),
(1, 1, '14:00:00', '17:00:00', 6),
(1, 2, '08:00:00', '12:00:00', 8),
(1, 3, '08:00:00', '12:00:00', 8),
(1, 3, '14:00:00', '17:00:00', 6),
(1, 4, '08:00:00', '12:00:00', 8),
(1, 5, '08:00:00', '12:00:00', 8),
-- Laura Montoya (Odontologia)
(2, 1, '08:00:00', '12:00:00', 6),
(2, 2, '14:00:00', '17:00:00', 6),
(2, 4, '08:00:00', '12:00:00', 6),
-- Ricardo Vargas (Pediatria)
(3, 2, '08:00:00', '12:00:00', 8),
(3, 3, '14:00:00', '17:00:00', 6),
(3, 5, '08:00:00', '12:00:00', 8);

-- ============================================
-- CITAS (20 registros variados)
-- ============================================
INSERT INTO citas (id_paciente, id_medico, id_especialidad, fecha_cita, hora_cita, motivo, estado, id_registrado_por) VALUES
-- PROGRAMADAS (proximas)
(1,  1, 1, '2026-05-12', '08:00:00', 'Control tension arterial',         'PROGRAMADA',  5),
(2,  1, 1, '2026-05-12', '08:30:00', 'Dolor de cabeza frecuente',        'PROGRAMADA',  5),
(3,  2, 2, '2026-05-13', '09:00:00', 'Limpieza dental',                  'PROGRAMADA',  5),
(4,  3, 3, '2026-05-13', '10:00:00', 'Consulta pediatrica rutina',       'PROGRAMADA',  5),
(5,  1, 1, '2026-05-14', '08:00:00', 'Gripa y fiebre',                   'PROGRAMADA',  5),

-- CONFIRMADAS
(6,  2, 2, '2026-05-15', '14:00:00', 'Extraccion molar',                 'CONFIRMADA',  5),
(7,  3, 3, '2026-05-15', '09:00:00', 'Vacunacion infantil',              'CONFIRMADA',  5),
(8,  1, 1, '2026-05-16', '08:30:00', 'Revision post operatoria',         'CONFIRMADA',  5),
(9,  2, 2, '2026-05-19', '08:00:00', 'Ortodoncia seguimiento',           'CONFIRMADA',  5),
(10, 3, 3, '2026-05-20', '10:00:00', 'Control peso y talla',             'CONFIRMADA',  5),

-- ATENDIDAS (pasadas)
(1,  1, 1, '2026-04-10', '08:00:00', 'Chequeo general anual',            'ATENDIDA',    5),
(3,  2, 2, '2026-04-11', '09:00:00', 'Caries molar inferior',            'ATENDIDA',    5),
(5,  1, 1, '2026-04-15', '08:30:00', 'Infeccion urinaria',               'ATENDIDA',    5),
(7,  3, 3, '2026-04-18', '10:00:00', 'Fiebre alta pediatrica',           'ATENDIDA',    5),
(9,  1, 1, '2026-04-22', '08:00:00', 'Dolor lumbar cronico',             'ATENDIDA',    5),

-- CANCELADAS
(2,  2, 2, '2026-04-25', '14:00:00', 'Blanqueamiento dental',            'CANCELADA',   5),
(4,  1, 1, '2026-04-28', '08:30:00', 'Revision dermatologica',           'CANCELADA',   5),
(6,  3, 3, '2026-05-02', '09:00:00', 'Control mensual pediatria',        'CANCELADA',   5),
(8,  1, 1, '2026-05-05', '08:00:00', 'Examen sangre resultados',         'CANCELADA',   5),
(10, 2, 2, '2026-05-07', '14:00:00', 'Protesis dental consulta',         'CANCELADA',   5);


UPDATE usuarios SET email = 'stevenalejandroaraquecastro@gmail.com'               WHERE id = 1; -- Carlos (MEDICO)
UPDATE usuarios SET email = 'stevenalejandroaraquecastro+medico2@gmail.com'        WHERE id = 2; -- Laura (MEDICO)
UPDATE usuarios SET email = 'stevenalejandroaraquecastro+medico3@gmail.com'        WHERE id = 3; -- Ricardo (MEDICO)
UPDATE usuarios SET email = 'stevenalejandroaraquecastro+enfermero@gmail.com'      WHERE id = 4; -- Maria Eugenia (ENFERMERO)
UPDATE usuarios SET email = 'stevenalejandroaraquecastro+recepcion@gmail.com'      WHERE id = 5; -- Jorge (RECEPCIONISTA)