package com.example.user.reminder;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/*
Declares a class named 'send' that extends AsyncTask.
AsyncTask enables proper and easy use of the UI thread. This class allows you to perform background operations
and publish results on the UI thread without having to manipulate threads and handlers.
<Void, Void, Void>: These are the types used by the AsyncTask:
1st Void: The type of the parameters sent to the task upon execution (not used here).
2nd Void: The type of the progress units published during the background computation (not used here).
3rd Void: The type of the result of the background computation (not used here, returns null).
*/
class send extends AsyncTask<Void,Void,Void> {
    Socket s; // Declares a Socket variable, which will represent the connection to the server.
    String data; // Declares a String variable to hold the data to be sent over the socket.
    PrintWriter pw; // Declares a PrintWriter variable, used to write data to the socket's output stream.

    @Override 
    // Annotation indicating that this method overrides a method from the AsyncTask superclass.
    // This method runs on a background thread. It performs the long-running operation (socket communication).
    // params: The parameters of the task (not used here, hence Void...).
    protected Void doInBackground(Void...params){
        try { 
            // Starts a try block to catch potential exceptions during socket operations.
            // Creates a new Socket and attempts to connect to the specified IP address ("192.168.137.14") and port (8000).
            s = new Socket("192.168.137.14",8000);

            // Creates a PrintWriter wrapped around the socket's output stream.
            // This allows writing text data to the socket.
            pw = new PrintWriter(s.getOutputStream());

            // Writes the content of the 'data' string to the output stream.
            pw.write(data);

            // Flushes the output stream, ensuring that any buffered data is sent immediately.
            pw.flush();

            // Closes the PrintWriter. This also closes the underlying output stream.
            pw.close();
            
            // Closes the socket connection.
            s.close();
        } catch (UnknownHostException e) { // Catches the UnknownHostException if the host IP address cannot be resolved.
            System.out.println("Fail"); // Prints "Fail" to the standard output.
            e.printStackTrace(); // Prints the stack trace of the exception for debugging.
        } catch (IOException e) { // Catches the IOException if an I/O error occurs during socket communication.
            System.out.println("Fail"); // Prints "Fail" to the standard output.
            e.printStackTrace(); // Prints the stack trace of the exception for debugging.
        }
        return null; // Returns null as the result of the background operation.
    }
}
