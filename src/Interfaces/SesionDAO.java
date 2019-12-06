/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import beans.Sesiones;
import java.util.Map;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public interface SesionDAO {
    
    /**
    * Encuentra la sesion
    *
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> find(Sesiones sesion);
    
}
