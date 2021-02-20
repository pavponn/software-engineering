package app;

import app2.ApplicationExample2;

public class AppMain {

    public static void main(String[] args) {
        try {
            ApplicationExample app = new ApplicationExample();
            ApplicationExample2 app2 = new ApplicationExample2();

            app.a();
            app2.a();
            app2.b();
            app.b();
            app.c();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
