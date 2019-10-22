package com.hpicorp.bcs.config;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Schedule {

	private final Scheduler scheduler;

	private static final Object SCHEDULE_FLAG = new Object();
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private Schedule(SchedulerFactoryBean schedulerFactory) {
		this.scheduler = schedulerFactory.getScheduler();
	}
	
	/**
	 * 排程停止
	 * @throws SchedulerException
	 */
	@PreDestroy
	public void stopSchedule() throws SchedulerException {
		synchronized (SCHEDULE_FLAG) {
			log.info("『 Schedule stop 』");
			this.scheduler.shutdown(true);
		}
	}

	/**
	 * 創建任務內容
	 * @param t 必需繼承 QuartzJobBean，設置為 JobClass
	 * @param detailName Class.名稱 - Class.id
	 * @param detailGroup Class.名稱_GROUP
	 * @param jobDataAsMap 任務相關 Map
	 * @return
	 */
	public <T extends QuartzJobBean> JobDetail createJobDetail(T t, String detailName, String detailGroup, Map<String, Object> jobDataAsMap) {
		JobDetailFactoryBean detailFactory = new JobDetailFactoryBean();
		detailFactory.setName(detailName);
		detailFactory.setGroup(detailGroup);
		detailFactory.setBeanName(detailName);
		detailFactory.setJobClass(t.getClass());
		detailFactory.setJobDataAsMap(jobDataAsMap);
		detailFactory.afterPropertiesSet();
		return detailFactory.getObject();
	}

	/**
	 * 創建調度時間（依照時間）
	 * @param startTime 預約時間
	 * @param jobDetail 任務內容
	 * @return
	 */
	public Trigger createSimpleTrigger(Date startTime, JobDetail jobDetail) {
		SimpleTriggerFactoryBean triggerFactory = new SimpleTriggerFactoryBean();
		triggerFactory.setName(UUID.randomUUID().toString());
		triggerFactory.setJobDetail(jobDetail);
		triggerFactory.setStartTime(startTime);
		triggerFactory.setRepeatCount(0);
		triggerFactory.setRepeatInterval(1000);
		triggerFactory.afterPropertiesSet();
		return triggerFactory.getObject();
	}

	/**
	 * 創建調度時間（Cron語法）
	 * @param cronExpression Cron語法
	 * @param jobDetail 任務內容
	 * @return
	 * @throws ParseException
	 */
	public CronTrigger createCronTrigger(String cronExpression, JobDetail jobDetail) throws ParseException {
		CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
		triggerFactory.setName(UUID.randomUUID().toString());
		triggerFactory.setJobDetail(jobDetail);
		triggerFactory.setCronExpression(cronExpression);
		triggerFactory.afterPropertiesSet();
		return triggerFactory.getObject();
	}

	/**
	 * 增加排程
	 * @param jobDetail 任務內容
	 * @param trigger 定時調度
	 * @param detailName 排程名稱
	 * @throws Exception 
	 */
	public void addSchedule(JobDetail jobDetail, Trigger trigger, String detailName) throws Exception {
		try {
			synchronized (SCHEDULE_FLAG) {
				Date result = scheduler.scheduleJob(jobDetail, trigger);
				log.info("『 Schedule add 』result => {}, detailName => {}", result, detailName);
			}
		} catch (Exception e) {
			log.error("『 Schedule add 』error => {}", e);
			throw new Exception("add schedule error => " + e);
		}
	}

	/**
	 * 刪除排程
	 * @param detailName 排程名稱
	 * @throws Exception
	 */
	public void deleteSchedule(String jobName, String jobGroupName) throws Exception {
		try {
			synchronized (SCHEDULE_FLAG) {
				boolean deleteJob = scheduler.deleteJob(new JobKey(jobName, jobGroupName));
				log.info("『 Schedule delete 』是否刪除成功 => {}, 刪除名稱 => {}", deleteJob, jobName);
			}
		} catch (Exception e) {
			log.error("『 Schedule delete 』error => {}", e);
			throw new Exception("delete schedule error => " + e);
		}
	}
	
	/**
	 * 確認排程是否已加入過排程任務清單
	 * @param value 任務名稱
	 * @return
	 */
	public Boolean checkScheduleExists(String value) {
		String queryStr = "select count(*) from QRTZ_JOB_DETAILS where JOB_NAME = :jobName ;";
		Query countQuery = em.createNativeQuery(queryStr);
		countQuery.setParameter("jobName", value);
		BigInteger count = (BigInteger) countQuery.getSingleResult();
		return count.intValue() > 0;
	}

}
