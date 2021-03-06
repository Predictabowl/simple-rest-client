package com.examples.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.examples.domain.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DoSomethingService {

	public static final String EMPLOYEES_URI = "http://localhost:8080/myapp/employees";
	private HttpClient client;
	
	public DoSomethingService() {
		client = HttpClientBuilder.create().build();
	}
	
	public Employee getEmployeeById(String id) throws IOException{
		var getRequest = new HttpGet(EMPLOYEES_URI+"/"+id);
		getRequest.addHeader("accept", MediaType.APPLICATION_JSON);
		
		HttpResponse response = client.execute(getRequest);
		var jsonResponse = EntityUtils.toString(response.getEntity());
		return new ObjectMapper().readValue(jsonResponse, Employee.class);
	}
	
	public List<Employee> getAllEmployees() throws IOException{
		HttpGet getRequest = new HttpGet(EMPLOYEES_URI);
		getRequest.addHeader("accept",MediaType.APPLICATION_JSON);
		
		HttpResponse response = client.execute(getRequest);
		var jsonResponse = EntityUtils.toString(response.getEntity());
		return Arrays.asList(new ObjectMapper().readValue(jsonResponse, Employee[].class));
	}
}
