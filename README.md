# kt-movie-api
Scala API to manage films and user's favourites list.

## Short Description
An hexagonal architecture has been used to seperate the different layers of the application. The **core** module implements the data model fitting the business requirements. The data model is representend in the database **MovieRegistry** by 3 tables **USER**, **MOVIE** and **FAVOURITE**, to store the information managing the users, movies and favourites respectively. The **infra** layer implements all the interaction between the data model and the [PostgreSQL](https://www.postgresql.org/) database. [Slick](https://scala-slick.org/) a functional database query and access library for Scala has been chosed to work with stored data. The **presentation** layer handles and implements all the interaction and requests that could have an external client who will be using the business features. A [Play](https://www.playframework.com/) server is used while [Tapir](https://github.com/softwaremill/tapir) enrichies the definition of the routes by providing swagger and documentation. While running the application this swagger can be reached at the path **/docs** port 9000 of where is running the application. For example if the application is running in local mode at: http://0.0.0.0:9000/docs/.

## Prerequesites
The program has been implemented using the following versions:
* sbt 1.5.5
* Java 11.0.12
* Scala 2.13.6.

## Requests
- **register**: to register a new user, the USER database has **pseudo** as a unique key so 2 differents users cannot have the same named when logged to the API. The password is saved encrypted in the database.
- **login**: to log a user delivering an JWT token with expiration time
- **logout**
-  **createMovie**: to add a new Movie, a reference to the user who created it is saved in the database. Only him will be able to update or delete that movie from the server. The **title** is here defined as the unique key.
- **getMovieDetails**: to list the details of a movie determined by a specific title
- **listMovies**: to get the list of movies matching a title, release date or genre.
- **updateMovie** and **deleteMovie**
- **addFavourite** and **deleteFavourite** to add or remove a movie as a favourite for a specific user
- 
## How to depLoy
### Local mode
1. Start by adding the necessary environment variables from where the app is running by giving authorization, executing and sourcing th set_var.sh script. From the root directory of the project, run:
```sh
sudo chmod 777 set_var.sh
./set_var.sh
source set_var.sh
```  
2. Run the application by running:
 ```sh
sbt run
``` 
3. Run the database in a docker container using docker-compose
```sh
docker-compose up --build
``` 
4. Run the initialisation of the database by running V1__init.sql with flyway or by running the SQL scripts in the db_script repository directly in the database (by using [DBeaver](https://dbeaver.io/) for example).
5. Interact with the swagger at http://0.0.0.0:9000/docs. Don't forget to pass the token received from the login body response to the authorize locking section to be able to use **logout** request and **Movie** and **Favourite** sections requests.

