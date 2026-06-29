package com.sfccn.pdftool.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelTool {
    public static final Logger logger = LogManager.getLogger(ExcelTool.class);
    private static CreationHelper createHelper = null;//wb.getCreationHelper();
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    public static void exportExcelX(List<String> titleList, List<List<String>> rowList, String xlsxFileName) throws IOException {
        exportExcelX(titleList, rowList, new File(xlsxFileName));
    }
        /**
     * 导出Excel 2007 OOXML (.xlsx)格式
     */
    public static void exportExcelX(List<String> titleList, List<List<String>> rowList, File xlsxFileName) throws IOException {

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");
        createHelper = wb.getCreationHelper();
        // 表头
        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        int columnIndex = 0;
        for(String title:titleList){
            CellStyle style = wb.createCellStyle();
            Font font = wb.createFont();
            font.setBold(true);
            style.setFont(font);
            createCell(title, row, columnIndex++, style);
        }
        CellStyle hlink_style = wb.createCellStyle();
        Font hlink_font = wb.createFont();
        hlink_font.setUnderline(Font.U_SINGLE);
        hlink_font.setColor(IndexedColors.BLUE.getIndex());
        hlink_style.setFont(hlink_font);
        for(List<String> list:rowList){
            columnIndex = 0;
            row = sheet.createRow(rowIndex++);
            //System.out.println("============================excel:" + list);
            for(String value:list){
                if(value != null && value.startsWith("http")){
                    createCell(value, row, columnIndex++, hlink_style);
                }
                else{
                    createCell(value, row, columnIndex++);
                }

            }
        }
        FileOutputStream os = new FileOutputStream(xlsxFileName);
        wb.write(os);
        wb.close();
        os.close();
        logger.info("excel保存成功====" +xlsxFileName.getAbsolutePath());
    }

    private static void createCell(String value, Row row, int column, CellStyle cellStyle) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        if(cellStyle != null){
            cell.setCellStyle(cellStyle);
        }
        if(value != null && value.startsWith("http")){
            XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
            link.setAddress(value);
            cell.setHyperlink(link);
        }
    }
    private static void createCell(String value, Row row, int column) {
        createCell(value, row, column, null);

    }
    /**
     * 读入excel文件，解析后返回
     * @param file
     * @throws IOException
     */
    public static List<List<String>> readExcel(File file) throws IOException {
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<List<String>> list = new ArrayList<>();
        if(workbook != null){
            for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if(sheet == null){
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum  = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for(int rowNum = firstRowNum+1;rowNum <= lastRowNum;rowNum++){
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if(row == null){
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    //String[] cells = new String[row.getPhysicalNumberOfCells()];
                    List<String> cells = new ArrayList<>();
                    //循环当前行
                    for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
                        Cell cell = row.getCell(cellNum);
//                        cells[cellNum] = getCellValue(cell);
                        cells.add(getCellValue(cell));
                    }
                    list.add(cells);
                }
                break;
            }
            workbook.close();
        }
        return list;
    }
    /**
     * 读入excel文件，解析后返回
     * @param file
     * @throws IOException
     */
    public static List<String[]> readExcel(File file, int sheetNum) throws IOException {
        checkFile(file);
        Workbook workbook = getWorkBook(file);
        List<String[]> list = new ArrayList<String[]>();
        if(workbook != null){
//            for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(sheetNum);
//                if(sheet == null){
//                    continue;
//                }
            //获得当前sheet的开始行
            int firstRowNum  = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了第一行的所有行
            for(int rowNum = firstRowNum+1;rowNum <= lastRowNum;rowNum++){
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if(row == null){
                    continue;
                }
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                if(firstCellNum < 0){
                    break;
                }
                //System.out.println("firstCellNum=" + firstCellNum);
                //获得当前行的列数
                int lastCellNum = row.getPhysicalNumberOfCells();
                String[] cells = new String[row.getPhysicalNumberOfCells()];
                //循环当前行
                for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
                    Cell cell = row.getCell(cellNum);
                    cells[cellNum] = getCellValue(cell);
                }
                list.add(cells);
            }
        }
        workbook.close();
//        }
        return list;
    }
    public static void checkFile(File file) throws IOException {
        //判断文件是否存在
        if(null == file){
            logger.error("文件不存在！");
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getName();
        //判断文件是否是excel文件
        if(!fileName.endsWith(xls) && !fileName.endsWith(xlsx)){
            logger.error(fileName + "不是excel文件");
            throw new IOException(fileName + "不是excel文件");
        }
    }
    public static Workbook getWorkBook(File file) {
        //获得文件名
        String fileName = file.getName();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = new FileInputStream(file);
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith(xls)){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith(xlsx)){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return workbook;
    }
    public static String getCellValue(Cell cell){
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if(cell.getCellType() == CellType.NUMERIC){
            cell.setCellType(CellType.STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()){
            case NUMERIC: //数字
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    cellValue = String.valueOf(cell.getDateCellValue());
                } else {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: //空值
                cellValue = "";
                break;
            case ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }
}
