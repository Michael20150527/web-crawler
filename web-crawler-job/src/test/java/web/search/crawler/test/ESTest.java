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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private JobRepositoryService jobRepositoryService;


    //创建索引和映射
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
        //声明页码数，从1开始
        int p = 1;
        //声明查询到的数据条数
        int pageSize = 0;

        do {
            //从数据库中查询数据
            Page<JobInfo> page = this.jobInfoService.findJobInfoByPage(p, 500);

            //声明容器存放JobInfoField
            List<JobInfoField> list = new ArrayList<>();

            //把查询到的数据封装为JobInfoField
            for (JobInfo jobInfo : page.getContent()) {
                //声明对象
                JobInfoField jobInfoField = new JobInfoField();
                //封装数据,复制数据
                BeanUtils.copyProperties(jobInfo, jobInfoField);

                //把封装好数据的对象放到list容器中
                list.add(jobInfoField);

            }

            //把封装好的数据保存到索引库中
            this.jobRepositoryService.saveAll(list);

            //页码数加一
            p++;

            //获取查询结果集的数据条数
            pageSize = page.getContent().size();

        } while (pageSize == 500);

    }
}
