/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsuarioDao;

import DAOFactory.MySqlDbDAOFactory;
import Interfaces.UsuarioDAO;
import beans.Usuarios;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import util.Util;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public class SqlDbUsuarioImpl implements UsuarioDAO {

    private final String SQL_LOG_IN = "{? = CALL login(?, ?)}";

    private final String SQL_ADD = "INSERT INTO Usuarios (Nombre, Apellido, Apodo, Password, Email, Telefono) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    Connection conexion;

    public SqlDbUsuarioImpl() {
        MySqlDbDAOFactory msqldf = new MySqlDbDAOFactory();
        conexion = msqldf.crearConexion();
    }

    @Override
    public Map<String, String> logIn(Usuarios usuario) {
        Map<String, String> results = new HashMap<String, String>();

        CallableStatement sentencia;

        try {
            sentencia = conexion.prepareCall(SQL_LOG_IN);

            sentencia.registerOutParameter(1, Types.VARCHAR);

            sentencia.setString(2, usuario.getApodo());
            sentencia.setString(3, usuario.getPassword());

            Boolean res = sentencia.execute();

            String resultadoString = sentencia.getString(1);

            Map<String, String> resultadoMap = Util.fromJson(resultadoString);

            if (resultadoMap.get("LOGUED_STATUS").equals("SUCCESS")) {
                results.put("STATE", resultadoMap.get("LOGUED_STATUS"));
                results.put("MESSAGE", "Usuario Logueado");
                results.put("APODO", usuario.getApodo());
                results.put("GROUP_NAME", resultadoMap.get("GROUP_NAME"));
                results.put("SESSION_ID", resultadoMap.get("SESSIONID"));
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
