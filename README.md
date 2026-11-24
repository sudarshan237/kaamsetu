# KaamSetu
Voice-first, hyper-local job matching for informal workers (wHACKiest 2026, Track 4: UnlockED).

## Problem
Daily-wage workers cannot find nearby jobs on text-heavy, English-first platforms.

## Solution
A worker speaks their skill and area; the system matches nearby jobs and reads them aloud.

## Architecture
Browser (Web Speech API) -> Spring Boot API (Java 21) -> Matching service (skill filter + Haversine distance) -> PostgreSQL. ElevenLabs TTS for audio results. AWS (EC2, S3, RDS) for deployment.

## Run
mvn spring-boot:run, then open http://localhost:8080

## Status
Early MVP built for the ideathon round. Remaining work is planned for the 24-hr build.

## Team Strikers
Sudarshan A Patil, Shubham, Suresh, Suman
