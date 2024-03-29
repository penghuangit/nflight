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

package com.abreqadhabra.nflight.samples.benchmark.document;

import java.util.List;
import java.util.Set;

/**
 * Simple data holder class for a com.abreqadhabra.nflight.samples.benchmark.document that contains a set of users and a list of tags
 * associated with the com.abreqadhabra.nflight.samples.benchmark.document.
 * 
 * @author patrick.peschlow
 */
public class Document {

    private Set<String> users;

    private List<String> tags;

    public Document(Set<String> users, List<String> tags) {
	this.users = users;
	this.tags = tags;
    }

    public Set<String> getUsers() {
	return users;
    }

    public List<String> getTags() {
	return tags;
    }

    @Override
    public String toString() {
	return "Document [users=" + users + ", tags=" + tags + "]";
    }
}
