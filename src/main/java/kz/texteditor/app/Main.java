package kz.texteditor.app;

import kz.texteditor.console.ConsoleMenu;
import kz.texteditor.console.ResultObserver;
import kz.texteditor.console.StatisticsObserver;
import kz.texteditor.export.ExportFormat;
import kz.texteditor.export.ExportJob;
import kz.texteditor.export.HtmlExportJob;
import kz.texteditor.export.TxtExportJob;
import kz.texteditor.model.EditorModel;
import kz.texteditor.processing.ProcessingService;
import kz.texteditor.service.EditorFacade;
import kz.texteditor.service.FileService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;

/** Entry point: creates dependencies, registers observers once, and starts the menu. */
public final class Main {

    private Main() { }

    public static void main(String[] args) throws Exception {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

        FileService fileService = new FileService();
        Map<ExportFormat, ExportJob> exportJobs = new EnumMap<>(ExportFormat.class);
        exportJobs.put(ExportFormat.TXT, new TxtExportJob());
        exportJobs.put(ExportFormat.HTML, new HtmlExportJob());

        EditorModel model = new EditorModel();
        StatisticsObserver statistics = new StatisticsObserver();
        model.addObserver(new ResultObserver(out));
        model.addObserver(statistics);

        EditorFacade facade = new EditorFacade(model, new ProcessingService(), fileService, exportJobs);
        new ConsoleMenu(facade, statistics, in, out).run();
    }
}
