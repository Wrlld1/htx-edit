package kz.texteditor.service;

import kz.texteditor.export.ExportFormat;
import kz.texteditor.export.ExportJob;
import kz.texteditor.model.EditorModel;
import kz.texteditor.model.EditorSnapshot;
import kz.texteditor.processing.ProcessingOptions;
import kz.texteditor.processing.ProcessingService;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

/**
 * Facade: a single entry point for the console. Hides the model, processing, files, and export.
 * The facade does not notify observers; the model does that.
 */
public class EditorFacade {

    public static final String NOT_PROCESSED_MESSAGE = "Сначала обработайте текст";

    private final EditorModel model;
    private final ProcessingService processingService;
    private final FileService fileService;
    private final Map<ExportFormat, ExportJob> exportJobs;

    public EditorFacade(EditorModel model,
                        ProcessingService processingService,
                        FileService fileService,
                        Map<ExportFormat, ExportJob> exportJobs) {
        this.model = Objects.requireNonNull(model, "model");
        this.processingService = Objects.requireNonNull(processingService, "processingService");
        this.fileService = Objects.requireNonNull(fileService, "fileService");
        this.exportJobs = Map.copyOf(exportJobs);
    }

    /** Option 2: reads a TXT file and replaces the source text. The current text remains unchanged on error. */
    public void open(String path) {
        Path file = toPath(path);
        try {
            model.setSource(fileService.read(file));
        } catch (IOException e) {
            throw new EditorException("Не удалось открыть файл: " + path + " (" + describe(e) + ")", e);
        }
    }

    /** Option 1: replaces the source text with the entered text. */
    public void updateSource(String text) {
        model.setSource(text);
    }

    /** Option 3: replaces the processing options. */
    public void updateOptions(ProcessingOptions options) {
        model.setOptions(options);
    }

    /** Option 4: processes the source with the current options and stores the result in the model. */
    public void processText() {
        EditorSnapshot state = model.snapshot();
        String result = processingService.process(state.sourceText(), state.options());
        model.setResult(result);
    }

    /** Options 6 and 7: saves only the current result. */
    public void saveResult(ExportFormat format, String path) {
        EditorSnapshot state = model.snapshot();
        if (!state.resultCurrent()) {
            throw new EditorException(NOT_PROCESSED_MESSAGE);
        }
        ExportJob job = exportJobs.get(format);
        if (job == null) {
            throw new EditorException("Формат не поддерживается: " + format);
        }
        Path file = toPath(path);
        try {
            job.export(state.resultText(), file);
        } catch (IOException e) {
            throw new EditorException("Не удалось сохранить файл: " + path + " (" + describe(e) + ")", e);
        }
    }

    public boolean canSave() {
        return model.snapshot().resultCurrent();
    }

    public boolean fileExists(String path) {
        return fileService.exists(toPath(path));
    }

    public EditorSnapshot getSnapshot() {
        return model.snapshot();
    }

    private static Path toPath(String path) {
        if (path == null || path.isBlank()) {
            throw new EditorException("Путь к файлу не указан");
        }
        try {
            return Path.of(path.trim());
        } catch (InvalidPathException e) {
            throw new EditorException("Недопустимый путь к файлу: " + path, e);
        }
    }

    private static String describe(IOException e) {
        return e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
    }
}
