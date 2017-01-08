NullPointerException (NPE) : a programming forum

By Oleksiy Grechnyev, Dec 2016- Jan 2017

development in progress

A gradle project. Requires JDK 8 and an external database server (preferably MySQL or MariaDB). With default settings it runs an embedded Tomcat web server on localhost:8080

How to Run?

1. Download:

git clone https://github.com/agrechnev/npe.git
cd npe

2. Edit file "src/main/resources/application.properties":

Set up the database settings (at least login+password) in the file "application.properties". If you want to use something different from MySQL or MariaDB, change "build.gradle" as well to include the proper JDBC driver for your database.

3. Build and run. In the project root directory (npe) type:

gradlew build
gradlew bootRun

Note that a fat JAR named npe-<version>.jar is created in the build/libs directory. It can be run with

java -jar build/libs/npe-<version>.jar

Note how Spring Boot plugin creates a fat jar with main class specified automatically. Cool!

4. Open localhost:8080 in your browser. It is better to use a Private(Anonymous) browser window. Enjoy!

For a quick start, log in with admin:admin (it works with an empty database), then create a sample database (Sample DB).
All users in the sample DB have their login names as passwords, e.g. admin:admin, brianna:brianna.

---------
More info:

This is a Rest Resource Server written on Java+Spring Boot+Hibernate.

Front End: A simple single-page AngularJS front end (directory src/main/resources/static).
Nothing fancy, simple bootstrappish look.

Unit Tests: Contains unit tests for classes UserController.
This is a demonstration of unit testing with Spring Boot and Spring Security. Uses Mockito+Hamcrest, of course.

Spring Security: I opted to use a simple password-based approach, outlined here

https://spring.io/guides/tutorials/spring-security-and-angular-js/

I didn't use oauth because I wanted to learn simpler things first. For the same reason: no hateoas in my rest.

Logging: Uses log file npe.log by default.

------------------
Rest API:

Authentication:

GET("/userauth") = Get current user principal (current user)
GET("/userid") = Get current user id (current user)
POST("/logout", {}) = Log out (current user)

User acccounts:

GET("/rest/user") =  Get all users as list of UserDto (admin only)
POST("/rest/user", UserDto userDto) = Create a new user (open)

GET("/rest/user/{id}") =  Get user (UserDto) by id (current user or admin)
PUT("/rest/user/{id}", UserDto userDto) =  Update user account (current user or admin)
DELETE("/rest/user/{id}") =  Delete user account (admin only)
POST("/rest/user/{id}/change_password", PasswordChanger passwordChanger) = Change password (current user or admin)
POST("/rest/user/{id}/delete_account", String passw) = Delete my account (current user)

Posts:

GET("/rest/post") =  Get all posts as a list of PostDto (open)
POST("/rest/post", PostDto postDto) =  Create a new post (current user)
GET("/rest/post/{id}") =  Get a post (PostDto) by id (open)
DELETE("/rest/post/{id}") =  Delete a post (post owner or admin)
PUT("/rest/post/{id}") =  Update a post (post owner)

Comments:

GET("/rest/post/{postId}/comment") =  Get all comments as a list of CommentDto (open)
POST("/rest/post/{postId}/comment", CommentDto commentDto) =  Create a new comment (current user)
GET("/rest/post/{postId}/comment/{id}") =  Get a comment (CommentDto) by id (open)
DELETE("/rest/post/{postId}/comment/{id}") = Delete a comment (admin, post owner, comment owner)
PUT("/rest/post/{postId}/comment/{id}") = Update a comment (comment owner)

Categories:

GET("/rest/category") =  Get all categories as a list of CategoryDto (open)

Sample DB:

POST("/rest/sample/create", String passw) = Create sample database (Admin Only)
POST("/rest/sample/delete", String passw) = Delete everything (Admin Only)