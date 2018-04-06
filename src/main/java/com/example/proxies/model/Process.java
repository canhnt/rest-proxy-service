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

package com.example.proxies.model;

import com.example.proxies.services.ExtractionProxyService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

/**
 * Represent a process used by the {@link ExtractionProxyService}
 */
public class Process {

    private String tempFilePath;

    public void setTempFilePath(final String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    @JsonIgnore
    public String getTempFilePath() {
        return tempFilePath;
    }

    public enum State {
        PENDING,
        PROGRESS,
        COMPLETED,
        ERROR
    }

    private String id;

    private State state;
    private String fileName;
    private String result;

    public Process() {
    }

    public Process(final String fileName) {
        this(fileName, State.PENDING);
    }

    public Process(final String fileName, final State state) {
        this.id =  UUID.randomUUID().toString();
        this.fileName = fileName;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String filename) {
        this.fileName = filename;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
