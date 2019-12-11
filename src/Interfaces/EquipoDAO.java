/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import beans.Equipos;
import beans.HistorialesEquipos;
import beans.Usuarios;
import java.util.Map;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public interface EquipoDAO {
    
    /**
    * Recupera la lista de equipos de la base de datos
    *
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> loadAll();
    
    /**
    * Marca el equipo como activado en la base de datos
    *
    * @param equipo
    * @param usuario
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> activar(Equipos equipo, Usuarios usuario);

    /**
    * Marca el equipo como activado en la base de datos
    *
    * @param historial
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> desactivar(HistorialesEquipos historial);
    
    /**
    * Elimina el equipo de la base de datos
    *
     * @param equipo
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> eliminar(Equipos equipo);
    
}
