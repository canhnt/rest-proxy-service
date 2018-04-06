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

import org.springframework.core.io.ByteArrayResource;

/**
 * FileMessageResource wraps the byte array and set proper filename. It's used for sending binary file via fomr-data message
 */
public class FileMessageResource extends ByteArrayResource {

    /**
     * The filename to be associated with the 'filename' field in the form-data
     */
    private final String filename;

    /**
     *
     * @param byteArray
     * @param filename The filename to be used in the form-data
     */
    public FileMessageResource(final byte[] byteArray, final String filename) {
        super(byteArray);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return filename;
    }
}