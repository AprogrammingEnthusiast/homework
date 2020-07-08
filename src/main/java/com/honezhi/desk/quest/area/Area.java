package com.honezhi.desk.quest.area;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Area {

	private int prefix;
	private short code;
	private String name;

	public int getPrefix() {
		return prefix;
	}

	public void setPrefix(int prefix) {
		this.prefix = prefix;
	}

	public short getCode() {
		return code;
	}

	public void setCode(short code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Area(int prefix, short code, String name) {
		super();
		this.prefix = prefix;
		this.code = code;
		this.name = name;
	}

}
