package kz.texteditor.model.observer;

/** Observer: subscriber to model changes. */
public interface EditorObserver {
    void onChanged(EditorSnapshot snapshot);
}
