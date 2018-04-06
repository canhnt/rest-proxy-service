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

package com.example.proxies.security;

import com.example.proxies.security.authentication.AuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * If the current context is not authenticated, this filter extracts the 'access_token' query param
 * and store it in the security context.
 */
public class AccessTokenQueryParamFilter extends GenericFilterBean {
    private static final Logger log = LoggerFactory.getLogger(AccessTokenQueryParamFilter.class);

    public static final String ACCESS_TOKEN_PARAM = "access_token";

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain filterChain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            final HttpServletRequest httpRequest = (HttpServletRequest) request;

            final String tokenValue = httpRequest.getParameter(ACCESS_TOKEN_PARAM);
            if (StringUtils.isNotEmpty(tokenValue)) {
                log.debug("Obtaining token '{}'. Add to security context.", tokenValue);
                SecurityContextHolder.getContext().setAuthentication(new AuthenticationToken(tokenValue));
            }

            filterChain.doFilter(request, response);
        }
    }
}