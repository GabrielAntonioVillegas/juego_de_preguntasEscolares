/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp_10_berea;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;
import javax.swing.SwingUtilities;

public class Juego {
    public ArrayList<String[]> matrizDatos = new ArrayList<>();
    
    
    public void hacerPregunta(JLabel label_pregunta, JButton boton1,JButton boton2,JButton boton3,JButton boton4){
        if(matrizDatos.isEmpty()){
            llamarPreguntas();
        }
        
        String[] datos = elegirPregunta();
        
        String pregunta_completa;
        pregunta_completa = "Segun el (Portfolio  " + datos[0] + ")  del/la compañero/a  (" + datos[1] + ")  del tema  (" + datos[2] + ")" + "\n" +" Responda: " + datos[3];
        String textoConFormato = "<html><p style=\"width:500px; text-align: center;\">" + pregunta_completa.replace("\n","<br>") + "</p></html>";
        label_pregunta.setText(textoConFormato);
        
        String boton1_formato = "<html><p style=\"width:250px; text-align: center;\">" + datos[4] + "</p></html>"; 
        String boton2_formato = "<html><p style=\"width:250px; text-align: center;\">" + datos[5] + "</p></html>"; 
        String boton3_formato = "<html><p style=\"width:250px; text-align: center;\">" + datos[6] + "</p></html>"; 
        String boton4_formato = "<html><p style=\"width:250px; text-align: center;\">" + datos[7] + "</p></html>";
        
        ArrayList<JButton> botones = new ArrayList<>();
        botones.add(boton1);
        botones.add(boton2);
        botones.add(boton3);
        botones.add(boton4);

        Collections.shuffle(botones);

        botones.get(0).setText(boton1_formato); 
        botones.get(1).setText(boton2_formato); 
        botones.get(2).setText(boton3_formato); 
        botones.get(3).setText(boton4_formato); 
    }
    
    public String[] elegirPregunta(){
        Random r = new Random();
        int indice = r.nextInt(matrizDatos.size());  
        String[] elegido = matrizDatos.get(indice);
        matrizDatos.remove(indice);
        
        return elegido;
    }
    
    public void llamarPreguntas(){
        Conexion conex = new Conexion();
        
        String sql = "WITH resp AS (SELECT r.id_pregunta, r.titulo_respuesta, r.es_correcta, ROW_NUMBER() OVER (PARTITION BY r.id_pregunta, r.es_correcta ORDER BY r.id_respuesta) AS rn FROM Respuesta r), pivot AS (SELECT p.id_portfolio, e.nombre, e.apellido, p.titulo AS titulo_portfolio, pr.titulo_pregunta, MAX(CASE WHEN r.es_correcta = 1 THEN r.titulo_respuesta END) AS respuestaCorrecta, MAX(CASE WHEN r.es_correcta = 0 AND r.rn = 1 THEN r.titulo_respuesta END) AS opcion2, MAX(CASE WHEN r.es_correcta = 0 AND r.rn = 2 THEN r.titulo_respuesta END) AS opcion3, MAX(CASE WHEN r.es_correcta = 0 AND r.rn = 3 THEN r.titulo_respuesta END) AS opcion4 FROM Portfolio p JOIN Estudiante e ON p.id_estudiante = e.id_estudiante JOIN Pregunta pr ON p.id_portfolio = pr.id_portfolio JOIN resp r ON pr.id_pregunta = r.id_pregunta GROUP BY p.id_portfolio, e.nombre, e.apellido, p.titulo, pr.titulo_pregunta) SELECT * FROM pivot;";
        
        try{
            Statement st = conex.establecerConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String[] registro = new String[8];
                
                registro[0] = rs.getString("id_portfolio");
                registro[1] = rs.getString("nombre") +" " + rs.getString("apellido");
                registro[2] = rs.getString("titulo_portfolio");
                registro[3] = rs.getString("titulo_pregunta");
                registro[4] = rs.getString("respuestaCorrecta");
                registro[5] = rs.getString("opcion2");
                registro[6] = rs.getString("opcion3");
                registro[7] = rs.getString("opcion4");

                matrizDatos.add(registro);
            }
        }catch(Exception e){
            System.out.println("Problema en consultar la BD " + e);
        }finally{
            conex.cerrarConexion();
        }
        
    }
}
