import java.util.*;
import java.io.*;
import java.net.*;

public class Naming{
	
	static Socket s;
	static DataInputStream dis;
	static DataOutputStream dos;
	static BufferedReader br;
	static PrintWriter pw;
	
	public static void main(String[] args){
		ArrayList<String> mylist = new ArrayList<String>();
		int id,  port, getChoice, flag=0;
		String host, temphost;
		
		System.out.println("Enter file name to read");
		Scanner sc = new Scanner(System.in);
		String filename = sc.nextLine();
		try{
			File file = new File(filename);
			String path = file.getAbsolutePath();
			BufferedReader br = new BufferedReader(new FileReader(path));
			String st;
			while ((st = br.readLine()) != null){
				mylist.add(st);
			}
		}catch(Exception e){System.out.println("File not Found");}
		
		id = Integer.parseInt(mylist.get(0));
		temphost = mylist.get(1);
		host = temphost.substring(0, temphost.length() - 5);
		port = Integer.parseInt(temphost.substring(temphost.length() - 4));
		
		try	{
			s=new Socket(host,port);
			dis=new DataInputStream(s.getInputStream());
			pw=new PrintWriter(s.getOutputStream(), true);
			new readS(s).start();
			
		}
		catch(Exception HO){System.out.println("Server Not Found"+HO);}
		
		while(true){
			try{
				do{
					System.out.println("Choose");
					System.out.println("1. Enter");
					System.out.println("2. Exit");
					System.out.println("(Enter only number) Choose what you want to do...");
					getChoice = sc.nextInt();
					
					if(getChoice == 1){
						pw.println("enter" +id);
					}
					else if(getChoice == 2){
						pw.println("exit"+id);
						
					}
					else{
						flag=1;
					}
					
				}while(flag!=1);
			}catch(Exception e){}
		}
	}
}


class readS extends Thread
{
	Socket clientSocket;
	
	readS(Socket s)
	{
		clientSocket=s; 
	}
	
	public void run()
	{
		while(true){
			try{
				BufferedReader in =new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String print =in.readLine();
				//System.out.println(print);
				
			}catch(Exception e){}
		}
	}
}