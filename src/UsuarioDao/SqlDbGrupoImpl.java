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
    
    private final String SQL_FIND_GRUPO = "SELECT ID_Grupo_Usuarios FROM Grupos_Usuarios WHERE Nombre = ?;";
    
    private final String SQL_FINDALL_GRUPO = "SELECT g.ID_Grupo_Usuarios, g.Nombre, t.ID_Tarifa, t.nombre, t.Precio_por_hora "
            + "FROM Grupos_Usuarios g "
            + "LEFT JOIN Tarifas t "
            + "ON g.ID_Tarifa_FK = t.ID_Tarifa;";
    
    private final String SQL_FINDALL_TARIFAS = "SELECT ID_Tarifa, nombre, Precio_por_hora "
            + "FROM Tarifas;";
    
    private final String SQL_UPDATE_TARIFA_GRUPO = "UPDATE Grupos_Usuarios SET ID_Tarifa_FK = ? WHERE ID_Grupo_Usuarios = ?;";    
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
    public Map<String, String> updateTarifa(GruposUsuarios grupo, Tarifas tarifa) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_UPDATE_TARIFA_GRUPO);
            
            sentencia.setInt(1, tarifa.getIDTarifa());
            sentencia.setInt(2, grupo.getIDGrupoUsuarios());

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
    
}
