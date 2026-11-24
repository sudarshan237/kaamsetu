package com.strikers.kaamsetu.controller;

import com.strikers.kaamsetu.model.Job;
import com.strikers.kaamsetu.model.JobMatchResponse;
import com.strikers.kaamsetu.repository.JobRepository;
import com.strikers.kaamsetu.service.MatchingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class KaamSetuController {

    private final MatchingService matchingService;
    private final JobRepository jobRepository;

    public KaamSetuController(MatchingService matchingService, JobRepository jobRepository) {
        this.matchingService = matchingService;
        this.jobRepository = jobRepository;
    }

    @GetMapping("/jobs/nearby")
    public List<JobMatchResponse> getNearbyJobs(
            @RequestParam(defaultValue = "electrician") String skill,
            @RequestParam(defaultValue = "13.1007") double lat,
            @RequestParam(defaultValue = "77.5963") double lon) {
        return matchingService.findTopMatches(skill, lat, lon, 3);
    }

    @PostMapping("/voice/match")
    public Map<String, Object> voiceMatch(@RequestBody Map<String, String> payload) {
        return matchingService.parseVoiceAndMatch(payload.getOrDefault("transcript", ""));
    }

    @PostMapping("/jobs")
    public Job createJob(@RequestBody Job job) {
        job.setActive(true);
        return jobRepository.save(job);
    }
}
