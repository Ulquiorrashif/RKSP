package PR1;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Generator implements Runnable {
    private BlockingQueue<File> queue;

    public Generator(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    private final List<String> fileTypes = List.of("XML", "JSON", "XLS");
    private final Random random = new Random();
    @Override
    public void run() {
        while (true) {

            try {
                Thread.sleep(random.nextInt(901) + 100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

                int randomFileSize = random.nextInt(91) + 10;
                File file = new File(this.fileTypes.get(random.nextInt(3)), randomFileSize);
            try {
                queue.put(file); // Добавляем файл в очередь
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
