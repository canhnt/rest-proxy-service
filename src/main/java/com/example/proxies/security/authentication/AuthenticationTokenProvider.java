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

package com.example.proxies.security.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * This provider validates token stored in the security context to create authenticated user with authority
 * 'ROLE_TKEXTRACTION'.
 */
@Component
public class AuthenticationTokenProvider implements AuthenticationProvider{
    private static final Logger log = LoggerFactory.getLogger(AuthenticationTokenProvider.class);

    @Autowired
    private TokenService tokenService;

    @Override
    public Authentication authenticate(final Authentication authn) throws AuthenticationException {
        if (!(authn instanceof AuthenticationToken)) {
            throw new AuthenticationServiceException("Expect a AuthenticationToken, but has " + authn.getClass().getCanonicalName());
        }
        final AuthenticationToken token = (AuthenticationToken)authn;
        if (!validate(token)) {
            log.debug("Invalid token {}", token);
            return null;
        }

        final User user = getUserInfo(token);
        // convert from validated token to authenticated user
        log.debug("Granted an access from user '{}'", user.getUsername());
        return new AuthenticatedUser(user);
    }

    private boolean validate(final AuthenticationToken authnToken) {
        log.debug("Verifying token '{}'", authnToken.getCredentials());
        final String tokenValue = (String) authnToken.getCredentials();
        return tokenService.validate(tokenValue);
    }

    private User getUserInfo(final AuthenticationToken token) {
        final String tokenValue = (String)token.getCredentials();
        return tokenService.getUser(tokenValue);
    }

    @Override
    public boolean supports(final Class<?> authnCls) {
        return AuthenticationToken.class.isAssignableFrom(authnCls);
    }
}
