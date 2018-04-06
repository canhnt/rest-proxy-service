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

package com.example.proxies.resources;
import com.example.proxies.exceptions.JobException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.Callable;

public abstract class GenericResource {
    static final Logger log = LoggerFactory.getLogger(GenericResource.class);

    @FunctionalInterface
    interface RestAction extends Callable<ResponseEntity> {
        ResponseEntity call() throws JobException;
    }

    protected ResponseEntity handleAction(final RestAction action) {
        HttpStatus httpStatus;

        try {
            return action.call();
        } catch (JobException e) {
            log.error(e.getMessage());

            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return createErrorResponse(httpStatus);
    }

    private static ResponseEntity createErrorResponse(final HttpStatus httpStatus) {
        return new ResponseEntity(httpStatus);
    }
}
