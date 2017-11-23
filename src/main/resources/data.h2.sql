--
-- Volcado de datos para la tabla t_account
--

INSERT INTO t_account VALUES ('c4d5b14a-706e-4959-8d20-12d6d06aec39', '2017-11-14 18:07:59', '2017-11-14 18:08:39', '0', 'ricardo.pina@gmail.com', 'ricardo pina', 'arellano', X'243261243130246d2f6e346b4a784e6d5268754c68463234677135446572415847696b766a7a4c786d544346754b75314679736f654e593072764336');
INSERT INTO t_account VALUES ('b29121f5-1a5f-40ee-bd8a-c45c798f32ce', '2017-11-14 18:11:41', '2017-11-14 18:14:51', '0', 'pinaarellano0@gmail.com', 'ricardo pina', 'arellano', X'2432612431302469505534723541577876656234634f557a34326b4165576d7839766e4e5a4e442f7872486f3854455244496f625237716174686871');

-- --------------------------------------------------------

--
-- Volcado de datos para la tabla t_chef
--

INSERT INTO t_chef VALUES ('934bc9fa-1473-43ee-ae4a-938c3ccdb43b', '2017-11-14 18:07:59', '2017-11-14 18:13:42', '0', 'MX', 'PIAR900114HDFXRC08', '0', 0, 'PIAR900114D76', 'ACTIVATED', 'c4d5b14a-706e-4959-8d20-12d6d06aec39');
INSERT INTO t_chef VALUES ('4d9d12e1-8003-4eda-87b1-cdc350eb2c2e', '2017-11-14 18:11:41', '2017-11-14 18:14:51', '0', 'MX', 'PIAR900114HDFXRC08', '0', 0, 'PIAR900114D76', 'ACTIVATED', 'b29121f5-1a5f-40ee-bd8a-c45c798f32ce');

-- --------------------------------------------------------

--
-- Volcado de datos para la tabla t_telephone
--

INSERT INTO t_telephone VALUES ('52a436c2-45a3-4eaf-ae7d-8581b4a63468', '52', 'Mobile', '5523360745', 'HOME', '4d9d12e1-8003-4eda-87b1-cdc350eb2c2e');
INSERT INTO t_telephone VALUES ('d7097d9d-0a0e-4b77-9b47-ce1413a0f46f', '52', 'Celular', '5537314411', 'HOME', '934bc9fa-1473-43ee-ae4a-938c3ccdb43b');
INSERT INTO t_telephone VALUES ('d02446ff-5ae3-4b66-a89a-fcd598a71578', '52', 'Mobile', '5539304411', 'HOME', '934bc9fa-1473-43ee-ae4a-938c3ccdb43b');
INSERT INTO t_telephone VALUES ('b18a03ed-8c0c-4799-ac54-fc27b0fa9ce0', '52', 'Celular', '5541422236', 'HOME', '4d9d12e1-8003-4eda-87b1-cdc350eb2c2e');
INSERT INTO t_telephone VALUES ('86cd7e51-013d-480f-b4e9-c11ba35ce085', '52', 'Mobile', '5557735165', 'HOME', NULL);
INSERT INTO t_telephone VALUES ('875ac239-38da-4bca-a686-dbdf6ca3803e', '52', 'Mobile', '5522327215', 'HOME', NULL);

-- --------------------------------------------------------

--
-- Volcado de datos para la tabla t_client
--

INSERT INTO t_client VALUES ('60c7b18e-519c-4d85-a73d-dfd6c301252a', '2017-11-14 18:18:16', '2017-11-14 18:19:55', NULL, '0', 'mylittlephantom@hotmail.com', 'erendira pina', 'arellano', 0, X'243261243130246c4b4b677662754877767241386a4247565a346d426542727a6730777278304c776d333242512f6e62324b34757855416a6f5a5432', 'ACTIVATED', '86cd7e51-013d-480f-b4e9-c11ba35ce085');
INSERT INTO t_client VALUES ('0baf0e8f-5e12-46d8-9ced-6c58b53bb1c2', '2017-11-14 18:22:34', '2017-11-14 18:24:04', NULL, '0', 'pacoloco@hotmail.com', 'francisco juarez', 'moriarte', 0, X'2432612431302457304e77506853776e7a77556b756e68372f77706c65584f44454d34523655346f6a5a486d5973704b73767a6a30484831336a6b4f', 'ACTIVATED', '875ac239-38da-4bca-a686-dbdf6ca3803e');

-- --------------------------------------------------------