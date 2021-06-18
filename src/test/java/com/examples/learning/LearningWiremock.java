package com.examples.learning;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.examples.domain.Employee;
import com.examples.service.DoSomethingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class LearningWiremock {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8080);
	
	private DoSomethingService service;

	@Before
	public void setUp() {
		service = new DoSomethingService();
	}
	
	@Test
	public void first_test() throws JsonProcessingException {
		String jsonEmployee = new ObjectMapper().writeValueAsString(new Employee("ID1", "first", 1200));
		
		stubFor(get(urlEqualTo("/myapp/employees/ID1"))
			.willReturn(ok()
					.withHeader("Content-Type", MediaType.APPLICATION_JSON)
					.withBody(jsonEmployee)
					)
			);
		assertThatCode(() -> {
			Employee employee = service.getEmployeeById("ID1");
			assertThat(employee).isEqualTo(new Employee("ID1", "first", 1200));
		}).doesNotThrowAnyException();
		
		verify(getRequestedFor(urlPathEqualTo("/myapp/employees/ID1")));
	}
	
	
}
