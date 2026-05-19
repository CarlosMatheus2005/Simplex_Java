import java.util.Scanner;

public class Simplex {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("Informe o Número de Variáveis: ");
        int n = sc.nextInt();
        
        System.out.print("Informe o Número de Restrições: ");
        int m = sc.nextInt();
        System.out.println();

        //Restricoes
        double[][] a = new double[m][n];

        //Valor das restricoes
        double[] b = new double[m];

        //Funcao Objetivo
        double[] z = new double[n];


        //Montando a Função Objetivo (z)
        for(int i = 0; i < n; i++){
            System.out.print("Informe o coeficiente x"+(i+1)+": ");
            z[i] = sc.nextDouble();
        }

        System.out.print("Função objetivo: z = ");
        for(int i = 0; i < n; i++){
            if(i == n-1){
                System.out.print("("+z[i]+")x"+(i+1));
            }else{
                System.out.print("("+z[i]+")x"+(i+1)+" + ");    
            } 
        }
        System.out.println();

        //Montando as Restrições
        for(int i = 0; i < m; i++){
            System.out.println("\nRestrição "+(i+1)); 

            for(int j = 0; j < n; j++){
                System.out.print("Informe o coeficiente x"+(j+1)+": ");
                a[i][j] = sc.nextDouble();
            }

            System.out.print("Resultado (b): ");
            b[i] = sc.nextDouble();
        }

    }
}
