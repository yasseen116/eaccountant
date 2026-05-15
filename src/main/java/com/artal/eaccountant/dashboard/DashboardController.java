package com.artal.eaccountant.dashboard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final DashboardFacade dashboardFacade;

    public DashboardController(DashboardFacade dashboardFacade) {
        this.dashboardFacade = dashboardFacade;
    }

    @GetMapping("/api/dashboard/summary")
    public DashboardSummaryResponse getSummary() {
        return dashboardFacade.getSummary();
    }
}
