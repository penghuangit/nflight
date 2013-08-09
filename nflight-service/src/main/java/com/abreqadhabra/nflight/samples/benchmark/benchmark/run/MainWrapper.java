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

package com.abreqadhabra.nflight.samples.benchmark.benchmark.run;

import com.abreqadhabra.nflight.samples.benchmark.benchmark.actor.ActorBenchmarkMain;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.document.DocumentBenchmarkMain;


/**
 * Main class that runs the specified com.abreqadhabra.nflight.samples.benchmark.benchmark.
 * 
 * @author patrick.peschlow
 */
public class MainWrapper {

    /**
     * Main method. Expects as first argument the com.abreqadhabra.nflight.samples.benchmark.benchmark type to run. The second argument
     * specifies an optional config properties file to use for the com.abreqadhabra.nflight.samples.benchmark.benchmark.
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
//	if (args.length == 0) {
//	    System.out.println("Please specify the com.abreqadhabra.nflight.samples.benchmark.benchmark type to run! "
//		    + availableBenchmarkInfo());
//	    return;
//	}

	//String type = args[0];
    	String type = "document";
	String configFile = args.length > 1 ? args[1] : null;
	switch (type) {
	case "document":
	    DocumentBenchmarkMain.go(configFile);
	    break;
	case "actor":
	    ActorBenchmarkMain.go(configFile);
	    break;
	default:
	    System.out.println("Invalid benchmark type! " + availableBenchmarkInfo());
	}
    }

    private static String availableBenchmarkInfo() {
	return "Possible types are: com.abreqadhabra.nflight.samples.benchmark.document, com.abreqadhabra.nflight.samples.benchmark.actor";
    }
}
