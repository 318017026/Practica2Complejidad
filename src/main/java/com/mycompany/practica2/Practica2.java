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
 * @version 1.0
 *
 */
public class Practica2 {

    /**
    * Metodo main.- Menu principal que nos permite realizar la generacion de 
    * certificados, pasandole los datos necesarios.
    * @param args .- Nombre del archivo de entrada con la grafica y nombre del 
    * archivo de salida con el certificado.
    */
    public static void main(String[] args){
        Practica2 p = new Practica2();
        p.generaCertificados(args);
    }
    
    /**
    * Metodo generaCertificados.- Metodo que implementa el algoritmo definido
    * en el documento de la practica para generar el certificado aleatorio.
    * @param args .- Nombre del archivo de entrada con la grafica y nombre del 
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
}
