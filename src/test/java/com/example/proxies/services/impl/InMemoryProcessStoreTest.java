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
import com.example.proxies.services.ProcessStore;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class InMemoryProcessStoreTest {

    private ProcessStore store;
    @Before
    public void setUp() {
        store = new InMemoryProcessStore();
    }

    @Test
    public void can_add_and_retrieve_process() throws JobException {
        final String id = store.addProcess(new Process("file-123"));
        final Process actualProcess = store.getProcess(id);
        assertEquals("file-123", actualProcess.getFileName());
        assertEquals(Process.State.PENDING, actualProcess.getState());
        assertEquals(id, actualProcess.getId());
    }

    @Test
    public void has_exception_when_process_not_found() {
        final String id = store.addProcess(new Process("file-123"));
        try {
            store.getProcess("foo-id");
        } catch (JobException e) {
            assertEquals("Process 'foo-id' not found", e.getMessage());
            return;
        }
        fail("Expect a JobException but nothing");
    }
}