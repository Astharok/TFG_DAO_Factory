/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import beans.GruposUsuarios;
import beans.Tarifas;
import java.util.Map;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public interface GrupoDAO {
    
    /**
    * Recupera el grupo de la base de datos
    *
     * @param grupo
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> find(GruposUsuarios grupo);

    /**
    * Recupera todos los grupos de la base de datos
    *
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> findall();

    /**
    * Actualiza la tarifa de un grupo en la base de datos
    *
     * @param grupo
     * @param tarifa
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> updateTarifa(GruposUsuarios grupo, Tarifas tarifa);
    
}
