package Dao;


import java.util.List;

public class KWSongListDataBiz {
   private int total;
   private List<KWSongListDataDetailBiz> data;
   private int rn;
   private int pn;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<KWSongListDataDetailBiz> getData() {
        return data;
    }

    public void setData(List<KWSongListDataDetailBiz> data) {
        this.data = data;
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public int getPn() {
        return pn;
    }

    public void setPn(int pn) {
        this.pn = pn;
    }
}

