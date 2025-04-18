
---

# Academic Counseling System

## Overview

This **Academic Counseling System** is a web-based application developed for UPTM students and counselors. The system facilitates appointment booking, counselor feedback, event management, and more. It features a chatbot, **Sophie**, to assist students and counselors with various functions.

## Features

- **Student Login** using Google OAuth
- **Counselor Login** using Google OAuth
- **Appointment Booking** for students
- **Counselor Feedback** after appointment sessions
- **Event Management** for counselors to create and manage events
- **Analytics Tool** for appointment and student wellness insights
- **Chatbot (Sophie)** to assist students and counselors with frequently asked questions

## Tech Stack

- **Frontend**: React.js
- **Backend**: Spring Boot
- **Database**: MySQL
- **Chatbot**: Rasa
- **Authentication**: Google OAuth

## Setup & Installation

Follow these steps to set up and run the project locally:

### Prerequisites

- **JDK** (Java Development Kit) version 11 or higher
- **Node.js** and **npm** for React
- **MySQL** for the database
- **Maven** for building the Spring Boot project

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/academic-counseling-system.git
cd academic-counseling-system
```

### 2. Setup Backend (Spring Boot)

- Navigate to the `backend-appointments` folder.

```bash
cd backend-appointments
```

- Configure the MySQL database connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/academic_counseling_db
spring.datasource.username=root
spring.datasource.password=yourpassword
```

- Build and run the Spring Boot application:

```bash
./mvnw spring-boot:run
```

### 3. Setup Frontend (React)

- Navigate to the `frontend` folder:

```bash
cd frontend
```

- Install the required dependencies:

```bash
npm install
```

- Build the React app:

```bash
npm run build
```

- The build files will be placed in the `backend-appointments/src/main/resources/static` directory for serving by Spring Boot.

### 4. Run the Application

Run the Spring Boot backend and the React frontend (since it's already integrated in the backend). Open your browser and go to `http://localhost:8080`.

### 5. Accessing the Application

- For **students**, login using the Google student email.
- For **counselors**, login using the Google counselor email.

### 6. Available Endpoints

- **/appointment**: The page where students can book appointments.
- **/counselor-dashboard**: Dashboard for counselors to manage appointments, feedback, and events.

## Running Tests

- Run the backend tests using Maven:

```bash
./mvnw test
```

- Run the frontend tests using:

```bash
npm test
```

## Contributing

We welcome contributions! Feel free to fork this project, open issues, and submit pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

