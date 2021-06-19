package com.examples.learning;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.examples.domain.Employee;
import com.examples.service.DoSomethingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class LearningWiremock {
	
	private static final String EMPLOYEE_URI = "/myapp/employees";

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
		
		stubFor(get(urlPathEqualTo(EMPLOYEE_URI+"/ID1"))
			.willReturn(ok()
					.withHeader("Content-Type", MediaType.APPLICATION_JSON)
					.withBody(jsonEmployee)
					)
			);
		assertThatCode(() -> {
			Employee employee = service.getEmployeeById("ID1");
			assertThat(employee).isEqualTo(new Employee("ID1", "first", 1200));
		}).doesNotThrowAnyException();
		
		verify(getRequestedFor(urlPathEqualTo(EMPLOYEE_URI+"/ID1")));
	}
	
	@Test
	public void test_get_all_employees() throws JsonProcessingException {
		List<Employee> employees = new LinkedList<>();
		employees.add(new Employee("ID1", "first", 1000));
		employees.add(new Employee("ID2", "second", 2000));
		employees.add(new Employee("ID3", "third", 3000));
		
		stubFor(get(urlEqualTo(EMPLOYEE_URI))
				.willReturn(ok()
						.withHeader("Content-Type", MediaType.APPLICATION_JSON)
						.withBody(new ObjectMapper().writeValueAsString(employees))));
		
		assertThatCode(() ->{
				List<Employee> received = service.getAllEmployees();
				assertThat(received).isEqualTo(employees);
			}).doesNotThrowAnyException();
		
		verify(getRequestedFor(urlEqualTo(EMPLOYEE_URI)));
	}
	
	
}
