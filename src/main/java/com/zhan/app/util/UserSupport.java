package com.zhan.app.util;

import java.beans.PropertyEditorSupport;

public class UserSupport extends PropertyEditorSupport {

	@Override
	public void setValue(Object value) {
		Object intVal = value;
		super.setValue(intVal);
	}

	@Override
	public String getAsText() {
		Object obj = this.getValue();
		return obj + "";
	}
}
