package com.smartouting.outing_service.controller;

import com.smartouting.outing_service.model.Outing;
import com.smartouting.outing_service.service.OutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/outing")
public class OutingController {

    @Autowired
    private OutingService outingService;

    // student apply
    @PostMapping("/apply")
    public Outing apply(@RequestBody Outing outing){
        return  outingService.applyForOuting(outing);

    }

    // wardern Approwal
    @PutMapping("/approve/{id}")
    public Outing approve (@PathVariable Long id, @RequestParam String comment )throws Exception{
        return outingService.approveOuting(id ,comment);
    }

    //for security
    @PutMapping("/scan/{id}")
    public Outing scanQR(@PathVariable Long id ){
        return outingService.verifyAndMarkOut(id);
    }

}
