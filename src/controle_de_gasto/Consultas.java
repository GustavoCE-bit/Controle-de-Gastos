package controle_de_gasto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author gusta
 */
public class Consultas {
    
    public Connection startConn(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/gusta/OneDrive/projetos de midia/programas/java/Controle_de_Gasto/database/Gastos.db");
            return conn;
        }
        catch (Exception erro){
            System.out.println("Erro ao iniciar conexão");
            System.out.println(erro);
        }
        return null;
    }
    
    public int getNewID(String table) throws SQLException{
        int newID = 0;
        Connection conn = null;
        try{
            conn = startConn();
            String getID = ("SELECT id FROM "+table+" WHERE id = (SELECT MAX (id) FROM "+table+")");
            
            PreparedStatement ps = conn.prepareStatement(getID);
            ResultSet rs = ps.executeQuery();
            
            if(rs.isClosed()){
                newID = 1;
            }else{
                newID = rs.getInt("id")+1;
            }
            return newID;
        }
        catch(Exception erro){
            System.out.println("Erro ao gerar novo ID");
            System.out.println(erro);
            return 0;
        }finally{
            conn.close();
        }
    }
    
    public int getMesID(String mes) throws SQLException{
        Connection conn = null;
        try{
            conn = startConn();
            String getID = "SELECT id FROM gastoTotalMes WHERE(mes = ?)";
            
            PreparedStatement ps = conn.prepareStatement(getID);
            ps.setString(1, mes);
            
            ResultSet rs = ps.executeQuery();
            if(rs.isClosed()){
                String newMes = "INSERT INTO gastoTotalMes(id, mes, gasto) VALUES(?, ?, ?)";
                
                ps = conn.prepareStatement(newMes);
                int id = getNewID("gastoTotalMes");
                ps.setInt(1, id);
                ps.setString(2, mes);
                ps.setDouble(3, 0);
                
                ps.executeUpdate();
                return id;
            }else{
                int id = rs.getInt("id");
                return id;
            }
        }
        catch(Exception erro){
            System.out.println("Erro ao retornar ID do mes");
            System.out.println(erro);
            return 0;
        }finally{
            conn.close();
        }
    }
    
    public int getDiaID(int dia, String mes) throws SQLException{
        Connection conn = null;
        try{
            int mesID = getMesID(mes);
            
            conn = startConn();
            String getID = "SELECT id FROM gastoTotalDia WHERE(dia = ? AND mes = ?)";
            
            PreparedStatement ps = conn.prepareStatement(getID);
            ps.setInt(1, dia);
            ps.setInt(2, mesID);
            ResultSet rs = ps.executeQuery();
            
            if(rs.isClosed()){
                String newDia = "INSERT INTO gastoTotalDia(id, dia, mes, gasto) VALUES(?, ?, ?, ?)";
                ps = conn.prepareStatement(newDia);
                
                int id = getNewID("gastoTotalDia");
                ps.setInt(1, id);
                ps.setInt(2, dia);
                ps.setInt(3, mesID);
                ps.setDouble(4, 0);
                
                ps.executeUpdate();
                return id;
            }else{
                int id = rs.getInt("id");
                return id;
            }
        }
        catch(Exception erro){
            System.out.println("Erro ao retornar ID do dia");
            System.out.println(erro);
            return 0;
        }finally{
            conn.close();
        }
    }
    
    public String getDia(int dia_ID) throws SQLException{
        Connection conn = null;
        try {
            String dia;
            conn = startConn();
            
            String getDia = ("SELECT * FROM gastoTotalDia WHERE id = " +dia_ID);
            PreparedStatement ps = conn.prepareStatement(getDia);
            ResultSet rs = ps.executeQuery();
            dia = String.valueOf(rs.getInt("dia"));
            
            String getMes = ("SELECT * FROM gastoTotalMes WHERE id = " +rs.getInt("mes"));
            ps = conn.prepareStatement(getMes);
            rs = ps.executeQuery();
            dia += ("/"+rs.getString("mes"));
            
            return dia;
        } catch (SQLException ex) {
            System.out.println("Erro ao retornar dia");
            System.out.println(ex);
            return null;
        }finally{
            conn.close();
        }
    }
    
    public double getValorGasto(int id, String table) throws SQLException{
        Connection conn = null;
        try{
            conn = startConn();
            String colum;
            if(table == "gastos"){
                colum = "valor_total";
            }else{
                colum = "gasto";
            }
            String getGasto = ("Select "+colum+" FROM "+table+" WHERE id = "+id);
        
            PreparedStatement ps = conn.prepareStatement(getGasto);
            ResultSet rs = ps.executeQuery();
            
            double gasto = rs.getDouble(colum);
            return gasto;
        }
        catch(Exception erro){
            System.out.println("Erro ao retornar valor do gasto");
            System.out.println(erro);
            return 0;
        }finally{
            conn.close();
        }
    }
    
    public void getGasto(Gasto gasto) throws SQLException{
        Connection conn = null;
        try{
            conn = startConn();
            
            String getGastos = "SELECT * FROM gastos WHERE(descricao LIKE '%"+gasto.getDescricao()+"%')";
            PreparedStatement ps = conn.prepareStatement(getGastos);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
               System.out.println(rs.getString("descricao")+" R$ "+rs.getDouble("valor"));
            }
        }catch(SQLException erro){
            System.out.println("Erro ao retornar gasto");
            System.out.println(erro);
        }finally{
            conn.close();
        }
    }
    
    public ArrayList<Gasto> getAllGastos() throws SQLException{
        Connection conn = null;
        try{
            conn = startConn();
            
            String getGastos = "SELECT * FROM gastos";
            PreparedStatement ps = conn.prepareStatement(getGastos);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<Gasto> result = new ArrayList<>();
            while(rs.next()){
                Gasto gasto = new Gasto();
                gasto.setId(rs.getInt("id"));
                gasto.setDescricao(rs.getString("descricao"));
                gasto.setDiaID(rs.getInt("dia"));
                gasto.setValor(rs.getDouble("valor"));
                gasto.setAbatimento_val(rs.getDouble("abatimento_val"));
                gasto.setJuros_val(rs.getDouble("juros_val"));
                gasto.setValor_total(rs.getDouble("valor_total"));
                result.add(gasto);
            }
            return result;
        }catch(Exception erro){
            System.out.println("Erro ao retornar gasto");
            System.out.println(erro);
            return null;
        }finally{
            conn.close();
        }
    }
    
    public ArrayList<Gasto_Diario> getAllGastosDiarios() throws SQLException{
        Connection conn = null;
        try{
            conn = startConn();
            
            String getGastos = "SELECT * FROM gastoTotalDia";
            PreparedStatement ps = conn.prepareStatement(getGastos);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<Gasto_Diario> result = new ArrayList<>();
            while(rs.next()){
                Gasto_Diario gasto = new Gasto_Diario();
                gasto.setDia(getDia(rs.getInt("id")));
                gasto.setValor(rs.getDouble("gasto"));
                result.add(gasto);
            }
            return result;
        }catch(Exception erro){
            System.out.println("Erro ao retornar gasto");
            System.out.println(erro);
            return null;
        }finally{
            conn.close();
        }
    }
    
    public ArrayList<Gasto_Mensal> getAllGastosMensais() throws SQLException{
        Connection conn = null;
        try{
            conn = startConn();
            
            String getGastos = "SELECT * FROM gastoTotalMes";
            PreparedStatement ps = conn.prepareStatement(getGastos);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<Gasto_Mensal> result = new ArrayList<>();
            while(rs.next()){
                Gasto_Mensal gasto = new Gasto_Mensal();
                gasto.setMes(rs.getString("mes"));
                gasto.setValor(rs.getDouble("gasto"));
                result.add(gasto);
            }
            return result;
        }catch(SQLException ex) {
            System.out.println("Erro ao retornar gasto");
            System.out.println(ex);
            return null;        
        }finally{
            conn.close();
        }
    }
    
    public boolean updateGasto(Gasto gasto) throws SQLException{
        Connection conn = null;
        try{
            conn = startConn();
            
            String updateItem = ("UPDATE gastos SET descricao = '"+gasto.getDescricao()+"', dia = "+gasto.getDiaID()+", valor = "+gasto.getValor()+", abatimento = "+gasto.isAbatimento()+", abatimento_val = "+gasto.getAbatimento_val()+", juros = "+gasto.isJuros()+", juros_val = "+gasto.getJuros_val()+", valor_total = "+gasto.getValor_total()+" WHERE id = "+gasto.getId());
            PreparedStatement ps = conn.prepareStatement(updateItem);
            
            ps.executeUpdate();
            
            updateValorGasto(gasto.getDiaID(),"gastoTotalDia");
            updateValorGasto(getMesID(gasto.getMes()),"gastoTotalMes"); 
            return true;
        }catch(Exception erro){
            System.out.println("Erro ao atualizar gasto");
            System.out.println(erro);
            return false;
        }finally{
            conn.close();
        }
    }
    
    public boolean deleteGasto(Gasto gasto) throws SQLException{
        Connection conn = null;
        try{
            conn = startConn();
        
            String deleteItem = ("DELETE FROM gastos WHERE id = ?");
            PreparedStatement ps = conn.prepareStatement(deleteItem);

            ps.setInt(1, gasto.getId());

            ps.executeUpdate();

            updateValorGasto(gasto.getDiaID(),"gastoTotalDia");
            updateValorGasto(getMesID(gasto.getMes()),"gastoTotalMes");
            return true;
        }catch(Exception erro){
            System.out.println("Erro ao deletar gasto");
            System.out.println(erro);
            return false;
        }finally{
            conn.close();
        }
    }
    
    public void updateValorGasto(int id, String table) throws SQLException{
        Connection conn = null;
        try{
            conn = startConn();
            String targetTable;
            String targetID;
            String targetColum;
            if(table == "gastoTotalDia"){
                targetTable = "gastos";
                targetID = "dia";
                targetColum = "valor_total";
            }else{
                targetTable = "gastoTotalDia";
                targetID = "mes";
                targetColum = "gasto";
            }
            String getAllGasto = ("SELECT "+targetColum+" FROM "+targetTable+" WHERE "+targetID+" = "+id);
            
            PreparedStatement ps = conn.prepareStatement(getAllGasto);
           
            ResultSet rs = ps.executeQuery();
            double val = 0;
            while(rs.next()){
                val += rs.getDouble(targetColum);
            }
            
            String updateGasto = "UPDATE "+table+" SET gasto= "+val+" WHERE id = "+id;
            
            ps = conn.prepareStatement(updateGasto);
            ps.executeUpdate();
        } catch (Exception erro) {
            System.out.println("Erro ao atualizar gasto");
            System.out.println(erro);
        }finally{
            conn.close();
        }
    }
    
    public boolean inserirGasto(Gasto gasto) throws SQLException{
        Connection conn = null;
        try{
            conn = startConn();
        
            String insertItem = "INSERT INTO gastos(id, descricao, dia, valor, abatimento, abatimento_val, juros, juros_val, valor_total) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertItem);
        
            ps.setInt(1, gasto.getId());
            ps.setString(2, gasto.getDescricao());
            ps.setInt(3,gasto.getDiaID());
            ps.setDouble(4, gasto.getValor());
            ps.setBoolean(5, gasto.isAbatimento());
            ps.setDouble(6, gasto.getAbatimento_val());
            ps.setBoolean(7, gasto.isJuros());
            ps.setDouble(8, gasto.getJuros_val());
            ps.setDouble(9, gasto.getValor_total());
        
            ps.executeUpdate();
                    
            updateValorGasto(gasto.getDiaID(),"gastoTotalDia");
            updateValorGasto(getMesID(gasto.getMes()),"gastoTotalMes");
            return true;
        }
        catch(Exception erro){
            System.out.println("Erro ao registrar novo gasto");
            System.out.println(erro);
            return false;
        }finally{
            conn.close();
        }
    }        
}