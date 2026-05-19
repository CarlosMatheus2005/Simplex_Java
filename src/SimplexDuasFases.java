import java.util.Scanner;

public class SimplexDuasFases {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        //Variáveis para -> x1, x2, ..., xn
        System.out.print("Informe o Número de Variáveis: ");
        int n = sc.nextInt();
        
        //Quantidade de Restricoes
        System.out.print("Informe o Número de Restrições: ");
        int m = sc.nextInt();
        System.out.println();

        //Restricoes
        double[][] a = new double[m][n];

        //Valor das restricoes
        double[] b = new double[m];

        //Funcao Objetivo
        double[] z = new double[n];

        //Tipo das Restricao
        String[] tipo = new String[m];

        //Contadores
        int qtdFolga = 0;
        int qtdExcesso = 0;
        int qtdArtificial = 0;

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

        //Montando as Restricoes
        for(int i = 0; i < m; i++){
            System.out.println("\nRestrição "+(i+1));

            for(int j = 0; j < n; j++){
                System.out.print("Informe o coeficiente x"+(j+1)+": ");
                a[i][j] = sc.nextDouble();
            }

            System.out.print("Tipo (<=, >=, =): ");
            tipo[i] = sc.next();
            System.out.print("Resultado (b): ");
            b[i] = sc.nextDouble();

            //contagem da quantidade de variaveis auxiliares
            if(tipo[i].equals("<=")){
                qtdFolga++;
            }else if(tipo[i].equals(">=")){
                qtdExcesso++;
                qtdArtificial++;
            }else{
                qtdArtificial++;
            }
        }

        //Matriz das folga, excesso e artificial
        double[][] folga = new double[m][qtdFolga];

        double[][] excesso = new double[m][qtdExcesso];

        double[][] artificial = new double[m][qtdArtificial];

        //variavel basica
        String[] var = new String[m];

        //preenchendo matrizes
        int f = 0;
        int e = 0;
        int art = 0;

        for(int i = 0; i < m; i++){
            if(tipo[i].equals("<=")){
                folga[i][f] = 1;
                var[i] = "f" + (f + 1);
                f++;

            }else if(tipo[i].equals(">=")){
                excesso[i][e] = -1;
                artificial[i][art] = 1;
                var[i] = "a" + (art + 1);
                e++;
                art++;

            }else{
                artificial[i][art] = 1;
                var[i] = "a" + (art + 1);
                art++;
            }
        }

        //Tamanho da tabela
        int totalColunas = n + qtdFolga + qtdExcesso + qtdArtificial + 1;

        //Tableau
        double[][] tableau = new double[m + 1][totalColunas];

        //Montando o Tableu{
            //Preenchendo a tabela com as Restricoes
            for(int i = 0; i < m; i++){
                //variáveis normais
                for(int j = 0; j < n; j++){
                    tableau[i][j] = a[i][j];
                }

                //folga
                for(int j = 0; j < qtdFolga; j++){
                    tableau[i][n + j] = folga[i][j];
                }

                //excesso
                for(int j = 0; j < qtdExcesso; j++){
                    tableau[i][n + qtdFolga + j] = excesso[i][j];
                }

                //artificial
                for(int j = 0; j < qtdArtificial; j++){
                    tableau[i][n + qtdFolga + qtdExcesso + j] = artificial[i][j];
                }

                //resultado
                tableau[i][totalColunas - 1] = b[i];
            }
        //}

        //Funcao Objetivo
        for(int j = 0; j < n; j++){
            tableau[m][j] = -z[j];
        }

        //Mostrando a Primeira Tabela
        System.out.println("Tabela Inicial: ");
        imprimirTableau(tableau, var, n, qtdFolga, qtdExcesso, qtdArtificial);
    }

    public static void imprimirTableau(double[][] tableau, String[] var, int n, int qtdFolga, int qtdExcesso, int qtdArtificial){
        System.out.printf("| %-18s ", "Variaveis Basicas");

        //Variaveis
        for(int i = 0; i < n; i++){
            System.out.printf("| %-8s ", "x" + (i + 1));
        }

        //Variaveis de folga
        for(int i = 0; i < qtdFolga; i++){
            System.out.printf("| %-8s ", "f" + (i + 1));
        }

        //Variaveis de excesso
        for(int i = 0; i < qtdExcesso; i++){
            System.out.printf("| %-8s ", "e" + (i + 1));
        }

        //Variaveis artificial
        for(int i = 0; i < qtdArtificial; i++){
            System.out.printf("| %-8s ", "a" + (i + 1));
        }

        //Coluna de valores das restricoes
        System.out.printf("| %-8s |\n", "b");

        int m = tableau.length - 1;

        //Linhas das restrições
        for(int i = 0; i < m; i++){
            System.out.printf("| %-18s ", var[i]);
            for(int j = 0; j < tableau[0].length; j++){
                System.out.printf("| %-8.2f ", tableau[i][j]);
            }
            System.out.println("|");
        }

        //Linha da função objetivo
        System.out.printf("| %-18s ", "Z");

        for(int j = 0; j < tableau[0].length; j++){
            System.out.printf("| %-8.2f ", tableau[m][j]);
        }

        System.out.println("|");
    }
}