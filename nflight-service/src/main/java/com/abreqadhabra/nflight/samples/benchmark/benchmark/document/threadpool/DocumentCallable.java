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

package com.abreqadhabra.nflight.samples.benchmark.benchmark.document.threadpool;

import java.util.concurrent.Callable;

import com.abreqadhabra.nflight.samples.benchmark.document.Document;
import com.abreqadhabra.nflight.samples.benchmark.document.usertags.UserTagsBundle;
import com.abreqadhabra.nflight.samples.benchmark.document.usertags.UserTagsCalculator;


/**
 * Callable for the parallel com.abreqadhabra.nflight.samples.benchmark.document scenario. It knows its assigned section of the shared
 * documents array and computes the user tag sets for this section when being executed.
 * 
 * @author patrick.peschlow
 */
public class DocumentCallable implements Callable<UserTagsBundle> {

    private Document[] documents;

    private int lo;

    private int hi;

    public DocumentCallable(Document[] documents, int lo, int hi) {
	this.documents = documents;
	this.lo = lo;
	this.hi = hi;
    }

    @Override
    public UserTagsBundle call() throws Exception {
	return UserTagsCalculator.compute(documents, lo, hi);
    }

}
