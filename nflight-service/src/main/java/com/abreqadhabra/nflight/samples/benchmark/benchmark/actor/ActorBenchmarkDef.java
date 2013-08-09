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

package com.abreqadhabra.nflight.samples.benchmark.benchmark.actor;

import java.util.List;
import java.util.Map;

import com.abreqadhabra.nflight.samples.benchmark.actor.Actor;
import com.abreqadhabra.nflight.samples.benchmark.actor.ActorContext;
import com.abreqadhabra.nflight.samples.benchmark.actor.Message;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.BenchmarkDef;


/**
 * Defines certain aspects of the com.abreqadhabra.nflight.samples.benchmark.actor com.abreqadhabra.nflight.samples.benchmark.benchmark. Generates all actors and initial messages before
 * each run of the com.abreqadhabra.nflight.samples.benchmark.benchmark, and makes sure that the context is initialized.
 * 
 * @author patrick.peschlow
 */
public abstract class ActorBenchmarkDef extends BenchmarkDef {

    private final ActorGenerator generator;

    protected Actor[] actors;

    protected ActorContext context;

    protected Map<Integer, List<Message>> initialMessages;

    public ActorBenchmarkDef(ActorGenerator generator) {
	this.generator = generator;
    }

    @Override
    public void before() {
	actors = generator.generateActors();
	initialMessages = generator.generateInitialMessages();
	initContext();
    }

    @Override
    public void afterAll() {
	if (ActorBenchmarkConfig.DEBUG) {
	    System.out.println("Total com.abreqadhabra.nflight.samples.benchmark.actor message receptions: " + Actor.COUNT_ALL.get());
	}
    }

    protected abstract void initContext();
}
