package dbm.photo.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableScheduling
public class PhotoScannerConfiguration {
    private static final Logger log = LoggerFactory.getLogger(PhotoScannerConfiguration.class);

    @Value("${photoscanner.thread.directory.corePoolSize:4}")
    private int directoryCorePoolSize;

    @Value("${photoscanner.thread.directory.maxPoolSize:8}")
    private int directoryMaxPoolSize;

    @Value("${photoscanner.thread.directory.queueCapacity:1000000}")
    private int directoryQueueCapacity;

    @Value("${photoscanner.thread.file.corePoolSize:16}")
    private int fileCorePoolSize;

    @Value("${photoscanner.thread.file.maxPoolSize:32}")
    private int fileMaxPoolSize;

    @Value("${photoscanner.thread.file.queueCapacity:1000000}")
    private int fileQueueCapacity;

    @Bean
    public ThreadPoolTaskExecutor directoryScannerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        log.info("directoryScannerExecutor - using thread options:\n corePoolSize: {}\n maxPoolSize: {}\nqueueCapacity: {}\n", directoryCorePoolSize, directoryMaxPoolSize, directoryQueueCapacity);
        executor.setCorePoolSize(directoryCorePoolSize);
        executor.setMaxPoolSize(directoryMaxPoolSize);
        executor.setQueueCapacity(directoryQueueCapacity);
        executor.setThreadNamePrefix("directoryScannerEx-");
        executor.setRejectedExecutionHandler(new ThreadPoolRejectedExecutionHandler(executor.getThreadNamePrefix(), executor.getCorePoolSize(), executor.getMaxPoolSize(), directoryQueueCapacity));
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor fileProcessorExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        log.info("fileProcessorExecutor - using thread options:\n corePoolSize: {}\n maxPoolSize: {}\nqueueCapacity: {}\n", fileCorePoolSize, fileMaxPoolSize, fileQueueCapacity);
        executor.setCorePoolSize(fileCorePoolSize);
        executor.setMaxPoolSize(fileMaxPoolSize);
        executor.setQueueCapacity(fileQueueCapacity);
        executor.setThreadNamePrefix("fileProcessorEx-");
        executor.setRejectedExecutionHandler(new ThreadPoolRejectedExecutionHandler(executor.getThreadNamePrefix(), executor.getCorePoolSize(), executor.getMaxPoolSize(), fileQueueCapacity));
        executor.initialize();
        return executor;
    }

    static class ThreadPoolRejectedExecutionHandler implements RejectedExecutionHandler {

        private String threadNamePrefix;
        private Integer corePoolSize;
        private Integer maxPoolSize;
        private Integer queueCapacity;

        public ThreadPoolRejectedExecutionHandler(String threadNamePrefix, Integer corePoolSize, Integer maxPoolSize, Integer queueCapacity) {
            this.threadNamePrefix = threadNamePrefix;
            this.corePoolSize = corePoolSize;
            this.maxPoolSize = maxPoolSize;
            this.queueCapacity = queueCapacity;
        }

        @Override
        public void rejectedExecution(Runnable worker, ThreadPoolExecutor executor) {
            try {
                LoggerFactory.getLogger(threadNamePrefix
                        + "[ setCore: " + corePoolSize
                        + " setMax: " + maxPoolSize
                        + " setQueue: " + queueCapacity
                        + "] REJECTED!!! Re-execute in 1 second"
                        + " [ poolSize: " + executor.getPoolSize()
                        + " corePoolSize: " + executor.getCorePoolSize()
                        + " largestPoolSize: " + executor.getLargestPoolSize()
                        + " maximumPoolSize: " + executor.getMaximumPoolSize()
                        + " activeThreads: " + executor.getActiveCount()
                        + " queuedTasks: " + executor.getTaskCount()
                        + " completedTasks: " + executor.getCompletedTaskCount() + " ] ");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            executor.execute(worker);
        }
    }
}
