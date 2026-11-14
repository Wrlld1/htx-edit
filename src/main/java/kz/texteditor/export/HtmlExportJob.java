package kz.texteditor.export;

public class HtmlExportJob extends ExportJob {

    @Override
    protected TextExporter createExporter() {
        return new HtmlExporter();
    }
}