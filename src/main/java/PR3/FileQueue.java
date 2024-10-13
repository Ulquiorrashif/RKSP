package PR3;

import PR1.File;
import io.reactivex.Observable;

public class FileQueue {
    private final int capacity;
    private final Observable<File> fileObservable;

    public FileQueue(int capacity) {
        this.capacity = capacity;
        this.fileObservable = new GeneratorObserv().generateFile();

    }
    // Получает наблюдаемый поток файлов
    public Observable<File> getFileObservable() {
        return fileObservable;
    }

}
