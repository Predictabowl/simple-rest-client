package com.examples.learning;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import com.examples.domain.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.ClientResponse.Status;

public class LearningHTTPRestful {

	private static final String EMPLOYEES_URI = "http://localhost:8080/myapp/employees";
	private HttpClient httpClient;

	@Before
	public void setUp() {
		httpClient = HttpClientBuilder.create().build();
	}
	
	@Test
	public void test_read_one_employee() throws ClientProtocolException, IOException {
		HttpGet getRequest = new HttpGet(EMPLOYEES_URI+"/ID1");
		getRequest.addHeader("accept", MediaType.APPLICATION_JSON);
		
		HttpResponse response = httpClient.execute(getRequest);
		
		assertThat(response.getStatusLine().getStatusCode()).isEqualTo(Status.OK.getStatusCode());

		String jsonResponse = EntityUtils.toString(response.getEntity());
		System.out.println(jsonResponse);
		ObjectMapper mapper = new ObjectMapper();
		Employee employee = mapper.readValue(jsonResponse, Employee.class);
		
		assertThat(employee).isEqualTo(new Employee("ID1", "first employee", 1000));
		
	}
	
	@Test
	public void test_read_all_employees() throws ClientProtocolException, IOException {
		HttpGet getRequest = new HttpGet(EMPLOYEES_URI);
		getRequest.addHeader("accept", MediaType.APPLICATION_JSON);
		
		HttpResponse response = httpClient.execute(getRequest);
		
		assertThat(response.getStatusLine().getStatusCode()).isEqualTo(Status.OK.getStatusCode());

		String jsonResponse = EntityUtils.toString(response.getEntity());
		ObjectMapper mapper = new ObjectMapper();
		List<Employee> employees = Arrays.asList(mapper.readValue(jsonResponse, Employee[].class));

		assertThat(employees).containsExactlyInAnyOrder(
				new Employee("ID1", "first employee", 1000)
				,new Employee("ID2", "second employee", 2000)
				,new Employee("ID3", "third employee", 3000));
	}
}
