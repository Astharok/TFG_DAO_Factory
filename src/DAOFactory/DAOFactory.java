/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOFactory;

import Interfaces.IFactory;

/**
 *
 * @author Jose Raimundo Montes Lopez
 */
public abstract class DAOFactory implements IFactory{
    
    public static final int MY_SQL = 1;
    
    /**
    * Selecciona la base de datos con la que trabajar
    * DAOFactory.MY_SQL
    *
    * @param db Parametro de seleccion de base de datos.
    * @return DAOFactory La factoria de base de datos seleccionada.
    */
    public static DAOFactory getDAOFactory(Integer db) {
        switch (db) {
            case MY_SQL:
                return new MySqlDbDAOFactory();
            default:
                return null;
        }
    }
    
}
