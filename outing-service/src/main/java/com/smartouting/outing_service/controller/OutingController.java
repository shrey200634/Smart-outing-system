package com.smartouting.outing_service.controller;

import com.smartouting.outing_service.dto.OutingRequestDTO;
import com.smartouting.outing_service.dto.OutingResponseDTO;
import com.smartouting.outing_service.service.OutingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/outing")
public class OutingController {

    @Autowired
    private OutingService service;

    @PostMapping("/apply")
    public ResponseEntity<OutingResponseDTO> apply(@RequestBody @Valid OutingRequestDTO request) {
        return ResponseEntity.ok(service.applyForOuting(request));
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<OutingResponseDTO> approve(@PathVariable Long id, @RequestParam String comment) throws Exception {
        return ResponseEntity.ok(service.approveOuting(id, comment));
    }

    @PutMapping("/scan/{id}")
    public ResponseEntity<OutingResponseDTO> scan(@PathVariable Long id) {
        return ResponseEntity.ok(service.verifyAndMarkOut(id));
    }
}