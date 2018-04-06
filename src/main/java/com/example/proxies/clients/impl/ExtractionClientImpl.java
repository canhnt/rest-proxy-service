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

import com.example.proxies.clients.ExtractionClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

@Component
public class ExtractionClientImpl implements ExtractionClient {
    private static final Logger log = LoggerFactory.getLogger(ExtractionClientImpl.class);

    // URL of TK extraction service, use Json response and Http status compliance
    // TODO make tihs url configurable
    private static final String EXTRACTION_URL = "https://backend.example.com/sourcebox/extract.do";

    private String account = "box_test";
    private String userName = "canh";
    private String password = "passw0rd";

    @Override
    public String extract(final String filePath, final String originalFileName) throws IOException {
        log.debug("Call TK extraction service for '{}'", filePath);

        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders headers = prepareHeaders();
        final MultiValueMap<String, Object> body = prepareBody(filePath, originalFileName);

        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        final ResponseEntity<String> responseEntity = restTemplate.postForEntity(getRequestUrl(),
                requestEntity, String.class);

        if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK &&
                StringUtils.isNotBlank(responseEntity.getBody())) {
            log.debug("TK extraction for '{}' has completed", filePath);
            log.debug(responseEntity.getBody());
            return responseEntity.getBody();
        }

        log.debug("Response: {}", responseEntity);
        throw new IOException("Failed to call TK extraction service.");
    }

    private MultiValueMap<String, Object> prepareBody(final String filePath, final String originalFileName) throws IOException {
        final MultiValueMap<String, Object> multiParts = new LinkedMultiValueMap<>();

        final HttpHeaders textHeader = new HttpHeaders();
        textHeader.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> accountEntity = new HttpEntity<>(this.account, textHeader);
        HttpEntity<String> usernameEntity = new HttpEntity<>(this.userName, textHeader);
        HttpEntity<String> passwordEntity = new HttpEntity<>(this.password, textHeader);

        // BUG?: if "filename" field is empty, TK extraction will return 500!
        final ByteArrayResource uploadedFileData = new FileMessageResource(FileUtils.readFileToByteArray(new File(filePath)), originalFileName);

        final HttpHeaders appOctetHeader = new HttpHeaders();
        appOctetHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        HttpEntity<ByteArrayResource> uploadedFileEntity = new HttpEntity<>(uploadedFileData, appOctetHeader);

        multiParts.add("account", accountEntity);
        multiParts.add("username", usernameEntity);
        multiParts.add("password", passwordEntity);
        multiParts.add("uploaded_file", uploadedFileEntity);

        return multiParts;
    }

    private HttpHeaders prepareHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Arrays.asList(MediaType.ALL));
        return headers;
    }

    public String getRequestUrl() {
        return EXTRACTION_URL;
    }
}
