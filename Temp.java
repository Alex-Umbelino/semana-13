import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class Temp extends JFrame implements ActionListener
{ 
  private JLabel jlbDigite ;
  private JTextField txtDigite;
  private JButton limpar,mostrar,sair;
  private JTextArea dig;
  private JScrollPane des;
  
  public void actionPerformed (ActionEvent e)
  {
    if(e.getSource()==mostrar)
    {   Connection conn = null;
      Temperatura temperatura;
     
      try {
         ConexaoBD bd = new ConexaoBD();
         conn = bd.conectar();
         conn.setAutoCommit(false);
      
              
         Termometro termo = new Termometro();
         //pega as temperaturas do ultimos 30 dias
         
          int dia=Integer.parseInt(JOptionPane.showInputDialog("Quantas temperaturas quer ver?"));
          Temperatura[] temps = termo.ultimosDias(conn, dia);
         String temP="";
         //imprime as temperaturas
         for(int i = 0; i < dia; i++){
        	  temP+=" \n"+temps[i];
         }
         
          Double m= termo.media(temps);
         String s = String.format (" %.2f", m);
         
         temP+="\n\nMedia "+s+"°"+" Maior "+termo.maior(temps)+"°"+" Menor "+termo.menor(temps)+"°";
         dig.setText(temP);
         
      } 
      catch (Exception ee) {
         ee.printStackTrace();
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
    if(e.getSource()==limpar)
    {
      dig.setText("");
    }
    if(e.getSource()==sair)
    { 
      System.exit(0);
     }
  }
  
  
  public Temp(Connection conn )
  {
    
   //titulo 
   super ("Histórico de temperatura");
   //painel de digitação
   JPanel painelPrincipal=new JPanel (new GridLayout(2,1));
   
   //painel da digitação
   JPanel painelDigitacao=new JPanel(new FlowLayout());
   //Criando a etiqueta
  // jlbDigite = new JLabel ("TEXTO:");
   
   //CRiando espaço e tamanho da parte de digitar
    dig=new JTextArea("");
     
      Temperatura temperatura;
   
      try {
         ConexaoBD bd = new ConexaoBD();
         conn = bd.conectar();
         conn.setAutoCommit(false);
      
      	// *** Inclusao de 100 temperaturas aleatorias entre 0 e 40 graus ***
         for(int i = 0; i < 100; i++)
         {
        	 temperatura = new Temperatura();
        	 //nao vai configurar o id por causa do autoincremento
        	 temperatura.setValor(40*Math.random());
        	 temperatura.incluir(conn);
         }
         conn.commit();
        
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

  
   
    //---------------------------------------------------------------------
   //Painel dos botoes
      JPanel painelBotoes=new JPanel (new GridLayout(1,3));
      //Criando os botões
       mostrar=new JButton("Buscar temperaturas");
        limpar=new JButton("Limpar");
          sair=new JButton("Sair");
      
      //Adicionando os botoes ao painel botoes
      painelBotoes.add(mostrar);
      painelBotoes.add(limpar);
      painelBotoes.add(sair);
      //Atrinuir observador aos botoes
      mostrar.addActionListener(this);
      limpar.addActionListener(this);
      sair.addActionListener(this);
    //---------------------------------------------------------------------
    
   //Tela toda
   Container retrato=getContentPane();
   
   //----------------------------------
  
  
    
   //-------------------------------- 
    
     retrato.setLayout(new BorderLayout());
     JScrollPane auxNomes=new JScrollPane(dig,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
  retrato.add(auxNomes,BorderLayout.CENTER);
   retrato.add(dig,BorderLayout.CENTER);
   retrato.add(painelBotoes,BorderLayout.SOUTH);
   
   
   
   //Caracteristicas finais
   setSize(700,300);
   
   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   setLocationRelativeTo(null);
   setVisible(true);
   
   
  } 
   //Metodo principal
   public static void main (String [] args)
   {
   
     // obtem conexao com o banco, que sera usada todo o tempo
      ConexaoBD bd = new ConexaoBD();
      try{
         Connection conn = bd.conectar();
         new Temp(conn);
      } catch (SQLException e){
         e.printStackTrace();
      }
   } 

   
}