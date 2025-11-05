/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp_10_berea;

/**
 *
 * @author gabiv
 */
import java.sql.*;



/**
 *
 * @author gabiv
 */
public class Conexion {
    Connection conectar = null;
    String user = "ur17dnifuzljxy9v";
    String passw = "yVwxk5xvETi1uNrSWLru";
    String bd = "b6mmmvfh0w9e8t0fpwpk";
    String ip="b6mmmvfh0w9e8t0fpwpk-mysql.services.clever-cloud.com";
    String puerto ="3306";
    String cadena = "jdbc:mysql://"+ip+":"+puerto+"/"+bd;
    
    private static Conexion instancia;
    
    public Connection establecerConexion(){
        try{
            //Class.forName("com.mysql.jdbc.Driver");
            conectar = DriverManager.getConnection(cadena, user, passw);
            System.out.println("Se conecto a la BD");
        }catch(Exception e){
            System.out.println("error " + e);
        }
        
        return conectar;
    }
    
    public void cerrarConexion(){
        try{
            if(conectar!=null && !conectar.isClosed()){
                conectar.close();
                System.out.println("Se cerro la conexion");
            }
        }catch(Exception e){
            System.out.println("error " + e);
        }
    }
    
    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    
    public Connection getConexion() {
        return conectar;
    }
    
}
