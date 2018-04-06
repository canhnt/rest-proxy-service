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

package com.example.proxies.security.login;

import com.example.proxies.model.SubmitResponse;
import com.example.proxies.security.authentication.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Collection;

@RestController
public class LoginResource {
    static final Logger log = LoggerFactory.getLogger(LoginResource.class);

    private static final Duration DEFAULT_TOKEN_DURATION = Duration.ofMinutes(10);

    @Autowired
    private TokenService tokenService;

    @GetMapping(value = "/accesstoken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubmitResponse> accessToken() {
        final User user = getCurrentUser();
        log.debug("Generate new token for user '{}'", user.getUsername());

        final String token = tokenService.createToken(DEFAULT_TOKEN_DURATION, user);
        return new ResponseEntity(new TokenResponse(user.getUsername(), token), HttpStatus.OK);
    }

    private User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String loggedInUser = authentication.getName();
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        log.debug("Logged in user '{}' with authorities '{}'", loggedInUser, authorities);
        return new User(loggedInUser, "", authorities);
    }

}
