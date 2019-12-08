/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import beans.Usuarios;
import java.util.Map;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public interface ConversacionDAO {

    /**
    * Encuentra la conversacion
    *
     * @param usuarioA
     * @param usuarioB
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> find(Usuarios usuarioA, Usuarios usuarioB);
    
}
