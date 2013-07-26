package com.abreqadhabra.nflight.samples.concurrent.benchmark.actor.threadpool;

import java.util.concurrent.ExecutorService;

import com.abreqadhabra.nflight.samples.concurrent.actor.Actor;
import com.abreqadhabra.nflight.samples.concurrent.benchmark.actor.parallel.AbstractDispatcher;

/**
 * Dispatcher for the threadpool actor benchmark. The dispatcher maintains an internal
 * {@link ExecutorService} and submits a new {@link ActorRunnable} when a scheduling request for an
 * actor is made.
 * 
 * @author patrick.peschlow
 */
public class ThreadpoolDispatcher extends AbstractDispatcher {

    protected final ExecutorService pool;

    public ThreadpoolDispatcher(Actor[] actors, ExecutorService threadPool) {
	super(actors);
	this.pool = threadPool;
    }

    @Override
    protected void schedule(int actorId) {
	pool.submit(new ActorRunnable(this, mailboxes[actorId]));
    }
}
