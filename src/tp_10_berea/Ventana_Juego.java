package tp_10_berea;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.*;
import javax.swing.SwingWorker;
import javax.swing.Timer;

public class Ventana_Juego extends javax.swing.JFrame {
    
    private TreeMap<String, String> datosPartida; //Para guardar los datos de la partida
    private String usuario;                       //para guardar el nombre de usuario 
    private Juego juego;                          //para instanciar la clase juego    
    
    private String respuestaSeleccionada = "";
    private String respuestaCorrecta = "";        //para guardar la pregunta correcta;
    private int contadorPreguntas = 0;            //para el contador de preguntas
    private int contador = 0;                     //para el temporizador
    
    private Color colorRojo;
    private Color colorAzul;
    private Color colorAmarillo;
    private Color colorVerde;
    
    int tiempo = 30;
    private Timer timer;
    
    public Ventana_Juego(String _usuario) {
        initComponents();
        usuario = _usuario;
       
        colorRojo = jButton1.getBackground();
        colorAzul = jButton2.getBackground();
        colorAmarillo = jButton3.getBackground();
        colorVerde = jButton4.getBackground();
        
        datosPartida = new TreeMap<>();
        datosPartida.put("puntaje","0");
        datosPartida.put("correctas","0");
        datosPartida.put("incorrectas","0");
        datosPartida.put("usuario",_usuario);
            
        label_jugador.setText("JUGADOR: "+ datosPartida.get("usuario"));
        label_puntos.setText("PUNTOS: "+ datosPartida.get("puntaje"));
        
        label_contenedor_check.setVisible(false);
        label_contenedor_resultado.setVisible(false);
        
        juego = new Juego();
        realizarPregunta(juego,_usuario);
    }
    public void realizarPregunta(Juego juego,String _usuario){
        if(contadorPreguntas == 0){
            usoPaneles(1);
        }
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                respuestaCorrecta = juego.hacerPregunta(label_pregunta,jButton1,
                        jButton2,jButton3,jButton4);
                contadorPreguntas++;
                return null;
            }

