# Design: Endpoint de Health via Spring Actuator

Data: 2026-08-12

## Objetivo

Expor um endpoint de health (`/actuator/health`) na API para verificação de liveness e readiness por orquestradores (Docker) e load balancers.

## Decisões

| Aspecto | Decisão |
|---|---|
| Implementação | Spring Boot Actuator (sem controller manual) |
| Endpoints expostos | Somente `/actuator/health` |
| Path | Padrão do Actuator (`/actuator/health`) |
| Detalhes na resposta | `never` — resposta mínima `{"status":"UP"}` |
| Autenticação | Nenhuma (uso interno/infraestrutura) |
| Banco de dados | Verificado internamente pelo `DataSourceHealthIndicator` do Spring Boot |

## Mudanças

### 1. `pom.xml`

Adicionar dependência:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### 2. `src/main/resources/application.properties`

Adicionar:

```properties
management.endpoints.web.exposure.include=health
management.endpoint.health.show-details=never
```

## Comportamento esperado

- `GET /actuator/health` → `200` com corpo `{"status":"UP"}` quando app e banco estão saudáveis.
- `GET /actuator/health` → `503` com corpo `{"status":"DOWN"}` quando o banco (ou outra dependência monitorada) falha.

Nenhum código Java novo é necessário — o Actuator detecta o `DataSource` e o expõe como health indicator automaticamente.

## Verificação

- O teste `@SpringBootTest contextLoads` já existente valida que o contexto sobe com a nova dependência e configuração.
- Confirmação manual: subir a aplicação e consultar `GET /actuator/health`.

## Fora de escopo

- Exporem outros endpoints do Actuator (metrics, env, logs etc.).
- Autenticação no endpoint.
- Path customizado.