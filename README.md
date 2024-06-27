# Deals Application

The **Deals** application is a Spring Boot backend connected to a PostgreSQL database, designed to manage currency exchange deals.

## Features

- Validates and processes currency exchange deals.
- Stores deals in a PostgreSQL database.
- Provides REST API endpoints for deal management.

## Technologies Used

- **Spring Boot**: Backend framework for Java applications.
- **PostgreSQL**: Relational database management system.
- **Docker**: Containerization platform for easy deployment.

## Prerequisites

Ensure you have the following installed on your machine:

- Docker
- Docker Compose

## Getting Started

### 1. Clone the Repository

#### git clone git@github.com:Hossamhaddad/FxDeals.git
####  cd FXDeal

### 2. Start Docker Containers
#### Make sure Docker Compose is installed on your machine. Use the following command to start the application:
#### docker-compose up -d

## Testing the Application with Postman
#### This example provides a basic outline on how to use Postman to interact with the Deals application's REST API endpoints.

**Send a POST Request to Create a Deal**

   - Open Postman and create a new request.
   - Set the request type to `POST`.
   - Enter the endpoint URL: `http://localhost:8080/deals`.
   - In the request body tab, select `raw` and choose `JSON` from the dropdown.
   - Paste the following JSON request example:

     ```json
     {
       "dealUniqueId": "12141241555",
       "fromCurrency": "USA",
       "toCurrency": "EUR",
       "dealTimestamp": "2024-06-25T10:30:00",
       "dealAmount": 456.10
     }
     ```



