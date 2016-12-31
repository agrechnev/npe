NullPointerException (NPE) : a programming forum

development in progress

Includes a simple AngularJS front end (directory /src/main/resources/static)

Spring Security: I opted to use a simple password-based approach, outlined here

https://spring.io/guides/tutorials/spring-security-and-angular-js/

I didn't use oauth because I wanted to learn simpler things first. For the same reason: no hateoas in my rest.

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