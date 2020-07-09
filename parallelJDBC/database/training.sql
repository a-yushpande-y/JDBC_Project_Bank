-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 09, 2020 at 02:32 PM
-- Server version: 10.4.13-MariaDB
-- PHP Version: 7.4.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `training`
--

-- --------------------------------------------------------

--
-- Table structure for table `accountdetails`
--

CREATE TABLE `accountdetails` (
  `accountNumber` int(10) NOT NULL,
  `accountHolderName` varchar(30) NOT NULL,
  `accountBalance` int(10) NOT NULL DEFAULT 0,
  `accountPassword` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `accountdetails`
--

INSERT INTO `accountdetails` (`accountNumber`, `accountHolderName`, `accountBalance`, `accountPassword`) VALUES
(5, 'Ayush', 650, 'root'),
(6, 'Ayush', 730, 'root'),
(7, 'Piyush', 0, 'qwerty');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `transactionId` int(11) NOT NULL,
  `accountNumber` int(11) NOT NULL,
  `transactionType` varchar(200) NOT NULL,
  `transactionAmount` int(10) NOT NULL DEFAULT 0,
  `transactionDate` datetime NOT NULL DEFAULT current_timestamp(),
  `transactionStatus` varchar(10) NOT NULL DEFAULT 'SUCCESS'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`transactionId`, `accountNumber`, `transactionType`, `transactionAmount`, `transactionDate`, `transactionStatus`) VALUES
(1, 6, 'ACCOUNT CREATION', 0, '2020-07-09 00:00:00', 'SUCCESS'),
(2, 7, 'ACCOUNT CREATION', 0, '2020-07-09 15:47:25', 'SUCCESS'),
(3, 6, 'DEPOSIT', 450, '2020-07-09 16:25:52', 'SUCCESS'),
(4, 5, 'DEPOSIT', 1000, '2020-07-09 16:34:25', 'SUCCESS'),
(5, 6, 'WITHDRAWL', 200, '2020-07-09 16:48:10', 'SUCCESS'),
(6, 5, 'WITHDRAWL', 230, '2020-07-09 17:08:21', 'SUCCESS'),
(7, 6, 'DEPOSIT', 230, '2020-07-09 17:08:21', 'SUCCESS'),
(8, 6, 'DEPOSIT', 110, '2020-07-09 17:13:14', 'SUCCESS'),
(9, 5, 'FUND TRANSFER TO AC. NO.6', 120, '2020-07-09 17:26:33', 'SUCCESS'),
(10, 6, 'FUND TRANSFER FROM AC. NO.5', 120, '2020-07-09 17:26:33', 'SUCCESS');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accountdetails`
--
ALTER TABLE `accountdetails`
  ADD PRIMARY KEY (`accountNumber`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transactionId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accountdetails`
--
ALTER TABLE `accountdetails`
  MODIFY `accountNumber` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `transactionId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
