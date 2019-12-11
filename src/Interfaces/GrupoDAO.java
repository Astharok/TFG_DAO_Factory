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
    * Inserta el grupo en la base de datos
    *
     * @param grupo
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> insertaGrupo(GruposUsuarios grupo);

    /**
    * Actualiza el grupo en la base de datos
    *
     * @param grupo
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> updateGrupo(GruposUsuarios grupo);

    /**
    * Inserta la tarifa en la base de datos
    *
     * @param tarifa
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> insertaTarifa(Tarifas tarifa);
    
    /**
    * Actualiza la tarifa en la base de datos
    *
     * @param tarifa
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> updateTarifa(Tarifas tarifa);

    /**
    * Recupera la tarifa de la base de datos
    *
     * @param tarifa
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> findTarifa(Tarifas tarifa);
    
    /**
    * Elimina el grupo de la base de datos
    *
     * @param grupo
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> eliminarGrupo(GruposUsuarios grupo);
    
    /**
    * Elimina la tarifa de la base de datos
    *
     * @param tarifa
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> eliminarTarifa(Tarifas tarifa);
    
}
