import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                synchronized (sizeToFreq) {
                    String str = generateRoute("RLRFR", 100);
                    int count = str.length() - str.replace(String.valueOf('R'), "").length();
                    System.out.println(str + " - " + count);
                    if (sizeToFreq.containsKey(count)) {
                        sizeToFreq.computeIfPresent(count, (k, v) -> ++v);
                    } else {
                        sizeToFreq.put(count, 1);
                    }
                }
            }).start();
        }
        while (true) { //не поняла как по-другому дождаться когда все потоки закончат работу
            synchronized (sizeToFreq) {
                if (sizeToFreq.values().stream().reduce(0, (a, b) -> a + b) == 1000) {
                    Optional<Integer> max = sizeToFreq.keySet().stream().max(Comparator.comparingInt(sizeToFreq::get));
                    System.out.println("Самое частое количество повторений " + max.get() +
                            " (встретилось " + sizeToFreq.get(max.get()) + " раз)\n" +
                            "Другие размеры:");
                    sizeToFreq.remove(max.get());
                    sizeToFreq.keySet()
                            .forEach(k -> System.out.println("- " + k + " (" + sizeToFreq.get(k) + " раз)"));
                    break;
                }
            }
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

}
