# How to Run Cineverse Ultimate

A premium movie ticket booking platform built from scratch with Java Spring Boot.

## Prerequisites
-   **Java 17+**: Ensure Java is installed (`java -version`).
-   **MySQL**: Ensure MySQL Server is running (`mysql --version`).

## Configuration
1.  **Database Setup**:
    Open your terminal/command prompt and create the database:
    ```bash
    mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS cineverse_db;"
    ```
2.  **App Configuration**:
    Check `src/main/resources/application.properties` to ensure your MySQL credentials match:
    ```properties
    spring.datasource.username=root
    spring.datasource.password=root
    ```

## Running the Application (VS Code)

**Method 1: Using the Java Extension Pack (Recommended)**
1.  Open the project folder in VS Code.
2.  Wait for the Java extensions to load (you will see "Importing project" at the bottom right).
3.  Open `src/main/java/com/example/cineverse/CineverseApplication.java`.
4.  You will see a small **Run | Debug** button appear just above the `public static void main` line.
5.  Click **Run**.

**Method 2: Using the Maven Sidebar**
1.  Click the **Maven** icon (M) in the left sidebar.
2.  Expand **CineverseUltimate** > **Lifecycle**.
3.  Right-click **spring-boot:run** and select **Run**.

*(Note: The first time you run it, it might take a minute to download dependencies. Subsequent runs will be faster.)*

**Using VS Code**
1.  Open the project folder.
2.  Go to **Run and Debug**.
3.  Click the **Play** button.

## Accessing the App
-   **Home Page**: [http://localhost:8081](http://localhost:8081)
-   **Admin Panel**: [http://localhost:8081/admin](http://localhost:8081/admin)

### Login Credentials
| Role | Email | Password |
| :--- | :--- | :--- |
| **Admin** | `admin@cineverse.com` | `admin123` |
| **User** | `lakshmi@example.com` | `lakshmi123` |
