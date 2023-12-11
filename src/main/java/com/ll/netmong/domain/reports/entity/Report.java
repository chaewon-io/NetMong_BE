package com.ll.netmong.domain.reports.entity;

import com.ll.netmong.domain.reports.util.ReportType;
import com.ll.netmong.domain.member.entity.Member;

public interface Report<T> {
    Member getReporter();
    T getReported();
    ReportType getReportType();
}
