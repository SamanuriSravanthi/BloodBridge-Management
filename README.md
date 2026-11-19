# SAVE LIFE - Blood Donation Management System

A Java 21 and Spring Boot web application for donor registration, donor profiles, blood requests, donation history, and basic administrator request management.

## Requirements

- Java 21
- MySQL with a database named `blood_donation`

The project uses the existing MySQL database. Hibernate creates or updates the application's tables when the application starts.

## Configure MySQL

The application defaults to `localhost:3306`, database `blood_donation`, and username `root`. Set these environment variables if your local MySQL settings are different. Do not put database passwords in source files.

In PowerShell, for the current terminal:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/blood_donation"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-local-mysql-password"
```

If your MySQL root account has no password, leave `DB_PASSWORD` unset.

## Run the application

From the `blood-donation` project folder:

```powershell
.\mvnw.cmd spring-boot:run
```

Then open `http://localhost:8080`.

## Create an administrator

Donor accounts are created through the registration page. Passwords need at least 12 characters and must not exceed BCrypt's 72-byte limit. To create the first administrator, set both variables before starting the application:

```powershell
$env:APP_ADMIN_EMAIL = "admin@example.com"
$env:APP_ADMIN_PASSWORD = "use-a-unique-password-with-at-least-12-characters"
.\mvnw.cmd spring-boot:run
```

The administrator account is created only if that email is not already registered. Its password is stored as a BCrypt hash. Keep the environment variables private.

## Run tests

```powershell
.\mvnw.cmd test
```

Tests use an in-memory H2 database, so they do not need a running MySQL server.

## Main pages

- `/` - home page and recent open blood requests
- `/register` and `/login` - donor sign-up and sign-in
- `/dashboard` - donor profile summary and donation history
- `/profile` - update blood group, city, phone, and availability
- `/requests` - view open requests
- `/requests/new` - submit a blood request (donors only)
- `/admin` - review requests and update their status (administrators only)
