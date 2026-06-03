# Scope
---

## Admin: модерация мероприятий

### Подтвердить мероприятие

`POST /admin/events/{id}/approve`

**Ответ (200 OK):**

```json
{
  "event_id": "uuid",
  "status": "approved",
  "moderated_by": "admin-uuid",
  "updated_at": "2025-03-16T16:20:00Z"
}
```

**Логика:**
- Только роль `admin`
- Статус события: `pending` → `approved`
- Записать `moderated_by` и `updated_at`

---

### Отклонить мероприятие

`POST /admin/events/{id}/reject`

**Ответ (200 OK):**

```json
{
  "event_id": "c4d3e2f1-0a9b-8c7d-6e5f-4a3b2c1d0e9f",
  "status": "rejected",
  "moderated_by": "7c9e6679-7425-40de-944b-e07fc1f90ae7",
  "updated_at": "2025-03-16T16:20:00Z"
}
```

**Логика:**
- Только роль `admin`
- Статус события: `pending` → `rejected`
- Записать `moderated_by` и `updated_at`

---

## Admin: подтверждение посещения

### Подтвердить посещение

`POST /admin/events/{id}/attendance/{user_id}`

**Тело запроса (опционально):**

Админ указывает, сколько виртуальной валюты начислить студенту за это посещение. Тело можно не передавать - тогда начисление **0**, баланс не меняется.

```json
{
  "reward_amount": 25
}
```

**Ответ (200 OK):**

```json
{
  "event_id": "uuid",
  "user_id": "uuid",
  "status": "attended",
  "attended_at": "2025-03-16T18:00:00Z"
}
```

**Логика:**
- Только роль `admin`
- Участие: `registered` → `attended` (или `planned` → `attended`, если допустимо по правилам)
- Записать `confirmed_by`, `confirmed_at` в `event_participants`

**После подтверждения:**
- Начисление `reward_amount` на `user.balance` (если `> 0`)
- Проверка и автовыдача достижений (см. ниже)
- Создание уведомлений (см. раздел «Уведомления»)

**Достижения (временная логика, TODO - критерии из БД):**

| Условие | `title` в таблице `achievements` |
|---------|----------------------------------|
| 1-е посещение (`attended`) | `Первое мероприятие` |
| ≥ 5 посещений | `5 мероприятий` |

Если строки с таким `title` в БД нет - ачивка не выдаётся, **основная транзакция не откатывается**.

При `approve` создателю события проверяется ачивка «первое одобренное созданное» - `title`: `Создал мероприятие` (та же схема: нет в БД - пропуск без ошибки).


---


## Пользователь: личные данные

### Мои мероприятия (на которые записан)

`GET /me/events`

**Ответ (200 OK):**

```json
{
  "events": [
    {
      "event_id": "uuid-1",
      "title": "IT-конференция 2025",
      "status": "approved",
      "participation_status": "registered"
    }
  ]
}
```

**Логика:**
- Текущий авторизованный пользователь
- Список из `event_participants` + данные события
- `participation_status`: `planned`, `registered`, `attended`

---

### Мои созданные мероприятия

`GET /me/created-events`

**Ответ (200 OK):**

```json
{
  "events": [
    {
      "event_id": "uuid-2",
      "title": "Воркшоп по Go",
      "status": "pending",
      "created_at": "2025-03-16T15:00:00Z"
    }
  ]
}
```

**Логика:**
- Текущий пользователь как `creator` в `event`

---

### Мои достижения

`GET /me/achievements`

**Ответ (200 OK):**

```json
{
  "achievements": [
    {
      "achievement_id": "d5e4f3a2-b1c0-4d3e-8f9a-0b1c2d3e4f5a",
      "title": "Первое мероприятие",
      "description": "Посетил первое мероприятие",
      "reward_amount": 10,
      "awarded_at": "2025-02-20T14:00:00Z"
    }
  ]
}
```

**Логика:**
- Текущий пользователь
- Join `user_achievements` + `achievements`

