package com.ll.netmong.domain.reports.util;

public enum ReportType {
    ABUSE("욕설"),
    OBSCENITY("음란"),
    DEFAMATION("비방"),
    SPAM("스팸 홍보/도배"),
    ILLEGAL_INFO("불법 정보"),
    HARMFUL_TO_TEENS("청소년 유해");

    private final String description;

    ReportType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
