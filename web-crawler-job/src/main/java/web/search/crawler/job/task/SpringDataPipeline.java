package web.search.crawler.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import web.search.crawler.job.pojo.JobInfo;
import web.search.crawler.job.service.JobInfoService;

@Component
public class SpringDataPipeline implements Pipeline {

    @Autowired
    private JobInfoService jobInfoService;


    @Override
    public void process(ResultItems resultItems, Task task) {
        // Get the encapsulated recruitment details object
        JobInfo jobInfo = resultItems.get("jobInfo");

        if (jobInfo != null) {
            // If it is not empty, save the data to the database
            this.jobInfoService.save(jobInfo);
        }
    }
}
