package kz.texteditor.export;

public class TxtExportJob extends ExportJob {

    @Override
    protected TextExporter createExporter() {
        return new TxtExporter();
    }
}