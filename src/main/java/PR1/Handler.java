package PR1;

import java.util.concurrent.BlockingQueue;

public class Handler implements Runnable {
    private BlockingQueue<File> queue;
    private String fileTypeForHandl;

    public Handler(BlockingQueue<File> queue, String fileTypeForHandl) {
        this.queue = queue;
        this.fileTypeForHandl = fileTypeForHandl;
    }

    @Override
    public void run() {
        while (true) {
            File currentFile = null;
            try {
                currentFile = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (currentFile.getType().equals(fileTypeForHandl)){
                try {
                    System.out.println("Начало обработки файла типа "+currentFile.getType());
                    Thread.sleep(currentFile.getSize()* 7);
                    System.out.println("Файл обработан за  "+currentFile.getSize());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
