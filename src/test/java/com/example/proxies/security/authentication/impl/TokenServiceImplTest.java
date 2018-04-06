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

package com.example.proxies.security.authentication.impl;

import com.example.proxies.security.authentication.TokenService;
import org.junit.Test;
import org.springframework.security.core.userdetails.User;

import java.time.Duration;
import java.util.Collections;

import static org.junit.Assert.*;

public class TokenServiceImplTest {
    private TokenService tokenService = new TokenServiceImpl();

    @Test
    public void can_create_and_validate_token() throws Exception {
        final User mockUser = new User("foo", "bar", Collections.emptyList());

        final String token = tokenService.createToken(Duration.ofSeconds(1), mockUser);
        assertTrue(tokenService.validate(token));
        assertEquals(mockUser, tokenService.getUser(token));

        Thread.sleep(1000);

        assertFalse(tokenService.validate(token));
        assertNull(tokenService.getUser(token));
    }

}