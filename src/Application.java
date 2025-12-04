import java.util.List;
import java.util.Scanner;

public class Application {
    public static void appLication() {
        System.out.println("Для выхода введите 'exit'");
        System.out.println("=========================================");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("\nВведите поисковый запрос: ");
            String query = scanner.nextLine();

            // Проверка на выход
            if (query.equalsIgnoreCase("exit")) {
                System.out.println("Завершение работы...");
                break;
            }

            // Проверка пустого запроса
            if (query.trim().isEmpty()) {
                System.out.println("Запрос не может быть пустым!");
                continue;
            }

            try {
                // Выполняем поиск
                WikipediaSearcher.SearchResponse response = WikipediaSearcher.search(query);

                // Проверяем результаты
                if (!response.hasResults()) {
                    System.out.println("По запросу '" + query + "' ничего не найдено.");
                    continue;
                }

                // Выводим результаты
                displaySearchResults(query, response);

                // Предлагаем выбрать статью
                handleArticleSelection(response);

            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
                System.out.println("Проверьте подключение к интернету.");
            }
        }

        scanner.close();
    }

    private static void displaySearchResults(String query, WikipediaSearcher.SearchResponse response) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Запрос: " + query);
        System.out.println("Всего найдено: " + response.getTotalHits());
        System.out.println("Топ-" + response.getResults().size() + " результатов:");
        System.out.println("=".repeat(50));

        int index = 1;
        for (SearchResult result : response.getResults()) {
            System.out.println(index + ". " + result);
            System.out.println();
            index++;
        }
    }

    private static void handleArticleSelection(WikipediaSearcher.SearchResponse response) {
        Scanner scanner = new Scanner(System.in);
        List<SearchResult> results = response.getResults();

        System.out.print("Введите номер статьи для открытия (1-" + results.size() +
                ") или 0 для нового поиска: ");

        try {
            int choice = scanner.nextInt();

            if (choice > 0 && choice <= results.size()) {
                SearchResult selected = results.get(choice - 1);
                String articleUrl = "https://ru.wikipedia.org/w/index.php?curid=" +
                        selected.getPageid();

                System.out.println("\nОткрываю статью: " + selected.getTitle());
                BrowserOpener.openInBrowser(articleUrl);
            } else if (choice != 0) {
                System.out.println("Некорректный номер.");
            }
        } catch (Exception e) {
            System.out.println("Пожалуйста, введите число.");
            scanner.nextLine(); // Очищаем буфер
        }
    }
}