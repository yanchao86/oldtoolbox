package com.pixshow.framework.schedule.internal;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.pixshow.framework.exception.api.SysException;
import com.pixshow.framework.schedule.api.SchedulerManager;
import com.pixshow.framework.schedule.api.annotation.ScheduleJob;
import com.pixshow.framework.utils.AnnotationUtility;
import com.pixshow.framework.utils.ClassUtility;

@Component("framework.schedule.SchedulerManager")
public class SchedulerManagerImpl implements ApplicationContextAware, SchedulerManager {

    protected Log                log = LogFactory.getLog(getClass());

    protected ApplicationContext applicationContext;

    @Resource(name = "framework.schedule.QuartzSchedulerFactoryBean")
    protected Scheduler          scheduler;

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleJob(String jobName, Class<? extends Job> jobClass, String cron, Map<String, Object> data) {

        if (this.scheduler == null) {
            if (log.isWarnEnabled()) {
                log.warn("没有定时任务配置文件[" + QuartzSchedulerFactoryBean.CONFIG_FILE + "]，功能不可用");
            }
            return;
        }

        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName).build();
        if (data != null) {
            jobDetail.getJobDataMap().putAll(data);
        }
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        triggerBuilder.withIdentity(jobName + "_trigger");
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
        CronTrigger trigger = (CronTrigger) triggerBuilder.build();

        try {
            if (this.scheduler.checkExists(jobDetail.getKey())) {
                CronTrigger oldTrigger = (CronTrigger) this.scheduler.getTrigger(trigger.getKey());
                if (!oldTrigger.getCronExpression().equals(trigger.getCronExpression())) {
                    //scheduler.resumeJob(jobDetail.getKey());
                    this.scheduler.deleteJob(jobDetail.getKey());
                    this.scheduler.scheduleJob(jobDetail, trigger);
                }
            } else {
                this.scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (SchedulerException e) {
            throw new SysException(e.getMessage(), e);
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Map<String, Object> jobs = this.applicationContext.getBeansWithAnnotation(ScheduleJob.class);
        for (String key : jobs.keySet()) {
            Job job = (Job) jobs.get(key);
            Class jobClass = ClassUtility.getUserClass(job);
            ScheduleJob scheduleJob = AnnotationUtility.findAnnotation(jobClass, ScheduleJob.class);
            String jobName = scheduleJob.name();
            String cron = scheduleJob.cron();

            if (scheduleJob.enable()) {
                scheduleJob(jobName, jobClass, cron, null);
            } else {
                deleteJob(jobName);
            }
        }
    }

    public void deleteJob(String jobName) {
        if (this.scheduler == null) {
            if (log.isWarnEnabled()) {
                log.warn("没有定时任务配置文件[" + QuartzSchedulerFactoryBean.CONFIG_FILE + "]，功能不可用");
            }
            return;
        }
        try {
            JobKey jobKey = new JobKey(jobName);
            scheduler.deleteJob(jobKey);

        } catch (SchedulerException e) {
            throw new SysException(e.getMessage(), e);
        }
    }

}
