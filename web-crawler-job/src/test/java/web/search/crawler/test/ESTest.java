package web.search.crawler.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import web.search.crawler.job.Application;
import web.search.crawler.job.pojo.JobInfo;
import web.search.crawler.job.pojo.JobInfoField;
import web.search.crawler.job.service.JobInfoService;
import web.search.crawler.job.service.JobRepositoryService;

import java.util.ArrayList;
import java.util.List;

/**
 * Migrate data from DB to ES manually
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private JobRepositoryService jobRepositoryService;

    /**
     * Create index and mapping
     */
    @Test
    public void createIndex() {
        this.elasticsearchTemplate.createIndex(JobInfoField.class);
        this.elasticsearchTemplate.putMapping(JobInfoField.class);
    }

    /**
     * Import data from DB to ES
     */
    @Test
    public void jobInfoData() {
        int p = 1;
        int pageSize = 0;

        do {
            // Retrieve data from DB
            Page<JobInfo> page = this.jobInfoService.findJobInfoByPage(p, 500);

            List<JobInfoField> list = new ArrayList<>();

            for (JobInfo jobInfo : page.getContent()) {
                JobInfoField jobInfoField = new JobInfoField();
                BeanUtils.copyProperties(jobInfo, jobInfoField);
                list.add(jobInfoField);
            }

            // Save data to ES
            this.jobRepositoryService.saveAll(list);

            // Page number plus one
            p++;

            // Get the number of result set
            pageSize = page.getContent().size();

        } while (pageSize == 500);
    }
}
