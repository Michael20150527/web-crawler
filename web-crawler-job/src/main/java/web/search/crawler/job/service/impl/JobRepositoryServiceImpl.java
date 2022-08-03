package web.search.crawler.job.service.impl;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web.search.crawler.job.dao.JobRepository;
import web.search.crawler.job.pojo.JobInfoField;
import web.search.crawler.job.pojo.JobResult;
import web.search.crawler.job.service.JobRepositoryService;

import java.util.List;

@Service
public class JobRepositoryServiceImpl implements JobRepositoryService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public void save(JobInfoField jobInfoField) {
        this.jobRepository.save(jobInfoField);
    }

    @Override
    public void saveAll(List<JobInfoField> list) {
        this.jobRepository.saveAll(list);
    }

    @Override
    public JobResult search(String salary, String jobaddr, String keyword, Integer page) {
        // Analyze parameter salary
        String[] salarys = salary.split("-");

        int salaryMin = 0, salaryMax = 0;

        // Get the minimum salary
        if ("*".equals(salarys[0])) {
            // If the minimum value is *, the minimum salary is 0
        } else {
            // If the minimum value is not *, it needs to be converted to numeric type and multiplied by 10000
            salaryMin = Integer.parseInt(salarys[0]) * 10000;
        }


        // Get the highest salary
        if ("*".equals(salarys[1])) {
            // If the maximum value is *, the maximum number is also included, set to 10 million
            salaryMax = 10000000;
        } else {
            // If the maximum value is not *, it needs to be converted to numeric type and multiplied by 10000
            salaryMax = Integer.parseInt(salarys[0]) * 10000;
        }

        // Judge whether the work place is empty
        if (StringUtils.isBlank(jobaddr)) {
            // If it is empty, set to *
            jobaddr = "*";
        }

        // Judge whether the query keyword is empty
        if (StringUtils.isBlank(keyword)) {
            // If it is empty, set to *
            keyword = "*";
        }

        Page<JobInfoField> pages = this.jobRepository.findBySalaryMinBetweenAndSalaryMaxBetweenAndJobAddrAndJobNameAndJobInfo(salaryMin,
                salaryMax, salaryMin, salaryMax, jobaddr, keyword, keyword, PageRequest.of(page-1,30));

        JobResult jobResult = new JobResult();

        jobResult.setRows(pages.getContent());
        // Set total pages
        jobResult.setPageTotal(pages.getTotalPages());

        return jobResult;
    }
}
