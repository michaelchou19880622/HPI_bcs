package com.hpicorp.bcs.controllers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpicorp.bcs.repositories.LineUserGroupRepository;
import com.hpicorp.bcs.services.AzureCDNService;
import com.hpicorp.bcs.services.UploadUidService;
import com.hpicorp.core.entities.LineUserGroup;
import com.hpicorp.core.entities.UploadUid;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/lineusergroup")
public class LineUserGroupController {
	
	private static final String groupDataKey = "groupData";
	
	private static final String fileListKey = "fileList";
	
	@Value("${upload.file.directory}")
	private String path;

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private LineUserGroupRepository lineUserGroupRepository;

	@Autowired
	private UploadUidService uploadUidService;
	
	@Autowired
	private AzureCDNService azureCDNService;

	/**
	 * [Read List]群組列表
	 * @param pageable
	 * @return
	 */
	@GetMapping("")
	public Page<LineUserGroup> getAllLineUserGroup(@PageableDefault(value = 10) Pageable pageable) {
		return lineUserGroupRepository.findAll(pageable);
	}
	
	/**
	 * [Read List]群組下拉選單
	 * @return
	 */
	@GetMapping("/all")
	public List<LineUserGroup> getAllLineUserGroup() {
		return lineUserGroupRepository.findAll();
	}

	/**
	 * [Read List]群組下拉選單(之後優化會刪掉)
	 * @return
	 */
	@GetMapping("/all/name")
	public List<Map<String, String>> getAllLineUserGroupName() {
		List<LineUserGroup> grouplist = lineUserGroupRepository.findAll();
		List<Map<String, String>> result = new ArrayList<>();
		for (LineUserGroup g : grouplist) {
			Map<String, String> mapped = new HashMap<>();
			mapped.put("id", String.valueOf(g.getId()));
			mapped.put("name", String.valueOf(g.getName()));
			result.add(mapped);
		}
		return result;
	}

