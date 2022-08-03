package web.search.crawler.job.task;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import web.search.crawler.job.pojo.JobInfo;

import java.util.List;

@Component
public class JobProcessor implements PageProcessor {

    private String url = "https://search.51job.com/list/000000,000000,0000,32%252C01,9,99,java,2," +
            "1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99" +
            "&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line" +
            "=&specialarea=00&from=&welfare=";

    @Override
    public void process(Page page) {
        // Parse the page to obtain the URL address of recruitment information details
        List<Selectable> list = page.getHtml().css("div#resultList div.el").nodes();


        // Judge whether the obtained set is empty
        if (list.size() == 0) {
            // If it is blank, it means this is the recruitment details page.
            // Parse the page, get the recruitment details and save the data
            this.saveJobInfo(page);

        } else {
            // If it is not empty, it means this is a list page.
            // Resolve the URL address of the details page and put it in the task queue
            for (Selectable selectable : list) {
                // Get url address
                String jobInfoUrl = selectable.links().toString();
                // Put the obtained URL address into the task queue
                page.addTargetRequest(jobInfoUrl);
            }

            // Get the URL of the next page
            String bkUrl = page.getHtml().css("div.p_in li.bk").nodes().get(1).links().toString();
            // Put the URL in the task queue
            page.addTargetRequest(bkUrl);

        }

    }

    // Analyze the page, obtain recruitment details and save data
    private void saveJobInfo(Page page) {
        JobInfo jobInfo  = new JobInfo();

        Html html = page.getHtml();

        // Get data and encapsulate them into objects
        jobInfo.setCompanyName(html.css("div.cn p.cname a","text").toString());
        jobInfo.setCompanyAddr(Jsoup.parse(html.css("div.bmsg").nodes().get(1).toString()).text());
        jobInfo.setCompanyInfo(Jsoup.parse(html.css("div.tmsg").toString()).text());
        jobInfo.setJobName(html.css("div.cn h1","text").toString());
        jobInfo.setJobAddr(html.css("div.cn span.lname","text").toString());
        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg").toString()).text());
        jobInfo.setUrl(page.getUrl().toString());

        // Get salary
        Integer[] salary = MathSalary.getSalary(html.css("div.cn strong", "text").toString());
        jobInfo.setSalaryMin(salary[0]);
        jobInfo.setSalaryMax(salary[1]);

        // Get release time
        String time = Jsoup.parse(html.css("div.t1 span").regex(".*发布").toString()).text();
        jobInfo.setTime(time.substring(0,time.length()-2));

        // Save the result
        page.putField("jobInfo",jobInfo);
    }


    private Site site = Site.me()
            .setCharset("gbk")
            .setTimeOut(10 * 1000)
            .setRetrySleepTime(3000)
            .setRetryTimes(3);

    @Override
    public Site getSite() {
        return site;
    }

    @Autowired
    private SpringDataPipeline springDataPipeline;

    //initialDelay: how long to execute the method after the task is started
    //fixedDelay: Interval between each execution
    @Scheduled(initialDelay = 1000, fixedDelay = 100 * 1000)
    public void process() {
        Spider.create(new JobProcessor())
                .addUrl(url) // Multiple URLs can be transferred
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(10)
                .addPipeline(this.springDataPipeline)
                .run();
    }
}
