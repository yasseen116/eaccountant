package com.artal.eaccountant.reports;

public abstract class ReportTemplate {

    public final byte[] generateReport() {
        Object data = loadData();
        return buildReport(data);
    }

    protected abstract Object loadData();

    protected abstract byte[] buildReport(Object data);
}
