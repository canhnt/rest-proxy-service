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

import java.math.BigInteger;
import java.security.SecureRandom;

public final class TokenGenerator {
    public static final int NUM_BITS = 130;
    private SecureRandom random = new SecureRandom();

    public String generateRandomToken() {
        return new BigInteger(NUM_BITS, random).toString(32);
    }
}
