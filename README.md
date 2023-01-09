# simple-account-service

# Description

Java, Spring Boot, JPA, H2

# Architecture & Design Pattern & Test

## Architecture

- The architecture follows the principles of `Clean Architecture`.
    - Application layer
        - Command
        - UseCase(adapter)
        - Service
        - EventHandler
    - Domain layer
        - Entity, VO
        - Event
        - Repository(interface)
    - Presentation layer (ui)
        - Controller
    - Persistence layer
        - Repository(JPA)
        - DomainEventPublisher

The main reason for applying Clean Architecture is to prevent changes from low-level modules (ui) from propagating to
high-level modules (core business logic).
The Adapter Pattern is applied to invert control direction between each module, remove dependencies, and inject each
implementation through an external framework, resulting in low coupling and high cohesion, and making it easy to write
testable code.

## Domain Design Pattern

- The concept of Aggregate is applied to separate the Account Aggregate and Auth Aggregate.
    - The Account Aggregate manages account information, including account retrieval, creation, and modification.
    - The Auth Aggregate handles authentication and returns authentication results to authorized accounts.
- The root aggregate of the Account Aggregate is the Account Entity, and the child attributes (property, child entity,
  VO) are designed to be accessible through the Account Entity.
    - The use of Getter Setter is minimized to prevent domain logic and rules from being scattered in multiple
      locations.

## Domain Event

- The part that sends an authentication code to users using external SMS is processed asynchronously by creating a
  separate transaction.
    - The business rule of sending SMS messages to users is considered the role of the Notification Domain, and it is
      implemented in a form that ensures final consistency rather than in the same transaction.

## Test

- Tests are written in some unit tests, integration tests, and acceptance tests. They are based on user scenarios.

# Feature

- Sign up
    - Request mobile authentication
    - Request mobile authentication code verification
    - Request sign up
- Log in
    - Log in with mobile
    - Log in with email
- View my information
    - Request to view my information
- Reset password
    - Request mobile password reset code
    - Request verification of password reset code
    - Request password reset

# Installation

## build

```shell
./gradlew clean build
```

## Run Test

```shell
./gradlew clean test --info
```

## Run

```shell
./gradlew bootRun

or 

./gradlew clean bootRun 
```

# API

## Documentation

http://localhost:8080/swagger-ui/

##  Request mobile authentication

```
POST {{host}}/sign-up/request-account-verification-mobile
Content-Type: application/json

{
"mobile": "01012341234"
}
```

## Request authentication code verification

```
POST {{host}}/sign-up/verify-mobile
Content-Type: application/json

{
"mobile": "01012341234",
"code": "185651"
}
```

##  Request sign up

```
POST {{host}}/sign-up/mobile
Content-Type: application/json

{
"mobile": "01012341234",
"email": "jake@gmail.com",
"name": "jake",
"nickName": "Jake",
"password": "password"
}
```

## Log in with mobile

```
POST {{host}}/sign-in/mobile
Content-Type: application/json

{
"mobile": "01012341234",
"password": "password"
}
```

## Log in with email

```
POST {{host}}/sign-in/email
Content-Type: application/json

{
"email": "jake@gmail.com",
"password": "password!"
}
```

##  Request to view my information

```
GET {{host}}/me
Authorization: Bearer {token}
```

## Request mobile password reset code

```
POST {{host}}/request-password-reset
Content-Type: application/json

{
"mobile": "01012341234"
}
```

##  Request password reset code verification

```
POST {{host}}/confirm-password-reset
Content-Type: application/json

{
"mobile": "01012341234",
"code": "615084"
}
```

##  Request password reset

```
PATCH {{host}}/password
Content-Type: application/json

{
"mobile": "01012341234",
"code": "123456",
"password": "password!",
"passwordConfirm": "password!"
```
