/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsuarioDao;

import DAOFactory.MySqlDbDAOFactory;
import Interfaces.GrupoDAO;
import beans.GruposUsuarios;
import beans.Tarifas;
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
public class SqlDbGrupoImpl implements GrupoDAO {

    private final String SQL_INSERT_GRUPO = "INSERT INTO Grupos_Usuarios (Nombre, ID_Tarifa_FK) VALUES (?, ?);";

    private final String SQL_UPDATE_GRUPO = "UPDATE Grupos_Usuarios SET Nombre = ?, ID_Tarifa_FK = ? WHERE ID_Grupo_Usuarios = ?;";

    private final String SQL_INSERT_TARIFA = "INSERT INTO Tarifas (Nombre, Precio_por_hora) VALUES (?, ?);";

    private final String SQL_UPDATE_TARIFA = "UPDATE Tarifas SET Nombre = ?, Precio_por_hora = ? WHERE ID_Tarifa = ?;";

    private final String SQL_FIND_TARIFA = "SELECT ID_Tarifa, nombre, Precio_por_hora "
            + "FROM "
            + "Tarifas WHERE ID_Tarifa = ?;";

    //private final String SQL_FIND_GRUPO = "SELECT ID_Grupo_Usuarios, Nombre, ID_Tarifa_FK FROM Grupos_Usuarios WHERE ID_Grupo_Usuarios = ?;";
    private final String SQL_FIND_GRUPO = "SELECT g.ID_Grupo_Usuarios, g.Nombre, t.ID_Tarifa, t.nombre, t.Precio_por_hora "
            + "FROM Grupos_Usuarios g "
            + "LEFT JOIN Tarifas t "
            + "ON g.ID_Tarifa_FK = t.ID_Tarifa "
            + "WHERE ID_Grupo_Usuarios = ?;";

    private final String SQL_FINDALL_GRUPO = "SELECT g.ID_Grupo_Usuarios, g.Nombre, t.ID_Tarifa, t.nombre, t.Precio_por_hora "
            + "FROM Grupos_Usuarios g "
            + "LEFT JOIN Tarifas t "
            + "ON g.ID_Tarifa_FK = t.ID_Tarifa;";

    private final String SQL_FINDALL_TARIFAS = "SELECT ID_Tarifa, nombre, Precio_por_hora "
            + "FROM Tarifas;";

    private final String SQL_UPDATE_TARIFA_GRUPO = "UPDATE Grupos_Usuarios SET ID_Tarifa_FK = ? WHERE ID_Grupo_Usuarios = ?;";
    
    private final String SQL_DELETE_GRUPO = "DELETE FROM Grupos_Usuarios WHERE ID_Grupo_Usuarios = ?";
    
    private final String SQL_DELETE_TARIFA = "DELETE FROM Tarifas WHERE ID_Tarifa = ?";
    
    Connection conexion;

    public SqlDbGrupoImpl() {
        MySqlDbDAOFactory msqldf = new MySqlDbDAOFactory();
        conexion = msqldf.crearConexion();
    }

    @Override
    public Map<String, String> find(GruposUsuarios grupo) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        ResultSet resultado;

