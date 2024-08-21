package com.wynd.vop.framework.exception;

import com.wynd.vop.framework.messages.MessageKey;
import com.wynd.vop.framework.messages.MessageSeverity;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class SnsExceptionTest {

	private MessageKey var1;
	private MessageSeverity var2;
	private HttpStatus var3;
	private Throwable var4;
	private String var5 = "text";

	@Test
	public void BaseSnsException() throws Exception {

		SnsException snsException = new SnsException(var1, var2, var3, var4, var5 = null);
	}

	@Test
	public void NonThrowableSnsException() throws Exception {

		SnsException snsException = new SnsException(var1, var2, var3, var5 = null);
	}

}
