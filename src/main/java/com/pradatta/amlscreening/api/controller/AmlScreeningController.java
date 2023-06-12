package com.pradatta.amlscreening.api.controller;

import com.pradatta.amlscreening.api.response.AmlScreeningResponse;
import com.pradatta.amlscreening.service.AmlScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aml-screening/api/v1")
public class AmlScreeningController {

    @Autowired
    AmlScreeningService amlScreeningService;
    @GetMapping("/check/{name}")
    AmlScreeningResponse checkSanctionListForName(@PathVariable String name) {
        return amlScreeningService.checkSanctionListForName(name);
    }
}
