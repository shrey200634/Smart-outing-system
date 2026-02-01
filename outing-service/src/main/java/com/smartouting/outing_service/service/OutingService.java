package com.smartouting.outing_service.service;

import com.smartouting.outing_service.model.Outing;
import com.smartouting.outing_service.repository.OutingRepository;
import com.smartouting.outing_service.util.QrCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OutingService {

    @Autowired
    private OutingRepository outingRepository;

    // student applies
    public Outing applyForOuting(Outing outing ){
        outing.setStatus("PENDING");
       return outingRepository.save(outing);
    }

    // 2. Warden Approves (Generates QR)
    public Outing approveOuting(Long outingId, String wardenComment) throws Exception {
        Outing outing = outingRepository.findById(outingId).orElseThrow();

        outing.setStatus("APPROVED");
        outing.setWardenComment(wardenComment);

        // Create a unique string for the QR code (ID + Status)
        String qrData = "ID:" + outing.getId() + "-STATUS:APPROVED-" + outing.getStudentId();
        String qrBase64 = QrCodeUtil.generateQR(qrData, 200, 200);

        outing.setQrCodeUrl(qrBase64);
        return outingRepository.save(outing);
    }

    // 3. Security Scans (Marks as OUT)
    public Outing verifyAndMarkOut(Long outingId) {
        Outing outing = outingRepository.findById(outingId).orElseThrow();

        if ("APPROVED".equals(outing.getStatus())) {
            outing.setStatus("OUT");
            outing.setOutDate(LocalDateTime.now());
            return outingRepository.save(outing);
        } else {
            throw new RuntimeException("Student is NOT approved to leave!");
        }
    }
}
