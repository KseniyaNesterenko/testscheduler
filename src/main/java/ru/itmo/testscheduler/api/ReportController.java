package ru.itmo.testscheduler.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.testscheduler.collector.api.CollectorFacade;

@Slf4j
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final CollectorFacade collectorFacade;

    @PostMapping
    public ResponseEntity<?> upload(
            @RequestParam String runId,
            @RequestParam MultipartFile file
    ) {
        try {
            collectorFacade.collect(file.getInputStream(), runId);
            return ResponseEntity.ok("uploaded");
        } catch (Exception e) {
            log.error("Failed to process report", e);
            return ResponseEntity.status(500).body("failed");
        }
    }
}