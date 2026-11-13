package kz.texteditor.model;

/** Observer: subscriber to model changes. */
public interface EditorObserver {
    void onChanged(EditorSnapshot snapshot);
}