        try {
            sentencia = conexion.prepareStatement(SQL_FIND_GRUPO);

            sentencia.setInt(1, grupo.getIDGrupoUsuarios());

            resultado = sentencia.executeQuery();

            GruposUsuarios grupoFound = new GruposUsuarios();
            List<Tarifas> resTarifas = new ArrayList<>();

            if (resultado.first()) {

                Tarifas tarifa = new Tarifas();

                tarifa.setIDTarifa(resultado.getInt("t.ID_Tarifa"));
                tarifa.setNombre(resultado.getString("t.nombre"));
                tarifa.setPrecioporhora(resultado.getDouble("t.Precio_por_hora"));

                grupoFound = new GruposUsuarios();

                grupoFound.setIDGrupoUsuarios(resultado.getInt("g.ID_Grupo_Usuarios"));
                grupoFound.setNombre(resultado.getString("g.Nombre"));
                grupoFound.setIDTarifaFK(tarifa);
                
            }

            sentencia = conexion.prepareStatement(SQL_FINDALL_TARIFAS);

            resultado = sentencia.executeQuery();

            while (resultado.next()) {

                Tarifas tarifa = new Tarifas();
                tarifa.setIDTarifa(resultado.getInt("ID_Tarifa"));
                tarifa.setNombre(resultado.getString("nombre"));
                tarifa.setPrecioporhora(resultado.getDouble("Precio_por_hora"));

                resTarifas.add(tarifa);
            }

            results.put("STATE", "SUCCESS");
            results.put("MESSAGE", "Grupo encontrado");

            results.put("GRUPO", Util.toJson(grupoFound));
            results.put("TARIFAS", Util.toJson(resTarifas));

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Grupo no se pudo encontrar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> findall() {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;
        ResultSet filas;

        try {
            sentencia = conexion.prepareStatement(SQL_FINDALL_GRUPO);

            filas = sentencia.executeQuery();

            List<GruposUsuarios> resGrupos = new ArrayList<>();
            List<Tarifas> resTarifas = new ArrayList<>();

            while (filas.next()) {

                Tarifas tarifa = new Tarifas();
                tarifa.setIDTarifa(filas.getInt("t.ID_Tarifa"));
                tarifa.setNombre(filas.getString("t.nombre"));
                tarifa.setPrecioporhora(filas.getDouble("t.Precio_por_hora"));

                GruposUsuarios grupo = new GruposUsuarios();

                grupo.setIDGrupoUsuarios(filas.getInt("g.ID_Grupo_Usuarios"));
                grupo.setNombre(filas.getString("g.Nombre"));
                grupo.setIDTarifaFK(tarifa);

                resGrupos.add(grupo);
            }

            sentencia = conexion.prepareStatement(SQL_FINDALL_TARIFAS);

            filas = sentencia.executeQuery();

            while (filas.next()) {

                Tarifas tarifa = new Tarifas();
                tarifa.setIDTarifa(filas.getInt("ID_Tarifa"));
                tarifa.setNombre(filas.getString("nombre"));
                tarifa.setPrecioporhora(filas.getDouble("Precio_por_hora"));

                resTarifas.add(tarifa);
            }

            results.put("STATE", "SUCCESS");
            results.put("MESSAGE", "Grupos recuperados");

            results.put("GRUPOS", Util.toJson(resGrupos));
            results.put("TARIFAS", Util.toJson(resTarifas));

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "No se pudo obtener la lista de grupos");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> insertaGrupo(GruposUsuarios grupo) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_INSERT_GRUPO);

            sentencia.setString(1, grupo.getNombre());
            sentencia.setInt(2, grupo.getIDTarifaFK().getIDTarifa());

            Integer filas = sentencia.executeUpdate();

            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Grupo insertado");
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Grupo no se pudo insertar");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Grupo no se pudo insertar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> updateGrupo(GruposUsuarios grupo) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_UPDATE_GRUPO);

            sentencia.setString(1, grupo.getNombre());
            sentencia.setInt(2, grupo.getIDTarifaFK().getIDTarifa());
            sentencia.setInt(3, grupo.getIDGrupoUsuarios());

            Integer filas = sentencia.executeUpdate();

            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Grupo actualizado");
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Grupo no se pudo actualizar");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Grupo no se pudo actualizar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> insertaTarifa(Tarifas tarifa) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_INSERT_TARIFA);

            sentencia.setString(1, tarifa.getNombre());
            sentencia.setDouble(2, tarifa.getPrecioporhora());

            Integer filas = sentencia.executeUpdate();

            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Tarifa insertada");
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Tarifa no se pudo insertar");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Tarifa no se pudo insertar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> updateTarifa(Tarifas tarifa) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_UPDATE_TARIFA);

            sentencia.setString(1, tarifa.getNombre());
            sentencia.setDouble(2, tarifa.getPrecioporhora());
            sentencia.setInt(3, tarifa.getIDTarifa());

            Integer filas = sentencia.executeUpdate();

            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Tarifa actualizada");
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Tarifa no se pudo actualizar");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Tarifa no se pudo actualizar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> findTarifa(Tarifas tarifa) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        ResultSet resultado;

        try {
            sentencia = conexion.prepareStatement(SQL_FIND_TARIFA);

            sentencia.setInt(1, tarifa.getIDTarifa());

            resultado = sentencia.executeQuery();

            Tarifas tarifaFound = new Tarifas();

            if (resultado.first()) {

                tarifaFound.setIDTarifa(resultado.getInt("ID_Tarifa"));
                tarifaFound.setNombre(resultado.getString("nombre"));
                tarifaFound.setPrecioporhora(resultado.getDouble("Precio_por_hora"));

                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Tarifa encontrada");
                results.put("TARIFA", Util.toJson(tarifaFound));
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Tarifa no encontrada");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Tarifa no se pudo encontrar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> eliminarGrupo(GruposUsuarios grupo) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_DELETE_GRUPO);
            
            sentencia.setInt(1, grupo.getIDGrupoUsuarios());

            Integer filas = sentencia.executeUpdate();
            
            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Grupo eliminado");
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Grupo no se pudo eliminar");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Grupo no se pudo eliminar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> eliminarTarifa(Tarifas tarifa) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_DELETE_TARIFA);
            
            sentencia.setInt(1, tarifa.getIDTarifa());

            Integer filas = sentencia.executeUpdate();
            
            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Tarifa eliminado");
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Tarifa no se pudo eliminar");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Tarifa no se pudo eliminar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

}
