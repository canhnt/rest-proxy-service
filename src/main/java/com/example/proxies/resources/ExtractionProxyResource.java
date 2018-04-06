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

import com.example.proxies.model.SubmitResponse;
import com.example.proxies.services.ExtractionProxyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ExtractionProxyResource extends GenericResource {
    @Autowired
    private ExtractionProxyService extractionProxyService;

    @RequestMapping(value = "/submit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubmitResponse> submit(@RequestParam("file") MultipartFile file) {
        final RestAction submit = () -> new ResponseEntity(extractionProxyService.submit(file), HttpStatus.OK);

        return handleAction(submit);
    }

    @GetMapping(value = "/retrieve/{processId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubmitResponse> retrieve(@PathVariable("processId") String processId) {
        final RestAction retrieveProcess = () -> new ResponseEntity(extractionProxyService.retrieve(processId), HttpStatus.OK);

        return handleAction(retrieveProcess);
    }
}
