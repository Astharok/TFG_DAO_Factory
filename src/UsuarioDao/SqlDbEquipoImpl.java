/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsuarioDao;

import DAOFactory.MySqlDbDAOFactory;
import Interfaces.EquipoDAO;
import beans.Equipos;
import beans.HistorialesEquipos;
import beans.Usuarios;
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

    private final String SQL_LOAD_EQUIPOS = "SELECT eq.ID_Equipo, eq.Nombre, hi.ID_Historial_Equipo, hi.Fecha_Inicio, us.ID_Usuario, us.Apodo "
            + "FROM equipos eq "
            + "LEFT JOIN historiales_equipos hi "
            + "ON (eq.ID_Equipo = hi.ID_Equipo_FK AND hi.Fecha_FIN IS NULL) "
            + "LEFT JOIN Usuarios us "
            + "ON (us.ID_usuario = hi.ID_Usuario_FK) "
            + "ORDER BY eq.ID_Equipo";
    
        private final String SQL_ACTIVAR = "INSERT INTO historiales_equipos (Fecha_Inicio, ID_Usuario_FK, ID_Equipo_FK) "
            + "VALUES (CURRENT_TIMESTAMP(), ?, ?)";
        
        private final String SQL_DESACTIVAR = "UPDATE historiales_equipos SET "
        + "Fecha_Fin = CURRENT_TIMESTAMP()"
        + "WHERE ID_Historial_Equipo = ?";
        
        private final String SQL_DELETE = "DELETE FROM Equipos WHERE ID_Equipo = ?";
        
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
                Usuarios usuario = new Usuarios();
                usuario.setIDUsuario(filas.getInt("ID_Usuario"));
                usuario.setNombre(filas.getString("Apodo"));
                
                List<HistorialesEquipos> historiales = new ArrayList<>();
                
                HistorialesEquipos historial = new HistorialesEquipos();
                historial.setIDHistorialEquipo(filas.getInt("ID_Historial_Equipo"));
                historial.setFechaInicio(filas.getTimestamp("Fecha_Inicio"));
                historial.setIDUsuarioFK(usuario);
                
                historiales.add(historial);
                        
                Equipos equipo = new Equipos();
                
                equipo.setIDEquipo(filas.getInt("ID_Equipo"));
                equipo.setNombre(filas.getString("Nombre"));
                equipo.setHistorialesEquiposCollection(historiales);
                
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

    @Override
    public Map<String, String> activar(Equipos equipo, Usuarios usuario) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_ACTIVAR);
            
            sentencia.setInt(1, usuario.getIDUsuario());
            sentencia.setInt(2, equipo.getIDEquipo());

            Integer filas = sentencia.executeUpdate();
            
            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Equipo activado");
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Equipo no se pudo activar");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "MESSAGE no se pudo activar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> desactivar(HistorialesEquipos historial) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_DESACTIVAR);
            
            sentencia.setInt(1, historial.getIDHistorialEquipo());

            Integer filas = sentencia.executeUpdate();
            
            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Equipo desactivado");
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Equipo no se pudo desactivar");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Equipo no se pudo desactivar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> eliminar(Equipos equipo) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_DELETE);
            
            sentencia.setInt(1, equipo.getIDEquipo());

            Integer filas = sentencia.executeUpdate();
            
            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Equipo eliminado");
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Equipo no se pudo eliminar");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Equipo no se pudo eliminar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

}
