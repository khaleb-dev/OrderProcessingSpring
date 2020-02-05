SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

CREATE TABLE `order_` (
  `OrderId` int(11) NOT NULL,
  `BuyerName` text NOT NULL,
  `BuyerEmail` text NOT NULL,
  `OrderDate` date NOT NULL DEFAULT current_timestamp(),
  `OrderTotalValue` float NOT NULL,
  `Address` text NOT NULL,
  `Postcode` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `order_` (`OrderId`, `BuyerName`, `BuyerEmail`, `OrderDate`, `OrderTotalValue`, `Address`, `Postcode`) VALUES
(1238, 'Mr Someone', 'someone@gmail.com', '2019-12-15', 22, 'Fantasy Street', 1111),
(2131, 'Mr Someone', 'someone@gmail.com', '2019-12-15', 22, 'Fantasy Street', 1111);

CREATE TABLE `order_item` (
  `OrderItemId` int(11) NOT NULL,
  `OrderId` int(11) NOT NULL,
  `SalePrice` float NOT NULL,
  `ShippingPrice` float NOT NULL,
  `TotalItemPrice` float NOT NULL,
  `SKU` text NOT NULL,
  `Status` enum('IN_STOCK','OUT_OF_STOCK','','') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `order_item` (`OrderItemId`, `OrderId`, `SalePrice`, `ShippingPrice`, `TotalItemPrice`, `SKU`, `Status`) VALUES
(2134, 2131, 12, 10, 22, 'R9QYPQF2', 'IN_STOCK'),
(4612, 1238, 12, 10, 22, '34XXQJFR', 'IN_STOCK');

ALTER TABLE `order_`
  ADD PRIMARY KEY (`OrderId`);

ALTER TABLE `order_item`
  ADD PRIMARY KEY (`OrderItemId`),
  ADD KEY `OrderId` (`OrderId`);

ALTER TABLE `order_item`
  ADD CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`OrderId`) REFERENCES `order_` (`OrderId`);
COMMIT;
