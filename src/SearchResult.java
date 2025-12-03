
public class SearchResult {
    private String title;
    private int pageid;
    private String snippet;

    public SearchResult(String title, int pageid, String snippet) {
        this.title = title;
        this.pageid = pageid;
        this.snippet = snippet;
    }

    // Геттеры
    public String getTitle() { return title; }
    public int getPageid() { return pageid; }
    public String getSnippet() { return snippet; }

    public String getCleanSnippet() {
        return snippet.replaceAll("<[^>]*>", "");
    }

    @Override
    public String toString() {
        return String.format("• %s\n  %s", title, getCleanSnippet());
    }


}
