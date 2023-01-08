# 2. password-encryption

Date: 2023-01-07

## Status

Accepted

## Context

사용자의 패스워드를 보호하기 위해 암호화 하여 DB에 저장한다.

한번 암호화 하여 저장된 패스워드는 다시 복호화할 필요가 없으므로 단방향 암호화하여 저장한다.

## Decision

### 암호화 조건

- 해시 함수를 반복해서 수행(key stretching)하여 패스워드를 추측하는데 시간이 오래 소요되도록 구성한다.
- random으로 생성된 aalt값을 추가하여 salt + password으로 hash 값을 생성한다.

대표적으로 PBKDF2와 bcrypt를 비교하고 선정한다.

PBKDF2
- iteration을 통해 반복하여 해시 함수를 수행하여 키 해석 시간을 늦출수 있다.
- SHA 해시 알고리즘을 기반으로 암호화를 수행한다.
- salt 값을 추가하여 구성할 수 있다.

Bcrypt
- cost factor를 사용하여 2^n 번 반복하여 해싱 암호화를 수행한다.
- blowfish 암호화 알고리즘을 기반하여 암호화 한다.
- salt 값을 추가하여 구성할 수 있다.

두 방법 모두 salt 값과 암호화된 hash값을 저장하여 단일 문자열로 저장할 수 있다.
SHA 방법 보다 blowfish를 복호화 하는 시간이 더 소요되는 이점이 있다.

두개의 방식에서 옮고 그름을 따질 수 없기에 KISA 표준에서 가이드 되는 SHA256 알고리즘을 사용하여 PBKDF2를 사용하여 비밀번호를 암호화 한다.

## Consequences

