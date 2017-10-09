package com.hcicloud.sap.quartz;

import javax.annotation.Resource;

import com.hcicloud.sap.entity.ConfigRepository;
import com.hcicloud.sap.job.SuccessVoiceJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import static org.quartz.TriggerKey.triggerKey;


@Configuration
@EnableScheduling
@Component
public class ScheduleRefreshDatabase {
    @Autowired
    private ConfigRepository repository;

    @Resource(name = "jobTrigger")
    private CronTrigger cronTrigger;

    @Resource(name = "scheduler")
    private Scheduler scheduler;

    @Autowired
    private SuccessVoiceJob successVoiceJob;

    @Scheduled(fixedRate = 1000*10) // 每隔10s查库，并根据查询结果决定是否重新设置定时任务
    public void scheduleUpdateCronTrigger() throws SchedulerException {
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(cronTrigger.getKey());
        String currentCron = trigger.getCronExpression();// 当前Trigger使用的
        String searchCron = repository.findOne(1L).getCron();// 从数据库查询出来的
//        System.out.println(currentCron);
//        System.out.println(searchCron);
        if (currentCron.equals(searchCron)) {
            // 如果当前使用的cron表达式和从数据库中查询出来的cron表达式一致，则不刷新任务
        } else {
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(searchCron);

            // 按新的cronExpression表达式重新构建trigger
            trigger = (CronTrigger) scheduler.getTrigger(cronTrigger.getKey());
            trigger = trigger.getTriggerBuilder().withIdentity(cronTrigger.getKey())
                    .withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(cronTrigger.getKey(), trigger);

            //更新成功单的触发器
            CronTrigger successTrigger = (CronTrigger) scheduler.getTrigger(triggerKey("voiceSuccess"));
            CronTrigger successTriggerNew = successTrigger.getTriggerBuilder().withIdentity(successTrigger.getKey()).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(successTrigger.getKey(), successTriggerNew);
        }



        //获取fsr定时任务的时间
        CronTrigger fsrTrigger = (CronTrigger) scheduler.getTrigger(triggerKey("voiceFsr"));
        String fsrCurrentCron = fsrTrigger.getCronExpression();// 当前Trigger使用的
        String fsrSearchCron = repository.findOne(2L).getCron();// 从数据库查询出来的
        if (fsrCurrentCron.equals(fsrSearchCron)) {
            // 如果当前使用的cron表达式和从数据库中查询出来的cron表达式一致，则不刷新任务
        } else {
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(fsrSearchCron);
            //更新防骚扰的触发器
            CronTrigger fsrTriggerNew = fsrTrigger.getTriggerBuilder().withIdentity(fsrTrigger.getKey()).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(fsrTrigger.getKey(), fsrTriggerNew);
        }

        //获取current定时任务的时间
        CronTrigger currentTrigger = (CronTrigger) scheduler.getTrigger(triggerKey("voiceCurrent"));
        String currentCurrentCron = currentTrigger.getCronExpression();// 当前Trigger使用的
        String currentSearchCron = repository.findOne(3L).getCron();// 从数据库查询出来的
        if (currentCurrentCron.equals(currentSearchCron)) {
            // 如果当前使用的cron表达式和从数据库中查询出来的cron表达式一致，则不刷新任务
        } else {
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(currentSearchCron);
            //更新防骚扰的触发器
            CronTrigger currentTriggerNew = currentTrigger.getTriggerBuilder().withIdentity(currentTrigger.getKey()).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(currentTrigger.getKey(), currentTriggerNew);
        }



        //获取fsr历史定时任务的时间
        CronTrigger fsrHistoryTrigger = (CronTrigger) scheduler.getTrigger(triggerKey("voiceFsrHistory"));
        String fsrHistoryCurrentCron = fsrHistoryTrigger.getCronExpression();// 当前Trigger使用的
        String fsrHistorySearchCron = repository.findOne(7L).getCron();// 从数据库查询出来的
        if (fsrHistoryCurrentCron.equals(fsrHistorySearchCron)) {
            // 如果当前使用的cron表达式和从数据库中查询出来的cron表达式一致，则不刷新任务
        } else {
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(fsrHistorySearchCron);
            //更新防骚扰的触发器
            CronTrigger fsrHistoryTriggerNew = fsrHistoryTrigger.getTriggerBuilder().withIdentity(fsrHistoryTrigger.getKey()).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(fsrHistoryTrigger.getKey(), fsrHistoryTriggerNew);
        }

        //获取success历史定时任务的时间
        CronTrigger successHistoryTrigger = (CronTrigger) scheduler.getTrigger(triggerKey("voiceSuccessHistory"));
        String successHistoryCurrentCron = successHistoryTrigger.getCronExpression();// 当前Trigger使用的
        String successHistorySearchCron = repository.findOne(11L).getCron();// 从数据库查询出来的
        if (successHistoryCurrentCron.equals(successHistorySearchCron)) {
            // 如果当前使用的cron表达式和从数据库中查询出来的cron表达式一致，则不刷新任务
        } else {
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(successHistorySearchCron);
            //更新防骚扰的触发器
            CronTrigger successHistoryTriggerNew = successHistoryTrigger.getTriggerBuilder().withIdentity(successHistoryTrigger.getKey()).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(successHistoryTrigger.getKey(), successHistoryTriggerNew);
        }


    }
}