package com.wynd.vop.framework.test.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class RequestResponseLoggingInterceptorTest {

	@Before
	public void setUp() throws Exception {
		Logger rootLogger = (Logger) LoggerFactory.getLogger(RequestResponseLoggingInterceptor.class);
		rootLogger.setLevel(Level.DEBUG);
	}

	@Test
	public void interceptWithNullResponse() throws Exception {
		Request request = new Request();
		new RequestResponseLoggingInterceptor().intercept(request, null, new RequestExecutionNullResponse());
	}

	@Test
	public void interceptWithMock() throws Exception {
		Request request = new Request();
		new RequestResponseLoggingInterceptor().intercept(request, null, new RequestExecutionMockResponse());
	}

	@Test
	public void interceptWithMockHeadersAndBody() throws Exception {
		final byte[] byteBody = "Foo".getBytes();
		RequestWithOctetStreamHeaders request = new RequestWithOctetStreamHeaders();
		new RequestResponseLoggingInterceptor().intercept(request, byteBody, new RequestExecutionMockResponse());
	}
	

	@Test
	public void interceptWithMockAndBody() throws Exception {
		final byte[] byteBody = "Foo".getBytes();
		Request request = new Request();
		RequestExecutionBinaryMockResponse response = new RequestExecutionBinaryMockResponse();
		new RequestResponseLoggingInterceptor().intercept(request, byteBody, response);
	}

	/**
	 * The Class RequestWithOctetStreamHeaders.
	 */
	private class RequestWithOctetStreamHeaders extends Request implements HttpRequest {

		@Override
		public HttpHeaders getHeaders() {
			return MockHttpHeaders.getMockAcceptOctetStream();
		}

	}

	/**
	 * The Class Request.
	 */
	private class Request implements HttpRequest {

		HttpHeaders headers = new HttpHeaders();

		@Override
		public HttpMethod getMethod() {
			return null;
		}

		@Override
		public URI getURI() {
			return null;
		}

		@Override
		public HttpHeaders getHeaders() {
			return headers;
		}

		@Override
		public String getMethodValue() {
			return null;
		}
	}

	/**
	 * The Class RequestExecutionNullResponse.
	 */
	private class RequestExecutionNullResponse implements ClientHttpRequestExecution {

		@Override
		public ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException {
			return null;
		}

	}

	/**
	 * The Class RequestExecutionMockResponse.
	 */
	private class RequestExecutionMockResponse implements ClientHttpRequestExecution {

		/** The response mock. */
		private ResponseMock responseMock = new ResponseMock();

		@Override
		public ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException {
			return responseMock;
		}

	}

	/**
	 * The Class RequestExecutionBinaryMockResponse.
	 */
	private class RequestExecutionBinaryMockResponse implements ClientHttpRequestExecution {

		/** The response mock. */
		private ResponseMockWithBody responseMockWithBody = new ResponseMockWithBody();
		HttpHeaders mockAcceptOctetStream = MockHttpHeaders.getMockAcceptOctetStream();

		@Override
		public ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException {
			this.responseMockWithBody.setHeaders(mockAcceptOctetStream);
			return this.responseMockWithBody;
		}

	}
	
	/**
	 * The Class RequestExecutionMockResponse.
	 */
	private static class MockHttpHeaders {

		public static HttpHeaders getMockAcceptOctetStream() {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_OCTET_STREAM_VALUE);
			return headers;
		}

	}

	/**
	 * The Class ResponseMockWithBody.
	 */
	private static class ResponseMockWithBody extends ResponseMock implements ClientHttpResponse {

		@Override
		public InputStream getBody() throws IOException {
			return new ByteArrayInputStream("Foo".getBytes());
		}
	}

	/**
	 * The Class ResponseMock.
	 */
	private static class ResponseMock implements ClientHttpResponse {

		private HttpStatus statusCode = HttpStatus.OK;

		private String statusText = "";

		private HttpHeaders headers = new HttpHeaders();

		@Override
		public HttpStatus getStatusCode() throws IOException {
			return statusCode;
		}

		@Override
		public int getRawStatusCode() throws IOException {
			return statusCode.value();
		}

		@Override
		public String getStatusText() throws IOException {
			return statusText;
		}

		@Override
		public HttpHeaders getHeaders() {
			return headers;
		}

		@Override
		public InputStream getBody() throws IOException {
			return null;
		}

		@Override
		public void close() {
		}

		public void setHeaders(HttpHeaders headers) {
			this.headers = headers;
		}
	}
}