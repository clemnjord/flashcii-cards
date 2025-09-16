# Copilot Instructions for Flashcii Cards

## Project Overview

Flashcii Cards is an open-source, self-hostable flashcard learning application designed to use AsciiDoc format (future goal). The project is a hobbyist endeavor in early development that follows Clean Architecture principles.

**Repository size**: ~75 Java source files, React TypeScript frontend, modular Maven backend
**Technology stack**: 
- Backend: Java 21, Spring Boot 3.5.4, Maven, H2 database, Clean Architecture
- Frontend: React 18, TypeScript, Tailwind CSS, Node.js 20+
- Infrastructure: Docker, Docker Compose

## Critical Build Requirements

### Java Backend Requirements
- **Java 21 is REQUIRED** - The Maven enforcer plugin will fail builds with Java 17 or lower
- Maven 3.0+ is required
- All backend modules use Java 21 features and Spring Boot 3.5.4

### Frontend Requirements
- Node.js 20+ (tested with v20.19.5)
- npm 10+ (tested with v10.8.2)

## Build Instructions

### Backend Build (Maven Multi-Module)
**ALWAYS ensure Java 21 is available before attempting backend builds**

```bash
# From repository root
cd backend

# Clean build (will fail with Java < 21)
mvn clean compile

# Run tests 
mvn test

# Package all modules
mvn clean package

# Run the web application (from web module)
cd web
mvn spring-boot:run
```

**Backend server runs on port 8080** with H2 database file storage at `./data/flashciicards-db`

**Database configuration**:
- H2 file-based database at `jdbc:h2:file:./data/flashciicards-db`
- Username: `sa`, Password: `password`
- Auto-schema creation enabled (`ddl-auto: update`)
- SQL logging enabled for development

### Frontend Build
```bash
# From repository root
cd frontend

# Install dependencies (~60s, expect npm warnings about deprecated packages)
npm install

# Development server (port 3000)
npm start

# Production build (~25s)
npm run build

# Tests (currently no tests exist - returns exit code 1)
npm test --passWithNoTests  # Use this flag to avoid exit code 1
```

### Docker Issues
**CRITICAL**: The `docker-compose.yml` file references a backend Dockerfile that does NOT exist. Docker Compose will fail to build the backend service. The missing Dockerfile is expected at `backend/Dockerfile`.

The frontend Dockerfile exists and works correctly.

## Project Architecture

### Backend Architecture (Clean Architecture)
```
backend/
├── pom.xml                    # Parent POM with Java 21 requirement
├── domain/                    # Core business logic, entities
├── application/               # Use cases, application services  
├── infrastructure/            # JPA repositories, external adapters
├── spring-shared/             # Shared Spring configuration
└── web/                       # REST controllers, Spring Boot app
    ├── src/main/java/com/clemnjord/flashcii/web/
    │   ├── WebApplication.java        # Main Spring Boot application
    │   ├── controller/DeckController.java  # REST endpoints
    │   └── config/ApplicationConfiguration.java
    └── src/main/resources/application.yml  # Spring configuration
```

**Main application class**: `backend/web/src/main/java/com/clemnjord/flashcii/web/WebApplication.java`

### Frontend Architecture
```
frontend/
├── package.json               # React app dependencies
├── src/
│   ├── index.tsx             # React app entry point
│   ├── App.tsx               # Main app component
│   ├── components/           # Reusable components
│   └── api/                  # Backend API integration
├── public/index.html         # HTML template
└── Dockerfile               # Container build (works)
```

## Testing Strategy

### Backend Tests
- **Unit tests**: Domain models, use cases (application layer)
- **Web layer tests**: `@WebMvcTest` with MockMvc for REST controllers
- **Test framework**: JUnit 5 + AssertJ + Mockito
- **Run tests**: `mvn test` (from backend directory)
- **No integration tests**: No IT.java files or test profiles exist

### Frontend Tests  
- **No tests currently exist** - `npm test` will exit with code 1
- **Workaround**: Use `npm test --passWithNoTests` to avoid failures
- **Test framework configured**: Jest + React Testing Library (unused)

## CI/CD Pipeline

### GitHub Actions
- **Qodana code quality scan** runs on PRs and pushes to main/releases branches
- Located: `.github/workflows/qodana_code_quality.yml`
- Uses JetBrains Qodana for static analysis
- Requires `QODANA_TOKEN_1864697221` secret

### Code Quality Tools
- **Qodana configuration**: `qodana.yaml` - JVM linter with recommended profile
- **Lefthook configuration**: `lefthook.yml` - Git hooks (examples only, not active)

## Common Issues and Workarounds

### Java Version Issues
- **Error**: "Detected JDK version X is not in the allowed range [21,)"
- **Solution**: Install and use Java 21 (OpenJDK or similar)
- **Check version**: `java -version`

### Docker Compose Issues  
- **Error**: Backend service fails to build (missing Dockerfile)
- **Workaround**: Run backend with `mvn spring-boot:run` instead of Docker
- **Volume warning**: `./example/rendered_asciidoc` directory doesn't exist but is referenced in volume mount
- **Frontend container**: Builds successfully using `frontend/Dockerfile`

### Frontend Build Warnings
- Multiple npm deprecation warnings are expected and can be ignored
- Browserslist outdated warnings can be ignored for development
- babel-preset-react-app maintenance warnings are expected

### Database
- H2 database auto-creates at `./data/flashciicards-db` 
- SQL logging enabled in development
- Database file persists between runs

## API Documentation
- Swagger UI available when backend runs: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec configured in `WebApplication.java`

## Environment Variables

### Frontend
- `REACT_APP_BACKEND_ADDRESS`: Backend host (default: localhost)
- `REACT_APP_BACKEND_PORT`: Backend port (default: 8080)

### Backend
- `FLASHCII_CARDS_PATH`: Path for card storage (used in Docker setup)

## Key Files Reference

### Root Directory
- `README.adoc` - Project overview and tech stack
- `docker-compose.yml` - Container orchestration (backend Dockerfile missing)
- `lefthook.yml` - Git hooks configuration (examples only)
- `qodana.yaml` - Code quality analysis config
- `.gitignore` - Excludes .vscode/, *.db, .idea/, **/target/

### Configuration Files
- `backend/pom.xml` - Maven parent POM with all dependencies
- `backend/web/src/main/resources/application.yml` - Spring Boot config
- `frontend/package.json` - Node.js dependencies and scripts  
- `frontend/tailwind.config.js` - Tailwind CSS configuration

## Trust These Instructions
These instructions have been validated by running actual build commands and examining the codebase structure. Only search for additional information if these instructions are incomplete or proven incorrect. The Java 21 requirement and missing backend Dockerfile are confirmed issues that must be addressed for successful builds.