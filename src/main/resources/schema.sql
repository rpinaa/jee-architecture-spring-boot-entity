--
-- Base de datos: `test`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `t_account`
--

CREATE TABLE IF NOT EXISTS `t_account` (
  `id` varchar(36) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `fist_name` varchar(80) DEFAULT NULL,
  `last_name` varchar(80) DEFAULT NULL,
  `secret` longblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `t_address`
--

CREATE TABLE IF NOT EXISTS `t_address` (
  `id` varchar(36) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `block` varchar(5) DEFAULT NULL,
  `colony` varchar(15) DEFAULT NULL,
  `country` varchar(10) DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `ext_number` varchar(5) DEFAULT NULL,
  `int_number` varchar(5) DEFAULT NULL,
  `municipality` varchar(20) DEFAULT NULL,
  `number` varchar(5) DEFAULT NULL,
  `state` varchar(15) DEFAULT NULL,
  `street` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `t_chef`
--

CREATE TABLE IF NOT EXISTS `t_chef` (
  `id` varchar(36) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  `country` varchar(2) DEFAULT NULL,
  `curp` varchar(18) DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `rfc` varchar(13) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `fk_id_account` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_chef_account`
  FOREIGN KEY (`fk_id_account`)
  REFERENCES `t_account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `t_client`
--

CREATE TABLE IF NOT EXISTS `t_client` (
  `id` varchar(36) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `country` varchar(2) DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `fist_name` varchar(80) DEFAULT NULL,
  `last_name` varchar(80) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `secret` longblob,
  `status` varchar(20) DEFAULT NULL,
  `fk_id_telephone` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_client_telephone`
  FOREIGN KEY (`fk_id_telephone`)
  REFERENCES `t_telephone` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `t_dish`
--

CREATE TABLE IF NOT EXISTS `t_dish` (
  `id` varchar(36) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  `allergens` varchar(20) DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `description` varchar(40) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `path_image` varchar(50) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `uuid_image` varchar(36) DEFAULT NULL,
  `fk_id_chef` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_dish_chef`
  FOREIGN KEY (`fk_id_chef`)
  REFERENCES `t_chef` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `t_order`
--

CREATE TABLE IF NOT EXISTS `t_order` (
  `id` varchar(36) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `comment` varchar(50) DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `finished_date` datetime DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `registered_date` datetime DEFAULT NULL,
  `rejected_date` datetime DEFAULT NULL,
  `scheduled_date` varchar(255) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `time_zone` varchar(255) DEFAULT NULL,
  `total` float DEFAULT NULL,
  `fk_id_address` varchar(36) DEFAULT NULL,
  `fk_id_chef` varchar(36) DEFAULT NULL,
  `fk_id_client` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `t_package`
--

CREATE TABLE IF NOT EXISTS `t_package` (
  `id` varchar(36) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `fk_id_dish` varchar(36) DEFAULT NULL,
  `fk_id_order` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `t_telephone`
--

CREATE TABLE IF NOT EXISTS `t_telephone` (
  `id` varchar(36) NOT NULL,
  `lada` varchar(5) DEFAULT NULL,
  `name` varchar(15) DEFAULT NULL,
  `number` varchar(12) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `fk_id_chef` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


--
-- Indices de la tabla `t_order`
--
ALTER TABLE `t_order`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKmdxjxqbgrjuka1o0ryhd8lh6s` (`fk_id_address`),
  ADD KEY `FKnnkk737w7hck6mex2lhc5gokn` (`fk_id_chef`),
  ADD KEY `FK9i6meo1rw967j70bvksbemlh1` (`fk_id_client`);

--
-- Indices de la tabla `t_package`
--
ALTER TABLE `t_package`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK80mv05lhoewvvl63ctw0ll2mw` (`fk_id_dish`),
  ADD KEY `FKl9iocvn3nnpaijyegwraonh4k` (`fk_id_order`);

--
-- Indices de la tabla `t_telephone`
--
ALTER TABLE `t_telephone`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK83tvpf5obvqh6pi6k8su3h3tm` (`fk_id_chef`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `t_chef`
--
ALTER TABLE `t_chef`
  ADD CONSTRAINT `FKi8vy65n7ve0x77hlqbw402xpr` FOREIGN KEY (`fk_id_account`) REFERENCES `t_account` (`id`);

--
-- Filtros para la tabla `t_client`
--
ALTER TABLE `t_client`
  ADD CONSTRAINT `FK9ltyy5tpd8prmvk0k2rsb2oro` FOREIGN KEY (`fk_id_telephone`) REFERENCES `t_telephone` (`id`);

--
-- Filtros para la tabla `t_dish`
--
ALTER TABLE `t_dish`
  ADD CONSTRAINT `FKdiw6p7gelub70axlkvo8ismn8` FOREIGN KEY (`fk_id_chef`) REFERENCES `t_chef` (`id`);

--
-- Filtros para la tabla `t_order`
--
ALTER TABLE `t_order`
  ADD CONSTRAINT `FK9i6meo1rw967j70bvksbemlh1` FOREIGN KEY (`fk_id_client`) REFERENCES `t_client` (`id`),
  ADD CONSTRAINT `FKmdxjxqbgrjuka1o0ryhd8lh6s` FOREIGN KEY (`fk_id_address`) REFERENCES `t_address` (`id`),
  ADD CONSTRAINT `FKnnkk737w7hck6mex2lhc5gokn` FOREIGN KEY (`fk_id_chef`) REFERENCES `t_chef` (`id`);

--
-- Filtros para la tabla `t_package`
--
ALTER TABLE `t_package`
  ADD CONSTRAINT `FK80mv05lhoewvvl63ctw0ll2mw` FOREIGN KEY (`fk_id_dish`) REFERENCES `t_dish` (`id`),
  ADD CONSTRAINT `FKl9iocvn3nnpaijyegwraonh4k` FOREIGN KEY (`fk_id_order`) REFERENCES `t_order` (`id`);

--
-- Filtros para la tabla `t_telephone`
--
ALTER TABLE `t_telephone`
  ADD CONSTRAINT `FK83tvpf5obvqh6pi6k8su3h3tm` FOREIGN KEY (`fk_id_chef`) REFERENCES `t_chef` (`id`);
