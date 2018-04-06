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

package com.example.proxies.services.impl;

import com.example.proxies.clients.ExtractionClient;
import com.example.proxies.services.ConsumerScheduler;
import com.example.proxies.services.ProcessConsumer;
import com.example.proxies.services.ProcessQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConsumerSchedulerImpl implements ConsumerScheduler {
    private static final Logger log = LoggerFactory.getLogger(ConsumerSchedulerImpl.class);

    private static final int MAX_WORKERS = 8;

    private ProcessQueue processQueue;
    private ExtractionClient extractionClient;

    private ExecutorService executor;
    private int numberOfWorkers;

    @Autowired
    public ConsumerSchedulerImpl(ProcessQueue processesQueue, ExtractionClient extractionClient, final int numberOfWorkers) {
        if (processesQueue == null) {
            throw new IllegalArgumentException("queue argument must not be null");
        }

        if (numberOfWorkers < 0 || numberOfWorkers > MAX_WORKERS) {
            throw new IllegalArgumentException("Argument must be between 1 and " + MAX_WORKERS);
        }

        this.numberOfWorkers = numberOfWorkers;
        this.processQueue = processesQueue;
        this.extractionClient = extractionClient;

        this.executor = Executors.newFixedThreadPool(this.numberOfWorkers);
    }

    @Override
    public void start() {
        for (int i = 0; i < this.numberOfWorkers; i++) {
            this.executor.execute(new ProcessConsumer(this.processQueue, this.extractionClient));
        }
    }

    @Override
    public void shutdown() {
        try {
            if (this.executor.awaitTermination(10, TimeUnit.SECONDS)) {
                log.info("All workers stop");
            } else {
                log.warn("Forcing shutdown workers");
                this.executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Exited while waiting for workers to terminate", e);
        }
    }
}
