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

package com.abreqadhabra.nflight.samples.benchmark.benchmark.document;

import com.abreqadhabra.nflight.samples.benchmark.benchmark.BenchmarkDef;
import com.abreqadhabra.nflight.samples.benchmark.document.Document;
import com.abreqadhabra.nflight.samples.benchmark.document.DocumentGenerator;


/**
 * Base class for the com.abreqadhabra.nflight.samples.benchmark.document com.abreqadhabra.nflight.samples.benchmark.benchmark. Generates all documents before the first run of the
 * com.abreqadhabra.nflight.samples.benchmark.benchmark starts.
 * 
 * @author patrick.peschlow
 */
public abstract class DocumentBenchmarkDef extends BenchmarkDef {

    protected Document[] documents;

    private DocumentGenerator documentGenerator;

    public DocumentBenchmarkDef(DocumentGenerator documentGenerator) {
	this.documentGenerator = documentGenerator;
    }

    @Override
    public void beforeAll() {
	documents = documentGenerator.generateDocuments();
    }
}
