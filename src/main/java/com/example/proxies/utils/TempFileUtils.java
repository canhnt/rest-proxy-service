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

package com.example.proxies.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TempFileUtils {
    private static final Logger log = LoggerFactory.getLogger(TempFileUtils.class);

    private TempFileUtils() {}

    public static void deleteTempFileIfExists(final String filePath) {
        if (StringUtils.isNotBlank(filePath)) {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                log.warn("Cannot delete temp file '{}'", filePath, e);
            }
        }
    }

    /**
     * Save to a temporary file
     * @param file
     * @return temporary file path
     * @throws IOException
     */
    public static Path saveFile(final MultipartFile file) throws IOException {
        final File tmpFile = File.createTempFile("tkextract", ".tmp");
        FileUtils.copyInputStreamToFile(file.getInputStream(), tmpFile);
        return tmpFile.toPath();
    }
}
