import crypto.CryptoManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n========= MENÚ PRINCIPAL =========");
            System.out.println("1. Cifrar archivo");
            System.out.println("2. Descifrar archivo");
            System.out.println("0. Salir");
            System.out.print("Elija una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print("Ingrese el nombre del archivo: ");
                    String archivo = scanner.nextLine();
                    System.out.print("Ingrese una contraseña: ");
                    String password = scanner.nextLine();
                    try {
                        CryptoManager.encryptFile(archivo, password);
                    } catch (Exception e) {
                        System.out.println("Error al cifrar el archivo: " + e.getMessage());
                    }
                    break;
                case "2":
                    System.out.print("Ingrese el nombre del archivo cifrado: ");
                    String archivoCifrado = scanner.nextLine();
                    System.out.print("Ingrese su contraseña: ");
                    String password2 = scanner.nextLine();
                    try {
                        CryptoManager.decryptFile(archivoCifrado, password2);
                    } catch (Exception e) {
                        System.out.println("Error al descifrar el archivo: " + e.getMessage());
                    }
                    break;
                case "0":
                    System.out.println("Programa finalizado.");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
}
