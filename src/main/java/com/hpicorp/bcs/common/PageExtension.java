package com.hpicorp.bcs.common;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageExtension {

	private PageExtension() {		
	}
	
	public static <T extends Object> T getFirstFromPage(Page<T> page) {
		List<T> pageContent = PageExtension.getListFromPage(page);
		if (pageContent == null || pageContent.isEmpty()) {
			return null;
		}
		return pageContent.get(0);
	}
	
	public static <T extends Object>List<T> getListFromPage(Page<T> page) {
		if (page == null) {
			return Collections.emptyList();
		} else {
			List<T> pageContent = page.getContent();
			if ( pageContent != null && ! pageContent.isEmpty() ) {
				return pageContent;
			} else {
				return Collections.emptyList();
			}
		}
	}
}
