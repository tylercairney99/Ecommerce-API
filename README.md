# E-commerce API 

## Overview

This e-commerce API is a personal project that utilizes a RESTful web service designed as a backend for an e-commerce platform. It provides a robust interface for handling product inventory, user accounts, and order management. Built with Spring Boot and Java, this API is equipped with a suite of endpoints that facilitate CRUD operations on a database of products and user orders.

## Features

- RESTful endpoints for product management (create, read, update, delete).
- Order processing and history retrieval.
- Pagination and sorting for product listings.
- Data validation and error handling.
- Robust Validation: Ensure data integrity with comprehensive validation and error handling.
- PostgreSQL Database Integration: Persistent storage for scalable data management.
- In-memory H2 database seeded with dummy data for testing and demonstration purposes.

## Technologies

- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- Java
- PostgreSQL
  

## Getting Started

### Prerequisites

- Java JDK 11 or later
- Maven

### Installation

1. Clone the repository:
```
git clone https://github.com/your-username/ecommerce-api.git
```

2. Navigate to the project directory:
```
cd ecommerce-api
```

3. Build the project:
```
mvn clean install
```

4. Run the applications:
```
mvn spring-boot:run
```

The API will be accessible at 'http://localhost:8080'.

## Usage

### Product Endpoints:

POST /api/products - Create a new product.

GET /api/products - Retrieve all products.

GET /api/products/{id} - Retrieve a product by its ID.

PUT /api/products/{id} - Update a product's information.

DELETE /api/products/{id} - Delete a product.

### User Endpoints:

POST /api/users - Create a new user.

GET /api/users - Retrieve all users.

GET /api/users/{id} - Retrieve a user by their ID.

PUT /api/users/{id} - Update a user's information.

DELETE /api/users/{id} - Delete a user.

### Order Endpoints:

POST /api/orders - Create a new order.

GET /api/orders - Retrieve all orders.

GET /api/orders/{id} - Retrieve an order by its ID.

PUT /api/orders/{id} - Update an order's details.

DELETE /api/orders/{id} - Delete an order.

### Order Line Endpoints:

POST /api/orderlines - Add a new order line.

GET /api/orderlines - Retrieve all order lines.

GET /api/orderlines/{id} - Retrieve an order line by its ID.

PUT /api/orderlines/{id} - Update an order line's details.

DELETE /api/orderlines/{id} - Delete an order line.

(possible more endpoints to come)

## Contact

For any inquiries, please reach out to 
