# OrderProcessingSpring
![GitHub last commit](https://img.shields.io/github/last-commit/m-devs/OrderProcessingSpring?color=%23dc143c) ![GitHub repo size](https://img.shields.io/github/repo-size/m-devs/OrderProcessingSpring?color=%23dc155d) ![GitHub top language](https://img.shields.io/github/languages/top/m-devs/OrderProcessingSpring?color=%23dc151b) [![CodeFactor](https://www.codefactor.io/repository/github/m-devs/orderprocessingspring/badge)](https://www.codefactor.io/repository/github/m-devs/orderprocessingspring)

[Student Task] An order processing app (in theory) ~ With Spring Boot

#### Written in Java (JDK 11 in IntelliJ IDEA), used XAMPP for local database emulation (SQL file included [in the repo](https://github.com/m-devs/OrderProcessingSpring/blob/master/orderprocessingdb.sql)), used FileZilla as FTP Server..

The inputFile.csv ([here](https://github.com/m-devs/OrderProcessingSpring/blob/master/inputFile.csv) in the repo) contains valid and the possible invalid lines of data.. also it contains data with the same ID as in [db_error_causers.csv](https://github.com/m-devs/OrderProcessingSpring/blob/master/db_error_causers.csv) which is already uploaded to the database only for error causing purposes (so it's in the sql file already)..

### Maven used for dependency management [(pom.xml)](https://github.com/m-devs/OrderProcessingSpring/blob/master/pom.xml)
You can see the used dependecies here: [Network dependecies](https://github.com/m-devs/OrderProcessingSpring/network/dependencies)

## Basic usage

1. **Clone** the repo.
```Git
git clone https://github.com/m-devs/OrderProcessingSpring.git
```
1. **Change** the **DB** connection details in the **[application.properties](https://github.com/m-devs/OrderProcessingSpring/blob/master/src/main/resources/application.properties)** file.
```Properties
...
spring.datasource.url=jdbc:mysql://localhost:3306/orderprocessingdb
spring.datasource.username=yourname
spring.datasource.password=yourpass
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL5Dialect
...
```
1. **Profit!**
**The Commands**
> Connect to your FTP server
>  * -S : Save the login details
>  * -A : Auto login
 ```CLI
 op:>con your.ftp.server.ip.address.before.the.port 2020 username password
 ```
>  Upload your file to your database and upload the response to the FTP server
>  * -R : Upload Response to FTP
>  * -F : If the file is invalid, force the valid data to be uploaded to the database)
 ```CLI
 op:>up path/to/your/file/your_file.csv -R -F
 ```

#### Check out [GitHub Wiki](https://github.com/m-devs/OrderProcessingSpring/wiki) for more information about the commands.
