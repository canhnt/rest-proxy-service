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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.example.proxies.exceptions.JobException;
import com.example.proxies.exceptions.JobException;
import com.example.proxies.model.Process;
import com.example.proxies.services.ProcessStore;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class InMemoryProcessStore implements ProcessStore {
    // Map from processId to the Process object
    private final Map<String, Process> tasks = new HashMap<>();

    @Override
    public String addProcess(final Process process) {
        final String id = process.getId();
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Process must have non-blank id");
        }
        this.tasks.put(id, process);
        return id;
    }

    @Override
    public Process getProcess(final String processId) throws JobException {
        final Process status = this.tasks.get(processId);
        if (status == null) {
            throw new JobException("Process '" + processId + "' not found");
        }

        return status;
    }
}
