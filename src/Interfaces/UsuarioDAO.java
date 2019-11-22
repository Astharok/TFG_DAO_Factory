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
    * @return boolean True si la insercion es correcta, False si no se pudo realizar la insercion.
    */
    public boolean insertarUsuario(Usuarios usuario);
    
}
