package PR2;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
//        Zadanie 1
//        readWithNio();




        createFile();
        Files.createFile(Path.of("src/main/java/PR2/des.txt"));
        long startTime1 = System.currentTimeMillis();
        copyFileByStream();
        long endTime1 = System.currentTimeMillis();
        printTimeAndMemoryUsage("FileInputStream/FileOutputStream", startTime1,
                endTime1);
        Files.delete(Path.of("src/main/java/PR2/des.txt"));

        Files.createFile(Path.of("src/main/java/PR2/des.txt"));
        long startTime2 = System.currentTimeMillis();
        copyFileByChannel();
        long endTime2 = System.currentTimeMillis();
        printTimeAndMemoryUsage("Chanel", startTime2,
                endTime2);
        Files.delete(Path.of("src/main/java/PR2/des.txt"));


        long startTime3 = System.currentTimeMillis();
        copyFileByFile();
        long endTime3 = System.currentTimeMillis();
        printTimeAndMemoryUsage("File", startTime3,
                endTime3);



        long startTime4 = System.currentTimeMillis();
        copyFileByApacheCommonsIO();
        long endTime4 = System.currentTimeMillis();
        printTimeAndMemoryUsage("Chanel", startTime4,
                endTime4);

        Files.delete(Path.of("src/main/java/PR2/des.txt"));
    }
    private static void printTimeAndMemoryUsage(String method, long startTime,
                                                long endTime)
    {
        long elapsedTime = endTime - startTime;
        System.out.println("Метод " + method + ":");
        System.out.println("Время выполнения: " + elapsedTime + " мс");
        Runtime runtime = Runtime.getRuntime();
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Использование памяти: " + memoryUsed / (1024 * 1024)
                + " МБ");
        System.out.println();
    }

    static void readWithNio() throws IOException {
        System.out.println("Вывод файла ");
        Path path = Paths.get("src/main/java/PR2/file.txt");
        List<String> list = Files.readAllLines(path);
        for (String line:
             list) {
            System.out.println(line);
        }


    }
    static void createFile() throws IOException {
        byte[] data = new byte[1024 * 1024]; // 1 МБ буфер
        FileOutputStream fos = new FileOutputStream("src/main/java/PR2/file100Mb.txt");
        for (int i = 0; i < 100; i++) {
            fos.write(data);
        }
        fos.close();
    }
    static void copyFileByStream(){
        try(
            InputStream inputStream = new FileInputStream("src/main/java/PR2/file100Mb.txt");
            OutputStream outputStream = new FileOutputStream("src/main/java/PR2/des.txt")){
            byte[] buffer = new byte[1024];
            int length;
// Читаем данные в байтовый массив, а затем выводим в OutStream
            while((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
    } catch (IOException e) {
            e.printStackTrace();
    }
    }


    static void copyFileByChannel() throws IOException {
        FileChannel srcFileChannel = new FileInputStream("src/main/java/PR2/file100Mb.txt").getChannel();
        FileChannel dstFileChannel	=	new	FileOutputStream("src/main/java/PR2/des.txt").getChannel();
        srcFileChannel.transferTo(srcFileChannel.position(),srcFileChannel.size(), dstFileChannel);

        srcFileChannel.close();
        dstFileChannel.close();
    }

    static void copyFileByFile() throws IOException {
        Files.copy(Path.of("src/main/java/PR2/file100Mb.txt"), Path.of("src/main/java/PR2/des.txt"));
    }
    private static void copyFileByApacheCommonsIO() throws IOException {
        File sourceFile = new File("src/main/java/PR2/file100Mb.txt");
        File destFile = new File("src/main/java/PR2/des.txt");
        FileUtils.copyFile(sourceFile, destFile);
    }

}

