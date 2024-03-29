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

package com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.forkjoin;

import java.util.concurrent.ForkJoinPool;

import com.abreqadhabra.nflight.samples.benchmark.actor.ActorContext;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.ActorBenchmarkConfig;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.ActorBenchmarkDef;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.ActorGenerator;


/**
 * Benchmark definition for the parallel com.abreqadhabra.nflight.samples.benchmark.actor scenario using a {@link ForkJoinPool}. When the
 * com.abreqadhabra.nflight.samples.benchmark.benchmark is started, the initial messages are scheduled. The com.abreqadhabra.nflight.samples.benchmark.benchmark ends when the context
 * indicates that it is finished.
 * 
 * Note that the pool does not need to be shut down when the com.abreqadhabra.nflight.samples.benchmark.benchmark has finished because all pool
 * threads are internally started as daemon threads.
 * 
 * @author patrick.peschlow
 */
public class ForkJoinActorBenchmark extends ActorBenchmarkDef {

    private ForkJoinPool forkJoinPool;

    private ForkJoinDispatcher dispatcher;

    public ForkJoinActorBenchmark(ActorGenerator generator, int numThreads) {
	super(generator);
	this.forkJoinPool = new ForkJoinPool(numThreads,
		ForkJoinPool.defaultForkJoinWorkerThreadFactory, null,
		ActorBenchmarkConfig.FORKJOIN_ASYNC_MODE);
    }

    @Override
    public Object execute() {
	dispatcher.scheduleInitially();
	context.waitForFinished();
	return null;
    }

    @Override
    protected void initContext() {
	dispatcher = new ForkJoinDispatcher(actors, forkJoinPool);
	context = new ActorContext(dispatcher, actors, initialMessages);
    }
}
