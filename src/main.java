import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import info.debatty.java.stringsimilarity.*;
public class main {

	public static void main(String[] args) {
		
		String[] tableAry = new String[8];
		String[] tableAry1 = new String[8];
		
		JaroWinkler l = new JaroWinkler();
		
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		
		String index1="";
		String index2="";

		
		int i = 0;
		int j = 0;
		try {
			
			//This is the stuff that connects to the mysql database
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection(
            		"jdbc:mysql://localhost:3306/employees", //this connects to the table
            		"user1",//user
            		"Password#1");//password
                        
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("SELECT table_name FROM information_schema.tables "
            		+ "WHERE table_schema ='employees';");
            
            //gets table names and puts them in an arry
            while(rs.next()) { 
            tableAry[i]=rs.getString(1);
            i++;
            }
            
            Connection con1=DriverManager.getConnection(
            		"jdbc:mysql://localhost:3306/employees1", //this connects to the table
                    "user1",//user
                    "Password#1");//password
            Statement stmtEmp1TableGather = con1.createStatement();
            ResultSet rsEmp1Table = stmtEmp1TableGather.executeQuery("SELECT table_name FROM information_schema.tables "
            		+ "WHERE table_schema ='employees';");

            
            while(rsEmp1Table.next()) { 
            tableAry1[j]=rsEmp1Table.getString(1);
            j++;
            }
            
            Statement stmt1=con.createStatement(); 
            ResultSet rs1;
            
            Statement stmt2=con.createStatement();
            ResultSet rs2;
            
            
            Statement stmt1Db2=con1.createStatement(); 
            ResultSet rs1Db2;
            
            Statement stmt2Db2=con1.createStatement();
            ResultSet rs2Db2;

            String columnName;
            String columnName1;
            
            for(int k=1; k <=tableAry.length-7; k++)
            {
            	rs1=stmt1.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'" + 
            tableAry[k] + "';");
            	rs1Db2=stmt1Db2.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'" + 
            tableAry1[k] + "';");
            	
            	while(rs1.next()&&rs1Db2.next())
            	{
            		//index1="";
            		//index2="";
            		
            		columnName = rs1.getString(4);
            		columnName1 = rs1Db2.getString(4);
            		
            		
            		rs2=stmt2.executeQuery("SELECT DISTINCT " + columnName + " FROM " + tableAry[k] + 
            				" ORDER BY " + columnName + " ASC;");
            		
            		rs2Db2 = stmt2Db2.executeQuery("SELECT DISTINCT " + columnName + " FROM " + tableAry1[k] + 
            				" ORDER BY " + columnName1 + " ASC;");
            		
            		while(rs2.next())// && rs2Db2.next())
            		{
            			index1=index1+rs2.getString(1)+", ";
            			//sb1.append(rs2.getString(1));
            			//sb1.append(", ");
            		}
            		
            		while(rs2Db2.next())
            		{
            			index2=index2+rs2Db2.getString(1)+", ";
            			//sb2.append(rs2Db2.getString(1));
            		}
            		
            		//index1 = sb1.toString();
            		//index2 = sb2.toString();
            		
            		System.out.println("Table: " + tableAry[k] + "  Column: " + columnName);
            		System.out.println(index1);
            		System.out.print("\n");
            		
            		System.out.println("Table: " + tableAry1[k] + "  Column: " + columnName1);
            		System.out.println(index2);
            		System.out.println("Similarty: " + l.similarity(index1, index2) + "\n");
            		
            		sb1.setLength(0);
            		
            		index1="";
            		index2="";
            		
            	}
            }
            
            //This will close the connect, put this after everything for sql is done
            con.close();  con1.close();
            System.out.println("done!");
        }catch(Exception e){ System.out.println(e);}

	}//End main
	
	
}
