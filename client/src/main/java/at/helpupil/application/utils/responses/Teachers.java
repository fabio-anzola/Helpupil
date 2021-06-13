package at.helpupil.application.utils.responses;

/**
 * Response object of all teachers in database
 */
public class Teachers {
    /**
     * array of all teachers in database
     */
    private Teacher[] results;
    /**
     * current page
     */
    private Integer page;
    /**
     * number of teachers per page
     */
    private Integer limit;
    /**
     * number of pages
     */
    private Integer totalPages;
    /**
     * number of teachers
     */
    private Integer totalResults;

    /**
     * @param results array of teachers
     * @param page current page
     * @param limit number of teachers per page
     * @param totalPages number of pages
     * @param totalResults number of teachers
     */
    public Teachers(Teacher[] results, Integer page, Integer limit, Integer totalPages, Integer totalResults) {
        this.results = results;
        this.page = page;
        this.limit = limit;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    /**
     * @return array of all teachers
     */
    public Teacher[] getResults() {
        return results;
    }

    /**
     * @param results array of all teachers
     */
    public void setResults(Teacher[] results) {
        this.results = results;
    }

    /**
     * @return current page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page current page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return number of teachers per page
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @param limit number of teachers per page
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
     * @return number of teachers
     */
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults number of teachers
     */
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
