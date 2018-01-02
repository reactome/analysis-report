package org.reactome.server.tools.analysis.exporter.playground.util;

import java.nio.file.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class FileWatcher {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(new Demo());
        thread.setName("fileWatcher");
        thread.start();
//        thread.run();
    }


}

class Demo implements Runnable {
    @Override
    public void run() {
        try {
//            final Path path = Paths.get("src/main/java/resources/123.txt");
            final Path path = FileSystems.getDefault().getPath("src/main/resources/");
            System.out.println(path);
            final WatchService watchService = path.getFileSystem().newWatchService();
            final WatchKey watchKey = watchService.poll(1, TimeUnit.SECONDS);

            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            System.out.println("Report any file changed within next 1 minutes...");
            while (true){
                if (watchKey != null) {
                    watchKey.pollEvents().stream().forEach(event ->
                            System.out.println(event.context()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
