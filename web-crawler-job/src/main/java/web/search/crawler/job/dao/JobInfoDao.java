package web.search.crawler.job.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import web.search.crawler.job.pojo.JobInfo;

public interface JobInfoDao extends JpaRepository<JobInfo, Long> {
}
