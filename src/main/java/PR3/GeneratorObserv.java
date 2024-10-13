package PR3;

import PR1.File;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorObserv {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final List<String> fileTypes = List.of("XML", "JSON", "XLS");
    private final Random random = new Random();
    // Генерирует файлы асинхронно с задержкой
    public Observable<File> generateFile() {
        return Observable
                .fromCallable(() -> {
                    try {
                        int randomFileSize = random.nextInt(91) + 10;
                        File file = new File(this.fileTypes.get(random.nextInt(3)), randomFileSize);
                        Thread.sleep(random.nextInt(901) + 100);
                        return file;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .repeat() // Повторяем бесконечно
                .subscribeOn(Schedulers.io()) // Выполняется в фоновом потоке
                .observeOn(Schedulers.io());
    }
}
