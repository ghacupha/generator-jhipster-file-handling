package io.github.deposits.app.report;

public interface Report<Report, Parameter> {

    Report createReport(Parameter reportParameter);
}
