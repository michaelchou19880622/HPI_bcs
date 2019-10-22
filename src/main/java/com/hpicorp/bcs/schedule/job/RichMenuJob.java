package com.hpicorp.bcs.schedule.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hpicorp.bcs.schedule.executer.RichMenuExecuter;
import com.hpicorp.core.common.JsonUtil;
import com.hpicorp.core.entities.RichMenu;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisallowConcurrentExecution
public class RichMenuJob extends QuartzJobBean {

	@Autowired
	private RichMenuExecuter richMenuExecuter;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		String str = JsonUtil.obj2String(jobDataMap.get("richMenu"));
		RichMenu richMenu = JsonUtil.string2Obj(str, RichMenu.class);
		try {
			richMenuExecuter.execute(richMenu);
		} catch (Exception e) {
			log.error("RichMenuJob executeInternal error => {}", e);
		}
	}

}
