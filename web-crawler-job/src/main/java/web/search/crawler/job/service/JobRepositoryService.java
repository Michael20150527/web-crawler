package web.search.crawler.job.service;

import web.search.crawler.job.pojo.JobInfoField;
import web.search.crawler.job.pojo.JobResult;

import java.util.List;

public interface JobRepositoryService {

    /**
     * 保存一条数据
     *
     * @param jobInfoField
     */
    public void save(JobInfoField jobInfoField);


    /**
     * 批量保存数据
     *
     * @param list
     */
    public void saveAll(List<JobInfoField> list);

    /**
     * 根据条件分页查询招聘信息
     * @param salary
     * @param jobaddr
     * @param keyword
     * @param page
     * @return
     */
    JobResult search(String salary, String jobaddr, String keyword, Integer page);
}
