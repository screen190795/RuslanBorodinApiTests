package api.data;

import java.util.List;

public class Resource {
  private Integer page;
  private Integer per_page;
  private Integer total;
  private Integer total_pages;
  private List<ResourceData> data;
  private Ad ad;



    public Resource(){
      super();
  }

    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPer_page() {
        return per_page;
    }

    public void setPer_page(Integer per_page) {
        this.per_page = per_page;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }


    public List<ResourceData> getData() {
        return data;
    }
    public void setData(List<ResourceData> data) {
        this.data = data;
    }

    public Ad getAd() {
        return ad;
    }
    public void setAd(Ad ad) {
        this.ad = ad;
    }
}
