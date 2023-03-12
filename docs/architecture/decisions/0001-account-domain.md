# 1. account-domain

Date: 2023-01-06

## Status

Accepted

## Context

회원 정보를 저장하여 로그인을 할 수 있는 기능을 제공하기 위해 Account Domain을 정의한다.

### 필수 요구사항
- 사용자의 정보는 다음과 같이 구성한다.
  - 이메일, 닉네임, 비밀번호, 이름, 전화번호
- 전화번호 인증을 통해 회원가입 할 수 있다.
- 사용자 식별 가능한 정보와 패스워드로 로그인을 할 수 있다.

## Decision

### 회원가입 정책
**Validation 정책**

- 올바른 이메일 형식만 지원한다. ex) account@account.com
- 닉네임의 경우 영문, 한글, 숫자를 허용한다.
- 패스워드의 경우 최소 6글자 최대 18글자로 구성하며 영문(대소문자)와 특수문자를 허용한다.
- 이름의 경우 영문, 한글만 허용한다.
- 올바른 전화번호 형식만 지원한다. ex) 01012341234

**식별 및 중복 정책**
- 동일한 이메일로 하나의 계정만 생성할 수 있다.
- 동일한 전화번호로 하나의 계정만 생성할 수 있다.
- 이메일, 전화번호중 하나라도 중복되는 경우 회원가입 할 수 없다.
- 닉네임, 이름의 경우 계정당 중복을 허용한다.

**회원가입 전화번호 인증 정책**
- 인증코드의 경우 랜덤한 숫자 6자리로 구성한다.
- 동일한 전화번호로 가입되지 않은 계정의 경우 해당 전화번호로 인증코드를 발급 받을수 있다.
- 발급받은 인증코드를 입력하여 인증을 완료할 수 있다.
- 발급받은 인증코드가 일치하지 않는 경우 에러가 발생한다.
- 인증코드가 발급되고 3분 이내 인증을 완료해야 한다.
- 인증코드를 발급한지 3분이 지난 경우 인증코드를 재발급 받아야 한다.
- 전화번호 인증한 사용자의 경우 동일한 이메일이 존재하지 않는다면 회원가입 할 수 있다.
- 전화번호 인증한 사용자의 경우 동일한 이메일이 존재하면 회원가입 할 수 없다.
- 전화번호 인증하지 않은 사용자의 경우 회원가입을 할 수 없다.

### Domain 설계

#### Domain 언어 정의
계정: Account
전화번호: Mobile
비밀번호: Password
이메일: Email
이름: Name
닉네임: NickName 
회원가입 인증코드: PinCode


### 회원가입 절차
1. Mobile PinCode 발급 요청 (VerifyRequest)
2. Mobile PinCode 인증 (Verified)
3. 회원정보 입력
  - 상태가 Verified이고 중복된 이메일 및 validation 정책 통과시 가입 가능
4. 완료

### PinCode를 별도의 Entity가 아닌 Value Object로 정의 및 계정의 상태 의사결정
PinCode의 경우 회원가입(Account 생성) 요청시 발급된다. 중복된 mobile이 있는 경우 발급되지 않는다. 즉 Account와 PinCode는 별도의 비즈니스 로직으로 
처리되는게 아닌 항상 Account의 mobile 정보와 결합되어 사용된다. 하지만 PinCode 발급은 회원가입이 완료된 상태가 아니므로 PinCode 발급은 사용자가 
회원가입을 요청한 상태로 정의한다. Mobile PinCode를 완료한 경우 Verified 상태이고 회원정보가 정상적으로 입력된 경우 Activated 상태로 정의한다.

#### Entity 정의
**Account**
```
Account {
  id,
  mobile,
  password,
  email,
  status,
  name,
  nickName,
  verificationCode,
  createdAt,
  updatedAt
}
```

- Account의 id는 GUID로 구성한다.
- PinCode의 경우 Account의 Value Object로 정의한다

**PinCode**
```
PinCode {
  mobile,
  code,
  status,
  expiresAt,
}
```

**Status 정의**
```
Status {
 INITIALIZED, VERIFICATION_REQUESTED, VERIFIED, ACTIVATED
}
```
- INITIALIZED: 계정 초기 생성 상태 
- VERIFICATION_REQUESTED: 회원가입 인증 요청 상태
- VERIFIED: 회원가입 인증 완료 상태
- ACTIVATED: 회원가입 완료 상태


## Consequences

- 동일한 Mobile 인증 요청을 여러번 하는 경우 제한 정책 결정 및 구현 방법 고려가 필요함