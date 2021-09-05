package com.tz.redismanager.controller.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * <p>动态开启，暂停，修改任务</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-09-03 0:20
 **/
@RestController
@Component
public class DynamicTask {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private ConcurrentHashMap<String, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        return threadPoolTaskScheduler;
    }

    //http://127.0.0.1/startTask?taskId=1&cron=*/3 * * * * *
    /**
     * 启动任务
     *
     * @param taskId 任务id
     * @param cron cron表达式
     * @return
     */
    @RequestMapping("/startTask")
    public String startCron(String taskId, String cron) {
        this.cancelTask(taskId);
        taskMap.put(taskId, threadPoolTaskScheduler.schedule(new TaskRunnable(taskId), new CronTrigger(cron)));
        System.out.println("DynamicTask.startCron()");
        return "startCron";
    }

    //http://127.0.0.1/stopTask?taskId=1
    /**
     * 停止任务
     *
     * @param taskId
     * @return
     */
    @RequestMapping("/stopTask")
    public String stopCron(String taskId) {
        this.cancelTask(taskId);
        System.out.println("DynamicTask.stopCron()");
        return "stopCron";
    }

    //http://127.0.0.1/changeTask?taskId=1&cron=*/10 * * * * *
    /**
     * 修改任务
     *
     * @param taskId
     * @param cron
     * @return
     */
    @RequestMapping("/changeTask")
    public String startCron10(String taskId, String cron) {
        //先停止
        this.stopCron(taskId);
        //再开启
        taskMap.put(taskId, threadPoolTaskScheduler.schedule(new TaskRunnable(taskId), new CronTrigger(cron)));
        System.out.println("DynamicTask.changeTask(" + taskId + ")");
        return "changeTask";
    }

    private void cancelTask(String taskId) {
        ScheduledFuture<?> scheduledFuture = taskMap.get(taskId);
        if (null != scheduledFuture) {
            scheduledFuture.cancel(true);
        }
    }

    private class TaskRunnable implements Runnable {

        private String taskId;

        public TaskRunnable(String taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            System.out.println("DynamicTask.TaskRunnable.run()，" + new Date() + "-->taskId:" + taskId);
        }
    }
}
