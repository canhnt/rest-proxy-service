/*
 * Copyright (C) 2018 Canh Ngo <canhnt@gmail.com>
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.proxies;

import com.example.proxies.clients.ExtractionClient;
import com.example.proxies.services.ConsumerScheduler;
import com.example.proxies.services.ProcessQueue;
import com.example.proxies.services.impl.ConsumerSchedulerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IntegrationServiceApplication {
	public static final int NUMBER_OF_WORKERS = 8;

	public static void main(String[] args) {
		SpringApplication.run(IntegrationServiceApplication.class, args);
	}

	@Bean
	@Autowired
	public ConsumerScheduler consumerScheduler(ProcessQueue processQueue, ExtractionClient extractionClient) {
		final ConsumerSchedulerImpl consumerScheduler = new ConsumerSchedulerImpl(processQueue, extractionClient, NUMBER_OF_WORKERS);
		consumerScheduler.start();
		return consumerScheduler;
	}
}