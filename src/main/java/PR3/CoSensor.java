package PR3;

import io.reactivex.Observable;
import io.reactivex.Observer;

import java.util.Random;

public class CoSensor extends Observable {
    @Override
    protected void subscribeActual(Observer observer) {
        new Thread(() -> {
            while (true) {
                int co2 = new Random().nextInt(30, 101);
                observer.onNext(co2); // Отправляем значение CO2 подписчикам
                try {
                    Thread.sleep(1000); // Пауза 1 секунда
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start(); // Запускаем поток для симуляции работы датчика
    }
}
