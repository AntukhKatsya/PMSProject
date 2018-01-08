package by.antukh.noteswithme.system;

/**
 * Created by acdc on 06.01.18.
 */

public interface ListenerSynchronization {

    void onSynchronizationStarted();

    void onSynchronizationStoped();

    void onSynchronizationComplete();

    void onSynchronizationProgress(int progress);

    void onSynchronizationError(String error);

    void onSynchronizationAlredyStarted();

    void onSynchronizationNotStarted();

}
