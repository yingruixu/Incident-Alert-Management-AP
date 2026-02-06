package com.shaun.incident_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IncidentController incidentController;

    // 手动触发自动关闭
//    @PostMapping("/runScheduler")
//    public String runSchedulerNow() {
//        incidentController.closeOldIncidents();
//        return "Scheduler triggered!";
//    }
}
