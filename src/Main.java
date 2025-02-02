import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jabkevich
 */

public class Main {
    private static final int PORT = 8080;
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("KV Store запущен на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Подключился новый клиент: " + clientSocket.getInetAddress());
                pool.execute(new ClientHandler(clientSocket));
            }
        }
    }
}


class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("Добро пожаловать в KV Store! Введите команду:");

            String command;
            while ((command = in.readLine()) != null) {
                System.out.println("Получена команда: " + command);
                String response = processCommand(command);
                out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processCommand(String command) {
        String[] parts = command.split(" ", 3);
        if (parts.length < 2) {
            return "Ошибка: неверный формат команды!";
        }

        String action = parts[0].toUpperCase();
        String key = parts[1];

        switch (action) {
            case "PUT":
                if (parts.length < 3) return "Ошибка: нужно указать значение!";
                String value = parts[2];
                KVStore.put(key, value);
                return "PUT: " + key + " => " + value;
            case "GET":
                String result = KVStore.get(key);
                return result != null ? result : "NULL";
            case "DELETE":
                KVStore.delete(key);
                return "OK";
            default:
                return "Неизвестная команда!";
        }
    }
}