            @Override
            protected void done() {
                usoPaneles(2);
                usoPaneles(3);
                temporizador();
            }
        };

        worker.execute();   
    }
    
    public void usoPaneles(int seleccion){
        //MOSTRAR PANEL DE CARGA
        if(seleccion == 1){
            panel_partida.setVisible(false);
            panelCarga.setVisible(true);
            jProgressBar1.setVisible(true);
            label_cargando.setVisible(true);
            jProgressBar1.setIndeterminate(true); 
        }
        //OCULTAR PANEL DE CARGA
        else if(seleccion == 2){
            jProgressBar1.setIndeterminate(false);
            jProgressBar1.setVisible(false);
            panelCarga.setVisible(false);
        }
        //MOSTRAR PANEL DE PARTIDA
        else if(seleccion == 3){
            jButton1.setBackground(colorRojo);
            jButton2.setBackground(colorAzul);
            jButton3.setBackground(colorAmarillo);
            jButton4.setBackground(colorVerde);
            
            label_fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/aula.png")));
            panel_partida.setVisible(true);
            bontonSiguiente.setVisible(false);
            
            label_contenedor_temporizador.setVisible(true);
            label_temporizador.setVisible(true);
            label_temporizador.setText("0");
            
            label_contador_preguntas.setText("PREGUNTA: "+contadorPreguntas+"/20");
            panel_contenedor_pregunta.setVisible(true);
            
            label_contenedor_check.setVisible(false);
            label_contenedor_resultado.setVisible(false);
            
            label_jugador.setVisible(true);
            label_puntos.setVisible(true);
            label_contador_preguntas.setVisible(true);
            
        }      
        //MOSTRAR PANEL RESPUESTA 
        else if(seleccion == 4){
            label_fondo.setIcon(new javax.swing.ImageIcon(getClass().
                    getResource("/img/aula_violeta.png")));
            label_contenedor_temporizador.setVisible(false);
            label_temporizador.setVisible(false);
            bontonSiguiente.setVisible(true);
            
            label_jugador.setVisible(false);
            label_puntos.setVisible(false);
            label_contador_preguntas.setVisible(false);
            
            contador=0;
            
            ArrayList<JButton> botones = new ArrayList<>();
            botones.add(jButton1);
            botones.add(jButton2);
            botones.add(jButton3);
            botones.add(jButton4);

            for (int i = 0; i < botones.size(); i++) {
                String textoBoton = botones.get(i).getText().replaceAll("<[^>]*>", "").trim();

                if (!textoBoton.equalsIgnoreCase(respuestaCorrecta.trim())) {
                    botones.get(i).setBackground(Color.gray);
                }
            }
        }
        //RESPUESTA MAL
        else if(seleccion == 5){
            panel_contenedor_pregunta.setVisible(false);
            label_contenedor_check.setVisible(true);
            label_contenedor_check.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/redCheck.png")));
            label_contenedor_resultado.setVisible(true);
            label_contenedor_resultado.setText("<html><p style='text-align:center;'>INCORRECTO<br>(-1)</p></html>");
        }
        //RESPUESTA BIEN
        else if(seleccion == 6){
            panel_contenedor_pregunta.setVisible(false);
            label_contenedor_check.setVisible(true);
            label_contenedor_check.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/checkVerde.png")));
            label_contenedor_resultado.setVisible(true);
            label_contenedor_resultado.setText("<html><p style='text-align:center;'>CORRECTO<br>(+1)</p></html>");
        }
    }
    public void compararRespuesta(JButton boton){
        if(timer != null) { 
            timer.stop();
        }
        respuestaSeleccionada = boton.getText().replaceAll("<[^>]*>", "").trim();  
        if(respuestaSeleccionada.equalsIgnoreCase(respuestaCorrecta.trim())){
            validarRespuesta(1);
            usoPaneles(4);
            usoPaneles(6);
        }else{
            validarRespuesta(0);
            usoPaneles(4);
            usoPaneles(5);
        }
    }  
    public void validarRespuesta(int suceso){
        //SUCESO EN 0 SIGNIFICA QUE EL JUGADOR SE QUEDO SIN TIEMPO Y NO CONTESTO O RESPONDIÓ MAL
        if(suceso==0){
            int aux = Integer.parseInt(datosPartida.get("puntaje"));
            aux--;
            String p = ""+aux;
            datosPartida.put("puntaje", p);
            
            int aux2 = Integer.parseInt(datosPartida.get("incorrectas"));
            aux2++;
            String p2 = ""+aux2;
            datosPartida.put("incorrectas", p2);
        }
        //SUCESO EN 1 SIGNIFICA QUE EL JUGADOR RESPONDIO BIEN
        else if(suceso==1){
            int aux = Integer.parseInt(datosPartida.get("puntaje"));
            aux++;
            String p = ""+aux;
            datosPartida.put("puntaje", p);
            
            int aux2 = Integer.parseInt(datosPartida.get("correctas"));
            aux2++;
            String p2 = ""+aux2;
            datosPartida.put("correctas", p2);
        
        }
        
        label_puntos.setText("PUNTOS: "+ datosPartida.get("puntaje"));
    }
    
    public void temporizador() {
        if (timer != null) {
            timer.stop();
        }
        contador = 0; 
        timer = new Timer(1000, e -> {
            if (contador >= tiempo) {
                timer.stop();
                usoPaneles(4);
                usoPaneles(5);
                if (respuestaSeleccionada.equals("")) {
                    validarRespuesta(0);
                }
            } else {
                contador++;
                label_temporizador.setText(String.valueOf(contador));
            }
        });
        timer.start();
    }
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCarga = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        label_cargando = new javax.swing.JLabel();
        panel_partida = new javax.swing.JPanel();
        label_contador_preguntas = new javax.swing.JLabel();
        label_temporizador = new javax.swing.JLabel();
        label_jugador = new javax.swing.JLabel();
        label_contenedor_temporizador = new javax.swing.JLabel();
        label_puntos = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        bontonSiguiente = new javax.swing.JButton();
        label_contenedor_check = new javax.swing.JLabel();
        label_contenedor_resultado = new javax.swing.JLabel();
        panel_contenedor_pregunta = new javax.swing.JPanel();
        label_pregunta = new javax.swing.JLabel();
        label_fondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelCarga.setBackground(new java.awt.Color(204, 204, 204));
        panelCarga.setForeground(new java.awt.Color(204, 204, 204));

        jProgressBar1.setForeground(new java.awt.Color(63, 17, 170));

        label_cargando.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        label_cargando.setForeground(new java.awt.Color(0, 0, 0));
        label_cargando.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_cargando.setText("CARGANDO");

        javax.swing.GroupLayout panelCargaLayout = new javax.swing.GroupLayout(panelCarga);
        panelCarga.setLayout(panelCargaLayout);
        panelCargaLayout.setHorizontalGroup(
            panelCargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCargaLayout.createSequentialGroup()
                .addContainerGap(152, Short.MAX_VALUE)
                .addGroup(panelCargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                    .addComponent(label_cargando, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(147, 147, 147))
        );
        panelCargaLayout.setVerticalGroup(
            panelCargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCargaLayout.createSequentialGroup()
                .addGap(155, 155, 155)
                .addComponent(label_cargando, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(244, Short.MAX_VALUE))
        );

        panel_partida.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_contador_preguntas.setFont(new java.awt.Font("Roboto Medium", 1, 18)); // NOI18N
        label_contador_preguntas.setForeground(new java.awt.Color(254, 254, 254));
        label_contador_preguntas.setText("PREGUNTA: ");
        panel_partida.add(label_contador_preguntas, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 360, 30));

        label_temporizador.setFont(new java.awt.Font("Roboto Black", 0, 24)); // NOI18N
        label_temporizador.setForeground(new java.awt.Color(255, 255, 255));
        label_temporizador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_temporizador.setText("0");
        panel_partida.add(label_temporizador, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 60, 90));

        label_jugador.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        label_jugador.setForeground(new java.awt.Color(255, 255, 255));
        label_jugador.setText("JUGADOR: ");
        panel_partida.add(label_jugador, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 360, 30));

        label_contenedor_temporizador.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        label_contenedor_temporizador.setForeground(new java.awt.Color(255, 255, 255));
        label_contenedor_temporizador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/temporizador.png"))); // NOI18N
        panel_partida.add(label_contenedor_temporizador, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 80, 90));

        label_puntos.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        label_puntos.setForeground(new java.awt.Color(255, 255, 255));
        label_puntos.setText("PUNTOS: ");
        panel_partida.add(label_puntos, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 360, 30));

        jButton4.setBackground(new java.awt.Color(38, 137, 12));
        jButton4.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("PREGUNTA 4");
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        panel_partida.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 390, 350, 70));

        jButton3.setBackground(new java.awt.Color(216, 158, 0));
        jButton3.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("PREGUNTA 3");
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        panel_partida.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, 350, 70));

        jButton2.setBackground(new java.awt.Color(19, 104, 206));
        jButton2.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("PREGUNTA 2");
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        panel_partida.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 350, 70));

        jButton1.setBackground(new java.awt.Color(226, 27, 60));
        jButton1.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("PREGUNTA 1");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panel_partida.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 350, 70));

        bontonSiguiente.setBackground(new java.awt.Color(255, 255, 255));
        bontonSiguiente.setFont(new java.awt.Font("Roboto Black", 0, 14)); // NOI18N
        bontonSiguiente.setForeground(new java.awt.Color(51, 51, 51));
        bontonSiguiente.setText("SIGUIENTE");
        bontonSiguiente.setBorder(null);
        bontonSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bontonSiguienteActionPerformed(evt);
            }
        });
        panel_partida.add(bontonSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 20, 110, 30));

        label_contenedor_check.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_contenedor_check.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/checkVerde.png"))); // NOI18N
        panel_partida.add(label_contenedor_check, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, -1, 270));

        label_contenedor_resultado.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        label_contenedor_resultado.setForeground(new java.awt.Color(255, 255, 255));
        label_contenedor_resultado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_contenedor_resultado.setText("CORRECTO");
        panel_partida.add(label_contenedor_resultado, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 200, 270));

        panel_contenedor_pregunta.setBackground(new java.awt.Color(255, 255, 255));
        panel_contenedor_pregunta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_pregunta.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        label_pregunta.setForeground(new java.awt.Color(102, 102, 102));
        label_pregunta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panel_contenedor_pregunta.add(label_pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 700, 90));

        panel_partida.add(panel_contenedor_pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 720, 90));

        label_fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/aula.png"))); // NOI18N
        panel_partida.add(label_fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 500));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_partida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelCarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_partida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelCarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(contador < tiempo){
            compararRespuesta(jButton1);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(contador < tiempo){
            compararRespuesta(jButton2);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void bontonSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bontonSiguienteActionPerformed

        if(contadorPreguntas < 20){
            realizarPregunta(juego, usuario);
            System.out.println("realizando pregunta: " + contadorPreguntas);
        }else{
            Ventana_Terminado ventana_juego = new Ventana_Terminado(datosPartida);
            this.setVisible(false);
            ventana_juego.setLocationRelativeTo(null);
            ventana_juego.setVisible(true);
        }
        
    }//GEN-LAST:event_bontonSiguienteActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(contador < tiempo){
            compararRespuesta(jButton3);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if(contador < tiempo){
            compararRespuesta(jButton4);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

  
    public static void main(String args[]) {
        
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana_Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana_Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana_Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana_Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bontonSiguiente;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JLabel label_cargando;
    private javax.swing.JLabel label_contador_preguntas;
    private javax.swing.JLabel label_contenedor_check;
    private javax.swing.JLabel label_contenedor_resultado;
    private javax.swing.JLabel label_contenedor_temporizador;
    private javax.swing.JLabel label_fondo;
    private javax.swing.JLabel label_jugador;
    private javax.swing.JLabel label_pregunta;
    private javax.swing.JLabel label_puntos;
    private javax.swing.JLabel label_temporizador;
    private javax.swing.JPanel panelCarga;
    private javax.swing.JPanel panel_contenedor_pregunta;
    private javax.swing.JPanel panel_partida;
    // End of variables declaration//GEN-END:variables
}
