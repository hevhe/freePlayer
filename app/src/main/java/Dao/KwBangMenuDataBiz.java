package Dao;

import java.util.List;

public class KwBangMenuDataBiz {
    String name;
    List<KwBangMenuDataDetailBiz> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KwBangMenuDataDetailBiz> getList() {
        return list;
    }

    public void setList(List<KwBangMenuDataDetailBiz> list) {
        this.list = list;
    }
}
