University Timetable

#Description 
University Timetable is an application that provides a class timetable for students and teachers. Users can view their timetable for a day or for a longer period.

#Used technologies 
The application is built using the following technologies:

* Spring Boot
* Spring Security with JWT
* HTML and CSS with Thymeleaf templates
* Swagger
* Spring Data JPA
* Spring MVC
* Tomcat 9.0
* JNDI
* Java 1.8
* PostgreSQL 6.1
* JUnit 5.0
* Spring Boot Test
* Mockito
* Logback
* Lombok

#Installation 
To install the application, follow these steps:

Download the project from GitHub: git clone https://github.com/DariaBoo/University_Web.git

Run the following SQL scripts to set up the database and user:
    scr/main/resources/createDB.sql
    createTables.sql
    fillTables.sql
    create user.sql
    
Run the application by executing src/main/java/ua/foxminded/university/AppSpringBoot.java.

Open your browser and navigate to http://localhost:8080/university.

#Authorization 
To log in to the application, use one of the following credentials:
    Admin: login=admin password=admin
    Teacher: login=professor password=professor
    Student: login=alumno password=alumno
    
#Tests 
The application has passed all tests. Here's a screenshot of the test results: 
![image](https://user-images.githubusercontent.com/79281909/230958060-a4b524be-2bfa-4a91-83c7-c7eecbab9405.png)

