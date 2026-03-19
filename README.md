# 🌍 NationScope

> A REST API that aggregates real-world country data from multiple sources and generates AI-powered executive socioeconomic reports using Google Gemini.

Built to demonstrate proficiency in REST API consumption and design, exception handling, AI integration, SOLID principles, and design patterns in a Spring Boot ecosystem.

---

## 📌 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Environment Variables](#environment-variables)
  - [Run Locally (without Docker)](#run-locally-without-docker)
  - [Run with Docker](#run-with-docker)
- [API Reference](#api-reference)
- [Example Response](#example-response)
- [External APIs](#external-apis)
- [Design Decisions](#design-decisions)
- [Project Goals](#project-goals)

---

## Overview

NationScope fetches data about any country in the world — general info, economic indicators, and social indicators — and uses **Google Gemini** to generate a structured executive report analyzing the country's socioeconomic stability and outlook.

The response includes:
- 🏳️ General country data (population, area, capital, languages, currencies, etc.)
- 📈 Economic indicators (GDP, inflation, growth rate, unemployment, public debt)
- 👨‍👩‍👧 Social indicators (HDI, literacy rate, life expectancy, poverty rate, education index)
- 🤖 AI-generated executive report with stability score and future projections

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| Build Tool | Maven |
| AI Integration | Google Gemini API (`google-genai`) |
| Caching | Redis + Spring Cache |
| Resilience | Resilience4J (Circuit Breaker) |
| HTTP Client | Spring RestClient |
| Code Generation | Lombok |
| Testing | JUnit 5 + Testcontainers |
| Containerization | Docker + Docker Compose |

---

## Architecture

NationScope follows an **Orchestrator Pattern** designed for scalability. The core flow is:

```
Controller
    └── OrchestratorImpl
            ├── AggregationService      → Builds the Country entity
            │       ├── CountryDataProvider
            │       │       ├── CountriesClient     (RestCountries API)
            │       │       └── WorldBankClient     (World Bank API)
            │       └── DevelopmentIndexService     (Calculates HDI, Education Index)
            └── AnalyzeService          → Sends data to Gemini and returns the report
```

**Why this architecture?**

The Orchestrator acts as a coordinator: it delegates country data collection to `AggregationService` and analysis to `AnalyzeService`. This separation means adding new features (e.g., sentiment analysis, historical comparisons) only requires implementing a new service — the orchestrator handles the composition without modification (Open/Closed Principle).

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- Docker and Docker Compose
- A [Google AI Studio](https://aistudio.google.com/) account to obtain a **Gemini API key**

---

### Environment Variables

| Variable | Description |
|---|---|
| `GOOGLE_API_KEY` | Your Gemini API key from Google AI Studio |

---

### Run Locally (without Docker)

**1. Start a Redis container:**

```bash
docker run -d --name redis -p 6379:6379 redis
```

**2. Set your Gemini API key:**

```bash
# Linux / macOS
export GOOGLE_API_KEY=your_gemini_api_key_here

# Windows (PowerShell)
$env:GOOGLE_API_KEY="your_gemini_api_key_here"
```

**3. Build and run the application:**

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

### Run with Docker

The entire stack (app + Redis) can be launched with a single command.

**1. Build the Docker image:**

```bash
docker build -t nationscope .
```

**2. Start all services:**

```bash
GOOGLE_API_KEY=your_gemini_api_key_here docker compose up
```

Or create a `.env` file in the project root:

```env
GOOGLE_API_KEY=your_gemini_api_key_here
```

Then run:

```bash
docker compose up
```

---

## API Reference

### Get Country Analysis

Fetches aggregated country data and generates an AI-powered socioeconomic report.

```
GET /api/v1/countries/{countryName}
```

**Path Parameters**

| Parameter | Type | Description |
|---|---|---|
| `countryName` | `string` | Full country name in English (e.g., `colombia`, `germany`) |

Note: If country's name is composed, you must add "%20" between the individual names.

**Responses**

| Status | Description |
|---|---|
| `200 OK` | Success — returns country data and AI analysis |
| `400 Bad Request` | Country not found or invalid input |
| `409 Conflict` | Data not available for this country from World Bank |
| `502 Bad Gateway` | Invalid response from World Bank API |
| `500 Internal Server Error` | Unexpected error |

**Error Response Body**

```json
{
  "timestamp": "2025-01-01T12:00:00",
  "status": 400,
  "message": "Country not found or mistyped",
  "errors": null
}
```

---

## Example Response

**Request:**
```
GET /api/v1/countries/colombia
```

**Response:**
```json
{
  "countryData": {
    "name": "Colombia",
    "capital": ["Bogotá"],
    "continents": ["South America"],
    "area": 1141748.0,
    "population": 53057212,
    "timeZones": ["UTC-05:00"],
    "languages": { "spa": "Spanish" },
    "currencies": {
      "COP": { "name": "Colombian peso", "symbol": "$" }
    },
    "economicIndicators": {
      "gdp": 418818154879.27,
      "growthRate": 1.60,
      "inflation": 6.61,
      "unemployment": 8.29,
      "publicDebt": 71.48
    },
    "socialIndicators": {
      "literacyRate": 95.34,
      "lifeExpectancy": 77.73,
      "hdi": 0.825,
      "povertyRate": 7.70,
      "educationIndex": 0.800
    }
  },
  "analysis": {
    "data": "**EXECUTIVE REPORT: SOCIOECONOMIC STABILITY AND OUTLOOK FOR COLOMBIA**\n\n**1. General Context**\nColombia represents a significant economic entity in South America...\n\n**3. Stability Score: 7.0 / 10**\n..."
  }
}
```

---

## External APIs

| API | Purpose | Docs |
|---|---|---|
| [REST Countries](https://restcountries.com/) | General country information | [restcountries.com](https://restcountries.com/) |
| [World Bank API](https://datahelpdesk.worldbank.org/knowledgebase/articles/889392) | Economic and social indicators | [World Bank Docs](https://datahelpdesk.worldbank.org/) |
| [Google Gemini](https://ai.google.dev/) | AI-powered executive report generation | [Google AI Docs](https://ai.google.dev/docs) |

---

## Design Decisions

**Orchestrator Pattern** — The `OrchestratorImpl` coordinates independent services without coupling them. This makes the system easy to extend and test.

**Circuit Breakers (Resilience4J)** — Applied on all external API calls (RestCountries, World Bank, Gemini). If a dependency becomes unavailable, the circuit opens and a graceful fallback is returned instead of cascading failures.

**Redis Caching** — Responses are cached by country name with a 60-minute TTL. This avoids redundant calls to external APIs (especially Gemini) for repeated queries, reducing latency and API costs.

**Global Exception Handler** — A `@ControllerAdvice` class centralizes all error handling, returning consistent `ApiError` responses across the API.

**SOLID Principles** — Each service has a single, well-defined responsibility. Interfaces are used for all services (`AggregationService`, `AnalyzeService`, `Orchestrator`), making implementations swappable and independently testable.

---

## Project Goals

This project was built to showcase:

- ✅ **REST API consumption** — integrating multiple third-party APIs with Spring RestClient
- ✅ **REST API design** — clean controller, service, and DTO layering
- ✅ **AI integration** — using Google Gemini to generate structured reports from dynamic data
- ✅ **Resilience patterns** — Circuit Breakers with Resilience4J and fallback strategies
- ✅ **Caching** — Redis integration with Spring Cache
- ✅ **Exception handling** — centralized, structured error responses
- ✅ **SOLID principles** — applied throughout the service and domain layers
- ✅ **Design patterns** — Orchestrator pattern for scalable feature composition
- ✅ **Containerization** — Docker + Docker Compose for reproducible environments
