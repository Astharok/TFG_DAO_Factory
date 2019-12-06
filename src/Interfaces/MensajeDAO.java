/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import beans.Conversaciones;
import beans.Mensajes;
import java.util.Map;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public interface MensajeDAO {

    /**
    * Inserta el mensaje
    *
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> insert(Mensajes mensaje);

    /**
    * Recuperas los mensajes de la conversacion
    *
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> findByConversacion(Conversaciones conversacion);
    
}
