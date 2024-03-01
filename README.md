# Banl Service REST App

## Description
This is app with a simple banking operations (like money transfer) and authorization via JWT token

## Installation
First clone this repository to your local machine:
```
git clone
```
Next add **.env** file to the root folder of the project. It must contain environment variables listed below:
```
DB_URL=jdbc:postgresql://bank-db/bank_service_app
DB_USERNAME=<database username>
DB_PASSWORD=<password for database user>
TOKEN_SECRET=<Your very secret token>
EXP_MINUTES=<Token expiration time in minutes>
```
You must have docker and docker compose installed.

Finally, run: 
```bash
docker compose up -d
```
This command will build new image from the [Dockerfile](/Dockerfile) for the app and starts containers with app and database.

## Usage
If run on default port 8080 go [here](http://localhost:8080/swagger-ui/index.html#/) for swagger docs