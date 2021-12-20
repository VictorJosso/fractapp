package fr.josso.fractales;

import fr.josso.fractales.Graphics.HelloApplication;
import javafx.application.Application;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println("Demo app to test gradle.\n-------------------------------------------------");
        System.out.println(Arrays.toString(args));
        new Thread(() -> Application.launch(HelloApplication.class)).start();
    }
}
