package com.jytb.logistics.util.excle;


import co.chexiao.phoenix.contract.bean.excel.ExcelData;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExcelUtil {
    private static final Logger logger =
            LoggerFactory.getLogger(ExcelUtil.class);

    private static void setHttpDownloadHeader(HttpServletResponse response, String fileName) throws IOException {
        response.reset();// 清空输出流
        response.setHeader("Content-disposition",
                "attachment; filename=" + new String(fileName.getBytes("utf-8"), "utf-8") + ".xlsx");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
    }

    public static List<ExcelData> createHeader(Collection<String> headerDatas) {
        List<ExcelData> headList = new ArrayList<>();
        //终端号 上传数据点 点火数据 熄火数据 设备报警 基站数据 最后上线时间 电量 定位方式
        for (String string : headerDatas) {
            ExcelData excelData = new ExcelData(string);
            excelData.setLength(300);
            headList.add(excelData);
        }
        return headList;
    }

    public static void exportExcel(List<ExcelData> headerList, List<List<ExcelData>> dataList, HttpServletResponse response, String fileName) throws IOException {
        OutputStream outputStream = null;
        try {
            setHttpDownloadHeader(response, fileName);
            exportExcel(headerList, dataList, outputStream = response.getOutputStream());
        } catch (Exception e) {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void exportExcel(Collection<String> headerNames, List<List<ExcelData>> dataList, HttpServletResponse response, String fileName) throws IOException {
        OutputStream outputStream = null;
        try {
            setHttpDownloadHeader(response, fileName);
            List<ExcelData> headerList = createHeader(headerNames);
            exportExcel(headerList, dataList, outputStream = response.getOutputStream());
        } catch (Exception e) {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 导出Excel
     */
    public static void exportExcel(List<ExcelData> headList, List<List<ExcelData>> dataList, OutputStream output) {
        SXSSFWorkbook wb = new SXSSFWorkbook(1000);
        SXSSFSheet sh = (SXSSFSheet) wb.createSheet("Sheet0");
        SXSSFRow rowHead = (SXSSFRow) sh.createRow(0);

        for (int i = 0; i < headList.size(); i++) {
            ExcelData excelData = headList.get(i);
            Cell xssFCell = rowHead.createCell(i);
            xssFCell.setCellValue(excelData.getData());
        }

        for (int i = 0; i < dataList.size(); i++) {
            Row dataRow = sh.createRow(i + 1);
            List<ExcelData> excelDataList = dataList.get(i);
            for (int j = 0; j < excelDataList.size(); j++) {
                ExcelData excelData = excelDataList.get(j);
                Cell xssFCell = dataRow.createCell(j);
                xssFCell.setCellValue(excelData.getData());
            }
        }

        // 设置自适应
        for (int i = 0; i < headList.size(); i++) {
            sh.autoSizeColumn(i);
            sh.setColumnWidth(i, sh.getColumnWidth(i) * 15 / 10);
        }

        try {
            wb.write(output);
        } catch (IOException e) {
            logger.error("生成excel出错", e);
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    logger.error("关闭excel出错", e);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * app发货单生成excel
     *
     * @param headList
     * @param dataList
     */
    public static void createExcel(List<ExcelData> headList, List<List<ExcelData>> dataList, String fullFilePath) {

        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建工作表
        HSSFSheet sheet = workbook.createSheet("sheet1");
        Row rowHead = sheet.createRow(0);
        for (int i = 0; i < headList.size(); i++) {
            ExcelData excelData = headList.get(i);
            sheet.setColumnWidth((short) i, 20 * excelData.getLength()); // (excelData.getData().length())

            Cell xssFCell = rowHead.createCell(i);
            xssFCell.setCellValue(excelData.getData());
        }

        for (int i = 0; i < dataList.size(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            List<ExcelData> excelDataList = dataList.get(i);
            for (int j = 0; j < excelDataList.size(); j++) {
                ExcelData excelData = excelDataList.get(j);
                Cell xssFCell = dataRow.createCell(j);
                xssFCell.setCellValue(excelData.getData());
            }
        }

        try {
            File dest = new File(fullFilePath);
            FileOutputStream xlsStream = new FileOutputStream(dest);
            workbook.write(xlsStream);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("生成excel出错", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.error("关闭excel出错", e);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 导出工单报表Excel
     */
    public static void exportGongdanExcel(List<ExcelData> headList, List<List<ExcelData>> dataList,
                                          int wireCount, int wirelessCount, OutputStream output) {
        Workbook wb = new SXSSFWorkbook(1000);
        Sheet sh = wb.createSheet("Sheet0");
        Row rowSummery = sh.createRow(0);
        Cell summeryWireXssFCell = rowSummery.createCell(0);
        summeryWireXssFCell.setCellValue("有线总量 ：" + wireCount);
        Cell summeryWirelessXssFCell = rowSummery.createCell(1);
        summeryWirelessXssFCell.setCellValue("无线总量 ：" + wirelessCount);
        Cell summeryXssFCell = rowSummery.createCell(2);
        summeryXssFCell.setCellValue("总量 ：" + (wireCount + wirelessCount));
        Row rowHead = sh.createRow(1);

        int i;
        for (i = 0; i < headList.size(); ++i) {
            ExcelData excelData = (ExcelData) headList.get(i);
            sh.setColumnWidth((short) i, 20 * excelData.getLength());
            Cell cell = rowHead.createCell(i);
            cell.setCellValue(excelData.getData());
        }

        for (i = 0; i < dataList.size(); ++i) {
            Row dataRow = sh.createRow(i + 2);
            List<ExcelData> excelDataList = (List) dataList.get(i);

            for (int j = 0; j < excelDataList.size(); ++j) {
                ExcelData excelData = (ExcelData) excelDataList.get(j);
                Cell cell = dataRow.createCell(j);
                cell.setCellValue(excelData.getData());
            }
        }

        try {
            wb.write(output);
        } catch (IOException var26) {
            logger.error("导出excel出错", var26);
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException var25) {
                    logger.error("关闭excel出错", var25);
                    var25.printStackTrace();
                }
            }

        }
    }

    /**
     * 导入 Excel
     */
    public List<ExcelData> inputExcel() {
        return null;
    }

    public static void main(String[] args) {
        List<ExcelData> headList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            headList.add(new ExcelData("A-" + i));
        }
        List<List<ExcelData>> dataList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            List<ExcelData> data = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                data.add(new ExcelData("B-" + i + "-" + j));
            }
            dataList.add(data);
        }
        try {
            FileOutputStream fOut = new FileOutputStream("E://tmp//a.xlsx");
            exportExcel(headList, dataList, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
