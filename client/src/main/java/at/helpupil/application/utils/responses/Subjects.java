package at.helpupil.application.utils.responses;

public class Subjects {
    private Subject[] results;
    private Integer page;
    private Integer lime;
    private Integer totalPages;
    private Integer totalResults;

    public Subjects(Subject[] results, Integer page, Integer lime, Integer totalPages, Integer totalResults) {
        this.results = results;
        this.page = page;
        this.lime = lime;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public Subject[] getResults() {
        return results;
    }

    public void setResults(Subject[] results) {
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLime() {
        return lime;
    }

    public void setLime(Integer lime) {
        this.lime = lime;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
