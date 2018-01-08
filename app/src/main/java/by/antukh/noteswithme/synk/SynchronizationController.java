package by.antukh.noteswithme.system;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static by.antukh.noteswithme.system.SynchronizationController.States.IN_PROGRESS;
import static by.antukh.noteswithme.system.SynchronizationController.States.NOT_STARTED;

/**
 * Created by acdc on 06.01.18.
 */

public class SynchronizationController {

    private static final String ANSWER_OK = "OK";
    private ArrayList<ListenerSynchronization> listeners;

    public enum States {
        NOT_STARTED,
        IN_PROGRESS,
    }

    private States currentState;
    private String lastError = null;

    private SynchronizationController() {
        currentState = NOT_STARTED;
        listeners = new ArrayList<>();
    }

    public void addListener(ListenerSynchronization listener) {
        listeners.add(listener);
    }


    public void removeListener(ListenerSynchronization listener) {
        listeners.remove(listener);
    }


    private static SynchronizationController instance;

    public static SynchronizationController getInstance() {
        if (instance == null)
            instance = new SynchronizationController();
        return instance;
    }

    public States getCurrentState() {
        return currentState;
    }

    public void stop() {
        if (currentState == NOT_STARTED) {
            notifyNotStarted();
            return;
        }
        currentState = NOT_STARTED;
        notifyStop();

    }

    public void start() {
        if (currentState == IN_PROGRESS) {
            notifyAlredyStarted();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                currentState = IN_PROGRESS;
                try {

                    ArrayList<Note> notesToSync = DB.getInstance().getUnsynkedNotes();

                    String data = Utils.getNotesJsonString(notesToSync);

                    int port = 1000;
                    String host = "192.168.100.222";
                    Socket socket = new Socket(host, port);

                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();
                    DataInputStream dis = new DataInputStream(is);
                    os.write(data.getBytes());
                    os.flush();

                    String readedData = "";
                    int c = 0;
                    while ((c = dis.read()) != -1)
                        readedData += (char) c;

                    socket.close();
                    if (readedData.length() == 0 || !readedData.equals(ANSWER_OK))
                        notifyError("Bad readed data: " + readedData);
                    else {
                        DB.getInstance().setDataToSynhronizedState();
                        notifyCompleted();
                    }
                } catch (Exception e) {
                    notifyError(e.getLocalizedMessage());
                    return;
                }
                currentState = NOT_STARTED;
            }
        }).start();
        notifyStarted();
    }

    private void notifyCompleted() {
        for (ListenerSynchronization listener : listeners)
            listener.onSynchronizationComplete();
    }

    private void notifyError(String errMsg) {
        for (ListenerSynchronization listener : listeners)
            listener.onSynchronizationError(errMsg);
    }

    private void notifyStarted() {
        for (ListenerSynchronization listener : listeners)
            listener.onSynchronizationStarted();
    }


    private void notifyNotStarted() {

        for (ListenerSynchronization listener : listeners)
            listener.onSynchronizationNotStarted();
    }

    private void notifyAlredyStarted() {

        for (ListenerSynchronization listener : listeners)
            listener.onSynchronizationAlredyStarted();
    }

    private void notifyStop() {

        for (ListenerSynchronization listener : listeners)
            listener.onSynchronizationStoped();
    }

}
