# KaamSetu — Voice-First Hyper-Local Job Board
**wHACKiest 2026 | Track 4: UnlockED | Team Strikers (ID: 36ba6650)**
*New Horizon College of Engineering*

> **Speak your skill and area, hear nearby jobs in your language.**

---

## Architecture
```
Worker / Employer (Browser + Web Speech API)
        │
        ▼
Spring Boot 3 REST API (Java 21 on AWS EC2)
        │
        ├──► Matching Service (Skill Filter + Haversine Distance) ──► PostgreSQL (AWS RDS)
        │
        └──► ElevenLabs TTS (Hindi / Kannada / English Audio) ──────► AWS S3 Audio Cache
```

## API Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/jobs/nearby?skill=electrician&lat=13.1007&lon=77.5963` | Returns top 3 nearest active jobs ranked by Haversine distance (km) |
| `POST` | `/api/v1/jobs` | Employer posts a new daily-wage job in <30 seconds |
| `POST` | `/api/v1/voice/match` | Parses spoken transcript + returns top 3 jobs + TTS audio script |

## Quick Start
1. Start PostgreSQL (or H2 fallback configured in `application.properties`).
2. Run:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Open `http://localhost:8080` in Chrome/Edge, click **Speak**, and say *"Electrician in Yelahanka"* (or use the tap-to-pick fallback menu).
