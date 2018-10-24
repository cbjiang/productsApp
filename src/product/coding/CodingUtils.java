package product.coding;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.scene.control.Alert;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/10/23.
 */
public class CodingUtils {
    public static final String[] ACTUALITY_TABLE_HEADER=new String[]{
            "商品条码","商品编码","商品名称","规格名称","货号","品牌","分类","实际库存","锁定库存","可用库存",
            "在途库存","品牌","制式","毛线材质","计价单位","毛线粗细","货号","材料包配线否","材质","形状",
            "主图来源","图案","是否可定制","店铺"
    };
    public static final String[] TEMP_TABLE_HEADER=new String[]{
            "商品条码","商品编码","商品名称","规格名称","实际数量","差异数量"
    };
    public static Boolean checkHeader(JSONObject header, String[] tempHeader){
        if(header.size() < tempHeader.length){
            return false;
        }
        for(int i=0;i<tempHeader.length;i++){
            if(!tempHeader[i].equals(header.getString(Integer.toString(i)))){
                return false;
            }
        }
        return true;
    }
    public static List<CodingModel> jsonArray2List(JSONArray data, String[] keys){
        Integer keySize = 12;
        if(keys.length != keySize){
            alert("错误","数据格式有误！", Alert.AlertType.ERROR);
            return null;
        }
        List<CodingModel> list = new LinkedList<>();
        for(int i=0;i<data.size();i++){
            list.add(new CodingModel(
                    "$".equals(keys[0])?null:data.getJSONObject(i).getString(keys[0]),
                    "$".equals(keys[1])?null:data.getJSONObject(i).getString(keys[1]),
                    "$".equals(keys[2])?null:data.getJSONObject(i).getString(keys[2]),
                    "$".equals(keys[3])?null:data.getJSONObject(i).getString(keys[3]),
                    "$".equals(keys[4])?null:data.getJSONObject(i).getString(keys[4]),
                    "$".equals(keys[5])?null:data.getJSONObject(i).getString(keys[5]),
                    "$".equals(keys[6])?null:data.getJSONObject(i).getInteger(keys[6]),
                    "$".equals(keys[7])?null:data.getJSONObject(i).getInteger(keys[7]),
                    "$".equals(keys[8])?null:data.getJSONObject(i).getInteger(keys[8]),
                    "$".equals(keys[9])?null:data.getJSONObject(i).getInteger(keys[9]),
                    "$".equals(keys[10])?null:data.getJSONObject(i).getInteger(keys[10]),
                    "$".equals(keys[11])?null:data.getJSONObject(i).getInteger(keys[11])
            ));
        }
        return list;
    }
    public static void alert(String title, String text, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
    public static String handleNameOrSpec(String str){
        return str.replaceAll("【配件包】","").replaceAll("不含网格片","")
                .replaceAll("\"","").replaceAll("“","").replaceAll("”","")
                .replaceAll(" ","").replaceAll("、","");
    }
    public static Set<String> generateCodeConditions(List<CodingModel> data){
        Set<String> codeConditions = new HashSet<>();
        for (int i = 0; i < data.size(); i++) {
            String objCode = data.get(i).getCode();
            if(objCode!=null) {
                String condition = objCode.split("-")[0] + "-" + objCode.split("-")[1] +
                        ( objCode.split("-").length>2?"-":"");
                if (!codeConditions.contains(condition)) {
                    codeConditions.add(condition);
                }
            }
        }
        return codeConditions;
    }
    public static Boolean compareCodeCondition(String code,String condition){
        if(condition.endsWith("-")){
            return code.contains(condition);
        }else{
            return code.equals(condition);
        }
    }
}
