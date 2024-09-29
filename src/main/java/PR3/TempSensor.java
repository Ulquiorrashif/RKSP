package PR3;

import io.reactivex.Observable;
import io.reactivex.Observer;

import java.util.Random;

public class TempSensor extends Observable {
    @Override
    protected void subscribeActual(Observer observer) {
        new Thread(() -> {
            while (true) {
                int temperature = new Random().nextInt(15, 31);
                observer.onNext(temperature);
                try {
                    Thread.sleep(1000); // Пауза 1 секунда
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start(); // Запускаем поток для симуляции работы датчика
    }
}
