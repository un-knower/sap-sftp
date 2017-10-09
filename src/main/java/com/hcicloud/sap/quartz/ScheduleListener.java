package com.hcicloud.sap.quartz;

import com.hcicloud.sap.job.*;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;


@Configuration
public class ScheduleListener implements ApplicationListener<ContextRefreshedEvent>{

    @Resource(name = "scheduler")
    private Scheduler scheduler;

    @Resource(name = "jobTrigger")
    private CronTrigger cronTrigger;

    private static final Logger logger = LoggerFactory.getLogger(ScheduleListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            logger.info("定时任务监听器，开始定时任务的调度");

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(cronTrigger.getKey());
            String currentCron = trigger.getCronExpression();// 当前数据库Trigger使用的

            //触发器设置默认的值
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(currentCron);

            //在scheduler是一个调度器，一个Scheduler注册JobDetail和Trigger。
            JobDetail jobDetail = JobBuilder.newJob(SuccessVoiceJob.class).withIdentity("success", "voice").build();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("voiceSuccess").withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail,cronTrigger);

            JobDetail jobDetailFsr = JobBuilder.newJob(FsrVoiceJob.class).withIdentity("fsr", "voice").build();
            CronTrigger cronTriggerFsr = TriggerBuilder.newTrigger().withIdentity("voiceFsr").withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetailFsr,cronTriggerFsr);

            JobDetail jobDetailCurrent = JobBuilder.newJob(CurrentVoiceJob.class).withIdentity("current", "voice").build();
            CronTrigger cronTriggerCurrent = TriggerBuilder.newTrigger().withIdentity("voiceCurrent").withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetailCurrent,cronTriggerCurrent);

            JobDetail jobDetailFsrHistory = JobBuilder.newJob(FsrHistoryVoiceJob.class).withIdentity("fsrHistory", "voice").build();
            CronTrigger cronTriggerFsrHistory = TriggerBuilder.newTrigger().withIdentity("voiceFsrHistory").withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetailFsrHistory,cronTriggerFsrHistory);

            JobDetail jobDetailSuccessHistory = JobBuilder.newJob(SuccessHistoryVoiceJob.class).withIdentity("successHistory", "voice").build();
            CronTrigger cronTriggerSuccessHistory = TriggerBuilder.newTrigger().withIdentity("voiceSuccessHistory").withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetailSuccessHistory,cronTriggerSuccessHistory);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
