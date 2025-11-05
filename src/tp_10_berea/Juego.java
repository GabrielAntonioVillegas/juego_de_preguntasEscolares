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
    private ArrayList<Integer> preguntasRealizadas = new ArrayList<Integer>();;
   
    public String llamarPreguntas(JLabel label_pregunta, JButton boton1, JButton boton2, JButton boton3, JButton boton4){
        Conexion conex = new Conexion();
        int portfolioElegido = generarNumeroRandom(preguntasRealizadas);
        String sql1 = "";
        String sql2 = "";
        String respuestaCorrecta = "";
        String textoConFormato = "";
        String boton1_formato = "";
        String boton2_formato = "";
        String boton3_formato = "";
        String boton4_formato = "";
        
        
        sql1= "SELECT E.nombre, E.apellido, P.titulo AS titulo_portfolio, PRE.titulo_pregunta FROM Estudiante E JOIN Portfolio P ON E.id_estudiante = P.id_estudiante JOIN Pregunta PRE ON P.id_portfolio = PRE.id_portfolio WHERE P.id_portfolio = ?;";
        sql2= "SELECT PRE.id_pregunta,(SELECT titulo_respuesta FROM Respuesta R2 WHERE R2.id_pregunta = PRE.id_pregunta ORDER BY R2.es_correcta DESC, R2.id_respuesta LIMIT 1 OFFSET 0) AS respuesta1,(SELECT titulo_respuesta FROM Respuesta R2 WHERE R2.id_pregunta = PRE.id_pregunta ORDER BY R2.es_correcta DESC, R2.id_respuesta LIMIT 1 OFFSET 1) AS respuesta2,(SELECT titulo_respuesta FROM Respuesta R2 WHERE R2.id_pregunta = PRE.id_pregunta ORDER BY R2.es_correcta DESC, R2.id_respuesta LIMIT 1 OFFSET 2) AS respuesta3,(SELECT titulo_respuesta FROM Respuesta R2 WHERE R2.id_pregunta = PRE.id_pregunta ORDER BY R2.es_correcta DESC, R2.id_respuesta LIMIT 1 OFFSET 3) AS respuesta4 FROM Pregunta PRE WHERE PRE.id_portfolio = ?;";
        
        
        try{
            PreparedStatement ps = conex.establecerConexion().prepareStatement(sql1);
            ps.setInt(1, portfolioElegido);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String nom = rs.getString("nombre");
                String ape = rs.getString("apellido");
                String portfolio = rs.getString("titulo_portfolio");
                String pregunta = rs.getString("titulo_pregunta");
                
                String pregunta_completa;
                pregunta_completa = "Segun el (Portfolio  " + portfolioElegido + ")  del/la compañero/a  (" + nom + " " + ape + ")  del tema  (" + portfolio + ")" + "\n" +" Responda: " + pregunta;

                textoConFormato = "<html><p style=\"width:500px; text-align: center;\">" + pregunta_completa.replace("\n","<br>") + "</p></html>";
                
            }
                
            PreparedStatement ps2 = conex.establecerConexion().prepareStatement(sql2); 
            ps2.setInt(1, portfolioElegido); ResultSet rs2 = ps2.executeQuery(); 
            while(rs2.next()){ 
                respuestaCorrecta = rs2.getString("respuesta1"); 
                String correcta = rs2.getString("respuesta1");     
                String opcion2 = rs2.getString("respuesta2"); 
                String opcion3 = rs2.getString("respuesta3"); 
                String opcion4 = rs2.getString("respuesta4"); 
                
                boton1_formato = "<html><p style=\"width:250px; text-align: center;\">" + correcta + "</p></html>"; 
                boton2_formato = "<html><p style=\"width:250px; text-align: center;\">" + opcion2 + "</p></html>"; 
                boton3_formato = "<html><p style=\"width:250px; text-align: center;\">" + opcion3 + "</p></html>"; 
                boton4_formato = "<html><p style=\"width:250px; text-align: center;\">" + opcion4 + "</p></html>";
                
            } 
            label_pregunta.setText(textoConFormato); 
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
            
        }catch(Exception e){ 
            System.out.println("Error: " + e);
        }finally{ 
            conex.cerrarConexion();
        } 
        return respuestaCorrecta; }
    
    public int generarNumeroRandom(ArrayList preguntasRealizadas){
        Conexion conex = new Conexion();
        String sql = "SELECT id_portfolio FROM Portfolio;";
        ArrayList<Integer> ides = new ArrayList<>();

        try {
            Statement st = conex.establecerConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                ides.add(rs.getInt("id_portfolio"));
            }
        } catch (Exception e) {
            System.out.println("error en generacionNumeroRandom" + e);
        } finally {
            conex.cerrarConexion();
        }

        Random r = new Random();
        int indice = r.nextInt(ides.size());  
        int elegido = ides.get(indice);

        while (preguntasRealizadas.contains(elegido)) {   
            indice = r.nextInt(ides.size());
            elegido = ides.get(indice);
        }

        preguntasRealizadas.add(elegido);   
        return elegido;
        
    }
    
}
