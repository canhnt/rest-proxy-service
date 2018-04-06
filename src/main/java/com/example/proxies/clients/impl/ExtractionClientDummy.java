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

package com.example.proxies.clients.impl;

import java.io.IOException;

import com.example.proxies.clients.ExtractionClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtractionClientDummy implements ExtractionClient {
    private static final Logger log = LoggerFactory.getLogger(ExtractionClientDummy.class);

    @Override
    public String extract(final String filePath, final String originalFileName) throws IOException {
        log.debug("Call TK extraction service for '{}'", filePath);

        try {
            Thread.sleep(10000);                 //10s
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        log.debug("TK extraction for '{}' has completed", filePath);
        return "dummy result";
    }
}
