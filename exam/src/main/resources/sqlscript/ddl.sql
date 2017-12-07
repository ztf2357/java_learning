
CREATE TABLE `orginfo` (
   `PoiId` bigint NOT NULL,
   `OrgName` varchar(50) NOT NULL,
   `Longitude` double NOT NULL,
   `Latitude` double NOT NULL,
   `Address` varchar(200) NOT NULL,
   `CityId` INT NOT NULL,
   `Phone` VARCHAR(50) NOT NULL,
   `RoomCount` INT NOT NULL,
   `OpenTime` datetime NOT NULL,
   PRIMARY KEY (`PoiId`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8



