# 🚘 Car Sharing Application

A modern car-sharing platform built with <img src="https://img.icons8.com/color/48/000000/spring-logo.png" width="16"/> **Spring Boot** that enables users to rent vehicles, manage payments via <img src="https://img.icons8.com/color/48/000000/stripe.png" width="16"/> **Stripe**, and receive <img src="https://img.icons8.com/color/48/000000/telegram-app.png" width="16"/> **Telegram** notifications

![car.png](images/car.png)

## ⚙️ Tech Stack

### Backend
- <img src="https://img.icons8.com/color/48/000000/java-coffee-cup-logo.png" width="16"/> **Java 21** - Core language
- <img src="https://img.icons8.com/color/48/000000/spring-logo.png" width="16"/> **Spring Boot 3.5.0** - Application framework
- <img src="https://img.icons8.com/color/48/000000/spring-logo.png" width="16"/> **Spring Security** - 🔐 Authentication & authorization
- <img src="https://jwt.io/img/pic_logo.svg" width="16"/> **JWT** - Secure token-based authentication
- <img src="https://mapstruct.org/images/mapstruct.png" width="16"/> **MapStruct** - Efficient DTO mapping
- <img src="https://img.icons8.com/color/48/database.png" width="16"/> **Liquibase**
- <img src="https://img.icons8.com/color/48/000000/mysql-logo.png" width="16"/> **MySQL** - Primary database (H2 for testing)

### Integrations
- <img src="https://img.icons8.com/color/48/000000/stripe.png" width="16"/> **Stripe API** - Payment processing
- <img src="https://img.icons8.com/color/48/000000/telegram-app.png" width="16"/> **Telegram Bot API** - Real-time notifications
- <img src="https://springdoc.org/images/springdoc-openapi.png" width="16"/> **SpringDoc OpenAPI** - API documentation


## ☑️ Key Features

- **User Management**: Registration, authentication (JWT), and role-based access (MANAGER/CUSTOMER)
- **Car Inventory**: CRUD operations for vehicles with real-time availability tracking
- **Rental System**:
    - Book cars with date range selection
    - Automatic inventory adjustment
    - Overdue rental detection
- **Payment Integration**:
    - Stripe payment processing
    - Session management with success/cancel endpoints
    - Fine calculation for overdue rentals
- **Real-time Notifications**:
    - Telegram alerts for new rentals
    - Payment confirmations
    - Daily overdue reminders
- **API Documentation**: Full Swagger/OpenAPI support


## 🚀 Getting Started

#### 1. Clone the repository:
    ```bash
    git clone https://github.com/smagles/car-sharing-app


#### 2. Database Setup

##### Option 1: Docker (Recommended)
    ```bash
    # Start all services in detached mode
    docker-compose up -d

    # Verify containers are running
    docker ps

##### Option 2: Manual MySQL Setup


#### 3. Service Configuration

##### Telegram Bot
    ```properties
    telegram.bot.username=your-bot-name 
    telegram.bot.token=your-bot-token
    telegram.bot.chat.id=your-chat-id

##### Stripe Payments
    ```properties
    stripe.secret.key=your-secret-key

![start.png](images/start.png)


## 📚 API Documentation
Access Swagger UI after launch:
🔗 http://localhost:8080/swagger-ui/index.html
![car-rental-swagger.png](images/car-rental-swagger.png)
