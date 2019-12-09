/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsuarioDao;

import DAOFactory.MySqlDbDAOFactory;
import Interfaces.AvisoDAO;
import beans.Avisos;
import beans.GruposUsuarios;
import beans.Usuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Util;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public class SqlDbAvisoImpl implements AvisoDAO {

    private final String SQL_ADD_AVISO = "INSERT INTO Avisos (Titulo, Texto, Fecha) "
            + "VALUES (?, ?, CURRENT_TIMESTAMP())";

    private final String SQL_ADD_AVISO_GRUPO = "INSERT INTO Grupos_Usuarios_Avisos (ID_Grupo_Usuario_FK, ID_Aviso_FK) "
            + "VALUES (?, ?)";
    
    private final String SQL_FIND_AVISOS_BY_USER = "SELECT Titulo, Texto, Fecha "
            + "FROM Avisos "
            + "WHERE ID_Aviso IN "
            + "(SELECT ID_Aviso_FK "
            + "FROM Grupos_Usuarios_Avisos "
            + "WHERE ID_Grupo_Usuario_FK = "
            + "(SELECT ID_Grupo_Usuario_FK "
            + "FROM Usuarios "
            + "WHERE ID_Usuario = ?)) "
            + "ORDER BY Fecha DESC";

    Connection conexion;

    public SqlDbAvisoImpl() {
        MySqlDbDAOFactory msqldf = new MySqlDbDAOFactory();
        conexion = msqldf.crearConexion();
    }

    @Override
    public Map<String, String> insert(Avisos aviso, List<GruposUsuarios> grupos) {
        Map<String, String> results = new HashMap<>();
        Boolean success = true;

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_ADD_AVISO, Statement.RETURN_GENERATED_KEYS);

            sentencia.setString(1, aviso.getTitulo());
            sentencia.setString(2, aviso.getTexto());

            Integer filasAviso = sentencia.executeUpdate();

            if (filasAviso > 0) {
                ResultSet generatedKeys = sentencia.getGeneratedKeys();
                if (generatedKeys.first()) {
                    Integer idAviso = generatedKeys.getInt(1);
                    sentencia = conexion.prepareStatement(SQL_ADD_AVISO_GRUPO);

                    for (GruposUsuarios grupo : grupos) {
                        sentencia.setInt(1, grupo.getIDGrupoUsuarios());
                        sentencia.setInt(2, idAviso);

                        Integer filasGrupoAviso = sentencia.executeUpdate();

                        if (filasGrupoAviso <= 0) {
                            success = false;
                        }
                    }
                }
            }else{
                success = false;
            }
            if (success) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Aviso Registrado");
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Aviso no se pudo registrar");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Aviso no se pudo registrar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
            System.err.println(ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> findByUser(Usuarios usuario) {
        Map<String, String> results = new HashMap<>();
        List<Avisos> avisos = new ArrayList<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_FIND_AVISOS_BY_USER);
            
            sentencia.setInt(1, usuario.getIDUsuario());

            ResultSet resultado = sentencia.executeQuery();
            
            while(resultado.next()){
                
                Avisos aviso = new Avisos();
                aviso.setTitulo(resultado.getString("Titulo"));
                aviso.setTexto(resultado.getString("Texto"));
                aviso.setFecha(resultado.getTimestamp("Fecha"));
                
                avisos.add(aviso);
                
            }
            
            if(avisos.size() > 0){
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Avisos encontrados");
                results.put("AVISOS", Util.toJson(avisos));
            }else{
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Avisos no encontrados");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Avisos no se pudieron encontrar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

}
