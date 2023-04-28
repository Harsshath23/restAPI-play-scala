
CREATE TABLE IF NOT EXISTS `employees`. `employeeform` (
    `id`  VARCHAR(50)  NOT NULL,
    `employeeID`  VARCHAR(50) NOT NULL,
    `companyID` VARCHAR(50) NOT NULL,
    `name` VARCHAR(50) NOT NULL,
    `position` VARCHAR(50) NOT NULL,
    `address` VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
)

DEFAULT CHARACTER SET = utf8

drop table 'employeeform'