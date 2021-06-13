package at.helpupil.application.utils.responses;

/**
 * Response object of documents
 */
public class Documents {
    /**
     * array of all approved documents
     */
    private Document[] results;
    /**
     * current page
     */
    private Integer page;
    /**
     * number of documents per page
     */
    private Integer limit;
    /**
     * number of pages
     */
    private Integer totalPages;
    /**
     * number of results
     */
    private Integer totalResults;

    /**
     * @param results all approved documents in database
     * @param page current page
     * @param limit number of documents per page
     * @param totalPages number of pages
     * @param totalResults number of results
     */
    public Documents(Document[] results, Integer page, Integer limit, Integer totalPages, Integer totalResults) {
        this.results = results;
        this.page = page;
        this.limit = limit;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    /**
     * @return all approved documents
     */
    public Document[] getResults() {
        return results;
    }

    /**
     * @param results approved documents
     */
    public void setResults(Document[] results) {
        this.results = results;
    }

    /**
     * @return current page number
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page number
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return limit of documents per page
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @param limit of documents per page
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * @return number of pages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages number of pages
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * @return number of approved documents
     */
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults number of approved documents
     */
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
