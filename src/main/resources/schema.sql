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
-- Estructura de tabla para la tabla `t_telephone`
--

CREATE TABLE IF NOT EXISTS `t_telephone` (
  `id` varchar(36) NOT NULL,
  `lada` varchar(5) DEFAULT NULL,
  `name` varchar(15) DEFAULT NULL,
  `number` varchar(12) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `fk_id_chef` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_telephone_chef`
  FOREIGN KEY (`fk_id_chef`)
  REFERENCES `t_chef` (`id`)
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
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_order_address`
  FOREIGN KEY (`fk_id_address`)
  REFERENCES `t_address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_chef`
  FOREIGN KEY (`fk_id_chef`)
  REFERENCES `t_chef` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_client`
  FOREIGN KEY (`fk_id_client`)
  REFERENCES `t_client` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
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
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_package_dish`
  FOREIGN KEY (`fk_id_dish`)
  REFERENCES `t_dish` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_package_order`
  FOREIGN KEY (`fk_id_order`)
  REFERENCES `t_order` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;
