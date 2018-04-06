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

package com.example.proxies.services;

import java.io.IOException;

import com.example.proxies.clients.ExtractionClient;
import com.example.proxies.model.Process;
import com.example.proxies.utils.TempFileUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A consumer that fetches processes from the queue and send request to the TK backend service
 */
public class ProcessConsumer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ProcessConsumer.class);

    private final ProcessQueue processQueue;

    private final ExtractionClient extractionClient;

    public ProcessConsumer(ProcessQueue processQueue, ExtractionClient extractionClient) {
        if (processQueue == null || extractionClient == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        }
        this.processQueue = processQueue;
        this.extractionClient = extractionClient;
    }

    @Override
    public void run() {
            try {
                while (true) {
                    final Process process = processQueue.take();
                    if (process == null) {
                        log.error("Null object in the queue. Something wrong");
                        continue;
                    }

                    handle(process);
                }
            } catch (InterruptedException e) {
                log.debug("Consumer thread '{}' will terminate", Thread.currentThread().getId());
            }
    }

    // Forward cv file to the TK backend service
    private void handle(final Process process) {
        synchronized (process) {
            process.setState(Process.State.PROGRESS);
        }

        final String tempFilePath = process.getTempFilePath();
        String result = StringUtils.EMPTY;
        Process.State state = Process.State.ERROR;
        try {
            result = extractionClient.extract(tempFilePath, process.getFileName());
            state = Process.State.COMPLETED;
        } catch (IOException e) {
            log.error("Failed to call backend service", e);
        } finally {
            TempFileUtils.deleteTempFileIfExists(tempFilePath);
        }

        saveResult(process, result, state);
    }

    private void saveResult(final Process process, final String result, final Process.State state) {
        synchronized (process) {
            process.setResult(result);
            process.setState(state);
        }
    }
}
