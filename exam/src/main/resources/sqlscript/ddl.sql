
CREATE TABLE `OrgInfo` (
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


 CREATE TABLE "RoomTypeInfo" (
  "RoomId" bigint(20) NOT NULL,
  "PartnerId" bigint(20) NOT NULL,
  "PoiId" bigint(20) NOT NULL,
  "RoomName" varchar(50) NOT NULL,
  "OriginalPrice" decimal(10,2) NOT NULL,
  PRIMARY KEY ("RoomId","PartnerId","PoiId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




