package com.hpicorp.bcs.entities.dto;

public class FromToBody {

	private Integer from;
	private Integer to;
	
	public FromToBody() {
	}

	public FromToBody(Integer from, Integer to) {
		super();
		this.from = from;
		this.to = to;
	}

	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	public Integer getTo() {
		return to;
	}

	public void setTo(Integer to) {
		this.to = to;
	}
	
	
	
}
