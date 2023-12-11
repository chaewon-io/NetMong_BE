package com.ll.netmong.domain.reports.repository;

import com.ll.netmong.domain.reports.entity.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCommentRepository extends JpaRepository<ReportComment, Long> {
}
