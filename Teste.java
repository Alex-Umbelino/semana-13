

import java.sql.Connection;
import java.sql.SQLException;

public class Teste {
	/**
	 * Propositalmente sem acentuacao
	 */
   public static void main(String[] args) {
      Connection conn = null;
      Temperatura temperatura;
   
      try {
         ConexaoBD bd = new ConexaoBD();
         conn = bd.conectar();
         conn.setAutoCommit(false);
      
      	// *** Inclusao de 100 temperaturas aleatorias entre 0 e 40 graus ***
         for(int i = 0; i < 100; i++){
        	 temperatura = new Temperatura();
        	 //nao vai configurar o id por causa do autoincremento
        	 temperatura.setValor(40*Math.random());
        	 temperatura.incluir(conn);
         }
         conn.commit();
         Termometro termo = new Termometro();
         //pega as temperaturas do ultimos 30 dias
         Temperatura[] temps = termo.ultimosDias(conn, 300);
         //imprime as temperaturas
         for(int i = 0; i < temps.length; i++){
        	 System.out.println(temps[i]);
         }
      } 
      catch (Exception e) {
         e.printStackTrace();
         if (conn != null) {
            try {
               conn.rollback();
            } 
            catch (SQLException e1) {
               System.out.print(e1.getStackTrace());
            }
         }
      } 
      finally {
         if (conn != null) {
            try {
               conn.close();
            } 
            catch (SQLException e1) {
               System.out.print(e1.getStackTrace());
            }
         }
      }
   }
}