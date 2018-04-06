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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * A trivial in-memory token service that supports token expiration.
 * TODO should clean up expired token periodically
 */
@Component
public class TokenServiceImpl implements TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);

    private Map<String, UserInfo> tokensStore = new HashMap<>();

    private TokenGenerator tokenGenerator = new TokenGenerator();

    private static class UserInfo extends User {
        private ZonedDateTime expiration;

        public UserInfo(User user, ZonedDateTime expiration) {
            super(user.getUsername(), user.getPassword(), user.getAuthorities());
            this.expiration = expiration;
        }

        public ZonedDateTime getExpiration() {
            return expiration;
        }
    }

    @Override
    public String createToken(final Duration duration, final User user) {
        final String token = tokenGenerator.generateRandomToken();

        final ZonedDateTime expirationTime = ZonedDateTime.now(ZoneOffset.UTC).plus(duration);
        tokensStore.put(token, new UserInfo(user, expirationTime));
        return token;
    }

    @Override
    public boolean validate(final String token) {
        if (StringUtils.isEmpty(token)) {
            throw new IllegalArgumentException("token must not be null or empty");
        }

        final UserInfo userInfo = tokensStore.get(token);

        if (userInfo == null) {
            log.debug("Token '{}' not found", token);
            return false;
        }

        final ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        final boolean valid = now.isBefore(userInfo.getExpiration());
        if (!valid) {
            log.debug("Token '{}' has expired", token);
            // remove expired token
            tokensStore.remove(token);
        }
        return valid;
    }

    @Override
    public User getUser(final String tokenValue) {
        return this.tokensStore.get(tokenValue);
    }
}
