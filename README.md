<h1 align=center>Java Antivirus Server</h1>

## Server Side of Antivirus

[Task](./TASK.md) completed as part of the educational project for the course "Information Security from Malware".

[Задание](./TASK.md) выполнено в рамках учебного проекта по дисциплине "Защита информации от вредоносного ПО".

## Description
[**EN**] | Server side of antivirus is a Java application that provides a REST API for scanning files for viruses and managing user licenses. The server uses Spring Boot framework and MySQL database to store user information and their licenses.

[**RU**] | Серверная часть антивируса написана на Java с использованием Spring Boot. Она предоставляет REST API для взаимодействия с клиентами и обработки файлов. Используемая база данных - MySQL. Обеспечивает хранение информации о пользователях и их лицензиях.

## Project Structure

- `antivirus`
  - `src`
    - `main`
      - `java/ru.mtuci.antivirus`
        - `configs` - Конфигурация Spring Security
        - `controllers` - REST контроллеры
        - `entities` - Сущности базы данных
        - `repositories` - Репозитории для работы с базой данных
        - `services` - Сервисы для обработки логики приложения
        - `utils` - Access\Refresh логика
    - `resources`
      - `application.properties` - Конфигурация приложения
      - `static`
        - `sqlPayloads` - SQL скрипты для создания базы данных

## Getting Started
1. Убедитесь, что у вас установлен Java 17 и MySQL.
2. Скачайте проект с GitHub.
3. Создайте базу данных MySQL с именем `antivirus`.
4. Откройте IntelliJ IDEA и импортируйте проект.
5. Настройте файл `application.properties` с вашими данными для подключения к базе данных.
6. Запустите приложение через IDE 
7. Готово! Серверная часть антивируса запущена и готова к работе.

## Technologies
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Lombok
- Maven
- Postman
- Dbeaver

## Contributors

<table>
    <tbody>
        <tr>
            <td>
                <img width=50 src="https://avatars.githubusercontent.com/u/130181963"/>
            </td>
            <td>
                <a href = "t.me/wumpochuck"><b>wumpochuck</b></a>
                <br>
            </td>
        </tr>
    </tbody>
</table>
