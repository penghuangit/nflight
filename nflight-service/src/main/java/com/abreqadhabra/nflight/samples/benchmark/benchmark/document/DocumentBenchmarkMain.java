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

import com.abreqadhabra.nflight.samples.benchmark.benchmark.Benchmark;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.BenchmarkConfig;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.BenchmarkDef;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.document.forkjoin.ForkJoinDocumentBenchmark;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.document.sequential.SequentialDocumentBenchmark;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.document.threadpool.ThreadpoolDocumentBenchmark;
import com.abreqadhabra.nflight.samples.benchmark.benchmark.run.MainWrapper;
import com.abreqadhabra.nflight.samples.benchmark.document.DefaultDocumentGeneratorConfig;
import com.abreqadhabra.nflight.samples.benchmark.document.DocumentGenerator;
import com.abreqadhabra.nflight.samples.benchmark.util.Statistics;


/**
 * Main class for the com.abreqadhabra.nflight.samples.benchmark.document com.abreqadhabra.nflight.samples.benchmark.benchmark. Defines and initializes the com.abreqadhabra.nflight.samples.benchmark.benchmark, runs the com.abreqadhabra.nflight.samples.benchmark.benchmark
 * using the configured strategy (sequential or one of the parallel variants) and finally prints
 * result statistics.
 * 
 * @author patrick.peschlow
 */
public class DocumentBenchmarkMain {

    /**
     * Helper main method to directly run this com.abreqadhabra.nflight.samples.benchmark.benchmark without additional configuration. For real
     * tests, the recommend way is running the com.abreqadhabra.nflight.samples.benchmark.benchmark via the {@link MainWrapper} class.
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
	go();
    }

    public static void go(String configFile) {
	if (configFile != null) {
	    DocumentConfigFileReader.evaluateConfigFile(configFile);
	}
	go();
    }

    public static void go() {
	BenchmarkDef benchmarkDef = defineBenchmark();

	Benchmark benchmark = initBenchmark();

	runBenchmark(benchmarkDef, benchmark);

	printStatistics(benchmarkDef);
    }

    private static BenchmarkDef defineBenchmark() {
	DocumentGenerator documentGenerator = new DocumentGenerator(
		new DefaultDocumentGeneratorConfig());

	switch (DocumentBenchmarkConfig.STRATEGY) {
	case SEQUENTIAL:
	    return new SequentialDocumentBenchmark(documentGenerator);
	case FORKJOIN:
	    return new ForkJoinDocumentBenchmark(documentGenerator,
		    DocumentBenchmarkConfig.NUM_THREADS);
	case THREADPOOL:
	    return new ThreadpoolDocumentBenchmark(documentGenerator,
		    DocumentBenchmarkConfig.NUM_THREADS);
	default:
	    throw new IllegalStateException("Unknown com.abreqadhabra.nflight.samples.benchmark.benchmark strategy");
	}
    }

    private static Benchmark initBenchmark() {
	BenchmarkConfig benchmarkConfig = new BenchmarkConfig(
		DocumentBenchmarkConfig.NUM_WARMUP_RUNS, DocumentBenchmarkConfig.NUM_TIMED_RUNS);
	return new Benchmark(benchmarkConfig);
    }

    private static void runBenchmark(BenchmarkDef benchmarkDef, Benchmark benchmark) {
	System.out.println("Starting the com.abreqadhabra.nflight.samples.benchmark.benchmark");

	benchmark.execute(benchmarkDef);
    }

    private static void printStatistics(BenchmarkDef benchmarkDef) {
	double mean = Statistics.computeSampleMean(benchmarkDef.getTimes());
	double stdDev = Statistics.computeStdDev(benchmarkDef.getTimes(), mean);

	System.out.println("Benchmark took an average time of " + mean + " ms (standard deviation "
		+ stdDev + ")");
    }
}