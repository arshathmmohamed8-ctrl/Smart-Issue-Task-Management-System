package com.smarttask.smartissuetaskapi.controller;

import com.smarttask.smartissuetaskapi.service.DashboardService;
import com.smarttask.smartissuetaskapi.service.DashboardStatistics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/statistics")
    public DashboardStatistics getStatistics() {
        return dashboardService.getStatistics();
    }
}
