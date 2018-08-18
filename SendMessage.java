package com.example.user.reminder;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

class send extends AsyncTask<Void,Void,Void> {
    Socket s;
    String data;
    PrintWriter pw;
    @Override
    protected Void doInBackground(Void...params){
        try {
            s = new Socket("192.168.137.14",8000);
            pw = new PrintWriter(s.getOutputStream());
            pw.write(data);
            pw.flush();
            pw.close();
            s.close();
        } catch (UnknownHostException e) {
            System.out.println("Fail");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fail");
            e.printStackTrace();
        }
        return null;
    }
}
