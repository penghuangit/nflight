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

package com.abreqadhabra.nflight.samples.benchmark.benchmark.document.forkjoin;

import com.abreqadhabra.nflight.samples.benchmark.document.usertags.UserTagsBundle;
import com.abreqadhabra.nflight.samples.benchmark.document.usertags.UserTagsCalculator;
import com.abreqadhabra.nflight.samples.benchmark.mapreduce.Output;


/**
 * Output entity for a com.abreqadhabra.nflight.samples.benchmark.document fork/join task. The entity carries a {@link UserTagsBundle} which
 * contains tag sets for users. When this entity is to be reduced with another entity, it calls the
 * {@link UserTagsCalculator#reduce(UserTagsBundle, UserTagsBundle)} method which contains the
 * business logic. The result of the reduce operation is stored in this entity.
 * 
 * @author patrick.peschlow
 */
public class DocumentOutput implements Output<UserTagsBundle> {

    private UserTagsBundle userTagBundle;

    public DocumentOutput(UserTagsBundle userTagBundle) {
	this.userTagBundle = userTagBundle;
    }

    @Override
    public UserTagsBundle getResult() {
	return userTagBundle;
    }

    @Override
    public Output<UserTagsBundle> reduce(Output<UserTagsBundle> other) {
	UserTagsCalculator.reduce(userTagBundle, other.getResult());
	return this;
    }
}
