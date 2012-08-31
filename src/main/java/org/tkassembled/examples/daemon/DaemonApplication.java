package org.tkassembled.examples.daemon;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaemonApplication implements Daemon {
	
	private static final Logger logger = LoggerFactory.getLogger(DaemonApplication.class);
	
	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	public void init(DaemonContext context) throws DaemonInitException,
			Exception {
		logger.debug("Daemon initialized with arguments {}.", context.getArguments());
	}

	public void start() throws Exception {
		logger.info("Starting up daemon.");
		
		this.executorService.execute(new Runnable() {
			
			CountDownLatch latch = new CountDownLatch(1);
			
			public void run() {
				try {
					latch.await();
				} catch (InterruptedException e) {
					logger.debug("Thread interrupted, probably means we're shutting down now.");
				}
			}
		});
	}

	public void stop() throws Exception {
		logger.info("Stopping daemon.");
		
		this.executorService.shutdown();
	}

	public void destroy() {
		logger.info("Destroying daemon.");
	}
}