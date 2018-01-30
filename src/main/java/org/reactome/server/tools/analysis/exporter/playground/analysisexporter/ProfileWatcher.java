package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;

/**
 * open a new thread and use {@see WatchService} to oversee the modification of config file.
 * once it was changed, invoke {@see ReportRenderer#loadPdfProfile} reload config file.
 */
class ProfileWatcher implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileWatcher.class);
    private String profilePath;
    private String profileName;
    private Thread thread;

    public ProfileWatcher(String threadName, String profilePath, String profileName) {
        thread = new Thread(this::run, threadName);
        this.profilePath = profilePath;
        this.profileName = profileName;
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        try {
            final Path path = Paths.get(profilePath);
            final WatchService watchService = path.getFileSystem().newWatchService();

            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            while (true) {
                WatchKey watchKey = watchService.take();
                if (watchKey != null) {
                    watchKey.pollEvents().stream().filter(event -> ((Path) event.context()).endsWith(profileName)).forEach(event ->
                    {
                        LOGGER.info("Reload {}", profilePath + profileName);
                        ReportRenderer.loadPdfProfile(profilePath + profileName);
                    });
                }
                watchKey.reset();
            }
        } catch (Exception e) {
            LOGGER.error("Fail to reload profile!");
        }
    }
}
