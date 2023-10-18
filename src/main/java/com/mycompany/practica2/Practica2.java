package com.mycompany.practica2;

import java.io.*;
import java.util.*;

/**
 * Complejidad Computacional 2024-1
 * Profesor: Oscar Hernández Constantino	
 * Ayudante: Malinali Gónzalez Lara
 * 
 * Practica 2
 * 
 * @author Rosales Jaimes Victor 
 * @author Lucio Rangel Juan Manuel
 * @version 3.7
 *
 */
public class Practica2 {

    /**
    * Metodo main.- Menu principal que nos permite realizar la generacion de 
    * certificados, pasandole los datos necesarios.
    * @param args .- Nombre del archivo de entrada con la grafica y nombre del 
    * archivo de salida con el certificado.
    */
    public static void main(String[] args) throws IOException{
        /*args = new String[2];
        args[0] = "Ejemplo1.txt"; // Codigo auxiliar para pruebas rapidas
        args[1] = "Certificado1.txt";/*/
        Practica2 p = new Practica2();
        p.generaCertificados(args);
        p.verificaCertificados(args);
    }
    
    /**
    * Metodo generaCertificados.- Metodo que implementa el algoritmo definido
    * en el documento de la practica para generar el certificado aleatorio.
    * @param args.- Nombre del archivo de entrada con la grafica y nombre del 
    * archivo de salida con el certificado.
    */
    public void generaCertificados(String[] args) {
        if (args.length != 2) {
            System.out.println("Uso: java GeneradorCertificados <archivoEntrada> <archivoSalida>");
            return;
        }

        String archivoEntrada = args[0];
        String archivoSalida = args[1];

        try {
            ArrayList<Integer> vertices = null;
            try (BufferedReader br = new BufferedReader(new FileReader(archivoEntrada))){
                String linea;
                vertices = new ArrayList<>();
                
                // Leer el archivo de entrada y extraer los vértices
                while ((linea = br.readLine()) != null) {
                    if (linea.startsWith("k:")) {
                        int k = Integer.parseInt(linea.split(":")[1].trim());
                        
                        // Seleccionar k vértices aleatorios
                        Random rand = new Random();
                        while (vertices.size() < k) {
                            int vertice = rand.nextInt(k) + 1;
                            if (!vertices.contains(vertice)) {
                                vertices.add(vertice);
                            }
                        }
                        break;
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            
            // Escribir los vértices seleccionados en el archivo de salida
            try ( 
                BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida))) {
                bw.write("[");
                for (int i = 0; i < vertices.size(); i++) {
                    bw.write(vertices.get(i).toString());
                    if (i < vertices.size() - 1) {
                        bw.write(",");
                    }
                }
                bw.write("]");
            }
            
            //Verificacion de que el proceso fue exitoso
            System.out.println("Certificado generado y guardado en " + archivoSalida);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
    * Metodo verificaaCertificados.- Metodo que implementa el algoritmo definido en
    * el documento de la practica para verificar el certificado dado en un ejemplar.
    *  archivoEntrada, String archivoCertificado
    * @param args.- Nombre del archivo de entrada con la grafica y nombre del 
    * archivo de salida con el certificado.
    */
    public void verificaCertificados(String[] args) {
        if (args.length != 2) {
            System.out.println("Uso: java GeneradorCertificados <archivoEntrada> <archivoSalida>");
            return;
        }

        String archivoEntrada = args[0];
        String archivoCertificado = args[1];
        System.out.println("FUNCIONA 1");
        try {
            // Leer el archivo de entrada y obtener la información de la gráfica
            List<String> grafica = leerArchivo(archivoEntrada);
            int k = obtenerValorK(grafica);
            Set<Integer> conjuntoUniverso = obtenerVertices(grafica);
            List<Integer> secuenciaVertices = leerCertificado(archivoCertificado);

            // Verificar que todos los vértices en la secuencia estén en V
            for (int v : secuenciaVertices) {
                if (!conjuntoUniverso.contains(v)) {
                    System.out.println("Error: El vértice " + v + " no está en el conjunto universo.");
                    invalido(k, conjuntoUniverso);
                    return;
                }
            }

            // Verificar que cada par de vértices adyacentes en la secuencia también sean adyacentes en G
            
            // AQUI ES DONDE ESTA EL PROBLEMA YA QUE LA VERIFICACION FALLA Y DA QUE ES INVALIDO SIEMPRE
            // ESTO ES POR EL METODO sonAdyacentes.
            for (int i = 0; i < secuenciaVertices.size() - 1; i++) {
                int v1 = secuenciaVertices.get(i);
                int v2 = secuenciaVertices.get(i + 1);
                if (!sonAdyacentes(grafica, v1, v2)) {
                    System.out.println("Error: Los vértices " + v1 + " y " + v2 + " no son adyacentes en la gráfica.");
                    invalido(k, conjuntoUniverso);
                    return;
                }
            }
            // Verificar que la secuencia de vértices sea una ruta simple
            if (!esRutaSimple(secuenciaVertices)){
                System.out.println("Error: La secuencia de vértices no forma una ruta simple.");
                invalido(k, conjuntoUniverso);
                return;
            }

                // El certificado es válido
                System.out.println("El certificado es válido y resuelve el problema de ruta inducida.");
                System.out.println("Número de elementos en el conjunto universo (S): " + conjuntoUniverso.size());
                System.out.println("Número de subconjuntos en C: " + calcularNumeroSubconjuntos(k, conjuntoUniverso.size()));
                System.out.println("Valor de K: " + k);
                System.out.println("Respuesta: SÍ");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
    * Metodo invalido.- Metodo que devuelve los datos cuando el certificado 
    * no resuelve el problema en el ejemplar dado.
    * @param k
    * @param conjuntoUniverso
    */
    private void invalido(int k, Set<Integer> conjuntoUniverso){
        System.out.println("El certificado no es válido y no resuelve el problema de ruta inducida.");
        System.out.println("Número de elementos en el conjunto universo (S): " + conjuntoUniverso.size());
        System.out.println("Número de subconjuntos en C: " + calcularNumeroSubconjuntos(k, conjuntoUniverso.size()));
        System.out.println("Valor de K: " + k);
        System.out.println("Respuesta: NO");
    }

    /**
    * Metodo obtenerValorK.- Metodo que obtiene el numero minimo de vertices
    * en la ruta (k).
    * @param grafica.- Ejemplar dado
    */
    private int obtenerValorK(List<String> grafica) {
        for (String linea : grafica){
            if (linea.startsWith("k: ")) {
                return Integer.parseInt(linea.split(":")[1].trim());
            }
        }
        return -1; // Valor por defecto si no se encuentra k en el archivo
    }

    /**
    * Metodo obtenerVertices.- Metodo que obtiene el conjunto de vertices de la
    * grafica.
    * @param grafica.- Ejemplar dado
    */
    private Set<Integer> obtenerVertices(List<String> grafica) {
        Set<Integer> conjuntoVertices = new HashSet<>();
        for (String linea : grafica) {
            if (!linea.startsWith("k:")) {
                int vertice = Integer.parseInt(linea.split(":")[0].trim());
                conjuntoVertices.add(vertice);
            }
        }
        return conjuntoVertices;
    }

    /**
    * Metodo leerCertificado.- Metodo que nos permite obtener el certificado dado.
    * @param archivoCertificado.- Nombre del archivo con el certificado.
    **/
    private List<Integer> leerCertificado(String archivoCertificado) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(archivoCertificado));
        String linea = br.readLine();
        br.close();

        // Extraer la secuencia de vértices del certificado
        String secuencia = linea.substring(1, linea.length() - 1);
        String[] verticesStr = secuencia.split(",");
        List<Integer> secuenciaVertices = new ArrayList<>();
        for (String verticeStr : verticesStr){
            secuenciaVertices.add(Integer.parseInt(verticeStr.trim()));
        }

        return secuenciaVertices;
    }

    /**
    * Metodo sonAdyacentes.- Metodo que verifica que 2 vertices sean adyacente en
    * la grafica.
    * @param grafica.- Ejemplar dado.
    * @param v1.- Vertice 1 
    * @param v2.- Vertice 2
    */
    private boolean sonAdyacentes(List<String> grafica, int v1, int v2) {
        for (String linea : grafica) {
            if (!linea.startsWith("k:")) {
                String verticeStr = linea.split(":")[1].trim();
                
                // Elimina los corchetes iniciales y finales, que encierran la lista de adyacencias
                verticeStr = verticeStr.replaceAll("^\\[|\\]$", ""); 
                String[] adyacencias = verticeStr.split(",");
                
                /* 
                
                //Con este codigo se puede ver el error, ya que el arreglo adyacencias
                deberia tener elementos de la siguiente forma:
                
                adyacencias[1]=(1,2)
                adyacencias[2]=(1,3)
                ...
                
                Pero regresa en su lugar:
                adyacencias[1]=(1
                adyacencias[2]=2)
                etc, lo cual causa error en la comparacion posterior, asi que lo que
                deberiamos hacer seria encontrar una forma de que adyacencias tenga el
                formato que queremos, pero no se como hacerlo.
                
                System.out.println("Imprime las adyacencias");
                for (String ad : adyacencias){
                    System.out.println(ad);
                }
                */

                for (String adyacencia : adyacencias) {
                    String[] vertices = adyacencia.split("\\)\\(");
                    
                    if (vertices.length == 2) {
                        int verticeA = Integer.parseInt(vertices[0]);
                        int verticeB = Integer.parseInt(vertices[1]);
                        if ((verticeA == v1 && verticeB == v2) || (verticeA == v2 && verticeB == v1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**     
    * Metodo esRutaSimple.- Metodo que verifica que la secuencia de vertices dada
    * es una ruta simple.
    * @param secuenciaVertices.- 
    */
    private boolean esRutaSimple(List<Integer> secuenciaVertices) {
        Set<Integer> conjuntoVistos = new HashSet<>();
        for (int vertice : secuenciaVertices){
            if (conjuntoVistos.contains(vertice)) {
                return false; // El vértice ya ha sido visitado
            }
            conjuntoVistos.add(vertice);
        }
        return true;
    }

    /**
    * Metodo calcularNumeroSubconjuntos.- Metodo para obtener el numero de subconjuntos.
    * @param k.- tamaño de los subconjuntos de C.
    * @param n.- tamaño del conjunto C.
    */
    private int calcularNumeroSubconjuntos(int k, int n) {
        // Calcula el número de subconjuntos de tamaño k de un conjunto de tamaño n
        if (k <= 0 || k > n) {
            return 0;
        }
        int numerador = 1;
        int denominador = 1;
        for (int i = n; i > n - k; i--) {
            numerador *= i;
        }
        for (int i = k; i > 0; i--) {
            denominador *= i;
        }
        return numerador / denominador;
    }

    /**
    * Metodo leerArchivo.- Metodo para obtener la grafica dado el archivo con
    * el ejemplar.
    * @param archivoEntrada.- Nombre del archivo con el ejemplar.
    */
    private List<String> leerArchivo(String archivoEntrada) throws IOException {
    List<String> lineas = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(archivoEntrada))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            lineas.add(linea);
        }
    }
    return lineas;
}

}
