import java.io.*;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author jabkevich
 */

public class KVStore {
    private static final ConcurrentSkipListMap<String, String> memTable = new ConcurrentSkipListMap<>();
    private static final int MAX_MEMTABLE_SIZE = 3;

    public static synchronized void put(String key, String value) {
        memTable.put(key, value);

        if (memTable.size() >= MAX_MEMTABLE_SIZE) {
            flushToSSTable();
        }
    }

    public static synchronized String get(String key) {
        if (memTable.containsKey(key)) {
            return memTable.get(key);
        }

        return readFromSSTables(key);
    }

    public static synchronized void delete(String key) {
        memTable.put(key, "TOMBSTONE");

        if (memTable.size() >= MAX_MEMTABLE_SIZE) {
            flushToSSTable();
        }
    }

    private static synchronized void flushToSSTable() {
        String filename = "sstable_" + new Date().getTime() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (var entry : memTable.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
            System.out.println("MemTable сброшена в " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Очищаем MemTable
        memTable.clear();
    }

    private static String readFromSSTables(String key) {

        return null;
    }
}
