/*
 * Copyright 2012 Patrick Peschlow
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
 */

package com.abreqadhabra.nflight.samples.benchmark.benchmark;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for com.abreqadhabra.nflight.samples.benchmark.benchmark definitions. A com.abreqadhabra.nflight.samples.benchmark.benchmark definition needs to define the actual
 * contents of the com.abreqadhabra.nflight.samples.benchmark.benchmark by implementing the {@link #execute()} method. Also, by overriding the
 * respective methods, the com.abreqadhabra.nflight.samples.benchmark.benchmark definition may choose to specify code that is executed
 * before/after the whole com.abreqadhabra.nflight.samples.benchmark.benchmark, or before/after each individual run.
 * 
 * The com.abreqadhabra.nflight.samples.benchmark.benchmark definition also acceptor as a collector for the results and execution times of the
 * individual runs of the com.abreqadhabra.nflight.samples.benchmark.benchmark.
 * 
 * @author patrick.peschlow
 */
public abstract class BenchmarkDef {

    protected List<Object> results = new ArrayList<>();

    protected List<Long> times = new ArrayList<>();

    public abstract Object execute();

    public void addResult(long durationMillis, Object result) {
	times.add(durationMillis);
	results.add(result);
    }

    public void beforeAll() {
	// Do nothing.
    }

    public void before() {
	// Do nothing.
    }

    public void after() {
	// Do nothing.
    }

    public void afterAll() {
	// Do nothing.
    }

    public List<Object> getResults() {
	return results;
    }

    public List<Long> getTimes() {
	return times;
    }
}
