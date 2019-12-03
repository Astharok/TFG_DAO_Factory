/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsuarioDao;

import DAOFactory.MySqlDbDAOFactory;
import Interfaces.GrupoDAO;
import beans.GruposUsuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import util.Util;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public class SqlDbGrupoImpl implements GrupoDAO {
    
    private final String SQL_FIND_GRUPO = "SELECT ID_Grupo_Usuarios FROM Grupos_Usuarios WHERE Nombre = ?;";
    
    Connection conexion;
    
    public SqlDbGrupoImpl() {
        MySqlDbDAOFactory msqldf = new MySqlDbDAOFactory();
        conexion = msqldf.crearConexion();
    }

    @Override
    public Map<String, String> find(GruposUsuarios grupo) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_FIND_GRUPO);
            
            sentencia.setString(1, grupo.getNombre());

            ResultSet resultado = sentencia.executeQuery();
            
            if(resultado.first()){
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Grupo encontrado");
                results.put("ID", resultado.getString("ID_Grupo_Usuarios"));
                results.put("NOMBRE", grupo.getNombre());
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Grupo no encontrado");
                results.put("NOMBRE", grupo.getNombre());
                results.put("SQL", SQL_FIND_GRUPO);
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Grupo no se pudo encontrar");
            results.put("NOMBRE", grupo.getNombre());
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }
    
}
