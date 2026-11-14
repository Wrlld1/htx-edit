package kz.texteditor.export.factory.creator;

import kz.texteditor.export.factory.product.TextExporter;
import kz.texteditor.export.factory.product.TxtExporter;

public class TxtExportJob extends ExportJob {

    @Override
    protected TextExporter createExporter() {
        return new TxtExporter();
    }
}