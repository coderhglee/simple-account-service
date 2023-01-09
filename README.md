# simple-account-service

# Description

Java, Spring Boot, JPA, H2

# Feature

- [x] 회원가입
    - [x] 모바일 인증 요청
    - [x] 모바일 인증코드 인증 요청
    - [x] 회원가입 요청
- [x] 로그인
    - [x] 모바일 로그인
    - [x] 이메일 로그인
- [x] 내정보 조회
    - [x] 내정보 조회 요청
- [x] 비밀번호 재설정
    - [x] 모바일로 비밀번호 재설정 코드 요청
    - [x] 비밀번호 재설정 코드 인증 요청
    - [x] 비밀번호 재설정 요청

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

## 회원가입 전화번호 인증 요청

```
POST {{host}}/sign-up/request-account-verification-mobile
Content-Type: application/json

{
"mobile": "01012341234"
}
```

## 회원가입 전화번호 인증코드 인증요청

```
POST {{host}}/sign-up/verify-mobile
Content-Type: application/json

{
"mobile": "01012341234",
"code": "185651"
}
```

## 회원가입 요청

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

## 전화번호 로그인 요청

```
POST {{host}}/sign-in/mobile
Content-Type: application/json

{
"mobile": "01012341234",
"password": "password"
}
```

## 이메일 로그인 요청

```
POST {{host}}/sign-in/email
Content-Type: application/json

{
"email": "jake@gmail.com",
"password": "password!"
}
```

## 내정보 조회 요청

```
GET {{host}}/me
Authorization: Bearer {token}
```

## 비밀번호 재설정 인증코드 요청

```
POST {{host}}/request-password-reset
Content-Type: application/json

{
"mobile": "01012341234"
}
```

## 비밀번호 재설정 인증코드 인증 요청

```
POST {{host}}/confirm-password-reset
Content-Type: application/json

{
"mobile": "01012341234",
"code": "615084"
}
```

## 비밀번호 재설정 요청

```
PATCH {{host}}/password
Content-Type: application/json

{
"mobile": "01012341234",
"code": "123456",
"password": "password!",
"passwordConfirm": "password!"
```
