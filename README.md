# File Management System

A comprehensive document management system built with Spring Boot that allows users to upload, download, search, and manage files through both a web interface and a REST API.

## Features

- Upload files with optional descriptions
- View a list of all uploaded files
- Download files
- Search for files by name
- Delete files
- Responsive web interface
- RESTful API for programmatic access

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- Thymeleaf for server-side templating
- Bootstrap 5 for responsive UI
- H2 Database (can be configured for other databases)
- Maven for dependency management

## Setup and Installation

### Prerequisites

- Java 17 or higher
- Maven

### Steps

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/filemanagement.git
   cd filemanagement
   ```

2. Configure the application:
   
   Edit `src/main/resources/application.properties` to set your preferred configuration:
   
   ```properties
   # File storage location (create this directory or change to your preferred location)
   file.upload-dir=./uploads
   
   # Database configuration (default is H2 in-memory database)
   spring.datasource.url=jdbc:h2:mem:filedb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=password
   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
   
   # Enable H2 console (optional, for development)
   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console
   
   # Maximum file size
   spring.servlet.multipart.max-file-size=10MB
   spring.servlet.multipart.max-request-size=10MB
   ```

3. Build the project:
   ```
   mvn clean install
   ```

4. Run the application:
   ```
   mvn spring-boot:run
   ```

5. Access the application:
   - Web Interface: http://localhost:8080
   - API: http://localhost:8080/api/v1/files
   - H2 Console (if enabled): http://localhost:8080/h2-console

## Usage

### Web Interface

1. **Home Page**: Navigate to http://localhost:8080 to see a list of all uploaded files.
2. **Upload Files**: Click on the "Upload File" button to go to the upload page.
3. **Download Files**: Click on the "Download" button next to any file to download it.

### REST API

The application provides a RESTful API for programmatic access:

#### Upload a File

```
POST /api/v1/files/upload
```

Request:
- Form-data:
  - `file`: The file to upload
  - `description` (optional): Description of the file

Response:
```json
{
  "fileName": "627b9ee7-856a-4ce9-a799-1b03977ac368Armand-Njapou-Resume.pdf",
  "fileDownloadUri": "http://localhost:8080/api/v1/files/download/627b9ee7-856a-4ce9-a799-1b03977ac368Armand-Njapou-Resume.pdf",
  "fileType": "application/pdf",
  "size": 12345
}
```

#### Download a File

```
GET /api/v1/files/download/{fileName}
```

Response: The file as a downloadable resource.

#### Get All Files

```
GET /api/v1/files/all
```

Response:
```json
[
  {
    "id": 1,
    "fileName": "627b9ee7-856a-4ce9-a799-1b03977ac368Resume.pdf",
    "originalFileName": "Resume.pdf",
    "fileType": "application/pdf",
    "fileSize": "12345",
    "filePath": "/path/to/file",
    "description": "My resume",
    "uploadTime": "2023-05-20T15:30:45"
  }
]
```

#### Search Files

```
GET /api/v1/files/search/{fileName}
```

Response: List of files matching the search criteria.

#### Delete a File

```
DELETE /api/v1/files/{id}
```

Response: No content (204) if successful.

## Project Structure

```
src/main/java/com/cwa/filemanagement/
├── config/                  # Configuration classes
│   └── FileStorageProperties.java
├── controller/              # Web and API controllers
│   ├── FileController.java  # REST API endpoints
│   └── WebController.java   # Web interface controllers
├── dto/                     # Data Transfer Objects
│   ├── ApiResponse.java
│   └── FileUploadResponse.java
├── entity/                  # JPA entities
│   └── FileEntity.java
├── exception/               # Custom exceptions
│   ├── FileNotFoundException.java
│   ├── FileStorageException.java
│   └── GlobalExceptionHandler.java
├── repository/              # Data access layer
│   └── FileRepository.java
├── service/                 # Business logic
│   ├── FileService.java     # Service interface
│   └── impl/
│       └── FileServiceImpl.java
└── FilemanagementApplication.java  # Main application class

src/main/resources/
├── application.properties   # Application configuration
└── templates/               # Thymeleaf templates
    ├── index.html           # File listing page
    └── upload.html          # File upload page
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.