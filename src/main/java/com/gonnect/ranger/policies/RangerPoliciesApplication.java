package com.gonnect.ranger.policies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;

@SpringBootApplication(exclude = ElasticsearchRestClientAutoConfiguration.class)
public class RangerPoliciesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RangerPoliciesApplication.class, args);
	}

}
