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

import com.example.proxies.services.ConsumerScheduler;
import com.example.proxies.services.ProcessQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class ContextClosedHandler implements ApplicationListener<ContextClosedEvent> {
    private static final Logger log = LoggerFactory.getLogger(ContextClosedHandler.class);

    @Autowired
    private ConsumerScheduler consumerScheduler;

    @Autowired
    private ProcessQueue processQueue;

    @Override
    public void onApplicationEvent(final ContextClosedEvent contextClosedEvent) {
        consumerScheduler.shutdown();
        if (!processQueue.isEmpty()) {
            log.warn("Queue is not empty. There are '{}' tasks left", processQueue.size());
        }
    }
}
