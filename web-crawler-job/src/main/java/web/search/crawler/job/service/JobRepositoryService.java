package web.search.crawler.job.service;

import web.search.crawler.job.pojo.JobInfoField;
import web.search.crawler.job.pojo.JobResult;

import java.util.List;

public interface JobRepositoryService {

    /**
     * Save a piece of data
     *
     * @param jobInfoField
     */
    public void save(JobInfoField jobInfoField);


    /**
     * Save data by batch
     *
     * @param list
     */
    public void saveAll(List<JobInfoField> list);

    /**
     * Query recruitment information by page according to conditions
     *
     * @param salary
     * @param jobaddr
     * @param keyword
     * @param page
     * @return
     */
    JobResult search(String salary, String jobaddr, String keyword, Integer page);
}
