/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOFactory;

import Interfaces.ConversacionDAO;
import Interfaces.EquipoDAO;
import Interfaces.GrupoDAO;
import Interfaces.MensajeDAO;
import Interfaces.SesionDAO;
import Interfaces.UsuarioDAO;
import UsuarioDao.SqlDbConversacionImpl;
import UsuarioDao.SqlDbEquipoImpl;
import UsuarioDao.SqlDbGrupoImpl;
import UsuarioDao.SqlDbMensajeImpl;
import UsuarioDao.SqlDbSesionImpl;
import UsuarioDao.SqlDbUsuarioImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public class MySqlDbDAOFactory extends DAOFactory {
    
    private Connection conexion;
    
    private static final String DATABASE = "MYSQL";
    private static final String JDBC = "jdbc:mysql:";
    private static final String IP = "192.168.2.200";
    private static final String DB = "fctdatabase";
    
    private static final String URL = JDBC + "//" + IP + "/" + DB;
    private static final String CONTROLADOR = "com.mysql.jdbc.Driver";
    private static final String USER = "FCTAdmin";
    private static final String PASS = "83vS4ZnWnQzzwk8M";

    public MySqlDbDAOFactory() {
    }

    @Override
    public Connection crearConexion() {
        try{
            //SELECCION DE CONTROLADOR
            Class.forName(CONTROLADOR);
            
            //SELECCION DE DB, USER Y PASS Y ESTABLECER CONEXION
            conexion = DriverManager.getConnection(URL, USER, PASS);
            
            System.out.println(DATABASE + "_SUCCESS: " + DB.toUpperCase() + " - Conexión a base de datos establecida");
        } catch (SQLException e) {
            System.err.println(DATABASE + "_FAILURE: " + DB.toUpperCase() + " - Conexión a base de datos fallida");
            System.err.println(e.getSQLState());
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println(DATABASE + "_FAILURE: " + DB.toUpperCase() + " - Conexión a base de datos fallida");
            System.err.println(DATABASE + "_FAILURE: " + DB.toUpperCase() + " - No se encuentra cargado el driver jdbc");
            System.err.println(e.getMessage());
        }
        return conexion;
    }

    @Override
    public UsuarioDAO getUsuarioDAO() {
        return new SqlDbUsuarioImpl();
    }

    @Override
    public EquipoDAO getEquipoDAO() {
        return new SqlDbEquipoImpl();
    }

    @Override
    public GrupoDAO getGrupoDAO() {
        return new SqlDbGrupoImpl();
    }

    @Override
    public MensajeDAO getMensajeDAO() {
        return new SqlDbMensajeImpl();
    }

    @Override
    public SesionDAO getSesionDAO() {
        return new SqlDbSesionImpl();
    }

    @Override
    public ConversacionDAO getConversacionDAO() {
        return new SqlDbConversacionImpl();
    }
    
}