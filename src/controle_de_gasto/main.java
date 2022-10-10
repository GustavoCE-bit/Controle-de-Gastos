package controle_de_gasto;

import java.sql.SQLException;

/**
 *
 * @author gusta
 */
public class main {
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Main_Menu menu = new Main_Menu();
        menu.setVisible(true);
        System.out.println("oi");
    }    
}