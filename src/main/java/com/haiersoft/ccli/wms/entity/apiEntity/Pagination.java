package com.haiersoft.ccli.wms.entity.apiEntity;

//@ApiModel(value = "BwlListPage")
//@NoArgsConstructor
//@Data
public class Pagination implements java.io.Serializable{
    private String curPage;

    private String pageSize;

    private String totalPages;

    private String totalRecords;

    public String getCurPage() {
        return curPage;
    }

    public void setCurPage(String curPage) {
        this.curPage = curPage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }
}