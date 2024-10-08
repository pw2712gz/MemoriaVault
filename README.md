# MemoriaVault

MemoriaVault is a photo management web application that allows users to upload, view, and manage their photos
efficiently. Built with Spring Boot for the backend.

https://github.com/pw2712gz/MemoriaVault/assets/123153833/701c640a-bad4-4f9e-9869-b40757033cc4

## Features

- **Photo Upload**: Users can upload photos through a simple interface.
- **Photo Gallery**: Uploaded photos are displayed in a responsive gallery.
- **Delete Photos**: Users can delete photos from the gallery.

## Technologies Used

- Spring Boot
- Spring Data JPA
- MySQL
- HTMX

## Getting Started

### Prerequisites

- Java 11 or later
- Maven
- MySQL

### Setup

1. **Clone the repository**

   ```sh
   git clone https://github.com/pw2712gz/MemoriaVault
   cd MemoriaVault
   ```

2. **Database Configuration**

   - Create a MySQL database named `memoriavault`.
   - Update `src/main/resources/application.properties` with your database username and password.

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/memoriavault?useSSL=false&serverTimezone=UTC
   spring.datasource.username=<yourUsername>
   spring.datasource.password=<yourPassword>
   ```

3. **Build and Run the Application**

   Use Maven to build and run the application.

   ```sh
   mvn spring-boot:run
   ```

### Accessing the Application

Open your web browser and go to `http://localhost:8080/memoriavault`. You should see the MemoriaVault interface ready
for use.

## Contributing

I welcome contributions to MemoriaVault.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
