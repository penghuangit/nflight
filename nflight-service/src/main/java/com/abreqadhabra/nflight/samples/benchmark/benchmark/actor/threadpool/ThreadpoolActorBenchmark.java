package com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.abreqadhabra.nflight.samples.benchmark.actor.ActorContext;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.ActorBenchmarkDef;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.ActorGenerator;


/**
 * Benchmark definition for the parallel com.abreqadhabra.nflight.samples.benchmark.actor scenario using a {@link ThreadPoolExecutor}. When the
 * com.abreqadhabra.nflight.samples.benchmark.benchmark is started, the initial messages are scheduled. The com.abreqadhabra.nflight.samples.benchmark.benchmark ends when the context
 * indicates that it is finished.
 * 
 * When all com.abreqadhabra.nflight.samples.benchmark.benchmark runs have completed, the threadpool is shut down. This is required because the
 * internal threads are not started as daemon threads.
 * 
 * @author patrick.peschlow
 */
public class ThreadpoolActorBenchmark extends ActorBenchmarkDef {

    private ExecutorService threadPool;

    private ThreadpoolDispatcher dispatcher;

    public ThreadpoolActorBenchmark(ActorGenerator generator, int numThreads) {
	super(generator);
	this.threadPool = Executors.newFixedThreadPool(numThreads);
    }

    @Override
    public Object execute() {
	dispatcher.scheduleInitially();
	context.waitForFinished();
	return null;
    }

    @Override
    public void afterAll() {
	threadPool.shutdown();
    }

    @Override
    protected void initContext() {
	dispatcher = new ThreadpoolDispatcher(actors, threadPool);
	context = new ActorContext(dispatcher, actors, initialMessages);
    }
}
