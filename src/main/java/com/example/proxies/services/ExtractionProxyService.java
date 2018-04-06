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
import com.example.proxies.model.SubmitResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Asynchronous proxy service to receive and monitor CV extraction submission process
 */
public interface ExtractionProxyService {
    /**
     * Submit a CV file for data extraction process
     * @param file
     * @return A response contains a processId used for monitoring the process
     * @throws JobException
     */
    SubmitResponse submit(MultipartFile file) throws JobException;

    /**
     * Query the status of the submitted process
     * @param processId
     * @return
     */
    Process retrieve(String processId) throws JobException;

    Process onBeforeProcess(final MultipartFile file) throws JobException;
}
