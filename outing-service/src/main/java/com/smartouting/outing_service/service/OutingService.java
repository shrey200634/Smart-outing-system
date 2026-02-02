package com.smartouting.outing_service.service;

import com.smartouting.outing_service.dto.OutingRequestDTO;
import com.smartouting.outing_service.dto.OutingResponseDTO;
import com.smartouting.outing_service.exception.ResourseNotFoundException;
import com.smartouting.outing_service.model.Outing;
import com.smartouting.outing_service.repository.OutingRepository;
import com.smartouting.outing_service.util.QrCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OutingService {

    @Autowired
    private OutingRepository outingRepository;

    // 1. APPLY (Student sends RequestDTO, we return ResponseDTO)
    public OutingResponseDTO applyForOuting(OutingRequestDTO request) {

        // Convert DTO to Entity
        Outing outing = new Outing();
        outing.setStudentId(request.getStudentId());
        outing.setStudentName(request.getStudentName());
        outing.setReason(request.getReason());
        outing.setDestination(request.getDestination());
        outing.setOutDate(request.getOutDate());
        outing.setReturnDate(request.getReturnDate());
        outing.setStatus("PENDING");

        // Simple AI Logic (We will upgrade this on Day 2)
        analyzeRequest(outing);

        // Save to DB
        Outing savedOuting = outingRepository.save(outing);

        // Convert back to DTO
        return mapToResponse(savedOuting);
    }

    // 2. APPROVE (Warden)
    public OutingResponseDTO approveOuting(Long id, String comment) throws Exception {
        Outing outing = outingRepository.findById(id)
                .orElseThrow(() -> new ResourseNotFoundException("Outing with ID " + id + " not found"));

        outing.setStatus("APPROVED");
        outing.setWardenComment(comment);

        // Generate QR Code
        String qrData = "ID:" + outing.getId() + "-STATUS:APPROVED-" + outing.getStudentId();
        outing.setQrCodeUrl(QrCodeUtil.generateQR(qrData, 200, 200));

        Outing savedOuting = outingRepository.save(outing);
        return mapToResponse(savedOuting);
    }

    // 3. SCAN (Guard)
    public OutingResponseDTO verifyAndMarkOut(Long id) {
        Outing outing = outingRepository.findById(id)
                .orElseThrow(() -> new ResourseNotFoundException("Outing with ID " + id + " not found"));

        if ("APPROVED".equals(outing.getStatus())) {
            outing.setStatus("OUT");
            outing.setOutDate(LocalDateTime.now());
            return mapToResponse(outingRepository.save(outing));
        } else {
            throw new ResourseNotFoundException("Student is NOT approved to leave!");
        }
    }

    // --- HELPER METHODS ---

    // Maps Entity -> Response DTO
    private OutingResponseDTO mapToResponse(Outing outing) {
        return new OutingResponseDTO(
                outing.getId(),
                outing.getStudentId(),
                outing.getStudentName(),
                outing.getReason(),
                outing.getDestination(),
                outing.getStatus(),
                outing.getAiFlag(),
                outing.getUrgencyScore(),
                outing.getWardenComment(),
                outing.getQrCodeUrl(),
                outing.getOutDate(),
                outing.getReturnDate()
        );
    }

    // Basic AI Analysis
    private void analyzeRequest(Outing outing) {
        String r = outing.getReason().toLowerCase();
        if (r.contains("doctor") || r.contains("hospital")) {
            outing.setAiFlag("⚠️ MEDICAL");
            outing.setUrgencyScore(98);
        } else {
            outing.setAiFlag("ℹ️ GENERAL");
            outing.setUrgencyScore(10);
        }
    }
    // get all outing for wardern
    public List<Outing> getAllOuting(){
        return outingRepository.findAll();

    }
   // get single outing by id
    public Outing getOutingById(Long  id){
        return outingRepository.findById(id)
                .orElseThrow(()->new ResourseNotFoundException("outing not found with id :" +id));
    }

}