
# User Service

Консольное Java-приложение для управления пользователями с использованием Hibernate и PostgreSQL.

## Используемые технологии

- Java 21
- Maven
- Hibernate ORM 6
- PostgreSQL 17
- Hibernate Validator
- SLF4J и Logback
- JUnit 5

## Возможности приложения

Приложение поддерживает основные CRUD-операции:

- **Create** - создание нового пользователя.
- **Read** - поиск пользователя по идентификатору и получение списка всех пользователей.
- **Update** - изменение данных пользователя.
- **Delete** - удаление пользователя.

Дополнительные возможности:

- Проверка корректности пользовательских данных.
- Проверка уникальности адреса электронной почты.
- Обработка ошибок базы данных.
- Логирование операций приложения.
- Управление транзакциями Hibernate.
- Покрытие функциональности модульными и интеграционными тестами.

## Структура сущности User

| Поле | Тип | Описание |
|---|---|---|
| id | Long | Уникальный идентификатор пользователя |
| name | String | Имя пользователя |
| email | String | Адрес электронной почты |
| age | Integer | Возраст пользователя |
| createdAt | LocalDateTime | Дата и время создания записи |

## Структура проекта

```text
user-service/
├── logs/
│   └── user-service.log
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── userservice/
│   │   │               ├── config/
│   │   │               │   └── HibernateUtil.java
│   │   │               ├── dao/
│   │   │               │   └── UserDao.java
│   │   │               ├── entity/
│   │   │               │   └── User.java
│   │   │               ├── exception/
│   │   │               │   ├── DatabaseException.java
│   │   │               │   └── DuplicateEmailException.java
│   │   │               ├── service/
│   │   │               │   └── UserService.java
│   │   │               └── Main.java
│   │   └── resources/
│   │       ├── hibernate.cfg.xml
│   │       └── logback.xml
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── userservice/
│                       └── service/
│                           └── UserServiceTest.java
├── pom.xml
├── README.md
└── .gitignore
```

## Требования для запуска

Перед запуском приложения необходимо установить:

- JDK 21 или новее.
- PostgreSQL 17.
- Maven.

## Настройка базы данных

1. Создать базу данных PostgreSQL с названием `user_service`.
2. Настроить переменные окружения:

| Переменная | Описание |
|---|---|
| DB_URL | URL подключения к PostgreSQL |
| DB_USERNAME | Имя пользователя базы данных |
| DB_PASSWORD | Пароль пользователя базы данных |

Пример значений:

```text
DB_URL=jdbc:postgresql://localhost:5432/user_service
DB_USERNAME=postgres
DB_PASSWORD=your_password
```

Вместо `your_password` необходимо указать собственный пароль.

## Запуск приложения

1. Убедиться, что PostgreSQL запущен.
2. Проверить наличие базы данных `user_service`.
3. Настроить необходимые переменные окружения.
4. Убедиться, что файл `hibernate.cfg.xml` настроен корректно.
5. Запустить класс `Main` через IntelliJ IDEA.

## Запуск тестов

Для запуска тестов с помощью Maven выполнить команду:

```bash
mvn test
```

Тесты проверяют валидацию данных, CRUD-операции, обработку ошибок и взаимодействие с базой данных.

## Логирование

Приложение сохраняет логи:

- В консоль.
- В файл `logs/user-service.log`.
