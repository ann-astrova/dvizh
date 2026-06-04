# dvizh

Spring Boot backend для платформы мероприятий.

## Структура проекта

```
src/main/java/com/example/demo/
├── DemoApplication.java   — точка входа, запуск приложения
├── entity/                — JPA-сущности (певращаются в таблицы в PostgreSQL)
├── enums/                 — перечисления (роли, статусы)
├── repository/            — интерфейсы доступа к БД (вместо скл хранимок)
├── service/               — бизнес-логика 
├── controller/            — REST API endpoints (HTTP-запросы)
├── dto/                   — объекты для JSON запросов/ответов
├── config/                — конфигурация Spring
└── exception/             — свои ошибки и их обработка (чтобы сопровождать ошибки комментариями)
```

## Путь потока данных

```
HTTP → controller → service → repository → entity → PostgreSQL
```