package com.eurodyn.qlack.common.search;

import java.io.Serializable;

/**
 * This class is a paging utility.
 *
 * @author European Dynamics SA
 */
public class PagingParams implements Serializable {

    private int pageSize;
    private int currentPage;
    /**
     * Default page size.
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * Default Constructor.
     */
    public PagingParams() {
        pageSize = DEFAULT_PAGE_SIZE;
        currentPage = 1;
    }

    /**
     * Parameterized Constructor.
     */
    public PagingParams(int pageSize, int currentPage) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    /**
     *
     */
    public PagingParams(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

}
