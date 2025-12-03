
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WikipediaSearcher {

    public static SearchResponse search(String query) throws Exception {
        // Кодируем запрос
        String encodedQuery = URLEncoder.encode(query, "UTF-8");

        // Формируем URL запроса
        String url = String.format(
                "https://ru.wikipedia.org/w/api.php?" +
                        "action=query&list=search&utf8=&format=json&srlimit=10&srsearch=%s",
                encodedQuery
        );

        // Отправляем запрос
        String jsonResponse = HttpRequestUtil.sendGetRequest(url);

        // Парсим ответ
        return parseSearchResults(jsonResponse);
    }

    private static SearchResponse parseSearchResults(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonObject queryObject = jsonObject.getAsJsonObject("query");

        // Общее количество результатов
        int totalHits = queryObject.getAsJsonObject("searchinfo")
                .get("totalhits").getAsInt();

        // Массив результатов
        JsonArray searchArray = queryObject.getAsJsonArray("search");
        List<SearchResult> results = new ArrayList<>();

        for (int i = 0; i < searchArray.size(); i++) {
            JsonObject result = searchArray.get(i).getAsJsonObject();
            String title = result.get("title").getAsString();
            int pageid = result.get("pageid").getAsInt();
            String snippet = result.get("snippet").getAsString();

            results.add(new SearchResult(title, pageid, snippet));
        }

        return new SearchResponse(results, totalHits);
    }

    // Вложенный класс для ответа поиска
    public static class SearchResponse {
        private List<SearchResult> results;
        private int totalHits;

        public SearchResponse(List<SearchResult> results, int totalHits) {
            this.results = results;
            this.totalHits = totalHits;
        }

        public List<SearchResult> getResults() { return results; }
        public int getTotalHits() { return totalHits; }
        public boolean hasResults() { return !results.isEmpty(); }
    }
}
