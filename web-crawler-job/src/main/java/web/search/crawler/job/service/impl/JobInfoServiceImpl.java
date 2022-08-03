package web.search.crawler.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.search.crawler.job.dao.JobInfoDao;
import web.search.crawler.job.pojo.JobInfo;
import web.search.crawler.job.service.JobInfoService;

import java.util.List;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Autowired
    private JobInfoDao jobInfoDao;


    @Override
    @Transactional
    public void save(JobInfo jobInfo) {
        JobInfo param = new JobInfo();
        param.setUrl(jobInfo.getUrl());
        param.setTime(jobInfo.getTime());

        List<JobInfo> list = this.findJobInfo(param);

        if (list.size() == 0) {
            // If the query result is empty,
            // it means that the recruitment information data does not exist or has been updated,
            // and the database needs to be added or updated
            this.jobInfoDao.saveAndFlush(jobInfo);
        }
    }

    @Override
    public List<JobInfo> findJobInfo(JobInfo jobInfo) {

        // Set query condition
        Example example = Example.of(jobInfo);

        List list = this.jobInfoDao.findAll(example);

        return list;
    }

    @Override
    public Page<JobInfo> findJobInfoByPage(int page, int rows) {

        Page<JobInfo> JobInfos = this.jobInfoDao.findAll(PageRequest.of(page - 1, rows));

        return JobInfos;
    }
}
