package kz.texteditor.console;

import kz.texteditor.export.ExportFormat;
import kz.texteditor.model.EditorSnapshot;
import kz.texteditor.processing.CaseMode;
import kz.texteditor.processing.ProcessingOptions;
import kz.texteditor.service.EditorException;
import kz.texteditor.service.EditorFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Консольный интерфейс: читает команды и печатает сообщения. Вся логика - в {@link EditorFacade}. */
public class ConsoleMenu {

    private static final String END_MARKER = ":end";
    private static final String INVALID_CHOICE = "Неверный пункт меню";
    private static final Set<String> YES = Set.of("y", "yes", "д", "да");

    private final EditorFacade facade;
    private final StatisticsObserver statistics;
    private final BufferedReader in;
    private final PrintStream out;

    public ConsoleMenu(EditorFacade facade, StatisticsObserver statistics, BufferedReader in, PrintStream out) {
        this.facade = facade;
        this.statistics = statistics;
        this.in = in;
        this.out = out;
    }

    /** Главный цикл. Завершается по пункту 0 или при конце ввода. */
    public void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = readLine();
            if (choice == null) {
                return;
            }
            try {
                running = handleMainChoice(choice.trim());
            } catch (EditorException e) {
                out.println(e.getMessage());
            }
        }
    }

    // ---------- главное меню ----------

    private void printMainMenu() {
        out.println();
        out.println("=== Text Editor ===");
        out.println("1. Ввести или заменить исходный текст");
        out.println("2. Открыть TXT");
        out.println("3. Настроить обработку");
        out.println("4. Обработать текст");
        out.println("5. Показать исходник и результат");
        out.println("6. Сохранить результат в TXT");
        out.println("7. Сохранить результат в HTML");
        out.println("0. Выход");
        out.print("> ");
        out.flush();
    }

    /** @return false, если пользователь выбрал выход */
    private boolean handleMainChoice(String choice) {
        switch (choice) {
            case "1" -> enterText();
            case "2" -> openFile();
            case "3" -> settingsMenu();
            case "4" -> process();
            case "5" -> showSourceAndResult();
            case "6" -> save(ExportFormat.TXT, "result.txt");
            case "7" -> save(ExportFormat.HTML, "result.html");
            case "0" -> {
                return false;
            }
            default -> out.println(INVALID_CHOICE);
        }
        return true;
    }

    private void enterText() {
        out.println("Введите текст. Для завершения введите отдельную строку " + END_MARKER);
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = readLine()) != null && !line.equals(END_MARKER)) {
            lines.add(line);
        }
        facade.updateSource(String.join("\n", lines));
        out.println("Исходный текст заменён.");
    }

    private void openFile() {
        String path = ask("Путь к TXT-файлу: ");
        facade.open(path);
        out.println("Файл открыт, исходный текст заменён.");
    }

    private void process() {
        facade.processText();
        statistics.resultStatistics()
                .ifPresent(stats -> out.println("Статистика результата: " + stats));
    }

    private void showSourceAndResult() {
        EditorSnapshot state = facade.getSnapshot();
        out.println("--- Исходник ---");
        out.println(state.sourceText());
        out.println("Статистика: " + statistics.sourceStatistics());
        if (!state.hasResult()) {
            out.println("--- Результат ---");
            out.println("Результата ещё нет: нужна обработка.");
            return;
        }
        out.println(state.resultCurrent() ? "--- Результат ---" : "--- Результат (устарел: нужна обработка) ---");
        out.println(state.resultText());
        statistics.resultStatistics().ifPresent(stats -> out.println("Статистика: " + stats));
    }

    // ---------- сохранение ----------

    private void save(ExportFormat format, String defaultPath) {
        if (!facade.canSave()) {
            out.println(EditorFacade.NOT_PROCESSED_MESSAGE);
            return;
        }
        String path = ask("Путь для сохранения [" + defaultPath + "]: ");
        if (path.isBlank()) {
            path = defaultPath;
        }
        if (facade.fileExists(path) && !confirm("Файл " + path + " уже существует. Перезаписать? (y/n): ")) {
            out.println("Сохранение отменено.");
            return;
        }
        facade.saveResult(format, path);
        out.println("Результат сохранён: " + path);
    }

    // ---------- настройки ----------

    private void settingsMenu() {
        while (true) {
            ProcessingOptions options = facade.getSnapshot().options();
            printSettingsMenu(options);
            String choice = readLine();
            if (choice == null) {
                return;
            }
            switch (choice.trim()) {
                case "1" -> facade.updateOptions(options.toBuilder()
                        .removeExtraSpaces(!options.removeExtraSpaces()).build());
                case "2" -> facade.updateOptions(options.toBuilder()
                        .fixPunctuation(!options.fixPunctuation()).build());
                case "3" -> facade.updateOptions(options.toBuilder()
                        .trimQuotes(!options.trimQuotes()).build());
                case "4" -> chooseCaseMode(options);
                case "0" -> {
                    return;
                }
                default -> out.println(INVALID_CHOICE);
            }
        }
    }

    private void printSettingsMenu(ProcessingOptions options) {
        out.println();
        out.println("--- Настройки обработки ---");
        out.println("1. Убрать лишние пробелы [" + onOff(options.removeExtraSpaces()) + "]");
        out.println("2. Исправить пунктуацию [" + onOff(options.fixPunctuation()) + "]");
        out.println("3. Исправить парные кавычки [" + onOff(options.trimQuotes()) + "]");
        out.println("4. Регистр [" + caseLabel(options.caseMode()) + "]");
        out.println("0. Вернуться в основное меню");
        out.print("> ");
        out.flush();
    }

    private void chooseCaseMode(ProcessingOptions options) {
        out.println("Выберите режим регистра: 1. сохранить  2. верхний  3. нижний");
        out.print("> ");
        out.flush();
        String choice = readLine();
        CaseMode mode = switch (choice == null ? "" : choice.trim()) {
            case "1" -> CaseMode.KEEP;
            case "2" -> CaseMode.UPPER;
            case "3" -> CaseMode.LOWER;
            default -> null;
        };
        if (mode == null) {
            out.println(INVALID_CHOICE);
            return;
        }
        facade.updateOptions(options.toBuilder().caseMode(mode).build());
    }

    private static String onOff(boolean value) {
        return value ? "ВКЛ" : "ВЫКЛ";
    }

    private static String caseLabel(CaseMode mode) {
        return switch (mode) {
            case KEEP -> "сохранить";
            case UPPER -> "верхний";
            case LOWER -> "нижний";
        };
    }

    // ---------- ввод ----------

    private String ask(String prompt) {
        out.print(prompt);
        out.flush();
        String line = readLine();
        return line == null ? "" : line.trim();
    }

    private boolean confirm(String prompt) {
        return YES.contains(ask(prompt).toLowerCase());
    }

    /** @return строка или null при конце ввода / ошибке чтения */
    private String readLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}
