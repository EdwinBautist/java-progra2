-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 14-06-2025 a las 10:51:03
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `Papeleria`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Categoria`
--

CREATE TABLE `Categoria` (
  `id_categoria` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Categoria`
--

INSERT INTO `Categoria` (`id_categoria`, `nombre`) VALUES
(1, 'Material Escolar'),
(2, 'Oficina'),
(3, 'Arte y Dibujo'),
(4, 'Escritura'),
(5, 'Papelería'),
(6, 'Organización'),
(7, 'Impresión'),
(8, 'Manualidades'),
(9, 'Regalería'),
(10, 'Mochilas');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Cliente`
--

CREATE TABLE `Cliente` (
  `id_cliente` int(11) NOT NULL,
  `nombre` varchar(128) NOT NULL,
  `telefono` varchar(10) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Cliente`
--

INSERT INTO `Cliente` (`id_cliente`, `nombre`, `telefono`, `email`) VALUES
(1, 'Colegio San José', '6012345678', 'contacto@colegiosanjose.edu'),
(2, 'Universidad Central', '6018765432', 'compras@universidadcentral.edu'),
(3, 'Oficinas Acme', '6015551234', 'logistica@acme.com'),
(4, 'Ana Beltrán', '3109876543', 'ana.beltran@gmail.com'),
(5, 'Carlos Mendoza', '3151234567', 'carlos.mendoza@hotmail.com'),
(6, 'Escuela Primaria ABC', '6013334444', 'administracion@escuelaabc.edu'),
(7, 'Diseño Creativo SAS', '6017778888', 'compras@disenocreativo.com'),
(8, 'Lucía Fernández', '3209998888', 'lucia.fernandez@yahoo.com'),
(9, 'Roberto Guzmán', '3176665555', 'roberto.guzman@outlook.com'),
(10, 'Instituto Técnico XYZ', '6012223333', 'insumos@institutotecnicoxyz.edu');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Compra`
--

CREATE TABLE `Compra` (
  `id_compra` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `id_proveedor` int(11) NOT NULL,
  `id_empleado` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Compra`
--

