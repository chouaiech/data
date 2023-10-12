/*
 * Copyright (c) 2023. European Commission
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package io.piveau.hub;

public final class Defaults {

    private Defaults() {
    }

    public static final String GREETING = "You know, for metadata!";

    public static final int SERVICE_PORT = 8080;

    public static final String APIKEY = "apiKey";

    public static final boolean VALIDATOR_ENABLED = false;

    public static final boolean SEARCH_SERVICE_ENABLED = false;

    public static final boolean LOAD_VOCABULARIES = true;
    public static final boolean LOAD_VOCABULARIES_FETCH = false;

    public static final boolean METRICS_HISTORY = false;

    public static final boolean XML_DECLARATION = true;

    public static final boolean TRANSLATION_SERVICE_ENABLED = false;

    public static final String METRICS_PIPE = "metrics";

    public static final boolean FORCE_UPDATES = false;

    public static final boolean EXPERIMENTS = false;

}
