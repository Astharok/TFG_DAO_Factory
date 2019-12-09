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
    
    /**
    * Devuelve el DAO de grupos
    *
    * @return GrupoDAO - El DAO de grupos.
    */
    public GrupoDAO getGrupoDAO();
    
    /**
    * Devuelve el DAO de mensajes
    *
    * @return GrupoDAO - El DAO de grupos.
    */
    public MensajeDAO getMensajeDAO();
    
    /**
    * Devuelve el DAO de sesiones
    *
    * @return GrupoDAO - El DAO de grupos.
    */
    public SesionDAO getSesionDAO();
    
    /**
    * Devuelve el DAO de conversaciones
    *
    * @return GrupoDAO - El DAO de grupos.
    */
    public ConversacionDAO getConversacionDAO();
    
    /**
    * Devuelve el DAO de avisos
    *
    * @return AvisoDAO - El DAO de avisos.
    */
    public AvisoDAO getAvisoDAO();
    
}
