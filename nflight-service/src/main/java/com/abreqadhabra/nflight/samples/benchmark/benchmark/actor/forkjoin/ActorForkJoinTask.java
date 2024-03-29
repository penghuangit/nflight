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

import java.util.concurrent.RecursiveAction;

import com.abreqadhabra.nflight.samples.benchmark.actor.Message;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.ActorBenchmarkConfig;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.parallel.AbstractDispatcher;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.parallel.Mailbox;


/**
 * A fork/join task for com.abreqadhabra.nflight.samples.benchmark.actor scheduling. When executed, the com.abreqadhabra.nflight.samples.benchmark.actor consumes a certain maximum amount
 * of messages from its mailbox. After the execution session the com.abreqadhabra.nflight.samples.benchmark.actor is unscheduled. If there are
 * further messages in its mailbox, the com.abreqadhabra.nflight.samples.benchmark.actor is scheduled for execution again.
 * 
 * @author patrick.peschlow
 */
public class ActorForkJoinTask extends RecursiveAction {

    private static long serialVersionUID = 1L;

    private AbstractDispatcher dispatcher;

    private Mailbox mailbox;

    public ActorForkJoinTask(AbstractDispatcher dispatcher, Mailbox mailbox) {
	this.dispatcher = dispatcher;
	this.mailbox = mailbox;
    }

    @Override
    protected void compute() {
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
