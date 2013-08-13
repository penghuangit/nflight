package com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.threadpool;

import com.abreqadhabra.nflight.samples.benchmark.actor.Message;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.ActorBenchmarkConfig;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.parallel.AbstractDispatcher;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.parallel.Mailbox;


/**
 * A runnable for com.abreqadhabra.nflight.samples.benchmark.actor scheduling. When executed, the com.abreqadhabra.nflight.samples.benchmark.actor consumes a certain maximum amount of
 * messages from its mailbox. After the execution session the com.abreqadhabra.nflight.samples.benchmark.actor is unscheduled. If there are
 * further messages in its mailbox, the com.abreqadhabra.nflight.samples.benchmark.actor is scheduled for execution again.
 * 
 * @author patrick.peschlow
 */
public class ActorRunnable implements Runnable {

    private AbstractDispatcher dispatcher;

    private Mailbox mailbox;

    public ActorRunnable(AbstractDispatcher dispatcher, Mailbox mailbox) {
	this.dispatcher = dispatcher;
	this.mailbox = mailbox;
    }

    @Override
    public void run() {
	consumeMessages();
	mailbox.setScheduled(false);
	if (!mailbox.isEmpty()) {
	    dispatcher.scheduleUnlessAlreadyScheduled(mailbox.getActor().getId());
	}
    }

    private void consumeMessages() {
	int counter = 0;
	Message message;
	while (counter++ < ActorBenchmarkConfig.MAX_CONSUME_BURST
		&& (message = mailbox.pollMessage()) != null) {
	    mailbox.getActor().receive(message);
	}
    }
}
