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
- <img src="https://img.icons8.com/color/48/database.png" width="16"/> **Liquibase** - Database migrations
- <img src="https://img.icons8.com/color/48/000000/mysql-logo.png" width="16"/> **MySQL** - Primary database (H2 for testing)
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" width="16"/> **Docker** – Containerization for deployment and testing
- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/maven/maven-original.svg" width="16"/> **Maven** – Project build and dependency management
- <img src="https://img.icons8.com/ios/50/000000/test-tube.png" width="16"/> **JUnit 5** – Unit testing framework


### Integrations
- <img src="https://img.icons8.com/color/48/000000/stripe.png" width="16"/> **Stripe API** - Payment processing
- <img src="https://img.icons8.com/color/48/000000/telegram-app.png" width="16"/> **Telegram Bot API** - Real-time notifications
- <img src="https://www.svgrepo.com/show/342276/swagger.svg" width="16"/> **SpringDoc OpenAPI** - API documentation
- <img src="https://img.icons8.com/color/48/000000/github.png" width="16"/> **GitHub Actions** – CI/CD pipelines

## 📌 About

This is a **learning project** where I’ve developed a backend for a car-sharing platform.  
The application allows users to:

- Rent cars and track their bookings
- Manage payments using **Stripe**
- Receive real-time updates through **Telegram notifications**
- Register and log in securely with JWT authentication
- Use role-based access for **users** and **managers**

## 🚀 Getting Started

### Environment Variables

The application uses the following environment variables:

| Variable              | Default value                                  | Description                          |
|-----------------------|------------------------------------------------|--------------------------------------|
| `JWT_EXPIRATION`      | `21600000`                                     | JWT expiration in milliseconds       |
| `JWT_SECRET`          | `my-very-strong-secret-32-chars-long-1234`    | Secret key used to sign JWT tokens   |
| `TELEGRAM_BOT_TOKEN`  | `your-bot-token`                               | Token for Telegram Bot               |
| `TELEGRAM_CHAT_ID`    | `12345678`                                     | Chat ID used by Telegram Bot         |
| `STRIPE_SECRET_KEY`   | `your-stripe-key`                              | Stripe API secret key                |



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
