package com.eurodyn.qlack.fuse.mailing.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * @author European Dynamics SA
 */
@Getter
@Setter
public class AttributeDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> attribute = new HashMap<String, Object>();

	public Object clearAttribute(String key) {
		return this.getAttribute().remove(key);
	}

	public Object getAttribute(String key) {
		return this.getAttribute().get(key);
	}

	public void setAttribute(String key, Object value) {
		this.getAttribute().put(key, value);
	}

}