/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsuarioDao;

import DAOFactory.MySqlDbDAOFactory;
import Interfaces.ConversacionDAO;
import beans.Usuarios;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import util.Util;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public class SqlDbConversacionImpl implements ConversacionDAO {
    
    private final String SQL_FIND_CONVERSACION = "{? = CALL getconversacion(?, ?)}";
    
    Connection conexion;

    public SqlDbConversacionImpl() {
        MySqlDbDAOFactory msqldf = new MySqlDbDAOFactory();
        conexion = msqldf.crearConexion();
    }

    @Override
    public Map<String, String> find(Usuarios usuarioA, Usuarios usuarioB) {
        Map<String, String> results = new HashMap<>();

        CallableStatement sentencia;

        try {
            sentencia = conexion.prepareCall(SQL_FIND_CONVERSACION);
            
            sentencia.registerOutParameter(1, Types.VARCHAR);

            sentencia.setInt(2, usuarioA.getIDUsuario());
            sentencia.setInt(3, usuarioB.getIDUsuario());

            Boolean res = sentencia.execute();
            
            String resultadoString = sentencia.getString(1);
            
            Map<String, String> resultadoMap = Util.fromJson(resultadoString);
            
            if (resultadoMap.get("REQUEST_STATUS").equals("SUCCESS")) {
                results.put("STATE", resultadoMap.get("REQUEST_STATUS"));
                results.put("MESSAGE", "Conversacion encontrada");
                results.put("CONVERSACION_ID", String.valueOf(resultadoMap.get("CONVERSACION_ID")));
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Conversacion no se pudo encontrar");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Conversacion no se pudo encontrar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

}
