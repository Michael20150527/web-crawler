package web.search.crawler.job.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import web.search.crawler.job.pojo.JobInfoField;

@Component
public interface JobRepository extends ElasticsearchRepository<JobInfoField,Long> {
    Page<JobInfoField> findBySalaryMinBetweenAndSalaryMaxBetweenAndJobAddrAndJobNameAndJobInfo(int salaryMin, int salaryMax, int
            salaryMin1, int salaryMax1, String jobaddr, String keyword, String keyword1, Pageable pageable);
}
