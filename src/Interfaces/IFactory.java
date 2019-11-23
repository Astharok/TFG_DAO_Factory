package Interfaces;

public interface IFactory {
    
    /**
    * Crea la conexion con la base de datos
    *
    * @return T - La conexion con la base de datos.
    */
    public <T> T crearConexion();
    
    /**
    * Devuelve el DAO de usuarios
    *
    * @return UsuarioDAO - El DAO de usuarios.
    */
    public UsuarioDAO getUsuarioDAO();
    
    /**
    * Devuelve el DAO de equipos
    *
    * @return EquipoDAO - El DAO de equipos.
    */
    public EquipoDAO getEquipoDAO();
    
}
