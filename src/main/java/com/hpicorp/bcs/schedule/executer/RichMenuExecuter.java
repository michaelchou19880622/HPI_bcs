package com.hpicorp.bcs.schedule.executer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.BcsLineUser;
import com.hpicorp.bcs.repository.BcsLineUserRepository;
import com.hpicorp.bcs.services.RichMenuListService;
import com.hpicorp.bcs.services.RichMenuService;
import com.hpicorp.core.common.ListExtension;
import com.hpicorp.core.dto.RichMenuMultiple;
import com.hpicorp.core.entities.LineUser;
import com.hpicorp.core.entities.RichMenu;
import com.hpicorp.core.entities.RichMenuList;
import com.hpicorp.core.repository.LineUserRepository;
import com.hpicorp.core.repository.RichMenuRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RichMenuExecuter {

	@Autowired
	private RichMenuService richMenuService;
	
	@Autowired
	private RichMenuListService richMenuListService;

	@Autowired
	private BcsLineUserRepository bcsLineUserRepository;
	
	@Autowired
	private RichMenuRepository richMenuRepository;

	private static final Integer LINE_MUL = 150;

	/**
	 * Step 1. 先判斷圖文選單是更改已綁定還是未綁定 
	 * 		   如果為 1 => 表示為未綁定 
	 * 		   如果為 2 => 表示為已綁定
	 * Step 2. 刪除舊的圖文選單(Line)
	 * Step 3. 刪除舊的圖文選單(MySql)
	 */
	public void execute(RichMenu richMenu) {
		if (richMenu.getGroupId() == 1) {
			// 先呼叫 Line 設置 Default 圖文選單
			this.richMenuService.setRichMenuDefault(richMenu.getRichMenuId());
			log.info("『 RichMenuExecuter execute 』未綁定用戶成功！");
			// 刪除掉生效時間在過去的Default圖文選單
			List<RichMenu> defaultList = richMenuRepository.findByGroupIdAndStartDateLessThan(1L, new Date());
			defaultList.forEach(i -> {
				if (richMenu.getId() != i.getId()) {
					richMenuService.deleteRichMenu(i);
					richMenuRepository.delete(i);
					RichMenuList richMenuList = richMenuListService.findByRichMenuId(i.getRichMenuId());
					richMenuListService.delete(richMenuList);
				}
			});
		} else if (richMenu.getGroupId() == 2) {
			List<String> bcslineUserMids = bcsLineUserRepository.findIdByBindStatus(BcsLineUser.STATUS_BINDED);
			
			List<List<String>> chopped = ListExtension.chopped(bcslineUserMids, LINE_MUL);
			for (int x = 0; x < chopped.size(); x++) {
				RichMenuMultiple rmm = new RichMenuMultiple(richMenu.getRichMenuId(), chopped.get(x));
				this.richMenuService.setRichMenuMultiple(rmm, this.richMenuService.getLineToken());
			}
			log.info("『 RichMenuExecuter execute 』已綁定用戶成功！");
		}
	}

}
