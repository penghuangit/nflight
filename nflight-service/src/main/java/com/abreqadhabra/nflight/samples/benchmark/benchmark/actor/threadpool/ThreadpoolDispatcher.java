package com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.threadpool;

import java.util.concurrent.ExecutorService;

import com.abreqadhabra.nflight.samples.benchmark.actor.Actor;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.parallel.AbstractDispatcher;


/**
 * Dispatcher for the threadpool com.abreqadhabra.nflight.samples.benchmark.actor com.abreqadhabra.nflight.samples.benchmark.benchmark. The dispatcher maintains an internal
 * {@link ExecutorService} and submits a new {@link ActorRunnable} when a scheduling requestByteBuffer for an
 * com.abreqadhabra.nflight.samples.benchmark.actor is made.
 * 
 * @author patrick.peschlow
 */
public class ThreadpoolDispatcher extends AbstractDispatcher {

    protected ExecutorService pool;

    public ThreadpoolDispatcher(Actor[] actors, ExecutorService threadPool) {
	super(actors);
	this.pool = threadPool;
    }

    @Override
    protected void schedule(int actorId) {
	pool.submit(new ActorRunnable(this, mailboxes[actorId]));
    }
}
