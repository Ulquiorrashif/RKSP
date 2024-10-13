package PR3;

import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static PR3.UserFriend.getFriends;

public class Main {
    public static void main(String[] args) {
//       task1();
//        task2();
//        task3();
        task4();
    }

    public static void task1(){
        TempSensor tempSensor = new TempSensor();
        CoSensor coSensor = new CoSensor();
        Alarm alarm = new Alarm();
        tempSensor.subscribe(alarm);
        coSensor.subscribe(alarm);
    }
    public static void task2(){
        Observable
                .fromCallable(() -> {
                    int count = new Random().nextInt(1001);
                    List<Integer> numbers = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        numbers.add(new Random().nextInt(1001));
                    }
                    return numbers;
                }).flatMapIterable(numbers -> numbers).count().subscribe(count -> {
                    System.out.println("Количество случайных чисел: " + count);
                }); // Считаем количество чисел

//        Observable observable = Observable.merge(Observable
//                .fromCallable(() -> {
//                    int count = new Random().nextInt(1001);
//                    List<Integer> numbers = new ArrayList<>();
//                    for (int i = 1; i < 10; i+=2) {
//                        numbers.add(i);
//                        Thread.sleep(1000);
//                    }
//                    return numbers;
//                }).flatMapIterable(numbers -> numbers),Observable
//                .fromCallable(() -> {
//                    int count = new Random().nextInt(1001);
//                    List<Integer> numbers = new ArrayList<>();
//                    for (int i = 0; i < 10; i+=2) {
//                        numbers.add(i);
//                    }
//                    return numbers;
//                }).flatMapIterable(numbers -> numbers));

        Observable<Integer> zippedStream = Observable.zip(Observable
                .fromCallable(() -> {
                    int count = new Random().nextInt(1001);
                    List<Integer> numbers = new ArrayList<>();
                    for (int i = 1; i < 10; i+=2) {
                        numbers.add(i);
                        Thread.sleep(1);
                    }
                    return numbers;
                }).flatMapIterable(numbers -> numbers), Observable
                .fromCallable(() -> {
                    int count = new Random().nextInt(1001);
                    List<Integer> numbers = new ArrayList<>();
                    for (int i = 0; i < 10; i+=2) {
                        numbers.add(i);
                    }
                    return numbers;
                }).flatMapIterable(numbers -> numbers), (first, second) -> {
            List<Integer> result = new ArrayList<>();
            result.add(first);
            result.add(second);
            return result;
        }).flatMapIterable(numbers -> numbers);

        // Подписываемся и выводим элементы
        zippedStream.subscribe(
                digit -> System.out.println("Received: " + digit),
                throwable -> System.err.println("Error: " + throwable),
                () -> System.out.println("Completed")
        );
        Observable
                .fromCallable(() -> {
                    int count = new Random().nextInt(1001);
                    List<Integer> numbers = new ArrayList<>();
                    for (int i = 1; i < 10; i+=2) {
                        numbers.add(i);
                        Thread.sleep(1);
                    }
                    return numbers;
                }).flatMapIterable(numbers -> numbers).lastElement().subscribe(digit -> System.out.println("Received: " + digit));
    }
    public static void task3(){
        // Создаем массив случайных userId
        Integer[] userIdArray = {1, 2, 3, 4, 5};
        // Создаем поток userId из массива
        Observable<Integer> userIdStream = Observable.fromArray(userIdArray);
        // Преобразуем поток userId в поток объектов UserFriend
        Observable<UserFriend> userFriendStream = userIdStream
                .flatMap(userId -> getFriends(userId));
        // Подписываемся на поток и выводим результат
        userFriendStream.subscribe(userFriend -> {
            System.out.println("User: " + userFriend.getUserId() + ", Friend: "
                    + userFriend.getFriendId());
        });

    }

    public static void task4(){
        int queueCapacity = 5;
        FileQueue fileQueue = new FileQueue(queueCapacity);
        String[] supportedFileTypes = {"XML", "JSON", "XLS"};
        for (String fileType : supportedFileTypes) {
            new HandlerObserv(fileType)
                    .processFiles(fileQueue.getFileObservable())
                    .subscribe(
                            () -> {}, // Обработка успешного завершения
                            throwable -> System.err.println("Error processing file: " + throwable)
 );
    }
    // Даем системе время для работы (можно изменить)
 try {
        Thread.sleep(10000); // Пусть система работает 10 секунд
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

}
