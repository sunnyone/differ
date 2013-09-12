package cz.nkp.differ.plugins.tools;


import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.awt.Color;

import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
/**
 *  @author Jonatan Svensson
 *  v. 12/09/2013
 */

public class ReportGenerator{

    public ReportGenerator(){
        build();
    }

    private void build() {
        String path=System.getProperty("user.dir")+"/report.pdf";
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(path);
        System.out.println("Saving to "+path);
        StyleBuilder boldStyle         = stl.style().bold();
        StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
        StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
                .setBorder(stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY);

        //                                                           title,     field name     data type
        TextColumnBuilder<String>     itemColumn      = col.column("Property",       "property",      type.stringType()).setStyle(boldStyle);
        TextColumnBuilder<String>    quantityColumn  = col.column("Source",   "source",  type.stringType());
        TextColumnBuilder<String> unitPriceColumn = col.column("Value", "value", type.stringType());
            try {
            report()//create new report design
                    .setColumnTitleStyle(columnTitleStyle)
                    .setSubtotalStyle(boldStyle)
                    .highlightDetailEvenRows()
                    .columns(//add columns
                             itemColumn, quantityColumn, unitPriceColumn)
                    .groupBy(itemColumn)
                    .title(cmp.text("Differ first test").setStyle(boldCenteredStyle))//shows report title
                    .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))//shows number of page at page footer
                    .setDataSource(createDataSource())//set datasource
                    .toPdf(pdfExporter);
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("property", "source", "value");
        dataSource.add("Color Space", "1", "12");
        dataSource.add("Metadata123", "312", "213213");
        return dataSource;
    }

    public void getResults(){

	}
	
	public void toPDF(){
		// Trigger from gui
	}
	
	public void getThumbnail(){
		// Extract thumbnail/adjusted-size image in some clever way to put in report
	}
	
}