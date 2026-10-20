//за каждые 5 купленных шоколадок 1 дают бесплатно
//при этом на 3 фантика от шоколажок дают бесплатно 1 шоколадку
//при налиции заданных ресурсов и цены на 1 шоколадку определить сколько можно купить их
//свои цифры использовать

import java.util.concurrent.Semaphore;

class ChocolateExchange {
    private int totalShokolad;
    private int totalFantik;

    private final Semaphore semaphore = new Semaphore(1);

    public ChocolateExchange(int initialShokolad, int initialFantik) {
        this.totalShokolad = initialShokolad;
        this.totalFantik = initialFantik;
    }


    public void processExchange(String threadName) {//метод
        try {//чтобы не было ошибки изолирует потоки от исключений которые могут возникнуть в других потоках

            semaphore.acquire();

            int bonusFromShok = totalShokolad / 5;
            totalShokolad += bonusFromShok;
            totalFantik += totalShokolad;

            System.out.println(threadName + totalShokolad + totalFantik);

            while (totalFantik >= 3) {
                int newShok = totalFantik / 3;//сколько получим шоколадок
                totalShokolad += newShok;
                totalFantik = (totalFantik % 3) + newShok;
                System.out.println(threadName  + newShok +  totalFantik);
            }

            System.out.println(totalShokolad + totalFantik);

        } catch (Exception e) {//исключение
            System.out.println("ошибка");
        }
    }
}

class ChokoThread extends Thread {
    private final ChocolateExchange exchange;//обмен в переводе

    public ChokoThread(String name, ChocolateExchange exchange) {
        super(name);
        this.exchange = exchange;
    }

    public void run() {
        exchange.processExchange(getName());
    }
}

public class Main {
    public static void main(String[] args) throws Exception {//обработка испключенрия в теории может выйти ошибка//throws выбрасывает исчключение
        ChocolateExchange exchange = new ChocolateExchange(7, 1);//сами ввели

        ChokoThread t1 = new ChokoThread("Поток-1", exchange);
        ChokoThread t2 = new ChokoThread("Поток-2", exchange);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

    }
}