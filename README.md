# Multi-Stop 🚗

**Multi-Stop** is a carpooling REST API built with Java and Spring Boot. Drivers can publish journeys with multiple stopping points; passengers can search trips and reserve seats between selected pickup and drop-off stops.

**GitHub:** https://github.com/Parlog07/Multi-Stop

## Features

- Driver, passenger, and vehicle management
- Trip publishing with ordered intermediate stops, passing times, and cumulative prices
- Trip search by departure, destination, and required seats, with pagination
- Booking seats between two stops, with automatic fare calculation
- Seat availability checks and pessimistic locking during booking creation
- Booking cancellation with a 24-hour cutoff
- Request validation and structured error responses
- JUnit 5 and Mockito service-layer unit tests

## Tech stack

Java 17 · Spring Boot 4.1.2 · Spring MVC · Spring Data JPA · PostgreSQL · MapStruct · Lombok · Jakarta Validation · Maven · JUnit 5 · Mockito

The Maven project uses WAR packaging.

## Requirements

- JDK 17
- Docker with PostgreSQL, or a local PostgreSQL instance
- Git
- Maven Wrapper (included)

## Installation

### 1. Clone

```bash
git clone https://github.com/Parlog07/Multi-Stop.git
cd Multi-Stop
```

### 2. Database

The current `src/main/resources/application.properties` expects PostgreSQL at `localhost:5433`, database `multistop_db`, user `multistop_user`, and password `123`.

For local development, create a matching Docker container:

```bash
docker run --name multistop-postgres \
  -e POSTGRES_DB=multistop_db \
  -e POSTGRES_USER=multistop_user \
  -e POSTGRES_PASSWORD=123 \
  -p 5433:5432 -d postgres:16
```

If the container already exists, use `docker start multistop-postgres` instead.

> **Security:** These are development-only credentials. Replace hardcoded passwords with environment-based configuration before public deployment. The current JPA configuration uses `ddl-auto=update`, suitable for development rather than a production migration strategy.

### 3. Start the application

```bash
./mvnw spring-boot:run
```

The default local base URL is `http://localhost:8080` (unless overridden in your environment).

## API endpoints

### Drivers

| Method | Endpoint | Action |
|---|---|---|
| POST | `/api/drivers` | Create driver |
| GET | `/api/drivers` | List drivers |
| GET | `/api/drivers/{id}` | Get driver |
| PUT | `/api/drivers/{id}` | Update driver |
| DELETE | `/api/drivers/{id}` | Delete driver |

### Passengers

| Method | Endpoint | Action |
|---|---|---|
| POST | `/api/passengers` | Create passenger |
| GET | `/api/passengers` | List passengers |
| GET | `/api/passengers/{id}` | Get passenger |
| PUT | `/api/passengers/{id}` | Update passenger |
| DELETE | `/api/passengers/{id}` | Delete passenger |

### Vehicles

| Method | Endpoint | Action |
|---|---|---|
| POST | `/api/vehicles` | Create vehicle |
| GET | `/api/vehicles` | List vehicles |
| GET | `/api/vehicles/{id}` | Get vehicle |
| GET | `/api/vehicles/driver/{driverId}` | List vehicles by driver |
| PUT | `/api/vehicles/{id}` | Update vehicle |
| DELETE | `/api/vehicles/{id}` | Delete vehicle |

### Trips

| Method | Endpoint | Action |
|---|---|---|
| POST | `/api/trips` | Publish trip |
| GET | `/api/trips` | List trips |
| GET | `/api/trips/{id}` | Get trip |
| GET | `/api/trips/driver/{driverId}` | List trips by driver |
| GET | `/api/trips/search` | Search trips |

Example search:

```http
GET /api/trips/search?departure=Marrakech&destination=Casablanca&seats=2&page=0&size=10
```

Search defaults: `seats=1`, `page=0` (zero-based), `size=10`.

### Bookings

