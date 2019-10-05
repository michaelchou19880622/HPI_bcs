package com.hpicorp.bcs.entities.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMsgBody {

	private List<String> UID;
	
	private String messageId;
	
	private String campaignName;
	
	private String content;

}
