package product.coding;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by cbjiang on 2018/10/17.
 */
public class CodingModel {

    /**
     * 商品条码
     */
    private StringProperty bar;

    /**
     * 商品编码
     */
    private StringProperty code;

    /**
     * 商品名称
     */
    private StringProperty name;

    /**
     * 规格名称
     */
    private StringProperty spec;

    /**
     * 品牌
     */
    private StringProperty brand;

    /**
     * 分类
     */
    private StringProperty type;

    /**
     * 实际库存
     */
    private SimpleObjectProperty<Integer> reality;

    /**
     * 锁定库存
     */
    private SimpleObjectProperty<Integer> locked;

    /**
     * 可用库存
     */
    private SimpleObjectProperty<Integer> available;

    /**
     * 在途库存
     */
    private SimpleObjectProperty<Integer> way;

    /**
     * 盘点数量
     */
    private SimpleObjectProperty<Integer> taking;

    /**
     * 差异数量
     */
    private SimpleObjectProperty<Integer> diff;


    public CodingModel(String bar, String code, String name, String spec, String brand, String type,
                       Integer reality, Integer locked, Integer available, Integer way, Integer taking, Integer diff){
        this.bar=new SimpleStringProperty(bar);
        this.code=new SimpleStringProperty(code);
        this.name=new SimpleStringProperty(name);
        this.spec=new SimpleStringProperty(spec);
        this.brand=new SimpleStringProperty(brand);
        this.type=new SimpleStringProperty(type);
        this.reality=new SimpleObjectProperty<>(reality);
        this.locked=new SimpleObjectProperty<>(locked);
        this.available=new SimpleObjectProperty<>(available);
        this.way=new SimpleObjectProperty<>(way);
        this.taking=new SimpleObjectProperty<>(taking);
        this.diff=new SimpleObjectProperty<>(diff);
    }

    public CodingModel copy(){
        return new CodingModel(this.getBar(),this.getCode(),this.getName(),this.getSpec(),this.getBrand(),
                this.getType(),this.getReality(),this.getLocked(),this.getAvailable(),this.getWay(),
                this.getTaking(),this.getDiff());
    }

    public String getBar() {
        return bar.get();
    }

    public StringProperty barProperty() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar.set(bar);
    }

    public String getCode() {
        return code.get();
    }

    public StringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSpec() {
        return spec.get();
    }

    public StringProperty specProperty() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec.set(spec);
    }

    public String getBrand() {
        return brand.get();
    }

    public StringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public Integer getReality() {
        return reality.get();
    }

    public SimpleObjectProperty<Integer> realityProperty() {
        return reality;
    }

    public void setReality(Integer reality) {
        this.reality.set(reality);
    }

    public Integer getLocked() {
        return locked.get();
    }

    public SimpleObjectProperty<Integer> lockedProperty() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked.set(locked);
    }

    public Integer getAvailable() {
        return available.get();
    }

    public SimpleObjectProperty<Integer> availableProperty() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available.set(available);
    }

    public Integer getWay() {
        return way.get();
    }

    public SimpleObjectProperty<Integer> wayProperty() {
        return way;
    }

    public void setWay(Integer way) {
        this.way.set(way);
    }

    public Integer getTaking() {
        return taking.get();
    }

    public SimpleObjectProperty<Integer> takingProperty() {
        return taking;
    }

    public void setTaking(Integer taking) {
        this.taking.set(taking);
    }

    public Integer getDiff() {
        return diff.get();
    }

    public SimpleObjectProperty<Integer> diffProperty() {
        return diff;
    }

    public void setDiff(Integer diff) {
        this.diff.set(diff);
    }
}