INSERT INTO `Compra` (`id_compra`, `fecha`, `total`, `id_proveedor`, `id_empleado`) VALUES
(1, '2023-05-10', 250000.00, 1, 2),
(2, '2023-05-15', 180000.00, 3, 2),
(3, '2023-05-20', 320000.00, 5, 3),
(4, '2023-06-01', 150000.00, 2, 3),
(5, '2023-06-05', 275000.00, 4, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Empleado`
--

CREATE TABLE `Empleado` (
  `id_empleado` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `nombre` varchar(128) NOT NULL,
  `cargo` varchar(50) DEFAULT NULL,
  `telefono` varchar(10) DEFAULT NULL,
  `direccion` varchar(150) DEFAULT NULL,
  `contrasena` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Empleado`
--

INSERT INTO `Empleado` (`id_empleado`, `email`, `nombre`, `cargo`, `telefono`, `direccion`, `contrasena`) VALUES
(1, 'juan@papeleria.com', 'Juan Pérez', 'Vendedor', '3111111111', 'Calle 123 #45-67', 'hashed_password_1'),
(2, 'maria@papeleria.com', 'María Gómez', 'Gerente', '3222222222', 'Av 8 #12-34', 'hashed_password_2'),
(3, 'carlos@papeleria.com', 'Carlos López', 'Almacenista', '3333333333', 'Carrera 56 #78-90', 'hashed_password_3'),
(4, 'laura@papeleria.com', 'Laura Martínez', 'Cajera', '3444444444', 'Diagonal 34 #56-12', 'hashed_password_4'),
(5, 'pedro@papeleria.com', 'Pedro Rodríguez', 'Vendedor', '3555555555', 'Transversal 78 #90-12', 'hashed_password_5');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Lote`
--

CREATE TABLE `Lote` (
  `id_lote` int(11) NOT NULL,
  `fecha_entrada` date NOT NULL,
  `fecha_salida` date DEFAULT NULL,
  `precio_uni` decimal(10,2) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `id_producto` int(11) DEFAULT NULL,
  `id_proveedor` int(11) DEFAULT NULL,
  `id_compra` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Lote`
--

INSERT INTO `Lote` (`id_lote`, `fecha_entrada`, `fecha_salida`, `precio_uni`, `cantidad`, `id_producto`, `id_proveedor`, `id_compra`) VALUES
(1, '2023-05-11', NULL, 6500.00, 50, 1, 1, 1),
(2, '2023-05-11', NULL, 9500.00, 30, 2, 1, 1),
(3, '2023-05-16', NULL, 2500.00, 40, 3, 3, 2),
(4, '2023-05-16', NULL, 800.00, 60, 5, 3, 2),
(5, '2023-05-21', NULL, 5000.00, 25, 4, 5, 3),
(6, '2023-05-21', NULL, 15000.00, 15, 6, 5, 3),
(7, '2023-06-02', NULL, 7000.00, 20, 7, 2, 4),
(8, '2023-06-06', NULL, 18000.00, 10, 8, 4, 5),
(9, '2023-06-06', NULL, 9000.00, 35, 9, 4, 5),
(10, '2023-06-06', NULL, 75000.00, 5, 10, 4, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Marca`
--

CREATE TABLE `Marca` (
  `id_marca` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Marca`
--

INSERT INTO `Marca` (`id_marca`, `nombre`) VALUES
(1, 'Bic'),
(2, 'Faber-Castell'),
(3, 'Norma'),
(4, 'Artline'),
(5, 'Pelikan'),
(6, 'Scribe'),
(7, 'Maped'),
(8, 'Staedtler'),
(9, 'Pritt'),
(10, 'Totto');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Producto`
--

CREATE TABLE `Producto` (
  `id_producto` int(11) NOT NULL,
  `nombre` varchar(128) NOT NULL,
  `precio_venta` decimal(10,2) NOT NULL,
  `id_categoria` int(11) DEFAULT NULL,
  `id_marca` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Producto`
--

INSERT INTO `Producto` (`id_producto`, `nombre`, `precio_venta`, `id_categoria`, `id_marca`) VALUES
(1, 'Lápiz grafito HB x12', 8500.00, 1, 1),
(2, 'Cuaderno cuadriculado 100h', 12500.00, 5, 6),
(3, 'Resaltador amarillo', 3500.00, 4, 4),
(4, 'Tijeras escolares', 6500.00, 1, 7),
(5, 'Goma de borrar blanca', 1200.00, 1, 2),
(6, 'Marcadores permanentes x6', 18500.00, 3, 4),
(7, 'Carpeta con gancho', 8500.00, 6, 6),
(8, 'Block de hojas blancas 100h', 22000.00, 5, 3),
(9, 'Bolígrafo azul x10', 12000.00, 4, 1),
(10, 'Mochila escolar', 89900.00, 10, 10);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Producto_Compra`
--

CREATE TABLE `Producto_Compra` (
  `id_compra` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Producto_Compra`
--

INSERT INTO `Producto_Compra` (`id_compra`, `id_producto`, `cantidad`, `precio_unitario`) VALUES
(1, 1, 50, 6500.00),
(1, 2, 30, 9500.00),
(2, 3, 40, 2500.00),
(2, 5, 60, 800.00),
(3, 4, 25, 5000.00),
(3, 6, 15, 15000.00),
(4, 7, 20, 7000.00),
(5, 8, 10, 18000.00),
(5, 9, 35, 9000.00),
(5, 10, 5, 75000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Producto_Venta`
--

CREATE TABLE `Producto_Venta` (
  `id_venta` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Producto_Venta`
--

INSERT INTO `Producto_Venta` (`id_venta`, `id_producto`, `cantidad`, `precio_unitario`) VALUES
(1, 1, 10, 8500.00),
(1, 2, 2, 12500.00),
(2, 3, 5, 3500.00),
(2, 5, 10, 1200.00),
(3, 6, 1, 18500.00),
(4, 4, 3, 6500.00),
(4, 7, 2, 8500.00),
(4, 9, 5, 12000.00),
(5, 2, 2, 12500.00),
(5, 8, 4, 22000.00),
(6, 1, 5, 8500.00),
(6, 5, 2, 1200.00),
(7, 10, 1, 89900.00),
(8, 3, 4, 3500.00),
(8, 7, 3, 8500.00),
(9, 2, 3, 12500.00),
(9, 9, 2, 12000.00),
(10, 1, 8, 8500.00),
(10, 4, 2, 6500.00),
(10, 6, 2, 18500.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Proveedor`
--

CREATE TABLE `Proveedor` (
  `id_proveedor` int(11) NOT NULL,
  `nombre` varchar(128) NOT NULL,
  `telefono` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Proveedor`
--

INSERT INTO `Proveedor` (`id_proveedor`, `nombre`, `telefono`) VALUES
(1, 'Distribuidora Papelera SA', '3101234567'),
(2, 'Suministros Escolares Ltda', '3152345678'),
(3, 'Arte y Diseño SAS', '3203456789'),
(4, 'Oficentro Colombia', '3014567890'),
(5, 'Papelería El Lápiz', '3175678901'),
(6, 'Distribel', '3126789012'),
(7, 'Librería Nacional', '3187890123'),
(8, 'Mundo Papelero', '3148901234'),
(9, 'Suministros Creativos', '3199012345'),
(10, 'Distriartes', '3000123456');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Venta`
--

CREATE TABLE `Venta` (
  `id_venta` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `precio_total` decimal(10,2) NOT NULL,
  `id_cliente` int(11) DEFAULT NULL,
  `id_empleado` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Venta`
--

INSERT INTO `Venta` (`id_venta`, `fecha`, `precio_total`, `id_cliente`, `id_empleado`) VALUES
(1, '2023-05-12', 45000.00, 1, 1),
(2, '2023-05-14', 32000.00, 3, 4),
(3, '2023-05-18', 18500.00, 2, 1),
(4, '2023-05-22', 65000.00, 4, 4),
(5, '2023-06-03', 112000.00, 6, 5),
(6, '2023-06-04', 24000.00, 5, 1),
(7, '2023-06-07', 89900.00, 8, 5),
(8, '2023-06-08', 37000.00, 7, 4),
(9, '2023-06-09', 55000.00, 9, 1),
(10, '2023-06-10', 125000.00, 10, 5);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `Categoria`
--
ALTER TABLE `Categoria`
  ADD PRIMARY KEY (`id_categoria`);

--
-- Indices de la tabla `Cliente`
--
ALTER TABLE `Cliente`
  ADD PRIMARY KEY (`id_cliente`);

--
-- Indices de la tabla `Compra`
--
ALTER TABLE `Compra`
  ADD PRIMARY KEY (`id_compra`),
  ADD KEY `id_proveedor` (`id_proveedor`),
  ADD KEY `id_empleado` (`id_empleado`);

--
-- Indices de la tabla `Empleado`
--
ALTER TABLE `Empleado`
  ADD PRIMARY KEY (`id_empleado`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indices de la tabla `Lote`
--
ALTER TABLE `Lote`
  ADD PRIMARY KEY (`id_lote`),
  ADD KEY `id_producto` (`id_producto`),
  ADD KEY `id_proveedor` (`id_proveedor`),
  ADD KEY `id_compra` (`id_compra`);

--
-- Indices de la tabla `Marca`
--
ALTER TABLE `Marca`
  ADD PRIMARY KEY (`id_marca`);

--
-- Indices de la tabla `Producto`
--
ALTER TABLE `Producto`
  ADD PRIMARY KEY (`id_producto`),
  ADD KEY `id_categoria` (`id_categoria`),
  ADD KEY `id_marca` (`id_marca`);

--
-- Indices de la tabla `Producto_Compra`
--
ALTER TABLE `Producto_Compra`
  ADD PRIMARY KEY (`id_compra`,`id_producto`),
  ADD KEY `id_producto` (`id_producto`);

--
-- Indices de la tabla `Producto_Venta`
--
ALTER TABLE `Producto_Venta`
  ADD PRIMARY KEY (`id_venta`,`id_producto`),
  ADD KEY `id_producto` (`id_producto`);

--
-- Indices de la tabla `Proveedor`
--
ALTER TABLE `Proveedor`
  ADD PRIMARY KEY (`id_proveedor`);

--
-- Indices de la tabla `Venta`
--
ALTER TABLE `Venta`
  ADD PRIMARY KEY (`id_venta`),
  ADD KEY `id_cliente` (`id_cliente`),
  ADD KEY `id_empleado` (`id_empleado`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `Categoria`
--
ALTER TABLE `Categoria`
  MODIFY `id_categoria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `Cliente`
--
ALTER TABLE `Cliente`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `Compra`
--
ALTER TABLE `Compra`
  MODIFY `id_compra` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `Empleado`
--
ALTER TABLE `Empleado`
  MODIFY `id_empleado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `Lote`
--
ALTER TABLE `Lote`
  MODIFY `id_lote` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `Marca`
--
ALTER TABLE `Marca`
  MODIFY `id_marca` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `Producto`
--
ALTER TABLE `Producto`
  MODIFY `id_producto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `Proveedor`
--
ALTER TABLE `Proveedor`
  MODIFY `id_proveedor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `Venta`
--
ALTER TABLE `Venta`
  MODIFY `id_venta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `Compra`
--
ALTER TABLE `Compra`
  ADD CONSTRAINT `Compra_ibfk_1` FOREIGN KEY (`id_proveedor`) REFERENCES `Proveedor` (`id_proveedor`),
  ADD CONSTRAINT `Compra_ibfk_2` FOREIGN KEY (`id_empleado`) REFERENCES `Empleado` (`id_empleado`);

--
-- Filtros para la tabla `Lote`
--
ALTER TABLE `Lote`
  ADD CONSTRAINT `Lote_ibfk_1` FOREIGN KEY (`id_producto`) REFERENCES `Producto` (`id_producto`),
  ADD CONSTRAINT `Lote_ibfk_2` FOREIGN KEY (`id_proveedor`) REFERENCES `Proveedor` (`id_proveedor`),
  ADD CONSTRAINT `Lote_ibfk_3` FOREIGN KEY (`id_compra`) REFERENCES `Compra` (`id_compra`);

--
-- Filtros para la tabla `Producto`
--
ALTER TABLE `Producto`
  ADD CONSTRAINT `Producto_ibfk_1` FOREIGN KEY (`id_categoria`) REFERENCES `Categoria` (`id_categoria`),
  ADD CONSTRAINT `Producto_ibfk_2` FOREIGN KEY (`id_marca`) REFERENCES `Marca` (`id_marca`);

--
-- Filtros para la tabla `Producto_Compra`
--
ALTER TABLE `Producto_Compra`
  ADD CONSTRAINT `Producto_Compra_ibfk_1` FOREIGN KEY (`id_compra`) REFERENCES `Compra` (`id_compra`),
  ADD CONSTRAINT `Producto_Compra_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `Producto` (`id_producto`);

--
-- Filtros para la tabla `Producto_Venta`
--
ALTER TABLE `Producto_Venta`
  ADD CONSTRAINT `Producto_Venta_ibfk_1` FOREIGN KEY (`id_venta`) REFERENCES `Venta` (`id_venta`),
  ADD CONSTRAINT `Producto_Venta_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `Producto` (`id_producto`);

--
-- Filtros para la tabla `Venta`
--
ALTER TABLE `Venta`
  ADD CONSTRAINT `Venta_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `Cliente` (`id_cliente`),
  ADD CONSTRAINT `Venta_ibfk_2` FOREIGN KEY (`id_empleado`) REFERENCES `Empleado` (`id_empleado`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
