package com.artal.eaccountant.dashboard;

public record DashboardSummaryResponse(
        long totalItems,
        int localStock,
        int fulfillmentStock,
        long restockAlerts,
        int recommendedTransferToFulfillment
) {
}
