package com.strikers.kaamsetu.model;

public record JobMatchResponse(
    Long jobId,
    String title,
    String skillRequired,
    String areaName,
    int wagePerDayInr,
    String employerName,
    String employerPhone,
    double distanceKm,
    String spokenSummary
) {}
