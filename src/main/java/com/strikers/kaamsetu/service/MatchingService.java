package com.strikers.kaamsetu.service;

import com.strikers.kaamsetu.model.Job;
import com.strikers.kaamsetu.model.JobMatchResponse;
import com.strikers.kaamsetu.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class MatchingService {

    private final JobRepository jobRepository;

    // Bengaluru labour cluster coordinates for hyper-local voice resolution
    private static final Map<String, double[]> BENGALURU_CLUSTERS = Map.of(
        "yelahanka", new double[]{13.1007, 77.5963},
        "hebbal", new double[]{13.0354, 77.5988},
        "peenya", new double[]{13.0285, 77.5197},
        "marathahalli", new double[]{12.9592, 77.6974},
        "whitefield", new double[]{12.9698, 77.7500},
        "electronic city", new double[]{12.8399, 77.6770},
        "jayanagar", new double[]{12.9308, 77.5838}
    );

    public MatchingService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<JobMatchResponse> findTopMatches(String skill, double userLat, double userLon, int limit) {
        List<Job> candidates = (skill == null || skill.isBlank())
                ? jobRepository.findByActiveTrue()
                : jobRepository.findBySkillRequiredIgnoreCaseAndActiveTrue(skill.trim());

        return candidates.stream()
                .map(job -> {
                    double dist = haversineKm(userLat, userLon, job.getLatitude(), job.getLongitude());
                    double roundedDist = Math.round(dist * 10.0) / 10.0;
                    String summary = String.format(
                        "%s in %s, %.1f kilometers away, paying %d rupees per day. Contact %s.",
                        job.getTitle(), job.getAreaName(), roundedDist, job.getWagePerDayInr(), job.getEmployerName()
                    );
                    return new JobMatchResponse(
                        job.getId(), job.getTitle(), job.getSkillRequired(), job.getAreaName(),
                        job.getWagePerDayInr(), job.getEmployerName(), job.getEmployerPhone(),
                        roundedDist, summary
                    );
                })
                .sorted(Comparator.comparingDouble(JobMatchResponse::distanceKm))
                .limit(limit)
                .toList();
    }

    public Map<String, Object> parseVoiceAndMatch(String transcript) {
        String lower = transcript == null ? "" : transcript.toLowerCase();
        String detectedSkill = "electrician";
        for (String s : List.of("electrician", "plumber", "mason", "driver", "cook")) {
            if (lower.contains(s)) {
                detectedSkill = s;
                break;
            }
        }

        String detectedArea = "yelahanka";
        double[] coords = BENGALURU_CLUSTERS.get("yelahanka");
        for (var entry : BENGALURU_CLUSTERS.entrySet()) {
            if (lower.contains(entry.getKey())) {
                detectedArea = entry.getKey();
                coords = entry.getValue();
                break;
            }
        }

        List<JobMatchResponse> matches = findTopMatches(detectedSkill, coords[0], coords[1], 3);
        return Map.of(
            "transcript", transcript,
            "extractedSkill", detectedSkill,
            "extractedArea", detectedArea,
            "matches", matches,
            "ttsProvider", "ElevenLabs-Multilingual-v2 (Planned S3 Audio Cache)"
        );
    }

    // Great-circle distance in kilometers
    public static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
