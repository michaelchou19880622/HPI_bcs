package com.hpicorp.bcs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.hpicorp.bcs.config.Schedule;
import com.hpicorp.bcs.schedule.job.RichMenuJob;
import com.hpicorp.core.common.HttpClientUtil;
import com.hpicorp.core.dto.ResponseBody;
import com.hpicorp.core.dto.RichMenuMultiple;
import com.hpicorp.core.entities.Action;
import com.hpicorp.core.entities.Bounds;
import com.hpicorp.core.entities.RichMenu;
import com.hpicorp.core.entities.RichMenuAreas;
import com.hpicorp.core.entities.RichMenuList;
import com.hpicorp.core.entities.RichMenuSize;
import com.hpicorp.core.entities.SystemConfig;
import com.hpicorp.core.enums.LineApiUrl;
import com.hpicorp.core.enums.ScheduleType;
import com.hpicorp.core.exception.AppException;
import com.hpicorp.core.repository.RichMenuRepository;
import com.hpicorp.core.repository.SystemConfigRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RichMenuService {

	@Autowired
	private SystemConfigRepository systemConfigRepository;
	
	private Gson gson = new Gson();
	
	private static final String BEARER = "Bearer ";
	private static final String AUTHORIZATION = "Authorization";

	@Value("${line.config.key}")
	private String lineConfigKey;
	
	@Value("${upload.file.directory}")
	private String path;
	
	@Autowired
	private Schedule schedule;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private RichMenuListService richMenuListService;
	
	@Autowired
	private RichMenuRepository richMenuRepository;
	
	/**
	 * 取得 richMenu list  
	 */
	public Object getRichMenuList() {
		return this.richMenuRepository.findByLevel();
	}

	public Object getRichMenuListById(RichMenu richMenu) {
		return this.richMenuRepository.findByChatBarText(richMenu.getChatBarText());
	}
	
	/**
	 * 創建 richMenu  
	 */
	public void createRichMenuList(List<RichMenu> richMenuList) throws Exception {
		
		for (RichMenu richMenu : richMenuList) {
			// Step 1. 呼叫 Line Api 保存該圖文選單內容
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.set(AUTHORIZATION, BEARER + this.getLineToken());
			HttpEntity<RichMenu> request = new HttpEntity<>(richMenu, headers);
			ResponseEntity<String> response = this.restTemplate.postForEntity(LineApiUrl.RICH_MENU.getValue(), request, String.class);
			String richmenuId = response.getBody();
			
			// Step 2. 上傳該圖檔，綁定該圖文選單
			this.uploadRichMenuImage(richmenuId, richMenu.getImage());
			
			// Step 3. 保存 RichMenu 到 db
			RichMenuSize size = richMenu.getSize();
			size.setRichmenu(richMenu);
			richMenu.setSize(size);
			List<RichMenuAreas> areasList = richMenu.getAreas();
			areasList.forEach(i -> {
				i.setRichmenu(richMenu);
				Action action = i.getAction();
				action.setRichmenuAreas(i);
				i.setAction(action);
				Bounds bounds = i.getBounds();
				bounds.setRichmenuAreas(i);
				i.setBounds(bounds);
			});
			richMenu.setAreas(areasList);
			richMenu.setRichMenuId(this.getRichMenuId(richmenuId));
			richMenu.setModifyTime(new Date());
			this.richMenuRepository.save(richMenu);

			// Step 4. 保存 RichMenuList 到 db
			RichMenuList rml = new RichMenuList();
			rml.setName(richMenu.getName());
			rml.setRichMenuId(richMenu.getRichMenuId());
			rml.setCustomeId(richMenu.getCustomId());
			rml.setCreateTime(new Date());
			this.richMenuListService.save(rml);
			
			// Step 5. 將任務放入排程之中
			if (richMenu.getLevel().equals("1"))
				addScheduleDate(richMenu);
			
		}
		
	}
	
	/**
	 * 修改 richMenu  
	 */
	public void updateRichMenuList(List<RichMenu> richMenuList) throws Exception {
		
		for (RichMenu richMenu : richMenuList) {
			// Step 1. 呼叫 Line Api 保存該圖文選單內容
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.set(AUTHORIZATION, BEARER + this.getLineToken());
			HttpEntity<RichMenu> request = new HttpEntity<>(richMenu, headers);
			ResponseEntity<String> response = this.restTemplate.postForEntity(LineApiUrl.RICH_MENU.getValue(), request, String.class);
			String richmenuId = response.getBody();
			
			// Step 2. 上傳該圖檔，綁定該圖文選單
			this.uploadRichMenuImage(richmenuId, richMenu.getImage());
			
			log.info("richMenu.getRichMenuId() = {}", richMenu.getRichMenuId());
			
			// Step 3. 刪除掉原本的圖文選單（ Line的 ），但需要判斷是否為空，因為有可能新增多層
			if (richMenu.getRichMenuId() != null) {

				try {
					deleteRichMenu(richMenu.getRichMenuId());
				} catch (Exception e) {
					if (e instanceof HttpClientErrorException && ((HttpClientErrorException) e).getStatusCode() == HttpStatus.NOT_FOUND) {
						log.info("Richmenu not found, no need to delete.");
					} else {
						log.info("Exception : {}", e);
					}
				}
				
				RichMenuList list = richMenuListService.findByRichMenuId(richMenu.getRichMenuId());
				log.info("list = {}", list);
				
				if (list != null) {
					richMenuListService.delete(list);
				}
			}
			
			// Step 4. 保存 RichMenu 到 db
			RichMenuSize size = richMenu.getSize();
			size.setRichmenu(richMenu);
			richMenu.setSize(size);
			List<RichMenuAreas> areasList = richMenu.getAreas();
			areasList.forEach(i -> {
				i.setRichmenu(richMenu);
				Action action = i.getAction();
				action.setRichmenuAreas(i);
				i.setAction(action);
				Bounds bounds = i.getBounds();
				bounds.setRichmenuAreas(i);
				i.setBounds(bounds);
			});
			richMenu.setAreas(areasList);
			richMenu.setRichMenuId(this.getRichMenuId(richmenuId));
			richMenu.setModifyTime(new Date());
			this.richMenuRepository.save(richMenu);

			// Step 5. 保存 RichMenuList 到 db
			RichMenuList rml = new RichMenuList();
			rml.setName(richMenu.getName());
			rml.setRichMenuId(richMenu.getRichMenuId());
			rml.setCustomeId(richMenu.getCustomId());
			rml.setCreateTime(new Date());
			this.richMenuListService.save(rml);
			
			// Step 6. 將任務放入排程之中
			if (richMenu.getLevel().equals("1")) {
				addScheduleDate(richMenu);
			}
			
		}
		
	}
	
	/**
	 * 設置 用戶 綁定 richmenu 
	 */
	public void setRichMenuByUser(String lineUserId, String richMenuId, String lineToken) {
		Header[] headers = this.getHeaders(lineToken);
		ResponseBody body = HttpClientUtil.postJSON(getLinkRichMenuUrl(lineUserId, richMenuId), "", headers);
		if (body.getStatus() != 200) {
			throw new AppException("綁定用戶 rich menu 發生錯誤" + body.getData());
		}
	}
	
	public void setRichMenuMultiple(RichMenuMultiple multiple, String lineToken) {
		Header[] headers = this.getHeaders(lineToken);
		String body = gson.toJson(multiple);
		ResponseBody response = HttpClientUtil.postJSON(LineApiUrl.LINK_RICH_MENU_MULTIPLE.getValue(), body, headers);
		if (response.getStatus() != 202) {
			throw new AppException("綁定用戶 rich menu 發生錯誤" + response.getData());
		}
	}
	
	public void setRichMenuDefault(String richmenuId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.set(AUTHORIZATION, BEARER + this.getLineToken());
		HttpEntity<Object> request = new HttpEntity<>(headers);
		this.restTemplate.postForObject(LineApiUrl.RICH_MENU_DEFAULT.getValue() + richmenuId, request, String.class);
	}
	
	/**
	 * 刪除 綁定 richMenu  
	 */
	public void deleteLinkRichMenuByUser(String lineUserId) {
		String lineToken = this.getLineToken();
		Header[] headers = this.getHeaders(lineToken);
		ResponseBody delete = HttpClientUtil.delete(getLinkRichMenuUrlByUser(lineUserId), headers);
		if (delete.getStatus() != 200) {
			throw new AppException("刪除綁定 richmenu 發生錯誤, " + delete.getData());
		}
	}
	
	public void deleteRichMenu(String richMenuId) {
		String token = this.getLineToken();
		HttpHeaders headers = new HttpHeaders();
		headers.set(AUTHORIZATION, BEARER + token);
		HttpEntity<Object> request = new HttpEntity<>(null, headers);
		
		this.restTemplate.exchange(this.getDeleteRichMenuUrl(richMenuId), HttpMethod.DELETE, request, Void.class);
	}
	
	/**
	 * 上傳 richmenu 的設定資訊
	 */
	public String createRichMenuDetail(RichMenu richMenu) {
		String token = this.getLineToken();
		Header[] headers = this.getHeaders(token);
		String body = gson.toJson(richMenu);
		return this.post(LineApiUrl.RICH_MENU.getValue(), body, headers);
	}
	
	/**
	 * 上傳 richmenu 的圖片檔 
	 */
	public void uploadRichMenuImage(String richMenuResponse, String imageName) throws IOException {
		String imageUrl = this.getImageUrl(richMenuResponse);
		byte[] imageByte = getImageByte(imageName);
		this.post(imageUrl, imageByte, getExtension(imageName));
	}
	
	/**
	 * 取得 Line 的 Token
	 * @return
	 */
	public String getLineToken() {
		Optional<SystemConfig> systemConfig = this.systemConfigRepository.findByConfigKey(lineConfigKey);
		if (!systemConfig.isPresent()) {
			throw new AppException("該 Config key 不存在");
		}
		return systemConfig.get().getConfigValue();
	}
	
	/**
	 * 取得 即將被刪除的 圖文選單 
	 */
	public List<RichMenu> getDeleteRichMenuList() {
		return this.richMenuRepository.findOldRichMenu(9L);
	}
	
	/**
	 * 前端執行刪除 圖文選單 
	 */
	public void deleteRichMenu(RichMenu richMenu) {
		// 取的所有的圖文選單
		List<RichMenu> richMenuList = this.richMenuRepository.findByChatBarText(richMenu.getChatBarText());
		
		// 刪除 Line 那邊的圖文選單
		richMenuList.forEach(i -> this.deleteRichMenu(i.getRichMenuId()));
		
		// 刪除資料庫的圖文選單 
		this.richMenuRepository.deleteAll(richMenuList);
	}
	
	/**
	 * 特別寫的 method
	 * 主要是可以透過 Line RichMenu API 一次取得所有列表
	 * 然後將全部的圖文選單一次刪除
	 * @throws IOException
	 */
	public void deleteAllRichMenuFromLine() throws IOException {
		String token = this.getLineToken();
		HttpHeaders headers = new HttpHeaders();
		headers.set(AUTHORIZATION, BEARER + token);
		HttpEntity<Object> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = this.restTemplate.exchange("https://api.line.me/v2/bot/richmenu/list", HttpMethod.GET, request, String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(response.getBody());
		JsonNode richmenus = jsonNode.get("richmenus");
		for (JsonNode richmenu : richmenus) {
			String richMenuId = richmenu.get("richMenuId").textValue();
			deleteRichMenu(richMenuId);
		}
	}
	
	/**
	 * 生成 Header，並轉成 []
	 * @return
	 */
	private Header[] getHeaders(String token) {
		List<Header> headerList = Lists.newArrayList();
		headerList.add(new BasicHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE));
		headerList.add(new BasicHeader(AUTHORIZATION, BEARER + token));
		return headerList.toArray(new Header[headerList.size()]);
	}
	
	/**
	 * 送出 post Http
	 * @param url
	 * @param headers
	 * @param httpEntity
	 */
	private String post(String url, String body, Header[] headers) {
		ResponseBody response = HttpClientUtil.postJSON(url, body, headers);
		if (response.getStatus() != 200) {
			throw new AppException(response.getData());
		}
		return response.getData();
	}
	
	/**
	 * 取得圖片，並將其轉為 byte 型態 
	 */
//	private byte[] getImageByte(String filePath) throws IOException {
//		File file = new File(path + "/" + filePath);
//		BufferedImage originalImage = ImageIO.read(file);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ImageIO.write(originalImage, getExtension(file), baos);
//		return baos.toByteArray();
//	}
	
	/**
	 * 取得圖片，並將其轉為 byte 型態 
	 */
	private byte[] getImageByte(String filePath) throws IOException {
		byte[] buffer = null;
		InputStream is = null;
        try {
        		is = new URL(filePath).openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = is.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            is.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) { 
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
        		is.close();
        }
        return buffer;
	}
	
	/**
	 * 取得圖片的副檔名 
	 */
	private String getExtension(String image) {
		int startIndex = image.lastIndexOf(46) + 1;
		int endIndex = image.length();
		String extension = image.substring(startIndex, endIndex);
		if (extension.equals("jpg") || extension.equals("jpeg")) {
			return MediaType.IMAGE_JPEG_VALUE;
		} else if (extension.equals("png")) {
			return MediaType.IMAGE_PNG_VALUE;
		}
		return null;
	}
	
	/**
	 * 取得上傳圖片的路徑 
	 */
	private String getImageUrl(String richMenuId) {
		return LineApiUrl.RICH_MENU.getValue() + getRichMenuId(richMenuId) + "/content";
	}
	
	/**
	 * 取得 richmenuId 
	 */
	private String getRichMenuId(String richMenuId) {
		String[] value = richMenuId.replaceAll("\"", "").split(":");
		return value[1].substring(0, value[1].length() - 1);
	}
	
	/**
	 * 送出 http post，上傳圖片 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	private void post(String url, byte[] imageByte, String contentType) throws IOException {
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		try {
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", contentType);
			con.setRequestProperty("Authorization", BEARER + this.getLineToken());
			con.setDoOutput(true);
			con.setDoInput(true);
			con.getOutputStream().write(imageByte);
		} catch (IOException e) {
			throw new AppException("上傳圖片失敗");
		} finally {
			con.disconnect();
		}
	}
	
	/**
	 * 取得 link user richmenu url 
	 */
	private String getLinkRichMenuUrl(String lineUserId, String richMenuId) {
		return LineApiUrl.LINK_RICH_MENU.getValue() + lineUserId + LineApiUrl.RICHMENU.getValue() + richMenuId;
	}
	
	/**
	 * 取得 link richmenu url by user 
	 */
	private String getLinkRichMenuUrlByUser(String lineUserId) {
		return LineApiUrl.LINK_RICH_MENU.getValue() + lineUserId + LineApiUrl.RICHMENU.getValue();
	}
	
	/**
	 * 取得 delete richmenu url 
	 */
	private String getDeleteRichMenuUrl(String richMenuId) {
		return LineApiUrl.RICH_MENU.getValue() + richMenuId;
	}
	
	/**
	 * 添加圖文選單排程機制，排程機制為 - Date 
	 * @param message 圖文選單物件
	 * @throws Exception
	 */
	private void addScheduleDate(RichMenu richMenu) throws Exception {
		String detailName = RichMenu.class.getSimpleName() + "-" + richMenu.getId();
		Map<String, Object> jobDataAsMap = new HashMap<>();
		jobDataAsMap.put("richMenu", richMenu);
		JobDetail jobDetail = this.schedule.createJobDetail(new RichMenuJob(), detailName, ScheduleType.RICH_MENU_GROUP.getValue(), jobDataAsMap);
		Trigger trigger = this.schedule.createSimpleTrigger(richMenu.getStartDate(), jobDetail);
		this.schedule.addSchedule(jobDetail, trigger, detailName);
	}

}