	/**
	 * [Read] 取得群組
	 * @param lineUserGroupId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/id/{id}")
	public Map<String, Object> getLineUserGroupById(@PathVariable(value = "id") Long lineUserGroupId) throws Exception {
		Map<String, Object> returnMap = new HashMap<>();
		
		// Step1. Get LineUserGroup Data
		LineUserGroup lineUserGroup = lineUserGroupRepository.findById(lineUserGroupId)
				.orElseThrow(() -> new Exception("get LineUserGroup error => " + lineUserGroupId));
		returnMap.put("lineUserGroup", lineUserGroup);
		
		// Step2. Get UploadUid Data
		List<Map<String, String>> uploadUid = uploadUidService.getFilenameAndOriginalFilenameByGroupId(lineUserGroupId);
		returnMap.put("uploadUid", uploadUid);

		return returnMap;
	}

	
	/**
	 * [Create]建立群組
	 * @param createData
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/newgroup")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<Object> createLineUserGroupWithCondition(@Valid @RequestBody List<Map<String, Object>> createData) throws Exception {

		// Step 1. 宣告資料
		Long groupId;
		Map<String, String> groupDataMap = new HashMap<>();
		List<Map<String, Object>> fileDataList = new ArrayList<>();

		// Step 2. 檢查參數
		if (createData.isEmpty()) {
			return ResponseEntity.badRequest().body("No data found !");
		}

		// Step 3. 取得資料
		for (int i = 0; i < createData.size(); i++) {
			if (createData.get(i).containsKey(groupDataKey)) {
				groupDataMap = (Map<String, String>) createData.get(i).get(groupDataKey);
			}
			if (createData.get(i).containsKey(fileListKey)) {
				fileDataList = (List<Map<String, Object>>) createData.get(i).get(fileListKey);
			}
		}

		// Step 4. 創建群組
		try {
			LineUserGroup lineUserGroup = new LineUserGroup();
			lineUserGroup.setName(groupDataMap.get("name"));
			lineUserGroup.setDescription(groupDataMap.get("description"));
			lineUserGroup.setModifyAccount(groupDataMap.get("modify_account"));
			LineUserGroup createdLineUserGroup = createLineUserGroup(lineUserGroup);
			groupId = createdLineUserGroup.getId();
		} catch (Exception e) {
			log.error("Create Group Exception - ", e);
			throw e;
		}

		// Step 5. 更新群組收尋語法
		if (!fileDataList.isEmpty()) {
			try {
				for (Map<String, Object> file : fileDataList) {
					uploadUidService.updateByFilename(getFilename(file), groupId);
				}
			} catch (Exception e) {
				log.error("create lineuser_group error => {}", e);
				throw e;
			}
		}

		// Step 6. update group with getusers
		try {
			String getUsers = buildGetUsersScript(fileDataList);
			lineUserGroupRepository.updateGetUsers(groupId, getUsers);
		} catch (Exception e) {
			log.error("Update Group with getUsers Exception - ", e);
			throw e;
		}
		return ResponseEntity.ok().build();
	}

	/**
	 * [Update]修改群組
	 * @param groupId
	 * @param modifyData
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@PutMapping("/modifygroup/{id}")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<Object> modifyGroup(@PathVariable(value = "id") Long groupId, @Valid @RequestBody List<Map<String, Object>> modifyData) throws Exception {
		
		// Step1. Check Data and Initialize
		if (modifyData.isEmpty())
			return ResponseEntity.badRequest().body("No data found !");
		
		Map<String, String> groupData = new HashMap<>();
		List<Map<String, Object>> fileData = new ArrayList<>();
		
		// Step2. Get Data
		for (int i = 0; i < modifyData.size(); i++) {
			if (modifyData.get(i).containsKey(groupDataKey))
				groupData = (Map<String, String>) modifyData.get(i).get(groupDataKey);
			if (modifyData.get(i).containsKey(fileListKey))
				fileData = (List<Map<String, Object>>) modifyData.get(i).get(fileListKey);
		}
		
		if (!lineUserGroupRepository.existsById(groupId))
			return ResponseEntity.badRequest().body("No Group Data Found. Invalid Id");
		
		// Step3. Update UploadUid with groupId
		if (!fileData.isEmpty()) {
			try {
				for (Map<String, Object> file : fileData) {
					uploadUidService.updateByFilename(getFilename(file), groupId);
				}
			} catch (Exception e) {
				log.error("Update UploadUid with groupId Exception => {}", e);
				throw e;
			}
		}
		
		// Step4. Update Group with getUsers
		try {
			String getUsers = buildGetUsersScript(fileData);
			lineUserGroupRepository.updateGetUsers(groupId, getUsers);
		} catch (Exception e) {
			log.error("Update Group with getUsers Exception => {}", e);
			throw e;
		}
		
		// Step5. Update Group
		try {
			LineUserGroup lineUserGroup = new LineUserGroup();
			lineUserGroup.setName(groupData.get("name"));
			lineUserGroup.setDescription(groupData.get("description"));
			lineUserGroup.setModifyAccount(groupData.get("modify_account"));
			updateLineUserGroup(groupId, lineUserGroup);
		} catch (Exception e) {
			log.error("Update Group Exception => {}", e);
			throw e;
		}
		return ResponseEntity.ok().build();
		
	}

	/**
	 * [Delete]刪除群組
	 * @param lineUserGroupId
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteLineUserGroup(@PathVariable(value = "id") Long lineUserGroupId) throws Exception {
		LineUserGroup lineUserGroup = lineUserGroupRepository.findById(lineUserGroupId)
				.orElseThrow(() -> new Exception("LineUserGroup error => " + lineUserGroupId));
		lineUserGroupRepository.delete(lineUserGroup);
		return ResponseEntity.ok().build();
	}

	/**
	 * 	群組上傳檔案，儲存CSV內的UID
	 * @param fileList
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/savefiles")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<Object> saveUploadCsvFiles(@Valid @RequestBody List<Map<String, Object>> fileList) throws Exception {
		
		// Step 1. check data is upload success
		if (fileList.isEmpty()) {
			return ResponseEntity.badRequest().body("File Not Found");
		}

		// Step 2. get data
		for (Map<String, Object> file : fileList) {
			String fileName = getFilename(file);
			String oriFilename = file.get("name").toString();

			// Step 2.5 check filename exist
			boolean fileExist = uploadUidService.existsByFilename(fileName);
			if (fileExist) {
				continue;
			}

			// Step 3. insert data into table
			azureCDNService.downFile(fileName);
			
			BufferedReader reader = null;
			List<UploadUid> uploadUidList = new ArrayList<>();
			Map<String, String> mapList = new HashMap<>();
			try {
				reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(path + "/" + fileName), StandardCharsets.UTF_8));
				String line = null;
				while ((line = reader.readLine()) != null) {
					String[] item = line.split(",");
					for (int x = 0; x < item.length; x++) {
						if (item[x].length() == 33 || item[x].charAt(0) == 'U') {
							mapList.put(item[x], item[x]);
						}
					}
				}
				mapList.forEach((k, v) -> {
					UploadUid uu = new UploadUid();
					uu.setUid(v);
					uu.setOriginalFilename(oriFilename);
					uu.setFilename(fileName);
					uploadUidList.add(uu);
				});
				uploadUidService.saveUploadUidByList(uploadUidList);
			} catch (Exception e) {
				log.error("save upload_uid error => {}", e);
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
					Path filePath = Paths.get(path + fileName);
					Files.delete(filePath);
				} catch (IOException e) {
					log.error("reader close error = {}", e);
				}
			}
		}
		return ResponseEntity.ok().build();
	}

	/**
	 * 	上傳檔案後點選移除檔案，刪掉資料庫無用的資料
	 * @param filename
	 * @return
	 */
	@DeleteMapping("/deletefile/{filename}")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<Object> deleteUploadedCsvFile(@PathVariable(value = "filename") String filename) {

		// Step1. Check Data
		if (filename.isEmpty())
			return ResponseEntity.badRequest().body("File Data Not Found");
		
		// Step2. delete data
		try {
			uploadUidService.deleteByFilename(filename);
		} catch (Exception e) {
			log.error("deleteUploadedCsvFile Exception - ", e);
			throw e;
		}
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 	比較上傳名單、LineUser有符合的數量
	 * @param fileList
	 * @return
	 */
	@PostMapping("/compare")
	public ResponseEntity<Object> compareLineUser(@Valid @RequestBody List<Map<String, String>> fileList) {
		List<String> mappedUidList = new ArrayList<>();
		if (fileList.isEmpty()) {
			return ResponseEntity.badRequest().body("尚未上傳檔案！");
		} else {
			for (Map<String, String> filenameMap : fileList) {
				String filename = filenameMap.get("response").split("success:")[1];
				List<UploadUid> uploadUidList = uploadUidService.findByfilename(filename);
				for (UploadUid uploadUid : uploadUidList) {
					if (!mappedUidList.contains(uploadUid.getUid()))
						mappedUidList.add(uploadUid.getUid());
				}
			}
		}
		return ResponseEntity.ok().body(mappedUidList.size());
	}

	private String buildGetUsersScript(List<Map<String, Object>> fileData) {
		boolean checkNoFile = false;
		StringBuilder whereScript = new StringBuilder("select line_uid from lineuser as l where ");
		// If there is file upload
		if (!fileData.isEmpty()) {
			checkNoFile = false;
			whereScript.append("l.line_uid in (select distinct uid from upload_uid where filename in (");
			for (int i = 0; i < fileData.size(); i++) {
				whereScript.append("'" + getFilename(fileData.get(i)) + "'");
				if (i < fileData.size() - 1)
					whereScript.append(", ");
			}
			whereScript.append(") and original_filename in (");
			for (int i = 0; i < fileData.size(); i++) {
				whereScript.append("'" + fileData.get(i).get("name") + "'");
				if (i < fileData.size() - 1)
					whereScript.append(", ");
			}
			whereScript.append("))");
		} else {
			checkNoFile = true;
		}
		if (checkNoFile) {
			whereScript = new StringBuilder();
			whereScript.append("select line_uid from lineuser as l ");
		}
		return whereScript.toString();
	}

	public String getFilename(Map<String, Object> file) {
		return file.get("response").toString().split("success:")[1];
	}
	
	private LineUserGroup createLineUserGroup(LineUserGroup lineUserGroup) {
		lineUserGroup.setModifyTime(new Date());
		lineUserGroup.setSystemFlag("N");
		return lineUserGroupRepository.save(lineUserGroup);
	}
	
	private LineUserGroup updateLineUserGroup(Long lineUserGroupId, LineUserGroup lineUserGroupDetails) throws Exception {

		LineUserGroup lineUserGroup = lineUserGroupRepository.findById(lineUserGroupId)
				.orElseThrow(() -> new Exception("update LineUserGroup error => " + lineUserGroupId));

		lineUserGroup.setName(lineUserGroupDetails.getName());
		lineUserGroup.setDescription(lineUserGroupDetails.getDescription());
		lineUserGroup.setModifyAccount(lineUserGroupDetails.getModifyAccount());
		lineUserGroup.setModifyTime(new Date());
		return lineUserGroupRepository.save(lineUserGroup);
	}
}
