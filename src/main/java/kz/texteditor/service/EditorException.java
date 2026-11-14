package kz.texteditor.service;

/** Понятная пользователю ошибка сценария. Сообщение выводится консолью как есть. */
public class EditorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EditorException(String message) {
        super(message);
    }

    public EditorException(String message, Throwable cause) {
        super(message, cause);
    }
}
