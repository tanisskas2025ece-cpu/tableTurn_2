# 🍽️ TableTurn – Restaurant Management System

## 📌 About the Project

TableTurn is a simple restaurant management system developed to manage restaurant operations in one place. It helps users manage restaurant tables, customer reservations, food orders, order items, and bills.

The project includes a Spring Boot backend connected to a MySQL database and a simple blue-themed frontend built using HTML, CSS, and JavaScript.

## 🎯 Objectives

* Manage restaurant tables and their availability.
* Create and manage customer reservations.
* Create and update food orders.
* Add food items to customer orders.
* Generate bills with tax and discount calculations.
* Store and retrieve restaurant data from a MySQL database.

## 🛠️ Technologies Used

### Backend

* Java
* Spring Boot
* Spring Data JPA
* REST API

### Frontend

* HTML
* CSS
* JavaScript

### Database

* MySQL

### Tools

* IntelliJ IDEA
* Postman
* MySQL Workbench
* Git and GitHub

## ✨ Features

### 1. Table Management

* Add restaurant tables.
* View available tables.
* Update table details.
* Delete tables.

### 2. Reservation Management

* Create customer reservations.
* Select a table and party size.
* Set reservation start and end times.
* View, update, and cancel reservations.

### 3. Order Management

* Create customer orders.
* Store reservation details.
* Update order status.
* View and delete orders.

### 4. Order Item Management

* Add food items to an order.
* Store item name, quantity, and unit price.
* Calculate item subtotal.
* View, update, and delete order items.

### 5. Bill Management

* Generate bills for orders.
* Calculate subtotal, tax, discount, and total amount.
* Store payment status and payment method.
* View, update, and delete bills.

## 🖥️ Frontend

The frontend is developed using HTML, CSS, and JavaScript and is served through Spring Boot's static resources.

All five modules are displayed on a single page with a simple blue-themed design.

## 🗄️ Database

The application uses MySQL to store restaurant information.

Main database tables include:

* `restaurant_tables`
* `reservations`
* `food_order`
* `order_items`
* `bills`

## 🚀 How to Run the Project

### Prerequisites

Install the following:

* Java JDK
* IntelliJ IDEA
* MySQL Server
* Maven
* Postman (for API testing)

### 1. Clone the repository

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
```

### 2. Open the project

Open the cloned project in IntelliJ IDEA.

### 3. Configure the database

Create a MySQL database:

```sql
CREATE DATABASE tableturn_new;
```

Update the database details in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tableturn_new
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```

Replace `YOUR_MYSQL_PASSWORD` with your MySQL password.

### 4. Run the application

Run the main Spring Boot application class from IntelliJ IDEA.

### 5. Open the frontend

Open the following URL in your browser:

```text
http://localhost:8080/
```

## 🔗 API Endpoints

| Module       | Endpoint            |
| ------------ | ------------------- |
| Tables       | `/api/tables`       |
| Reservations | `/api/reservations` |
| Orders       | `/api/orders`       |
| Order Items  | `/api/order-items`  |
| Bills        | `/api/bills`        |

Use Postman to test the REST API endpoints.

## 🧪 Testing

The backend REST APIs were tested using Postman. The frontend was connected to the Spring Boot APIs for creating, viewing, updating, and deleting records.

## 📚 Learning Outcomes

* Understanding Spring Boot application structure.
* Creating REST APIs using Spring Boot.
* Connecting Java applications with MySQL.
* Performing CRUD operations.
* Connecting frontend JavaScript with backend APIs.
* Testing REST APIs using Postman.
* Understanding database relationships between restaurant modules.

## 👩‍💻 Author

**Tanisska**

BE – Electronics and Communication Engineering

## 📄 License

This project was developed for academic and learning purposes.
