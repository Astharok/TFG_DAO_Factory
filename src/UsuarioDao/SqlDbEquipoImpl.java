/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsuarioDao;

import DAOFactory.MySqlDbDAOFactory;
import Interfaces.EquipoDAO;
import beans.Equipos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Util;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public class SqlDbEquipoImpl implements EquipoDAO {

    private final String SQL_LOAD_EQUIPOS = "SELECT Nombre FROM equipos";

    Connection conexion;

    public SqlDbEquipoImpl() {
        MySqlDbDAOFactory msqldf = new MySqlDbDAOFactory();
        conexion = msqldf.crearConexion();
    }

    @Override
    public Map<String, String> loadAll() {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_LOAD_EQUIPOS);

            ResultSet filas = sentencia.executeQuery();
            
            results.put("STATE", "SUCCESS");
            results.put("MESSAGE", "Equipos recuperados");
            
            List<Equipos> resEquipos = new ArrayList<>();

            while (filas.next()) {
                String nombre = filas.getString("Nombre");
                
                //Assuming you have a user object
                Equipos equipo = new Equipos();
                
                equipo.setNombre(nombre);
                
                resEquipos.add(equipo);
            }
            
            results.put("EQUIPOS", Util.toJson(resEquipos));

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "No se pudo obtener la lista de equipos");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

}
