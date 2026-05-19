import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("Informe o Número de Variáveis: ");
        int var = sc.nextInt();   

        System.out.println("Informe o Número de Restrições: ");
        int rest = sc.nextInt();

        double[][] A = new double[rest][var];
        double[] b = new double[var];
        double[] c = new double[var];

    }
}
