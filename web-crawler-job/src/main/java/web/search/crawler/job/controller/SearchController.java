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

    //salary: *-*
    //page: 1
    //jobaddr: 北京
    //keyword: java
    //Request URL: http://127.0.0.1:8080/search
    //Request Method: POST

    /**
     * 根据条件分页查询招聘信息
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
