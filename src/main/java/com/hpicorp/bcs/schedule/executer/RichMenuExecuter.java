package com.hpicorp.bcs.schedule.executer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpicorp.bcs.entities.BcsLineUser;
import com.hpicorp.bcs.repository.BcsLineUserRepository;
import com.hpicorp.bcs.services.RichMenuListService;
import com.hpicorp.bcs.services.RichMenuService;
import com.hpicorp.core.common.ListExtension;
import com.hpicorp.core.dto.RichMenuMultiple;
import com.hpicorp.core.entities.BcsSendGroup;
import com.hpicorp.core.entities.BcsSendGroupDetail;
import com.hpicorp.core.entities.RichMenu;
import com.hpicorp.core.repository.BcsSendGroupDetailRepository;
import com.hpicorp.core.repository.BcsSendGroupRepository;
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

	@Autowired
	private BcsSendGroupRepository bcsSendGroupRepository;

	@Autowired
	private BcsSendGroupDetailRepository bcsSendGroupDetailRepository;
	
    @PersistenceContext
    private EntityManager entityManager;
	

	public static List<String> validQueryOp = Arrays.asList(new String[] { ">", ">=", "<", "<=", "=" });

	private static final Integer LINE_MUL = 150;

	public void execute(RichMenu richMenu) {

		// Step 1: 取得 groupId
		Long richmenuGroupId = richMenu.getGroupId();
		log.info("『 RichMenuExecuter execute 』: richmenuGroupId = {}", richmenuGroupId);

		// Step 2: 依照 groupId 查看 groupType
		String groupType = bcsSendGroupRepository.findGroupTypeByGroupId(richmenuGroupId);
		log.info("『 RichMenuExecuter execute 』: groupType = {}", groupType);

		// Step 3: 依照groupType 去取得對應的 UID list
		if (!BcsSendGroup.GROUP_TYPE_UID_LIST.equals(groupType) && !BcsSendGroup.GROUP_TYPE_CONDITIONS.equals(groupType)) {

			// groupType 不符合圖文選單群組類型，提示錯誤。
			log.info("『 RichMenuExecuter execute 』 : GroupType is not matching to the type of Richmenu SendGroup!");

			return;
		}
		
		// Step 4: 針對篩選出的uid 進行更新圖文選單
		List<BcsSendGroupDetail> list_bcsSendGroupDetails = bcsSendGroupDetailRepository.findBySendGroupGroupId(richmenuGroupId);
		log.info("『 RichMenuExecuter execute 』: list_bcsSendGroupDetails = {}", list_bcsSendGroupDetails);

		try {
			Query query = buildFindQuery(list_bcsSendGroupDetails, "DISTINCT MID");
			log.info("『 RichMenuExecuter execute 』: query = {}", query);
			
			@SuppressWarnings("unchecked")
			List<String> list_Uids = query.getResultList();
			log.info("『 RichMenuExecuter execute 』: list_Uids.size() = {}", list_Uids.size());
			log.info("『 RichMenuExecuter execute 』: list_Uids = {}", list_Uids);
			
			// 切割partition，每150個uid切割一個part。
			List<List<String>> chopped = ListExtension.chopped(list_Uids, LINE_MUL);
			log.info("『 RichMenuExecuter execute 』: chopped.size() = {}", chopped.size());
			
			int Count_Success = 0;
			
			boolean isUpdateSuccess = false;
			
			for (int i = 0; i < chopped.size(); i++) {
				RichMenuMultiple richMenuMultiple = new RichMenuMultiple(richMenu.getRichMenuId(), chopped.get(i));
				log.info("『 RichMenuExecuter execute 』: richMenuMultiple = {}", richMenuMultiple);
				
				isUpdateSuccess = this.richMenuService.setRichMenuMultiple(richMenuMultiple, this.richMenuService.getLineTokenFromBcsSystemConfig());
				
				if (!isUpdateSuccess) {
					continue;
				}
				
				// 計算更新成功數量
				Count_Success++;
			}

			log.info("『 RichMenuExecuter execute 』: 圖文選單更新完畢!");
			log.info("『 RichMenuExecuter execute 』: 預期更新用戶數 : {}", list_Uids.size());
			log.info("『 RichMenuExecuter execute 』: 成功更新用戶數 : {}", Count_Success);
			
			Count_Success = 0;

		} catch (Exception e) {
			log.info("『 RichMenuExecuter execute 』: 圖文選單更新發生異常 - Exception : {}", e);
			
			return;
		}
	}
	
	private Query buildFindQuery(List<BcsSendGroupDetail> sendGroupDetails, String selectColumns) throws Exception {
		return this.buildFindQuery(sendGroupDetails, selectColumns, null);
	}

	private Query buildFindQuery(List<BcsSendGroupDetail> sendGroupDetails, String selectColumns, String mid) throws Exception {
		Validate.notEmpty(sendGroupDetails);
		Validate.notEmpty(selectColumns);

		List<BcsSendGroupDetail> sendGroupSetting = new ArrayList<BcsSendGroupDetail>();
		List<BcsSendGroupDetail> uploadMidSetting = new ArrayList<BcsSendGroupDetail>();

		// 分離 Upload Mid Detail Setting
		for (BcsSendGroupDetail detail : sendGroupDetails) {
			if ("UploadMid".equals(detail.getQueryField())) {
				uploadMidSetting.add(detail);
			} else {
				sendGroupSetting.add(detail);
			}
		}

		sendGroupDetails = sendGroupSetting;

		// 驗證 queryOp，避免SQL攻擊(SQL injection)
		checkSendGroupDetail(sendGroupDetails);

		String sqlString = "SELECT " + selectColumns + " FROM ";

		if (sendGroupDetails != null && sendGroupDetails.size() > 0) {
			sqlString += generateMidFieldSettingFrom(sendGroupDetails, 1);
		}

		// Setting Upload Mid SQL
		if (uploadMidSetting != null && uploadMidSetting.size() > 0) {
			if (sendGroupDetails != null && sendGroupDetails.size() > 0) {
				sqlString += ", " + generateUploadMidSettingFrom(uploadMidSetting, sendGroupDetails.size() * 2 + 1);
			} else {

				selectColumns = selectColumns.replace("MID", "SETMID");

				sqlString = "SELECT " + selectColumns + " FROM " + generateUploadMidSettingFrom(uploadMidSetting, 1);

				if (StringUtils.isNotBlank(mid)) {
					sqlString += " WHERE SETMID = ?" + (uploadMidSetting.size() + 1) + " ";
				}
			}
		}

		// Setting Upload Mid SQL
		if (uploadMidSetting != null && uploadMidSetting.size() > 0) {
			if (sendGroupDetails != null && sendGroupDetails.size() > 0) {
				sqlString += " WHERE MID = EVENT_SET.SETMID ";

				if (StringUtils.isNotBlank(mid)) {
					sqlString += " AND MID = ?" + (sendGroupDetails.size() * 2 + 1 + uploadMidSetting.size()) + " ";
				}
			}
		} else {
			if (sendGroupDetails != null && sendGroupDetails.size() > 0) {
				if (StringUtils.isNotBlank(mid)) {
					sqlString += " WHERE MID = ?" + (sendGroupDetails.size() * 2 + 1) + " ";
				}
			}
		}

		log.info(sqlString);

		if (StringUtils.isBlank(sqlString)) {
			throw new Exception("SQL Error : Blank");
		}

		Query query = entityManager.createNativeQuery(sqlString);
		query.setHint("javax.persistence.query.timeout", 30000);

		for (int i = 0; i < sendGroupDetails.size(); i++) {
			query.setParameter(2 * i + 1, sendGroupDetails.get(i).getQueryField());
			query.setParameter(2 * i + 2, sendGroupDetails.get(i).getQueryValue());
			log.info("setParameter Field:" + (2 * i + 1) + ", " + sendGroupDetails.get(i).getQueryField());
			log.info("setParameter Value:" + (2 * i + 2) + ", " + sendGroupDetails.get(i).getQueryValue());
		}

		// Setting Upload Mid Parameter
		if (uploadMidSetting != null && uploadMidSetting.size() > 0) {

			for (int i = 0; i < uploadMidSetting.size(); i++) {
				String value = uploadMidSetting.get(i).getQueryValue();
				query.setParameter(sendGroupDetails.size() * 2 + i + 1, value.split(":")[0]);
				log.info("setParameter:" + (sendGroupDetails.size() * 2 + i + 1) + ", " + value.split(":")[0]);
			}
		}

		if (StringUtils.isNotBlank(mid)) {
			query.setParameter(sendGroupDetails.size() * 2 + uploadMidSetting.size() + 1, mid);
			log.info("setParameter:" + (sendGroupDetails.size() * 2 + uploadMidSetting.size() + 1) + ", " + mid);
		}

		return query;
	}
	

	
	private String generateMidFieldSettingFrom(List<BcsSendGroupDetail> sendGroupDetails, int params){

		if(sendGroupDetails != null && sendGroupDetails.size() > 0){
			String sqlString = 
					"( "
					+ " SELECT f.MID as MID"
					+ " FROM BCS_USER_FIELD_SET f ";
			
			sqlString += " INNER JOIN BCS_LINE_USER u ON u.MID = f.MID ";
			
			if(sendGroupDetails.size() > 1){
				for(int i = 1; i < sendGroupDetails.size(); i++){
					sqlString += " INNER JOIN BCS_USER_FIELD_SET f" + i + " ON f.MID = f" + i + ".MID ";
				}
			}
			
			BcsSendGroupDetail detail = sendGroupDetails.get(0);
			sqlString += " WHERE f.KEY_DATA = ?" + params + " and f.VALUE " + detail.getQueryOp() + " ?" + (params+1) + " ";
			
			sqlString += " AND (u.STATUS = 'BINDED' OR u.STATUS = 'UNBIND') ";

			if(sendGroupDetails.size() > 1){
				for(int i = 1; i < sendGroupDetails.size(); i++){
					detail = sendGroupDetails.get(i);
					sqlString += " AND f" + i + ".KEY_DATA = ?" + (2*i+params) + " and f" + i + ".VALUE " + detail.getQueryOp() + " ?" + (2*i+params+1) + " ";
				}
			}
			
			sqlString+= " ) AS FIELD_SET ";
			
			return sqlString;
		}
		
		return null;
	}
	
	/**
	 * 產生 Upload Mid Setting From SQL
	 * 
	 * @param sendGroupDetails
	 * @param params
	 * @return
	 */
	private String generateUploadMidSettingFrom(List<BcsSendGroupDetail> sendGroupDetails, int params) {

		if (sendGroupDetails != null && sendGroupDetails.size() > 0) {
			String sqlString = "( " + " SELECT s.MID as SETMID" + " FROM BCS_USER_EVENT_SET s ";

			sqlString += " INNER JOIN BCS_LINE_USER k ON k.MID = s.MID ";

			if (sendGroupDetails.size() > 1) {
				for (int i = 1; i < sendGroupDetails.size(); i++) {
					sqlString += " INNER JOIN BCS_USER_EVENT_SET s" + i + " ON s.MID = s" + i + ".MID ";
				}
			}

			sqlString += " WHERE s.REFERENCE_ID = ?" + params + " ";

			sqlString += " AND (k.STATUS = 'BINDED' OR k.STATUS = 'UNBIND') ";

			if (sendGroupDetails.size() > 1) {
				for (int i = 1; i < sendGroupDetails.size(); i++) {
					sqlString += " OR s" + i + ".REFERENCE_ID = ?" + (i + params) + " ";
				}
			}

			sqlString += " ) AS EVENT_SET ";

			return sqlString;
		}

		return null;
	}

	/**
	 * 驗證 queryField、queryOp，避免SQL攻擊(SQL injection)
	 * 
	 * @param sendGroupDetails
	 */
	private void checkSendGroupDetail(List<BcsSendGroupDetail> sendGroupDetails) {
		for (BcsSendGroupDetail sendGroupDetail : sendGroupDetails) {
			String queryOp = sendGroupDetail.getQueryOp();

			if (!validQueryOp.contains(queryOp)) {
				throw new IllegalArgumentException("queryOp is illegal! queryOp : " + queryOp);
			}
		}
	}
}
