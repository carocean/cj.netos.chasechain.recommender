package cj.netos.chasechain.recommender;
//该表存用户个人空间
public class RecommendedItem {

    public final static transient String _COL_NAME = "chasechain.recommended.items";
    String item;//由于内容物的标识在全局在整个追链系统中是唯一的（以指代实际物的标识映射而来），因此不必关心在哪个池中，只要用户拉取过这个就排除
    long ctime;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }
}
