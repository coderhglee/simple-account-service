# Architecture

## Architecture Decision Records

동료들이 참고할 수 있도록, 설계 결정 사항을 문서화.
[decisions](decisions/)경로에, [template.md](decisions/templates/template.md) 양식으로 기록.
[adr-tools](https://github.com/npryce/adr-tools) 프로그램을 사용하여 새 문서 작성.

### adr-tools

설치

```sh
brew install adr-tools
```

새 결정사항 작성

```sh
# !! 프로젝트 루트 경로에서 !! #
adr new 'Decision to record'
```
