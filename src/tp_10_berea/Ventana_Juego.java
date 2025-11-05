//String textoOriginal = "Según los diferentes algoritmos mencionados, luego de varias ejecuciones, ¿cual tiene una mayor probabilidad de repetirse el resultado?";
//String textoConFormato = "<html><p style=\"width:500px; text-align: center;\">" + textoOriginal + "</p></html>";
//jLabel2.setText(textoConFormato);
package tp_10_berea;

import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.*;
import javax.swing.SwingWorker;
import javax.swing.Timer;

public class Ventana_Juego extends javax.swing.JFrame {
    
    private TreeMap<String, String> datosPartida; //Para guardar los datos de la partida
    private String usuario; //para guardar el nombre de usuario 
    private String respuestaCorrecta = "";//para guardar la pregunta correcta;
    private Juego juego; //para instanciar la clase juego
    private int contador = 0; //para el temporizador
    private int contadorPreguntas = 0; //para el contador de preguntas
    private String respuestaSeleccionada = "";
    
    private Color colorRojo;
    private Color colorAzul;
    private Color colorAmarillo;
    private Color colorVerde;
    
    public Ventana_Juego(String _usuario) {
        initComponents();
        usuario = _usuario;
       
        colorRojo = jButton1.getBackground();
        colorAzul = jButton2.getBackground();
        colorAmarillo = jButton3.getBackground();
        colorVerde = jButton4.getBackground();
        
        juego = new Juego();
        realizarPregunta(juego,_usuario);
            
    }
    public void validarRespuesta(int suceso){
        //SUCESO EN 0 SIGNIFICA QUE EL JUGADOR SE QUEDO SIN TIEMPO Y NO CONTESTO O RESPONDIÓ MAL
        if(suceso==0){
            int aux = Integer.parseInt(datosPartida.get("puntaje"));
            aux--;
            String p = ""+aux;
            datosPartida.put("puntaje", p);
            
            int aux2 = Integer.parseInt(datosPartida.get("incorrectas"));
            aux2--;
            String p2 = ""+aux2;
            datosPartida.put("puntaje", p2);
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
            datosPartida.put("puntaje", p2);
        
        }
    }
    
    public void iniciarPartida(String _usuario){
        
        usoPaneles(3);
        datosPartida = new TreeMap<>();
        datosPartida.put("puntaje","0");
        datosPartida.put("correctas","0");
        datosPartida.put("incorrectas","0");
        datosPartida.put("usuario",_usuario);
            
        label_jugador.setText("JUGADOR: "+ datosPartida.get("usuario"));
        label_puntos.setText("PUNTOS: "+ datosPartida.get("puntaje"));
        
        temporizador();
    }
    
    public void temporizador(){
        Timer timer = new Timer(1000, e -> {
            contador++;
            label_temporizador.setText(""+contador);
            if (contador == 30) {
                ((Timer)e.getSource()).stop();
                usoPaneles(4);
                if(respuestaSeleccionada.equals("")){
                    validarRespuesta(0);
                }
               
            }
        });

        timer.start();
    }
    
    public void realizarPregunta(Juego juego,String _usuario){
        usoPaneles(1);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                respuestaCorrecta = juego.llamarPreguntas(label_pregunta,jButton1,jButton2,jButton3,jButton4);
                System.out.println(respuestaCorrecta);
                contadorPreguntas++;
                return null;
            }

            @Override
            protected void done() {
                usoPaneles(2);
                iniciarPartida(_usuario);
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
        }      
        //MOSTRAR PANEL RESPUESTA 
        else if(seleccion == 4){
            label_fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/aula_violeta.png")));
            label_contenedor_temporizador.setVisible(false);
            label_temporizador.setVisible(false);
            bontonSiguiente.setVisible(true);
            contador=0;
            ArrayList<JButton> botones = new ArrayList<JButton>();
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

        panel_contenedor_pregunta.setBackground(new java.awt.Color(255, 255, 255));

        label_pregunta.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        label_pregunta.setForeground(new java.awt.Color(102, 102, 102));
        label_pregunta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panel_contenedor_preguntaLayout = new javax.swing.GroupLayout(panel_contenedor_pregunta);
        panel_contenedor_pregunta.setLayout(panel_contenedor_preguntaLayout);
        panel_contenedor_preguntaLayout.setHorizontalGroup(
            panel_contenedor_preguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_contenedor_preguntaLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(label_pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        panel_contenedor_preguntaLayout.setVerticalGroup(
            panel_contenedor_preguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_contenedor_preguntaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_pregunta, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel_partida.add(panel_contenedor_pregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 720, 70));

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
        String textoBoton = jButton1.getText().replaceAll("<[^>]*>", "").trim();
        if (textoBoton.equalsIgnoreCase(respuestaCorrecta.trim())) {
            validarRespuesta(1);
        }else{
            validarRespuesta(0);
        }
        usoPaneles(4);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String textoBoton = jButton2.getText().replaceAll("<[^>]*>", "").trim();
        if (textoBoton.equalsIgnoreCase(respuestaCorrecta.trim())) {
            validarRespuesta(1);
        }else{
            validarRespuesta(0);
        }
        usoPaneles(4);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void bontonSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bontonSiguienteActionPerformed
        if(contadorPreguntas < 20){
            contador = 0;
            realizarPregunta(juego,usuario);
            System.out.println("realizando pregunta: " + contadorPreguntas);
        }
        
    }//GEN-LAST:event_bontonSiguienteActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String textoBoton = jButton3.getText().replaceAll("<[^>]*>", "").trim();
        if (textoBoton.equalsIgnoreCase(respuestaCorrecta.trim())) {
            validarRespuesta(1);
        }else{
            validarRespuesta(0);
        }
        usoPaneles(4);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String textoBoton = jButton4.getText().replaceAll("<[^>]*>", "").trim();
        if (textoBoton.equalsIgnoreCase(respuestaCorrecta.trim())) {
            validarRespuesta(1);
        }else{
            validarRespuesta(0);
        }
        usoPaneles(4);
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
