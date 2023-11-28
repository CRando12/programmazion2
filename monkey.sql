-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Nov 28, 2023 alle 12:45
-- Versione del server: 10.4.28-MariaDB
-- Versione PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `monkey`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `items`
--

CREATE TABLE `items` (
  `id` int(5) NOT NULL,
  `type` enum('fruit','monkey','background') NOT NULL,
  `name` varchar(50) NOT NULL,
  `price` int(5) NOT NULL,
  `img_url` text NOT NULL,
  `level` enum('1','2','3','4') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `items`
--

INSERT INTO `items` (`id`, `type`, `name`, `price`, `img_url`, `level`) VALUES
(1, 'fruit', 'Mela', 15, 'mela.png', '2'),
(2, 'fruit', 'Pera', 25, 'pera.png', '2'),
(3, 'fruit', 'Fragola', 35, 'fragola.png', '3'),
(4, 'background', 'Sfondo 1', 8, 'sfondo1.jpg', '1'),
(5, 'background', 'Sfondo 2', 20, 'sfondo2.jpg', '2'),
(6, 'background', 'Sfondo 3', 35, 'sfondo3.jpg', '3'),
(7, 'background', 'Sfondo 4', 50, 'sfondo4.jpg', '4'),
(8, 'monkey', 'Scimmia 1', 25, 'scimmia1.png', '2'),
(9, 'monkey', 'Scimmia 2', 45, 'scimmia2.png', '3');

-- --------------------------------------------------------

--
-- Struttura della tabella `players`
--

CREATE TABLE `players` (
  `nickname` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `level` enum('1','2','3','4') NOT NULL DEFAULT '1',
  `score` int(5) NOT NULL,
  `played` int(5) NOT NULL,
  `credits` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `players`
--

INSERT INTO `players` (`nickname`, `password`, `level`, `score`, `played`, `credits`) VALUES
('caterina', 'rando', '4', 4365, 9, 30),
('davide', '1234', '1', 0, 0, 0),
('francesca', '1234', '1', 0, 0, 0),
('mariorossi', '1234', '2', 680, 10, 17);

-- --------------------------------------------------------

--
-- Struttura della tabella `player_item`
--

CREATE TABLE `player_item` (
  `id` int(5) NOT NULL,
  `nickname` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `player_item`
--

INSERT INTO `player_item` (`id`, `nickname`) VALUES
(1, 'caterina'),
(2, 'caterina'),
(3, 'caterina'),
(5, 'caterina');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `players`
--
ALTER TABLE `players`
  ADD PRIMARY KEY (`nickname`);

--
-- Indici per le tabelle `player_item`
--
ALTER TABLE `player_item`
  ADD PRIMARY KEY (`id`,`nickname`),
  ADD KEY `FK_player_item` (`nickname`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `items`
--
ALTER TABLE `items`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `player_item`
--
ALTER TABLE `player_item`
  ADD CONSTRAINT `FK_player_item` FOREIGN KEY (`nickname`) REFERENCES `players` (`nickname`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
