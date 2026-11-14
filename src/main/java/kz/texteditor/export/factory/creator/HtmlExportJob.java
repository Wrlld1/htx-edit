package kz.texteditor.export.factory.creator;

import kz.texteditor.export.factory.product.HtmlExporter;
import kz.texteditor.export.factory.product.TextExporter;

public class HtmlExportJob extends ExportJob {

    @Override
    protected TextExporter createExporter() {
        return new HtmlExporter();
    }
}