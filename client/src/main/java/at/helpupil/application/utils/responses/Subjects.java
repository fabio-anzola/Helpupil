package at.helpupil.application.utils.responses;

/**
 * Response object of all subjects in database
 */
public class Subjects {
    /**
     * array of all subjects
     */
    private Subject[] results;
    /**
     * current page
     */
    private Integer page;
    /**
     * number of subjects per page
     */
    private Integer limit;
    /**
     * number of pages
     */
    private Integer totalPages;
    /**
     * number of subjects in database
     */
    private Integer totalResults;

    /**
     * @param results all subjects in database
     * @param page current page
     * @param limit number of subjects per page
     * @param totalPages number of pages
     * @param totalResults number subjects in database
     */
    public Subjects(Subject[] results, Integer page, Integer limit, Integer totalPages, Integer totalResults) {
        this.results = results;
        this.page = page;
        this.limit = limit;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    /**
     * @return array of subjects
     */
    public Subject[] getResults() {
        return results;
    }

    /**
     * @param results array of subjects
     */
    public void setResults(Subject[] results) {
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
     * @return number of subjects per page
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @param limit number of subjects per page
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
     * @return number of subjects
     */
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults number of subjects
     */
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
