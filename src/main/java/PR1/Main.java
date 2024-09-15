package PR1;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        Pr1();
//        Pr2();
        Pr3();

    }

    static void Pr3(){
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(5);
        Thread generator = new Thread(new Generator(queue));
        Thread handlerXML = new Thread(new Handler(queue,"XML"));
        Thread handlerJSON = new Thread(new Handler(queue,"JSON"));
        Thread handlerXLS = new Thread(new Handler(queue,"XLS"));

        generator.start();
        handlerXML.start();
        handlerJSON.start();
        handlerXLS.start();
    }


    static void Pr2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Input");
            System.out.println("getActiveCount "+threadPoolExecutor.getActiveCount());
//            System.out.println("getActiveCount "+threadPoolExecutor.getPoolSize());

            String userInput = scanner.nextLine();
            // Проверяем, если пользователь хочет выйти
            if ("exit".equalsIgnoreCase(userInput)) {
                break;
            }
            // Преобразуем введенное значение в число
            int number = Integer.parseInt(userInput);


            Future<Integer> future = executorService.submit(() ->{
                try {
                    int delayInSeconds = ThreadLocalRandom.current().nextInt(1, 6);
                    Thread.sleep(1000*delayInSeconds);
//                    System.out.println(number*number);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return number*number;});
//            if (threadPoolExecutor.getActiveCount()==3){
//                int result = future.get();
//                System.out.println("Результат: " + result);
//            }
            int result = future.get();
            System.out.println("Результат: " + result);
        }
    }
    static void Pr1() throws ExecutionException, InterruptedException {
        int [ ] mas = new int [10000];
        initMass(mas);

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();


        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        Date dateStart = new Date();
        System.out.println(summMass(mas));
        Date dateEnd = new Date();
        System.out.println(dateEnd.getTime()-dateStart.getTime());

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory used: " + (memoryAfter - memoryBefore) + " bytes");


//        МЕТОД 2 Многопоточность


        runtime.gc();
        long memoryBefore1 = runtime.totalMemory() - runtime.freeMemory();

        Date dateStart1 = new Date();
        System.out.println(summMassWithThread(mas));
        Date dateEnd1 = new Date();
        System.out.println(dateEnd1.getTime()-dateStart1.getTime());

        long memoryAfter1 = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory used: " + (memoryAfter1 - memoryBefore1) + " bytes");


        runtime.gc();

        long memoryBefore2 = runtime.totalMemory() - runtime.freeMemory();
        Date dateStart2 = new Date();
        System.out.println(summMassWithFork(mas));
        Date dateEnd2 = new Date();
        System.out.println(dateEnd2.getTime()-dateStart2.getTime());


        long memoryAfter2 = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory used: " + (memoryAfter2 - memoryBefore2) + " bytes");
    }

    static void initMass(int [] mas){
        for (int i = 0; i < 10000; i++) {
            mas[i]=i;
        }
    }
    static int  summMass(int [] mas){

        return Arrays.stream(mas).reduce(0,(a,b)->{
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return a+b; });
    }
    static  int summMassWithThread(int [] mas) throws InterruptedException, ExecutionException {
        List list=Arrays.stream(mas).boxed().collect(Collectors.toList());;
        int countThread = Runtime.getRuntime().availableProcessors();
        // Создаем пул потоков для выполнения задач
        ExecutorService executorService = Executors.newFixedThreadPool(countThread);
        // Создаем список задач для каждого подсписка
        List<Callable<Integer>> tasks = new ArrayList<>();
        int stepSubMass = 10000 / countThread;
        for (int i = 0; i < countThread; i++) {
            final int startIndex = i * stepSubMass;
            final int endIndex = (i == countThread - 1) ? 10000 : (i +
                    1) * stepSubMass;
            tasks.add(() -> summMass ( list.subList(startIndex, endIndex).stream().mapToInt((item)-> (int) item).toArray()));
        }

        List<Future<Integer>> futures = executorService.invokeAll(tasks);
        // Инициализируем переменную для хранения максимального значения
        int max = futures.stream().map(integerFuture -> {
            try {
                return integerFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).reduce(0,(a, b)->a+b);

        executorService.shutdown();
        // Возвращаем максимальное значение из всех подсписков
        return max;
    }

    static  int summMassWithFork(int [] mas)  {
        List list=Arrays.stream(mas).boxed().collect(Collectors.toList());;
        // Создаем пул потоков ForkJoin
        ForkJoinPool forkJoinPool = new ForkJoinPool();
//        // Создаем корневую задачу MaxFinderTask для всего списка
        MaxFinderTask task = new MaxFinderTask(list, 0, list.size());
//        // Выполняем корневую задачу и получаем результат
        return forkJoinPool.invoke(task);
    }
    static class MaxFinderTask extends RecursiveTask<Integer> {
        private List<Integer> list;
        private int start;
        private int end;
        // Конструктор MaxFinderTask для создания задачи для подсписка
        MaxFinderTask(List<Integer> list, int start, int end) {
            this.list = list;
            this.start = start;
            this.end = end;
        }
        // Метод compute(), выполняющий вычисления для задачи
        @Override
        protected Integer compute() {
            // Если в подсписке только один элемент, вернем его
            if (end - start <= 1000) {
                return summMass(list.subList(start, end).stream().mapToInt((item)-> (int) item).toArray());
            }
            // Найдем середину подсписка
            int middle = start + (end - start) / 2;
            // Создаем две подзадачи для левой и правой половин подсписка
            MaxFinderTask leftTask = new MaxFinderTask(list, start, middle);
            MaxFinderTask rightTask = new MaxFinderTask(list, middle, end);
            // Запускаем подзадачу для правой половины параллельно
            leftTask.fork();
            // Вычисляем максимальные значения в левой и правой половинах
//            подсписка
            rightTask.fork();
            int rightResult = rightTask.join();
            int leftResult = leftTask.join();

            // Возвращаем максимальное значение из левой и правой половин
            return(leftResult + rightResult);
        }
    }

    }
