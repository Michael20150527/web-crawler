package web.search.crawler.job.service;

import org.springframework.data.domain.Page;
import web.search.crawler.job.pojo.JobInfo;

import java.util.List;

public interface JobInfoService {

    /**
     * Save job information
     *
     * @param jobInfo
     */
    public void save(JobInfo jobInfo);


    /**
     * Query job information according to conditions
     *
     * @param jobInfo
     * @return
     */
    public List<JobInfo> findJobInfo(JobInfo jobInfo);

    /**
     * Query data by page
     *
     * @param page
     * @param rows
     * @return
     */
    Page<JobInfo> findJobInfoByPage(int page, int rows);
}
