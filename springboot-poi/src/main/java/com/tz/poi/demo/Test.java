package com.tz.poi.demo;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName Test
 * @Description
 * @Date 14:15 2021/6/20
 **/
public class Test {
    public static void main(String[] args) throws Exception {
        List<JSONObject> jsonObjects = new LinkedList<>();

        JSONObject p1 = new JSONObject();
        p1.put("学校", "清华");
        p1.put("姓名", "张撒");
        p1.put("性别", "nan");
        p1.put("专业", "计算机科学与技术");
        p1.put("年龄", 22);
        JSONObject p11 = new JSONObject();
        p11.put("学校", "清华");
        p11.put("姓名", "张撒22");
        p11.put("性别", "nan");
        p11.put("专业", "计算机科学与技术");
        p11.put("年龄", 22);
        JSONObject p2 = new JSONObject();
        p2.put("学校", "北大");
        p2.put("姓名", "里斯");
        p2.put("性别", "nan");
        p2.put("专业", "计算机科学与技术");
        p2.put("年龄", "21");
        JSONObject p3 = new JSONObject();
        p3.put("学校", "北大");
        p3.put("姓名", "wuwang");
        p3.put("性别", "nan");
        p3.put("专业", "计算机科学与技术");
        p3.put("年龄", "21");
        jsonObjects.add(p1);
        jsonObjects.add(p11);
        jsonObjects.add(p2);
        jsonObjects.add(p3);
        export(null, jsonObjects);
    }

    public static void export(HttpServletResponse response, List<JSONObject> jsonObjects) throws IOException {

        //Create blank workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        //Create a blank sheet
        HSSFSheet spreadsheet = workbook.createSheet(
                "sheet");
        spreadsheet.setColumnWidth(0, 6000);
        spreadsheet.setColumnWidth(1, 4000);
        spreadsheet.setColumnWidth(2, 4000);
        // 设置第一列的数据
        HSSFRow row0 = spreadsheet.createRow(0);
        row0.setHeight((short) 500);
        HSSFCell cell = row0.createCell(0);
        cell.setCellValue("学校");
        // 设置第二列的数据，加斜线
        HSSFCell cell1 = row0.createCell(1);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.forInt(2));
        cellStyle.setVerticalAlignment(VerticalAlignment.forInt(1));
        cell1.setCellStyle(cellStyle);
        String val = "姓名" + "         " + "属性";
        cell1.setCellValue(val);
        HSSFPatriarch patriarch = spreadsheet.createDrawingPatriarch();
        HSSFClientAnchor a = new HSSFClientAnchor(0, 0, 1023, 255, (short) 1, 0, (short) 1, 0);
        HSSFSimpleShape shape1 = patriarch.createSimpleShape(a);
        shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        shape1.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID);

        // 纵向字段
        Set<String> strings = jsonObjects.get(0).keySet();
        int x = 2;
        for (String string : strings) {
            HSSFCell cellTmp = row0.createCell(x);
            if (!"学校".equals(string) && !"姓名".equals(string)) {
                cellTmp.setCellValue(string);
                x++;
            }
        }
        AtomicInteger rowid = new AtomicInteger(1);
        for (JSONObject jsonObject : jsonObjects) {
            Set<String> cells = jsonObject.keySet();
            HSSFRow row = spreadsheet.createRow(rowid.getAndIncrement());
            row.setHeight((short) 500);
            Cell cellTmp0 = row.createCell(0);
            cellTmp0.setCellValue(jsonObject.getString("学校"));
            Cell cellTmp1 = row.createCell(1);
            cellTmp1.setCellValue(jsonObject.getString("姓名"));
            cells.removeIf(next -> "学校".equals(next) || "姓名".equals(next));
            int i = 2;

            // 循环次数
            for (String no : cells) {
                // 获取值
                for (String value : cells) {
                    Cell cellTmp = row.createCell(i);
                    HSSFCell cell2 = row0.getCell(i);
                    String stringCellValue = cell2.getStringCellValue();
                    if (value.equals(stringCellValue)) {
                        cellTmp.setCellValue(jsonObject.getString(stringCellValue));
                        i++;
                        break;
                    }
                }
            }

        }
        int rowNum = spreadsheet.getPhysicalNumberOfRows();//获得总行数
        for (int i = 1; i < rowNum; i++) {
            HSSFCell cellA = spreadsheet.getRow(i).getCell(0);
            HSSFRow row = spreadsheet.getRow(i + 1);
            if (null == row) {
                break;
            }
            HSSFCell cellB = row.getCell(0);
            if (cellA.getStringCellValue().equals(cellB.getStringCellValue())) {
                spreadsheet.addMergedRegion(new CellRangeAddress(i, i + 1, 0, 0));
            }
        }


        //Write the workbook in file system
        String name = "/tmp/excel/" + System.currentTimeMillis() + ".xls";
        FileOutputStream out = new FileOutputStream(
                new File(name));
        workbook.write(out);
//        String fileName = URLEncoder.encode("统计.xls", "UTF-8");
//        response.setHeader("filename", fileName);
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");
////        response.setHeader("Content-Disposition","attachment;filename=tj.xls");
//        response.setHeader("content-disposition", "attachment;filename=" + new
//                String(fileName.getBytes("ISO8859-1")));
//        ServletOutputStream outputStream = response.getOutputStream();
//        workbook.write(outputStream);
//        outputStream.flush();
//        outputStream.close();
    }
}
