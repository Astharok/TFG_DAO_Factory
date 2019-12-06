/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsuarioDao;

import DAOFactory.MySqlDbDAOFactory;
import Interfaces.SesionDAO;
import beans.Sesiones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public class SqlDbSesionImpl implements SesionDAO {
    
    private final String SQL_FIND_SESSION = "SELECT ID_Usuario_FK FROM Sesiones WHERE ID_Sesion = ?;";
    
    Connection conexion;

    public SqlDbSesionImpl() {
        MySqlDbDAOFactory msqldf = new MySqlDbDAOFactory();
        conexion = msqldf.crearConexion();
    }

    @Override
    public Map<String, String> find(Sesiones sesion) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_FIND_SESSION);
            
            sentencia.setInt(1, sesion.getIDSesion());

            ResultSet resultado = sentencia.executeQuery();
            
            if(resultado.first()){
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Sesion encontrada");
                results.put("ID_Usuario_FK", String.valueOf(resultado.getInt("ID_Usuario_FK")));
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Sesion no encontrada");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Sesion no se pudo encontrar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

}
