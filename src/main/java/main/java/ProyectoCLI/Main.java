package main.java.ProyectoCLI;

import ProyectoCLI.CLI.Menu;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner mainScanner = new Scanner(System.in);
        Menu mainMenu = new Menu(mainScanner);
        System.out.println("Bienvenido al Sistema de Gestion CLI");
        mainMenu.start();
        mainScanner.close();
    }
}
