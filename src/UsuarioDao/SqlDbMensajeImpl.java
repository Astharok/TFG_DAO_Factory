/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsuarioDao;

import DAOFactory.MySqlDbDAOFactory;
import Interfaces.MensajeDAO;
import beans.Conversaciones;
import beans.Mensajes;
import beans.Usuarios;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Util;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public class SqlDbMensajeImpl implements MensajeDAO {

    private final String SQL_ADD = "INSERT INTO Mensajes (Texto_Mensaje, ID_Conversacion_FK, ID_Usuario_FK) "
            + "VALUES (?, ?, ?)";

    private final String SQL_FIND_MENSAJES_CONVERSACION = "SELECT Texto_Mensaje, Fecha, ID_Usuario_FK FROM Mensajes WHERE ID_Conversacion_FK = ?;";

    Connection conexion;

    public SqlDbMensajeImpl() {
        MySqlDbDAOFactory msqldf = new MySqlDbDAOFactory();
        conexion = msqldf.crearConexion();
    }

    @Override
    public Map<String, String> insert(Mensajes mensaje) {
        Map<String, String> results = new HashMap<String, String>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_ADD);

            sentencia.setString(1, mensaje.getTextoMensaje());
            sentencia.setInt(2, mensaje.getIDConversacionFK().getIDConversacion());
            sentencia.setInt(3, mensaje.getIDUsuarioFK().getIDUsuario());

            Integer filas = sentencia.executeUpdate();

            if (filas > 0) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Mensaje Registrado");
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Mensaje no se pudo registrar");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Mensaje no se pudo registrar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

    @Override
    public Map<String, String> findByConversacion(Conversaciones conversacion) {
        Map<String, String> results = new HashMap<>();

        PreparedStatement sentencia;

        try {
            sentencia = conexion.prepareStatement(SQL_FIND_MENSAJES_CONVERSACION);

            sentencia.setInt(1, conversacion.getIDConversacion());

            ResultSet resultado = sentencia.executeQuery();

            if (resultado.first()) {
                results.put("STATE", "SUCCESS");
                results.put("MESSAGE", "Mensajes encontrados");
                List<Mensajes> mensajes = new ArrayList<Mensajes>();

                Mensajes mensaje = new Mensajes();
                Usuarios usuario = new Usuarios();

                mensaje = new Mensajes();
                usuario = new Usuarios();

                usuario.setIDUsuario(resultado.getInt("ID_Usuario_FK"));

                mensaje.setTextoMensaje(resultado.getString("Texto_Mensaje"));
                mensaje.setFecha(resultado.getTimestamp("Fecha"));
                mensaje.setIDUsuarioFK(usuario);

                mensajes.add(mensaje);

                while (resultado.next()) {
                    mensaje = new Mensajes();
                    usuario = new Usuarios();

                    usuario.setIDUsuario(resultado.getInt("ID_Usuario_FK"));
                    
                    mensaje.setTextoMensaje(resultado.getString("Texto_Mensaje"));
                    mensaje.setFecha(resultado.getTimestamp("Fecha"));
                    mensaje.setIDUsuarioFK(usuario);

                    mensajes.add(mensaje);
                }
                results.put("HISTORIAL_CHAT", Util.toJson(mensajes));
            } else {
                results.put("STATE", "FAILURE");
                results.put("MESSAGE", "Mensajes no encontrados");
            }

            sentencia.close();
        } catch (SQLException ex) {
            results.put("STATE", "EXCEPTION");
            results.put("MESSAGE", "Mensajes no se pudo encontrar");
            results.put("EXCEPTION_MESSAGE", ex.getMessage());
        }

        return results;
    }

}