package product.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by cbjiang on 2018/10/17.
 */
public class ExcelUtil {

    public static JSONArray importExcel(HSSFWorkbook wb){

        JSONArray bookObj = new JSONArray();

        HSSFWorkbook xs = wb;
        for (int s = 0; s < xs.getNumberOfSheets(); s++) {
            HSSFSheet sheet = xs.getSheetAt(s);
            int lastRowNum = sheet.getLastRowNum();
            JSONArray sheetObj=new JSONArray();
            for (int i = 0; i <= lastRowNum; i++) {
                HSSFRow row = sheet.getRow(i);
                if(row!=null){
                    JSONObject rowObj = new JSONObject();
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        HSSFCell cell = row.getCell(j);
                        if(cell!=null) {
                            try {
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    rowObj.put(Integer.toString(cell.getColumnIndex()), cell.getNumericCellValue());
                                } else {
                                    rowObj.put(Integer.toString(cell.getColumnIndex()), cell.getStringCellValue());
                                }
                            } catch (Exception e) {
                                rowObj.put(Integer.toString(cell.getColumnIndex()), new HSSFRichTextString().getString());
                            }
                        }
                    }
                    if(rowObj.size()>0){
                        sheetObj.add(rowObj);
                    }
                }
            }
            bookObj.add(sheetObj);
        }
        return bookObj;
    }

    public static Boolean exportExcel(String path,String sheetName,String[] header,JSONArray data){
        Integer headerRowNum = 0;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);
        HSSFRow headerRow = sheet.createRow(headerRowNum);
        for(int i=0;i<header.length;i++){
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(new HSSFRichTextString(header[i]));
        }

        for(int i=0;i<data.size();i++){
            JSONArray rowData=data.getJSONArray(i);
            HSSFRow row = sheet.createRow(i+1);
            for(int j=0;j<rowData.size();j++){
                HSSFCell cell = row.createCell(j);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(new HSSFRichTextString(rowData.getString(j)));
            }
        }

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(new File(path));
            workbook.write(fos);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
