package com.hpicorp.bcs.enums;

public enum LineApiUrl {

	// liff
	VIEW("/view"),
	LIFF("https://api.line.me/liff/v1/apps/"),
	LIFF_END_POINT("line://app/"),
	
	// rich menu
	RICHMENU("/richmenu/"),
	RICH_MENU("https://api.line.me/v2/bot/richmenu/"),
	RICH_MENU_LIST("https://api.line.me/v2/bot/richmenu/list"),
	RICH_MENU_DEFAULT("https://api.line.me/v2/bot/user/all/richmenu/"),
	LINK_RICH_MENU("https://api.line.me/v2/bot/user/"),
	LINK_RICH_MENU_MULTIPLE("https://api.line.me/v2/bot/richmenu/bulk/link");
	
	
	private String value;
	
	private LineApiUrl(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
