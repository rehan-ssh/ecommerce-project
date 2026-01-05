# E-Commerce Microservices Platform

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5%2B%20%2F%204.0%2B-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.x-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-blue)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-Latest-black)
![Redis](https://img.shields.io/badge/Redis-Latest-red)

A modern, scalable e-commerce platform built with microservices architecture using Spring Boot and Spring Cloud. This project demonstrates enterprise-level patterns including service discovery, API gateway, distributed messaging, caching, and more.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Development](#development)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)

## 🎯 Overview

This e-commerce platform is designed as a microservices-based system that provides a scalable and maintainable solution for online retail operations. The platform includes:

- **User Management**: Handle user registration, authentication, and authorization using OAuth2
- **Product Catalog**: Manage product listings with AI-powered features
- **Payment Processing**: Secure payment integration with Stripe
- **Email Notifications**: Asynchronous email service using Kafka
- **Service Discovery**: Dynamic service registration and discovery with Eureka
- **API Gateway**: Centralized routing and load balancing

## 🏗 Architecture

The platform follows a microservices architecture pattern with the following components:

```
┌─────────────────────────────────────────────────────┐
│                   API Gateway                       │
│              (Port: 8085)                          │
│         Routes, Load Balancing, Monitoring         │
└──────────────────┬──────────────────────────────────┘
                   │
         ┌─────────┴──────────┐
         │   Service Discovery │
         │   (Eureka - 8761)   │
         └─────────┬───────────┘
                   │
    ┏━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━┓
    ┃                                  ┃
┌───▼────────┐  ┌──────────┐  ┌────────▼──┐  ┌────────────┐
│   User     │  │  Product │  │  Payment  │  │   Email    │
│  Service   │  │  Service │  │  Service  │  │  Service   │
└───┬────────┘  └────┬─────┘  └───────────┘  └─────┬──────┘
    │                │                               │
┌───▼────┐      ┌───▼───┐                      ┌────▼────┐
│ MySQL  │      │ MySQL │                      │  Kafka  │
│   DB   │      │   DB  │                      │ Cluster │
└────────┘      └───-───┘                      └─────────┘
```

## 🛠 Tech Stack

### Core Technologies
- **Java 17**: Primary programming language
- **Spring Boot 3.5.x / 4.0.x**: Application framework
- **Spring Cloud 2025.x**: Microservices framework
- **Maven**: Build automation and dependency management

### Microservices Components
- **Spring Cloud Netflix Eureka**: Service discovery and registration
- **Spring Cloud Gateway**: API gateway and routing
- **Spring Cloud LoadBalancer**: Client-side load balancing

### Data & Messaging
- **MySQL 8.0+**: Relational database for persistent storage
- **Redis**: Caching layer for improved performance
- **Apache Kafka**: Event streaming and asynchronous messaging
- **Spring Data JPA**: Data access and persistence
- **Flyway**: Database migration and versioning

### Security & Integration
- **Spring Security**: Authentication and authorization
- **OAuth2 Authorization Server**: Secure API access
- **Stripe API**: Payment processing integration
- **Spring AI**: AI-powered features integration (OpenAI)
- **JavaMail**: Email functionality

### Monitoring & DevOps
- **Spring Boot Actuator**: Production-ready monitoring
- **Prometheus**: Metrics collection and monitoring
- **Lombok**: Reduce boilerplate code

## 📁 Project Structure

```
ecommerce-project/
├── ApiGateway/          # API Gateway service
├── ServiceDiscovery/    # Eureka server for service registry
├── UserService/         # User management and authentication
├── ProductService/      # Product catalog management
├── PaymentService/      # Payment processing
└── EmailService/        # Email notification service
```

### Service Details

#### 🚪 ApiGateway (Port: 8085)
- **Purpose**: Central entry point for all client requests
- **Features**:
  - Route management and request forwarding
  - Load balancing across service instances
  - Monitoring with Prometheus metrics
  - Service discovery integration
- **Key Dependencies**: Spring Cloud Gateway, Eureka Client, Actuator

#### 🔍 ServiceDiscovery (Port: 8761)
- **Purpose**: Service registry and discovery using Netflix Eureka
- **Features**:
  - Dynamic service registration
  - Health monitoring of registered services
  - Service instance lookup
  - Eureka dashboard at `http://localhost:8761`
- **Key Dependencies**: Netflix Eureka Server

#### 👤 UserService (Dynamic Port)
- **Purpose**: User management, authentication, and authorization
- **Features**:
  - User registration and profile management
  - OAuth2 authorization server
  - JWT token-based authentication
  - Integration with Kafka for event publishing
  - MySQL database for user data persistence
  - Flyway database migrations
- **Key Dependencies**: Spring Security, OAuth2, Spring Data JPA, MySQL, Kafka, Flyway

#### 🛍 ProductService (Dynamic Port)
- **Purpose**: Product catalog and inventory management
- **Features**:
  - Product CRUD operations
  - AI-powered product features (OpenAI integration)
  - Redis caching for improved performance
  - MySQL database with Flyway migrations
  - Service discovery integration
- **Key Dependencies**: Spring Web, Spring Data JPA, MySQL, Redis, Spring AI, Flyway

#### 💳 PaymentService (Dynamic Port)
- **Purpose**: Payment processing and transaction management
- **Features**:
  - Stripe payment integration
  - Payment processing and verification
  - Transaction management
- **Key Dependencies**: Spring Web MVC, Stripe Java SDK

#### 📧 EmailService (Dynamic Port)
- **Purpose**: Asynchronous email notification service
- **Features**:
  - Kafka consumer for email events
  - Email template processing
  - JavaMail for sending emails
  - Service discovery integration
- **Key Dependencies**: Spring Kafka, JavaMail, Eureka Client

## ✅ Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 17 or higher**
  ```bash
  java -version  # Should show version 17 or higher
  ```

- **Apache Maven 3.6+**
  ```bash
  mvn -version
  ```

- **MySQL 8.0+**
  - Running on `localhost:3306`
  - Create databases: `user_service_db`, `product_service_db`
  - Create user: `ecom_user` with appropriate permissions

- **Redis Server**
  - Running on default port `6379`

- **Apache Kafka**
  - Kafka broker running on default port `9092`
  - Required for EmailService

- **Git** for version control

### Optional
- **Docker & Docker Compose** (for containerized deployment)
- **IntelliJ IDEA** or **Eclipse** (recommended IDEs)
- **Postman** or **curl** for API testing

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/rehan-ssh/ecommerce-project.git
cd ecommerce-project
```

### 2. Set Up Databases

```sql
-- Connect to MySQL
mysql -u root -p

-- Create databases
CREATE DATABASE user_service_db;
CREATE DATABASE product_service_db;

-- Create user and grant permissions
CREATE USER 'ecom_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON user_service_db.* TO 'ecom_user'@'localhost';
GRANT ALL PRIVILEGES ON product_service_db.* TO 'ecom_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Environment Variables

Create environment variables or update `application.properties` files:

```bash
# For UserService
export PORT=8081
export SENDER_EMAIL=your-email@example.com

# For ProductService (if using Spring AI)
export SPRING_AI_OPENAI_API_KEY=your-openai-api-key

# For PaymentService (if using Stripe)
export STRIPE_API_KEY=your-stripe-api-key
```

### 4. Update Database Passwords

Update the `spring.datasource.password` in:
- `UserService/src/main/resources/application.properties`
- `ProductService/src/main/resources/application.properties`

### 5. Start Required Services

```bash
# Start MySQL (if not running as a service)
sudo systemctl start mysql

# Start Redis
redis-server

# Start Kafka (with Zookeeper)
# Navigate to your Kafka installation directory
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
```

## ⚡ Quick Start

### Starting the Services

The services should be started in the following order:

#### 1. Start Service Discovery (Eureka Server)

```bash
cd ServiceDiscovery
./mvnw spring-boot:run
```

Wait for the Eureka dashboard to be available at `http://localhost:8761`

#### 2. Start Individual Services

Open separate terminal windows for each service:

```bash
# Terminal 2 - User Service
cd UserService
export PORT=8081
./mvnw spring-boot:run

# Terminal 3 - Product Service
cd ProductService
./mvnw spring-boot:run

# Terminal 4 - Payment Service
cd PaymentService
./mvnw spring-boot:run

# Terminal 5 - Email Service
cd EmailService
./mvnw spring-boot:run
```

#### 3. Start API Gateway

```bash
# Terminal 6 - API Gateway
cd ApiGateway
./mvnw spring-boot:run
```

### Verify Services

1. **Check Eureka Dashboard**: Visit `http://localhost:8761` to see all registered services
2. **Check API Gateway**: `http://localhost:8085/actuator/health`
3. **Test API**: Try accessing services through the gateway:
   ```bash
   curl http://localhost:8085/users/health
   curl http://localhost:8085/products/health
   ```

## 🔌 API Endpoints

### Via API Gateway (http://localhost:8085)

#### User Service Routes
```
POST   /users/register          # User registration
POST   /users/login             # User authentication
GET    /users/profile           # Get user profile
PUT    /users/profile           # Update user profile
```

#### Product Service Routes
```
GET    /products                # List all products
GET    /products/{id}           # Get product by ID
POST   /products                # Create new product
PUT    /products/{id}           # Update product
DELETE /products/{id}           # Delete product
```

### Direct Service Access

- **Eureka Dashboard**: `http://localhost:8761`
- **API Gateway Actuator**: `http://localhost:8085/actuator`
- **User Service**: Dynamic port (check Eureka)
- **Product Service**: Dynamic port (check Eureka)

> **Note**: It's recommended to access all services through the API Gateway for proper load balancing and routing.

## ⚙️ Configuration

### Key Configuration Files

Each service has its own `application.properties` file:

```
ServiceName/src/main/resources/application.properties
```

### Important Configuration Properties

#### API Gateway Configuration
```properties
server.port=8085
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
spring.cloud.gateway.server.webmvc.routes[0].id=product-service
spring.cloud.gateway.server.webmvc.routes[0].predicates[0]=Path=/products/**
```

#### Service Discovery Configuration
```properties
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

#### Database Configuration (UserService/ProductService)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/database_name
spring.datasource.username=ecom_user
spring.datasource.password=your_password
```

### Changing Ports

To change service ports, update the `server.port` property in the respective `application.properties` file.

## 💻 Development

### Building the Project

Build all services:

```bash
# Build individual service
cd ServiceName
./mvnw clean install

# Skip tests during build
./mvnw clean install -DskipTests
```

### Running Tests

```bash
cd ServiceName
./mvnw test
```

### Code Style

This project uses:
- **Lombok** for reducing boilerplate code
- Standard Spring Boot project structure
- RESTful API design principles

### Adding a New Service

1. Create a new Spring Boot project
2. Add Spring Cloud dependencies
3. Configure Eureka client
4. Register routes in API Gateway
5. Update this README

## 🐛 Troubleshooting

### Common Issues and Solutions

#### 1. Service Not Registering with Eureka

**Problem**: Service doesn't appear in Eureka dashboard

**Solution**:
- Ensure Eureka Server is running on port 8761
- Check `eureka.client.service-url.defaultZone` configuration
- Verify network connectivity
- Check service logs for connection errors

#### 2. Database Connection Failed

**Problem**: `CommunicationsException` or connection refused

**Solution**:
```bash
# Verify MySQL is running
sudo systemctl status mysql

# Check connection
mysql -u ecom_user -p

# Verify database exists
SHOW DATABASES;

# Check firewall settings
sudo ufw status
```

#### 3. Port Already in Use

**Problem**: `Port 8085 is already in use`

**Solution**:
```bash
# Find process using the port
lsof -i :8085

# Kill the process
kill -9 <PID>

# Or change the port in application.properties
```

#### 4. Redis Connection Failed

**Problem**: Cannot connect to Redis

**Solution**:
```bash
# Start Redis server
redis-server

# Verify Redis is running
redis-cli ping
# Should respond with: PONG
```

#### 5. Kafka Consumer Not Receiving Messages

**Problem**: EmailService not receiving Kafka events

**Solution**:
- Verify Kafka is running: `bin/kafka-broker-api-versions.sh --bootstrap-server localhost:9092`
- Check topic exists: `bin/kafka-topics.sh --list --bootstrap-server localhost:9092`
- Verify consumer group: Check service logs
- Ensure Zookeeper is running

#### 6. Maven Build Failures

**Problem**: Build fails with dependency errors

**Solution**:
```bash
# Clear Maven cache
rm -rf ~/.m2/repository

# Force update dependencies
./mvnw clean install -U

# Use Maven wrapper
./mvnw clean install
```

#### 7. Gateway Returns 503 Service Unavailable

**Problem**: API Gateway cannot route to services

**Solution**:
- Check if target service is registered in Eureka
- Verify service is healthy: Check Eureka dashboard
- Review Gateway logs for routing errors
- Ensure service discovery is working

### Logs Location

Service logs are typically output to console by default. To enable file logging, add to `application.properties`:

```properties
logging.file.name=logs/service-name.log
logging.level.root=INFO
logging.level.com.centillion=DEBUG
```

### Getting Help

If you encounter issues not covered here:
1. Check service logs for detailed error messages
2. Review Spring Boot and Spring Cloud documentation
3. Open an issue on GitHub with:
   - Error message
   - Steps to reproduce
   - Environment details (OS, Java version, etc.)

## 🤝 Contributing

We welcome contributions to the e-commerce platform! Here's how you can help:

### Getting Started

1. **Fork the Repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/ecommerce-project.git
   ```

2. **Create a Feature Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make Your Changes**
   - Write clean, documented code
   - Follow existing code style and patterns
   - Add tests for new features
   - Update documentation as needed

4. **Test Your Changes**
   ```bash
   ./mvnw test
   ./mvnw spring-boot:run
   ```

5. **Commit Your Changes**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   ```

6. **Push to Your Fork**
   ```bash
   git push origin feature/your-feature-name
   ```

7. **Open a Pull Request**
   - Provide a clear description of changes
   - Reference any related issues
   - Ensure all tests pass

### Contribution Guidelines

- **Code Quality**: Maintain high code quality standards
- **Testing**: Include unit and integration tests
- **Documentation**: Update README and code comments
- **Commit Messages**: Use conventional commit format
  - `feat:` for new features
  - `fix:` for bug fixes
  - `docs:` for documentation
  - `refactor:` for code refactoring
  - `test:` for adding tests

### Code Review Process

1. Submit pull request
2. Automated tests run
3. Code review by maintainers
4. Address feedback
5. Merge when approved

### Areas for Contribution

- 🐛 Bug fixes
- ✨ New features
- 📝 Documentation improvements
- 🧪 Test coverage
- 🎨 UI/UX enhancements
- ⚡ Performance optimization
- 🌐 Internationalization


## 📞 Contact & Support

- **Repository**: [https://github.com/rehan-ssh/ecommerce-project](https://github.com/rehan-ssh/ecommerce-project)
- **Issues**: [GitHub Issues](https://github.com/rehan-ssh/ecommerce-project/issues)

## 🙏 Acknowledgments

Built with:
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [Netflix OSS](https://netflix.github.io/)
- [Stripe](https://stripe.com/)
- [OpenAI](https://openai.com/)

---

## 🗺 Roadmap

Future enhancements planned:
- [ ] Docker containerization
- [ ] Kubernetes deployment configurations
- [ ] CI/CD pipeline setup
- [ ] Order management service
- [ ] Shopping cart service
- [ ] Inventory management
- [ ] API documentation with Swagger/OpenAPI
- [ ] Distributed tracing with Zipkin/Jaeger
- [ ] Centralized logging with ELK stack
- [ ] GraphQL API support
- [ ] Frontend application (React/Angular)

---
