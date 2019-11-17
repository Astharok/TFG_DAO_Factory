/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsuarioDao;

import DAOFactory.MySqlDbDAOFactory;
import Interfaces.UsuarioDAO;
import beans.Usuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public class SqlDbUsuarioImpl implements UsuarioDAO{

    private final String SQL_ADD = "INSERT INTO Usuarios (Nombre, Apellido, Apodo, Password, Email, Telefono) "
            + "VALUES (?, ?, ?, ?, ?, ?)";


    Connection conexion;
    
    public SqlDbUsuarioImpl() {
        MySqlDbDAOFactory msqldf = new MySqlDbDAOFactory();
        conexion = msqldf.crearConexion();
    }
    
    @Override
    public boolean insertarUsuario(Usuarios usuario) {
        Boolean resultado = false;
        
        PreparedStatement sentencia;
        
        try {
            sentencia = conexion.prepareStatement(SQL_ADD);
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getApellido());
            sentencia.setString(3, usuario.getApodo());
            sentencia.setString(4, usuario.getPassword());
            sentencia.setString(5, usuario.getEmail());
            sentencia.setString(6, usuario.getTelefono());
            
            Integer filas = sentencia.executeUpdate();
            
            if (filas > 0) {
                resultado = true;
            }
            
            System.out.println("Usuario insertado en base de datos MySQL.");
            System.out.println("Usuario: " + usuario.getApodo() + ": " + usuario.getNombre() + " " + usuario.getApellido());
            
            sentencia.close();
        } catch (SQLException ex) {
            System.err.println("Error al insertar el usuario en base de datos MySQL.");
            System.err.println("Usuario: " + usuario.getApodo() + ": " + usuario.getNombre() + " " + usuario.getApellido());
            System.err.println(ex.getCause());
            System.err.println(ex.getMessage());
        }

        return resultado;
    }
    
}