| Method | Endpoint | Action |
|---|---|---|
| POST | `/api/bookings` | Create booking |
| GET | `/api/bookings` | List bookings |
| GET | `/api/bookings/{id}` | Get booking |
| GET | `/api/bookings/passenger/{passengerId}` | List passenger bookings |
| GET | `/api/bookings/trip/{tripId}` | List trip bookings |
| PATCH | `/api/bookings/{id}/cancel` | Cancel booking |

## Example requests

Set `Content-Type: application/json` for JSON request bodies.

### Create driver

`POST /api/drivers`

```json
{
  "lastName": "Amine",
  "firstName": "Ayoub",
  "email": "ayoub@example.com",
  "phone": "0612345678",
  "licenseNumber": "LIC123"
}
```

### Create vehicle

`POST /api/vehicles`

```json
{
  "make": "Dacia",
  "model": "Logan",
  "registrationNumber": "12345-A-6",
  "color": "White",
  "capacity": 4,
  "driverId": 1
}
```

### Create passenger

`POST /api/passengers`

```json
{
  "lastName": "Bennani",
  "firstName": "Sara",
  "email": "sara@example.com",
  "phone": "0611223344"
}
```

### Publish trip

`POST /api/trips`

```json
{
  "vehicleId": 1,
  "seatsOffered": 3,
  "stoppingPoints": [
    {"city": "Marrakech", "pointOrder": 1, "passingTime": "2027-06-10T09:00:00", "priceFromDeparture": 0},
    {"city": "Benguerir", "pointOrder": 2, "passingTime": "2027-06-10T10:00:00", "priceFromDeparture": 40},
    {"city": "Casablanca", "pointOrder": 3, "passingTime": "2027-06-10T12:00:00", "priceFromDeparture": 100}
  ]
}
```

The times above are illustrative; use future dates when actually testing.

### Book seats

`POST /api/bookings`

```json
{
  "passengerId": 1,
  "tripId": 1,
  "pickUpPointId": 100,
  "dropOffPointId": 200,
  "numberOfSeats": 2
}
```

**Important:** Replace example IDs with IDs returned by your API. The pickup and drop-off IDs must belong to the same trip.

Fare calculation:

```text
totalAmount = (dropoff.priceFromDeparture - pickup.priceFromDeparture) × numberOfSeats
```

For example, two seats from Benguerir (40) to Casablanca (100) cost `(100 - 40) × 2 = 120` in the configured price units.

Cancel a booking using `PATCH /api/bookings/{id}/cancel`.

## Business rules

- Trips require at least two stops with valid order, increasing passing times, and increasing cumulative prices.
- Seats offered cannot exceed the assigned vehicle's capacity.
- Bookings require a published trip, a future pickup, and enough remaining seats.
- Pickup must precede drop-off, and both stops must belong to the trip.
- Confirmed bookings count against the trip's offered seats.
- Cancellation is allowed only **more than 24 hours before pickup**; already-cancelled bookings cannot be cancelled again.
- The current seat model counts bookings across the **whole trip**; it does not reuse seats between non-overlapping segments.

## Error handling

The API uses Spring `ProblemDetail` responses:

| Status | When |
|---|---|
| `400 Bad Request` | Invalid request data or `IllegalArgumentException` |
| `404 Not Found` | `ResourceNotFoundException` |
| `409 Conflict` | `BusinessException` |

Validation errors include an `errors` object with field messages.

## Testing

Service-layer unit tests use **JUnit 5 and Mockito**, with mocked dependencies for Driver, Passenger, Vehicle, Trip, and Booking services.

```bash
./mvnw test
```

Build the application:

```bash
./mvnw clean package
```

## Project structure

```text
src/
├── main/
│   ├── java/com/multi/multistop/
│   │   ├── controller/
│   │   ├── dto/request/
│   │   ├── dto/response/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── exception/
│   │   ├── mapper/
│   │   ├── repository/
│   │   └── service/imp/
│   └── resources/application.properties
└── test/java/com/multi/multistop/service/
```

## Author

**Parlog07** — https://github.com/Parlog07

---

*Multi-Stop — a Spring Boot carpooling REST API.*
