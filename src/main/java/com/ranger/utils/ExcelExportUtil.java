package com.ranger.utils;

import com.ranger.activity.vo.ActivityRegistrationExportVO;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * 督办任务excel导出工具类;
 *
 * @author Zhangwei
 */
@Service
public class ExcelExportUtil {

    public static int cols = 6;//excel里表格列


    /**
     * POI : 导出数据，存放于Excel中
     * <p>
     * os     输出流 (action: OutputStream os = response.getOutputStream();)
     * firstList 要导出的数据记录集合(根数据)
     * secondList 重点任务数据(重大工作项:一级目录)
     * thirdList 分解任务数据(任务分解项:二级目录)
     */
    public void exportTaskSumPoi(List<ActivityRegistrationExportVO> ExportVOS, String activityName, HttpServletResponse response) {

        //取出数据源;
        String headTitle = activityName;//标题;


        try {
            HSSFWorkbook book = new HSSFWorkbook();


            //表内容默认从工作表第三行开始;
            int start = 2;
            // 在Excel工作薄中建一张工作表;
            String sheetTitle = activityName;
            HSSFSheet sheet = book.createSheet(sheetTitle);
            //设置标题和表头;
            createTitle(book, sheet, headTitle);
            createHead(book, sheet);

            /**
             * 样式
             */
            CellStyle contentStyle = createContentStyle(book);
            CellStyle otherStyle = createOtherStyle(book);

            int i = 0;
            for (ActivityRegistrationExportVO activityExportVO : ExportVOS) {
                Row row = sheet.createRow(start + i);
                row.createCell((short)0).setCellValue(activityExportVO.getStageName());
                row.createCell((short)1).setCellValue(activityExportVO.getGroupName());
                row.createCell((short)2).setCellValue(activityExportVO.getGoodName());
                row.createCell((short)3).setCellValue(activityExportVO.getUserId());
                row.createCell((short)4).setCellValue(activityExportVO.getUserName());
                row.createCell((short)5).setCellValue(activityExportVO.getUserPhone());
                i++;
            }

            //File outFile = new File("活动报名人员类别导出.xlsx");
            /*FileOutputStream fout = new FileOutputStream("活动报名人员类别导出.xlsx");
            book.write(fout);
            fout.close();*/

            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String((activityName).getBytes("gb2312"), "ISO-8859-1") + ".xls");
            OutputStream os = response.getOutputStream();
            book.write(os);
            os.flush();
            os.close();

            //return outFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return null;
    }


    /**
     * 给excel设置标题
     *
     * @param sheet
     */
    public static void createTitle(Workbook book, Sheet sheet, String headTitle) {
        CellStyle style = createTitleStyle(book);
        Row row = sheet.createRow(0);// 创建第一行,设置表的标题;
        row.setHeightInPoints(36);//设置行的高度是34个点
        Cell cell = row.createCell(0);
        cell.setCellValue(headTitle);
        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cols - 1));//第一行跨表所有列;
    }

    /**
     * 设置导出表的表头
     *
     * @param book
     * @param sheet
     */
    private static void createHead(Workbook book, Sheet sheet) {
        // 设置单元格格式(文本)
        // 第二行为表头行
        String title = "";
        CellStyle style = createHeadStyle(book);

        Row row = sheet.createRow(1);// 创建第一行
        row.setHeightInPoints(25);//设置行的高度是20个点
        for (int j = 0; j < cols; j++) {
            sheet.autoSizeColumn((short)j);
            Cell cell = row.createCell(j);
            //cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            if (j == 0) {
                title = "活动阶段";
            }
            if (j == 1) {
                title = "活动分组";
            }
            if (j == 2) {
                title = "活动项目";
            }
            if (j == 3) {
                title = "用户id";
            }
            if (j == 4) {
                title = "用户姓名";
            }
            if (j == 5) {
                title = "用户手机号";
            }
            cell.setCellValue(title);
            cell.setCellStyle(style);
        }
    }

    /**
     * 创建标题样式
     *
     * @param book
     * @return
     */
    public static CellStyle createTitleStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        Font font = book.createFont();
        font.setFontHeightInPoints((short) 20); // 字体大小
        font.setFontName("宋体");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗体
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 创建表头样式
     *
     * @param book
     * @return
     */
    public static CellStyle createHeadStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); // 填充单元格
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index); // 填浅蓝色
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        Font font = book.createFont();
        font.setFontHeightInPoints((short) 11); // 字体大小
        font.setFontName("黑体");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗体
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 创建内容部分前8列单元格样式
     *
     * @param book
     * @return
     */
    public static CellStyle createContentStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); // 填充单元格
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cellStyle.setWrapText(true);//自动换行
        Font font = book.createFont();
        font.setFontHeightInPoints((short) 20); // 字体大小
        font.setFontName("宋体");
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 创建内容其它部分单元格样式
     *
     * @param book
     * @return
     */
    public static CellStyle createOtherStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cellStyle.setWrapText(true);//自动换行
        Font font = book.createFont();
        font.setFontHeightInPoints((short) 15); // 字体大小
        font.setFontName("宋体");
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 设置字符串内注解样式;
     *
     * @param book
     * @param str  传入的待处理字符串;
     * @return
     */
    public static RichTextString noteFontStyle(Workbook book, String str) {
        //定义字体
        Font hFont = book.createFont();
        hFont.setFontHeightInPoints((short) 10);//字体大小
        hFont.setFontName("楷体");
        RichTextString richString = null;
        try {//2007版excel
            richString = new XSSFRichTextString(str);//2007版excel
        } catch (Exception e) {
            richString = new HSSFRichTextString(str);//2003版excel
        }
        if (str.indexOf("（") == -1) {
            return richString;
        }
        //字体样式设置到字符串中;
        richString.applyFont(str.indexOf("（"), str.indexOf("）"), hFont);
        return richString;
    }

    /**
     * 设置日期格式;
     *
     * @param book
     * @param
     * @return
     */
    public static CellStyle setDateStyle(Workbook book) {
        CellStyle style = book.createCellStyle();
        try {//2007版excel
            DataFormat format = book.createDataFormat();
            style.setDataFormat(format.getFormat("yyyy年MM月dd日"));
        } catch (Exception e) {//2003版excel
            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy年MM月dd日"));
        }

        return style;
    }

}
