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

import com.example.proxies.exceptions.JobException;
import com.example.proxies.model.Process;

public interface ProcessStore {
    /**
     * Add the new process to the store
     * @param process
     * @return The processId
     */
    String addProcess(Process process);

    Process getProcess(final String processId) throws JobException;
}
