SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE TABLE `order_` (
  `Entry` int(11) NOT NULL,
  `OrderId` int(11) NOT NULL,
  `BuyerName` text NOT NULL,
  `BuyerEmail` text NOT NULL,
  `OrderDate` date NOT NULL DEFAULT current_timestamp(),
  `OrderTotalValue` decimal(15,10) NOT NULL,
  `Address` text NOT NULL,
  `Postcode` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `order_` (`Entry`, `OrderId`, `BuyerName`, `BuyerEmail`, `OrderDate`, `OrderTotalValue`, `Address`, `Postcode`) VALUES
(0, 1238, 'Mr Someone', 'someone@gmail.com', '2020-03-05', '22.0000000000', 'Fantasy Street', 1111),
(1, 2131, 'Mr Someone', 'someone@gmail.com', '2020-03-05', '22.0000000000', 'Fantasy Street', 1111);

CREATE TABLE `order_item` (
  `Entry` int(11) NOT NULL,
  `OrderItemId` int(11) NOT NULL,
  `OrderId` int(11) NOT NULL,
  `SalePrice` decimal(15,10) NOT NULL,
  `ShippingPrice` decimal(15,10) NOT NULL,
  `TotalItemPrice` decimal(15,10) NOT NULL,
  `SKU` text NOT NULL,
  `Status` enum('IN_STOCK','OUT_OF_STOCK') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `order_item` (`Entry`, `OrderItemId`, `OrderId`, `SalePrice`, `ShippingPrice`, `TotalItemPrice`, `SKU`, `Status`) VALUES
(0, 4612, 1238, '12.0000000000', '10.0000000000', '22.0000000000', '34XXQJFR', 'IN_STOCK'),
(1, 2134, 2131, '12.0000000000', '10.0000000000', '22.0000000000', 'R9QYPQF2', 'IN_STOCK');

ALTER TABLE `order_`
  ADD PRIMARY KEY (`Entry`);

ALTER TABLE `order_item`
  ADD PRIMARY KEY (`Entry`),
  ADD KEY `OrderId` (`OrderId`);

ALTER TABLE `order_`
  MODIFY `Entry` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

ALTER TABLE `order_item`
  MODIFY `Entry` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;