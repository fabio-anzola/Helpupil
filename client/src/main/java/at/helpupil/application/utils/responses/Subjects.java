package at.helpupil.application.utils.responses;

public class Subjects {
    private Subject[] results;
    private Integer page;
    private Integer limit;
    private Integer totalPages;
    private Integer totalResults;

    public Subjects(Subject[] results, Integer page, Integer limit, Integer totalPages, Integer totalResults) {
        this.results = results;
        this.page = page;
        this.limit = limit;
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

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
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
