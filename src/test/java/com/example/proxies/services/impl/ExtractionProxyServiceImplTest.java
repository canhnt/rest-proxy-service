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
import com.example.proxies.services.ConsumerScheduler;
import com.example.proxies.services.ProcessProducer;
import com.example.proxies.services.ProcessStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExtractionProxyServiceImplTest {
    @InjectMocks
    private ExtractionProxyServiceImpl proxyService;

    @Mock
    private ConsumerScheduler mockConsumerScheduler;

    @Mock
    private ProcessStore mockProcessStore;

    @Mock
    private ProcessProducer mockProcessProducer;

    @Before
    public void setUp() throws JobException {
        doNothing().when(mockConsumerScheduler).start();
        doReturn("1234").when(mockProcessStore).addProcess(any());
    }

    @Test(expected=JobException.class)
    public void should_handle_empty_file() throws Exception {
        final byte[] content = null;
        final MultipartFile file = new MockMultipartFile("test-file", content);
        proxyService.submit(file);

        Mockito.verify(mockConsumerScheduler);
    }

    @Test
    public void should_process_non_empty_file() throws Exception {
        final MultipartFile file = new MockMultipartFile("test-file", "content".getBytes());
        final SubmitResponse resp = proxyService.submit(file);

        assertNotNull(resp.getProcessId());
        Mockito.verify(mockProcessProducer, times(1)).add(any());
    }

    @Test
    public void query_process() throws Exception {
        final Process mockProcess = new Process("file-123");
        mockProcess.setId("1234");
        when(mockProcessStore.getProcess("1234")).thenReturn(mockProcess);

        final Process actual = proxyService.retrieve("1234");
        assertEquals(mockProcess.getId(), actual.getId());
        assertEquals(mockProcess.getFileName(), actual.getFileName());
    }

}