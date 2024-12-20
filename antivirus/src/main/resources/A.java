spring.application.name=antivirus 
 
spring.datasource.url=jdbc:mysql://localhost:3306/antivirus 
spring.datasource.username=${SQL_LOGIN} 
spring.datasource.password=${SQL_PASSWORD} 
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver 
 
# Hibernate property for database create/update/drop 
spring.jpa.hibernate.ddl-auto=update 
 
jwt.secret = ${JWT_SECRET} 
jwt.expiration = ${JWT_EXPIRATION}