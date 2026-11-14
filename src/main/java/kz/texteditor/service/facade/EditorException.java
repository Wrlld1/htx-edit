package kz.texteditor.service.facade;

/** A user-friendly workflow error. The console displays the message as-is. */
public class EditorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EditorException(String message) {
        super(message);
    }

    public EditorException(String message, Throwable cause) {
        super(message, cause);
    }
}
