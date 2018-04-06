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

import com.example.proxies.exceptions.JobException;
import com.example.proxies.model.Process;
import com.example.proxies.model.SubmitResponse;
import com.example.proxies.services.ExtractionProxyService;
import com.example.proxies.services.ProcessProducer;
import com.example.proxies.services.ProcessStore;
import com.example.proxies.utils.TempFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class ExtractionProxyServiceImpl implements ExtractionProxyService {
    private static final Logger log = LoggerFactory.getLogger(ExtractionProxyServiceImpl.class);

    private ProcessStore processStore;
    private final ProcessProducer processProducer;

    @Autowired
    public ExtractionProxyServiceImpl(ProcessStore processStore, final ProcessProducer processProducer) {
        this.processStore = processStore;
        this.processProducer = processProducer;
    }

    @Override
    public SubmitResponse submit(MultipartFile file) throws JobException {
        log.info("Submitted file " + file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new JobException("Submit file '" + file.getOriginalFilename() + "' is empty");
        }

        final Process process = onBeforeProcess(file);
        process(process);
        return new SubmitResponse(process.getId());
    }

    @Override
    public Process onBeforeProcess(final MultipartFile file) throws JobException {
        final Path tempFile;
        try {
            tempFile = TempFileUtils.saveFile(file);
        } catch (IOException e) {
            throw new JobException("Failed to save file", e);
        }

        final Process process = new Process(file.getOriginalFilename());
        process.setTempFilePath(tempFile.toAbsolutePath().toString());

        this.processStore.addProcess(process);
        return process;
    }

    @Override
    public Process retrieve(final String processId) throws JobException {
        final Process process = this.processStore.getProcess(processId);
        // clone process to make sure it's thread-safe
        Process clonedProcess = new Process();
        synchronized (process) {
            try {
                BeanUtils.copyProperties(process, clonedProcess);
            } catch (BeansException e) {
                log.error("Failed to copy Process object", e);
            }
        }
        return clonedProcess;
    }

    private void process(final Process process) throws JobException {
        log.debug("Submit document '{}' to queue", process.getFileName());

        try {
            processProducer.add(process);
        } catch (InterruptedException e) {
            log.error("Cannot add process to queue", e);
        }
    }

}
