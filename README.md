# Ecommerce API

## Overview

This Ecommerce API is a RESTful web service designed as a backend for an e-commerce platform. It provides a robust interface for handling product inventory, user accounts, and order management. Built with Spring Boot and Java, this API is equipped with a suite of endpoints that facilitate CRUD operations on a database of products and user orders.

## Features

- RESTful endpoints for product management (create, read, update, delete).
- User authentication and authorization (coming soon).
- Order processing and history retrieval.
- Pagination and sorting for product listings.
- Data validation and error handling.
- In-memory H2 database seeded with dummy data for testing and demonstration purposes.

## Technologies

- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- Java

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

GET /api/products - Retrieve all products.

POST /api/products - Create a new product.

GET /api/products/{id} - Retrieve a product by its ID.

PUT /api/products/{id} - Update a product's information.

DELETE /api/products/{id} - Delete a product.

(more endpoints to come)

## Contact

For any inquiries, please reach out to Tyler Cairney at tylercairney99@gmail.com
