package PR3;

import PR1.File;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class HandlerObserv {
    private final String supportedFileType;
    // Создает обработчик файлов для определенного типа файлов
    public HandlerObserv(String supportedFileType) {
        this.supportedFileType = supportedFileType;
    }
    // Обрабатывает файлы асинхронно с задержкой
    public Completable processFiles(Observable<File> fileObservable) {
        return fileObservable
                .filter(file -> file.getType().equals(supportedFileType))
 .flatMapCompletable(file -> {
            long processingTime = file.getSize() * 7;
            return Completable
                    .fromAction(() -> {
                        Thread.sleep(processingTime);
                        System.out.println("Обработка " +
                                supportedFileType + " размер  " + file.getSize());
                    })
                    .subscribeOn(Schedulers.io())
 .observeOn(Schedulers.io());
        })
                .onErrorComplete();
    }

}
