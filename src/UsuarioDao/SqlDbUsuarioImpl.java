/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsuarioDao;

import DAOFactory.MySqlDbDAOFactory;
import Interfaces.UsuarioDAO;
import beans.GruposUsuarios;
import beans.Recargas;
import beans.Tarifas;
import beans.Usuarios;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Util;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public class SqlDbUsuarioImpl implements UsuarioDAO {

    private final String SQL_LOG_IN = "{? = CALL login(?, ?)}";
    
    //private final String SQL_CHANGE_SALDO = "{? = CALL changesaldo(?, ?, ?)}";
    private final String SQL_CHANGE_SALDO = "INSERT Into recargas (Fecha, Cantidad, Notas, ID_Usuario_FK) "
            + "VALUES (CURRENT_TIMESTAMP(), ?, ?, ?)";

    private final String SQL_ADD = "INSERT INTO Usuarios (Nombre, Apellido, Apodo, Password, Email, Telefono, ID_Grupo_Usuario_FK) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private final String SQL_EDIT = "UPDATE Usuarios SET "
            + "Nombre = ?, Apellido = ?, Apodo = ?, Password = ?, Email = ?, Telefono = ?, ID_Grupo_Usuario_FK = ? "
            + "WHERE ID_Usuario = ?";
    
    private final String SQL_LOAD_USUARIOS = "SELECT ID_Usuario, Apodo, Nombre, Apellido, Email, Telefono, Saldo FROM Usuarios";
    
    private final String SQL_FIND_USUARIO = "SELECT u.ID_Usuario, u.Nombre, u.Apellido, u.Apodo, u.Email, u.Telefono, u.Saldo, "
            + "g.Nombre, "
            + "t.Precio_por_hora "
            + "FROM Usuarios u "
            + "INNER JOIN "
            + "grupos_usuarios g "
            + "INNER JOIN "
            + "tarifas t "
            + "WHERE u.ID_Usuario = ? "
            + "AND "
            + "u.ID_Grupo_Usuario_FK = g.ID_Grupo_Usuarios "
            + "AND t.ID_Tarifa = g.ID_Tarifa_FK";

    Connection conexion;

    public SqlDbUsuarioImpl() {
        MySqlDbDAOFactory msqldf = new MySqlDbDAOFactory();
        conexion = msqldf.crearConexion();
    }

    @Override
    public Map<String, String> logIn(Usuarios usuario) {
        Map<String, String> results = new HashMap<>();

        CallableStatement sentencia;

        try {
            sentencia = conexion.prepareCall(SQL_LOG_IN);

            sentencia.registerOutParameter(1, Types.VARCHAR);

            sentencia.setString(2, usuario.getApodo());
            sentencia.setString(3, usuario.getPassword());

            sentencia.execute();

            String resultadoString = sentencia.getString(1);

            Map<String, String> resultadoMap = Util.fromJson(resultadoString);

            if (resultadoMap.get("LOGUED_STATUS").equals("SUCCESS")) {
                results.put("STATE", resultadoMap.get("LOGUED_STATUS"));
                results.put("MESSAGE", "Usuario Logueado");
                
                GruposUsuarios grupo = new GruposUsuarios();
                grupo.setNombre(resultadoMap.get("GROUP_NAME"));
                
                Usuarios usuarioLogueado = new Usuarios();
                
                usuarioLogueado.setIDUsuario(Double.valueOf(String.valueOf(resultadoMap.get("ID_USUARIO"))).intValue());
                usuarioLogueado.setNombre(resultadoMap.get("NOMBRE"));
                usuarioLogueado.setApellido(resultadoMap.get("APELLIDO"));
                usuarioLogueado.setApodo(resultadoMap.get("APODO"));
                usuarioLogueado.setEmail(resultadoMap.get("EMAIL"));
                usuarioLogueado.setTelefono(resultadoMap.get("TELEFONO"));
                usuarioLogueado.setSaldo(Double.valueOf(String.valueOf(resultadoMap.get("SALDO"))));
                usuarioLogueado.setIDGrupoUsuarioFK(grupo);
                
                results.put("LOGUED_USER", Util.toJson(usuarioLogueado));
                results.put("SESSION_ID", String.valueOf(resultadoMap.get("SESSION_ID")));
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Usuario no se pudo loguear");
                results.put("APODO", usuario.getApodo());
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Usuario no se pudo loguear");
            results.put("APODO", usuario.getApodo());
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> insertarUsuario(Usuarios usuario) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_ADD);
            
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getApellido());
            sentencia.setString(3, usuario.getApodo());
            sentencia.setString(4, usuario.getPassword());
            sentencia.setString(5, usuario.getEmail());
            sentencia.setString(6, usuario.getTelefono());
            sentencia.setInt(7, usuario.getIDGrupoUsuarioFK().getIDGrupoUsuarios());

            Integer filas = sentencia.executeUpdate();
            
            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Usuario Registrado");
                results.put("APODO", usuario.getApodo());
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Usuario no se pudo registrar");
                results.put("APODO", usuario.getApodo());
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Usuario no se pudo registrar");
            results.put("APODO", usuario.getApodo());
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> loadAll() {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_LOAD_USUARIOS);

            ResultSet filas = sentencia.executeQuery();
            
            results.put("STATE", "SUCCESS");
            results.put("MESSAGE", "Usuarios recuperados");
            
            List<Usuarios> resUsuarios = new ArrayList<>();

            while (filas.next()) {
                Integer id = filas.getInt("ID_Usuario");
                String apodo = filas.getString("Apodo");
                String nombre = filas.getString("Nombre");
                String apellido = filas.getString("Apellido");
                String email = filas.getString("Email");
                String telefono = filas.getString("Telefono");
                Double saldo = filas.getDouble("Saldo");
                
                //Assuming you have a user object
                Usuarios usuario = new Usuarios();
                
                usuario.setIDUsuario(id);
                usuario.setApodo(apodo);
                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setEmail(email);
                usuario.setTelefono(telefono);
                usuario.setSaldo(saldo);
                
                resUsuarios.add(usuario);
            }
            
            results.put("USUARIOS", Util.toJson(resUsuarios));

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "No se pudo obtener la lista de equipos");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> changeSaldo(Recargas recarga) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_CHANGE_SALDO);
            
            sentencia.setDouble(1, recarga.getCantidad());
            sentencia.setString(2, recarga.getNotas());
            sentencia.setInt(3, recarga.getIDUsuarioFK().getIDUsuario());

            Integer filas = sentencia.executeUpdate();
            
            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Saldo modificado");
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "No se pudo modificar el saldo");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "No se pudo modificar el saldo");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> find(Usuarios usuario) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_FIND_USUARIO);
            
            sentencia.setInt(1, usuario.getIDUsuario());

            ResultSet resultado = sentencia.executeQuery();
            
            if(resultado.first()){
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Usuario encontrado");
                
                Tarifas tarifa = new Tarifas();
                tarifa.setPrecioporhora(resultado.getDouble("t.Precio_por_hora"));
                
                GruposUsuarios grupo = new GruposUsuarios();
                grupo.setNombre(resultado.getString("g.Nombre"));
                grupo.setIDTarifaFK(tarifa);
                
                Usuarios foundUser = new Usuarios();
                foundUser.setIDUsuario(resultado.getInt("u.ID_Usuario"));
                foundUser.setApodo(resultado.getString("u.Apodo"));
                foundUser.setEmail(resultado.getString("u.Email"));
                foundUser.setNombre(resultado.getString("u.Nombre"));
                foundUser.setApellido(resultado.getString("u.Apellido"));
                foundUser.setTelefono(resultado.getString("u.Telefono"));
                foundUser.setSaldo(resultado.getDouble("u.Saldo"));
                foundUser.setIDGrupoUsuarioFK(grupo);
                
                results.put("USUARIO", Util.toJson(foundUser));
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "usuario no encontrado");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "usuario no se pudo encontrar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> editarUsuario(Usuarios usuario) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_EDIT);
            
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getApellido());
            sentencia.setString(3, usuario.getApodo());
            sentencia.setString(4, usuario.getPassword());
            sentencia.setString(5, usuario.getEmail());
            sentencia.setString(6, usuario.getTelefono());
            sentencia.setInt(7, usuario.getIDGrupoUsuarioFK().getIDGrupoUsuarios());
            sentencia.setInt(8, usuario.getIDUsuario());

            Integer filas = sentencia.executeUpdate();
            
            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Usuario Actualizado");
                results.put("APODO", usuario.getApodo());
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Usuario no se pudo actualizar");
                results.put("APODO", usuario.getApodo());
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Usuario no se pudo actualizar");
            results.put("APODO", usuario.getApodo());
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

}
