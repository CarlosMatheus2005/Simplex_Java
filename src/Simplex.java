import java.util.Scanner;

public class Simplex {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        //Variáveis para -> x1, x2, ..., xn
        System.out.print("Informe o Número de Variáveis: ");
        int n = sc.nextInt();
        
        //Quantidade de Restricoes <=
        System.out.print("Informe o Número de Restrições: ");
        int m = sc.nextInt();
        System.out.println();

        //Restricoes
        double[][] a = new double[m][n];

        //Valor das restricoes
        double[] b = new double[m];

        //Funcao Objetivo
        double[] z = new double[n];

        //Variaveis de Folga 
        double[][] f = new double[m][m];
                
        //Variaveis de Folga que depois virarao as variaveis normais 
        String[] var = new String[m];

        //Tabela Tableau (Linhas: restrições + Função Objetivo, Colunas: variaveis + restrições + FO)
        double[][] tableau = new double[m+1][n+m+1];

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
            f[i][i] = 1;
            var[i] = "f"+(i+1);
        }
        System.out.println();

        //Montando o Tableu {
            //Preenchendo a tabela com as restrições
            for(int i = 0; i < m; i++){
                //Coeficientes das variáveis normais
                for(int j = 0; j < n; j++){
                    tableau[i][j] = a[i][j];
                }
                //Variáveis de folga
                    for(int j = 0; j < m; j++){
                    tableau[i][n + j] = f[i][j];
                }
                //Coluna b
                tableau[i][n + m] = b[i];
            }

            //Função objetivo
            for(int j = 0; j < n; j++){
                tableau[m][j] = -z[j]; //Tranformando em negativo para PADRONIZAR
            }

            //Folgas da função objetivo = 0
            for(int j = 0; j < m; j++){
                tableau[m][n + j] = 0;
            }

            //b da função objetivo = 0
            tableau[m][n + m] = 0;
        //}

        //Mostando a Primeira Tabela 
        System.out.println("Tabela Inicial: ");
        imprimirTableu(tableau, var, n, m);

        //Resultado Final
        resolverTableu(tableau, var, n, m);
    }

    public static void resolverTableu(double[][] tableau, String[] var, int n, int m){

        while(true){
            int colunaPivo = 0;
            double menor = tableau[m][0];

            //Coluna do Menor Pivo
            for(int j = 1; j < n + m; j++){
                if(tableau[m][j] < menor){
                    menor = tableau[m][j]; //Pegando o menor valor da linha de Z
                    colunaPivo = j;
                }
            }

            //Se não existir número negativo no Z:
            //solução ótima encontrada
            if(menor >= 0){
                break;
            }

            //Linha do Menor Pivo
            int linhaPivo = -1;
            double menorRazao = Double.MAX_VALUE;

            for(int i = 0; i < m; i++){
                double elemento = tableau[i][colunaPivo];
                //evita divisão por zero ou negativos
                if(elemento > 0){
                    double razao = tableau[i][n + m] / elemento; //razao = b / pelo valor na coluna do pivo
                    if(razao < menorRazao){
                        menorRazao = razao;
                        linhaPivo = i;
                    }
                }
            }

            //problema ilimitado
            if(linhaPivo == -1){
                System.out.println("Solução ilimitada.");
                return;
            }

            //Muda a variável básica
            if(colunaPivo < n){
                var[linhaPivo] = "x" + (colunaPivo + 1);
            }else{
                var[linhaPivo] = "f" + (colunaPivo - n + 1);
            }

            //Normalizando a linha do pivo
            double pivo = tableau[linhaPivo][colunaPivo];

            for(int j = 0; j < n + m + 1; j++){
                tableau[linhaPivo][j] /= pivo; 
            }

            //Zerando coluna do pivo
            for(int i = 0; i < m + 1; i++){
                if(i != linhaPivo){
                    double fator = tableau[i][colunaPivo];
                    for(int j = 0; j < n + m + 1; j++){
                        tableau[i][j] -= fator * tableau[linhaPivo][j];
                    }
                }
            }
        }
        System.out.println();

        //Tabela Final
        System.out.println("Tabela Final: ");
        imprimirTableu(tableau, var, n, m);
        System.out.println();

        //Solução ótima
        System.out.println("\nSolução ótima encontrada:");
        for(int i = 0; i < m; i++){
            System.out.println(var[i] + " = " + tableau[i][n + m]);
        }
        System.out.println("Z = " + tableau[m][n + m]);
    }
   
    public static void imprimirTableu(double[][] tableau, String[] var, int n, int m){
        System.out.printf("| %-18s ", "Variaveis Basicas");

        //Variaveis
        for(int i = 0; i < n; i++){
            System.out.printf("| %-8s ", "x"+(i+1));
        }

        //Variaveis de folga
        for(int i = 0; i < m; i++){
            System.out.printf("| %-8s ", "f"+(i+1));
        }

        //Coluna de valores das restricoes
        System.out.printf("| %-8s |\n", "b");

        //Linhas das restrições
        for(int i = 0; i < m; i++){
            System.out.printf("| %-18s ", var[i]);
            for(int j = 0; j < n + m + 1; j++){
                System.out.printf("| %-8.2f ", tableau[i][j]);
            }
            System.out.println("|");
        }

        //Linha da função objetivo
        System.out.printf("| %-18s ", "z");

        for(int j = 0; j < n + m + 1; j++){
            System.out.printf("| %-8.2f ", tableau[m][j]);
        }

        System.out.println("|");
    }
}

