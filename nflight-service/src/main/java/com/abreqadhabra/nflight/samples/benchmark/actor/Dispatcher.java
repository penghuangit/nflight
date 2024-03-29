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

package com.abreqadhabra.nflight.samples.benchmark.actor;

/**
 * Dispatcher for com.abreqadhabra.nflight.samples.benchmark.actor scenarios. Dispatches messages to the receiving com.abreqadhabra.nflight.samples.benchmark.actor's mailboxes.
 * 
 * @author patrick.peschlow
 */
public interface Dispatcher {

    void dispatch(int receiverId, Message message);

    void addInitialMessage(int receiverId, Message message);
}
