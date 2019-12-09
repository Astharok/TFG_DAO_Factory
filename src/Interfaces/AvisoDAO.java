/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import beans.Avisos;
import beans.GruposUsuarios;
import beans.Usuarios;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public interface AvisoDAO {
    
    /**
    * Insert el aviso en la base de datos
    *
     * @param aviso
     * @param grupos
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> insert(Avisos aviso, List<GruposUsuarios> grupos);

    /**
    * Insert el aviso en la base de datos
    *
     * @param usuario
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> findByUser(Usuarios usuario);
    
}
