-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 21, 2023 at 01:49 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `skiplate`
--

-- --------------------------------------------------------

--
-- Table structure for table `categori`
--

CREATE TABLE `categori` (
  `idCategori` int(11) NOT NULL,
  `categoriName` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categori`
--

INSERT INTO `categori` (`idCategori`, `categoriName`) VALUES
(1, 'Academic'),
(2, 'Non-Academic'),
(3, 'Organization'),
(4, 'Event'),
(5, 'Job');

-- --------------------------------------------------------

--
-- Table structure for table `tags`
--

CREATE TABLE `tags` (
  `idTags` int(11) NOT NULL,
  `nameTags` varchar(11) NOT NULL,
  `colorTagas` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tags`
--

INSERT INTO `tags` (`idTags`, `nameTags`, `colorTagas`) VALUES
(1, 'Tag 1', 'Red'),
(2, 'Tag 2', 'Green'),
(3, 'Tag 3', 'Yellow'),
(4, 'Tag 4', 'Blue');

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE `task` (
  `idTask` int(11) NOT NULL,
  `titleTask` varchar(20) NOT NULL,
  `dateTask` datetime NOT NULL,
  `detailTask` varchar(50) NOT NULL,
  `idCategori` int(11) NOT NULL,
  `idTag` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `statusTask` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `task`
--

INSERT INTO `task` (`idTask`, `titleTask`, `dateTask`, `detailTask`, `idCategori`, `idTag`, `idUser`, `statusTask`) VALUES
(4, 'Task 1', '2020-06-05 04:03:00', 'Sahur', 4, 4, 3, 0),
(6, 'Task 2', '2024-05-05 04:00:00', 'Tidur', 2, 3, 3, 0),
(7, 'Task 3', '2017-05-06 00:00:00', 'Kerja', 3, 2, 3, 0),
(8, 'Task 4', '2023-12-21 22:20:00', 'Tidur', 2, 1, 3, 0),
(9, 'Task 5', '2023-12-03 00:00:00', 'Kerja', 5, 3, 3, 0),
(10, 'Task 6', '2023-12-22 20:01:00', 'Belajar', 1, 1, 3, 0);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `idUser` int(11) NOT NULL,
  `nameUser` varchar(50) NOT NULL,
  `emailUser` varchar(50) NOT NULL,
  `passwordUser` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`idUser`, `nameUser`, `emailUser`, `passwordUser`) VALUES
(3, 'coba', 'coba@gmail.com', 'coba'),
(4, 'Coba2', 'Coba2@gmail.com', 'coba2');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categori`
--
ALTER TABLE `categori`
  ADD PRIMARY KEY (`idCategori`);

--
-- Indexes for table `tags`
--
ALTER TABLE `tags`
  ADD PRIMARY KEY (`idTags`);

--
-- Indexes for table `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`idTask`),
  ADD KEY `idUser` (`idUser`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`idUser`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categori`
--
ALTER TABLE `categori`
  MODIFY `idCategori` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `tags`
--
ALTER TABLE `tags`
  MODIFY `idTags` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `task`
--
ALTER TABLE `task`
  MODIFY `idTask` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `task`
--
ALTER TABLE `task`
  ADD CONSTRAINT `task_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
