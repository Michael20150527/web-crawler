package web.search.crawler.job.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import web.search.crawler.job.pojo.JobResult;
import web.search.crawler.job.service.JobRepositoryService;

@RestController
public class SearchController {

    @Autowired
    private JobRepositoryService jobRepositoryService;

    /**
     * Query recruitment information by page according to conditions
     *
     * @param salary
     * @param jobaddr
     * @param keyword
     * @param page
     * @return
     */
    @RequestMapping(value = "search",method = RequestMethod.POST)
    public JobResult search(String salary, String jobaddr, String keyword, Integer page) {
        JobResult jobResult =  this.jobRepositoryService.search(salary,jobaddr,keyword,page);
        return jobResult;
    }

}
