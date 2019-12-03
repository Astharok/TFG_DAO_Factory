/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import beans.Recargas;
import beans.Usuarios;
import java.util.Map;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public interface UsuarioDAO {
    
    /**
    * loguea un usuario
    *
    * @param usuario El usuario a loguear en la base de datos.
    * @return boolean True si el logueo es correcto, False si no se pudo realizar.
    */
    public Map<String, String> logIn(Usuarios usuario);
    
    /**
    * Inserta un usuario en la abase de datos
    *
    * @param usuario El usuario a instertar en la base de datos.
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> insertarUsuario(Usuarios usuario);
    
    /**
    * Recupera la lista de usuarios de la base de datos
    *
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> loadAll();
    
    /**
    * Modifica el saldo de un usuario
    *
    * @return Map<String, String> Mapa JSON con respuestas a la peticion.
    */
    public Map<String, String> changeSaldo(Recargas recarga);
    
}
