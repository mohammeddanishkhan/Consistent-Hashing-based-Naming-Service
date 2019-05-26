import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Bootstrap {

	static int id;
	static Socket s;
	static ServerSocket ss;
	static HashMap<Integer, String> map = new HashMap<Integer, String>();
	static ArrayList<Integer> serverIds = new ArrayList<Integer>();

	public static void main(String[] args) {

		ArrayList<String> mylist = new ArrayList<String>();

		int port;

		System.out.println("Enter file name to read");
		Scanner sc = new Scanner(System.in);
		String filename = sc.nextLine();
		try {
			File file = new File(filename);
			String path = file.getAbsolutePath();
			BufferedReader br = new BufferedReader(new FileReader(path));
			String st;
			while ((st = br.readLine()) != null) {
				mylist.add(st);
			}

		} catch (Exception e) {
			System.out.println("File not Found");
		}
		mylist.removeAll(Arrays.asList("", null));

		String[] trial2 = null;

		id = Integer.parseInt(mylist.get(0));
		serverIds.add(id);
		port = Integer.parseInt(mylist.get(1));
		for (int c = 2; c < mylist.size(); c++) {
			String trial = mylist.get(c);
			trial2 = trial.split(" ");
			int g = Integer.parseInt(trial2[0]);
			String h = trial2[1];
			map.put(g, h);

		}

		new writeP().start();
		try {
			ss = new ServerSocket(port);
			while (true) {
				s = ss.accept();
				new readP(s).start();
			}

		} catch (Exception ioe) {
			System.out.println(ioe);
		}

	}
}

class readP extends Thread {
	Socket clientSocket, m, n;
	static Socket d = null;
	DataInputStream dis;
	DataOutputStream dos;
	BufferedReader br;

	readP(Socket s) {
		clientSocket = s;
	}

	public void run() {
		Bootstrap bs2 = new Bootstrap();
		Map<Integer, String> map3 = new TreeMap<Integer, String>(bs2.map);
		while (true) {
			try {
				dis = new DataInputStream(clientSocket.getInputStream());
				dos = new DataOutputStream(clientSocket.getOutputStream());

				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String print = in.readLine();
				PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
				if (print.contains("enter")) {
					int namingServId = Integer.parseInt(print.substring(5, print.length()));

					if (bs2.serverIds.contains(namingServId)) {
						pw.println("Naming Server is already there.");
					} else {
						bs2.serverIds.add(namingServId);
						Collections.sort(bs2.serverIds);

						for (int a = 0; a < bs2.serverIds.size(); a++) {
							if (a == (bs2.serverIds.size() - 1)) {
								pw.println("Successful Entry");
								pw.printf("Range of keys managed by this server = %d - 1023 ", namingServId);
								pw.println("Predecessor nameserver id= " + bs2.serverIds.get(a - 1));
								break;

							} else if (namingServId == bs2.serverIds.get(a)) {
								pw.println("Successful Entry");
								pw.printf("Range of keys managed by this server = %d - %d", namingServId,
										bs2.serverIds.get(a + 1));
								pw.printf("Predecessor nameserver id= %d and Successor nameserver id= %d",
										bs2.serverIds.get(a - 1), bs2.serverIds.get(a + 1));
								pw.println("");
								break;
							}
						}
					}
				}

				else if (print.contains("exit")) {
					int namingServ = Integer.parseInt(print.substring(4, print.length()));
					int del = bs2.serverIds.indexOf(namingServ);
					pw.println("Successful exit");
					bs2.serverIds.remove(del);

				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				break;

			}
		}
	}
}

class writeP extends Thread {

	int getChoice, flag = 0;
	Scanner sc = new Scanner(System.in);

	writeP() {
	}

	public void run() {
		while (true) {
			try {
				do {

					Bootstrap bs = new Bootstrap();
					Map<Integer, String> map2 = new TreeMap<Integer, String>(bs.map);

					System.out.println("Choose");
					System.out.println("1. Lookup Key");
					System.out.println("2. Insert Key");
					System.out.println("3. Delete Key");
					getChoice = sc.nextInt();

					if (getChoice == 1) {
						int printid = 0;
						System.out.println("Enter Key");

						Collections.sort(bs.serverIds);
						int key = sc.nextInt();
						String value = map2.get(key);
						 
							for (int q = 0; q < bs.serverIds.size(); q++) {

								int serverId = bs.serverIds.get(q);
								if (bs.serverIds.size() == 1 ||key>bs.serverIds.get(bs.serverIds.size()-1)) {
									if(value!=null) {
										System.out.println("Value at key " + key + " = " + value);
										System.out.println("Key is handled by server id = " + serverId);
									}
									else System.out.println("Key not found");
									break;
								} else if (serverId >= key) {
									if(value!=null) {
										System.out.println("Value at key " + key + " = " + value);
										System.out.println("Key is handled by server id = " + serverId);
									}
									else System.out.println("Key not found");
									break;
								} 
								
								else {
									System.out.println("looked up key at serverId " + serverId + " key was not here");
								}
							}
							
						
					} else if (getChoice == 2) {
						int printid = 0;
						System.out.println("Enter Key");
						int key = sc.nextInt();
						System.out.println("Enter Value");
						Scanner sc2 = new Scanner(System.in);
						String value = sc2.nextLine();
						bs.map.put(key, value);
						for (int q = 0; q < bs.serverIds.size(); q++) {

							int serverId = bs.serverIds.get(q);
							if (bs.serverIds.size() == 1 || key>bs.serverIds.get(bs.serverIds.size()-1)) {
								// System.out.println("Value at key "+key+" = "+value);
								System.out.println("Successful Insertion");
								System.out.println("Key is handled by server id = " + serverId);
								break;
							} else if (serverId >= key) {
								// System.out.println("Value at key "+key+" = "+value);
								System.out.println("Successful Insertion");
								System.out.println("Key is handled by server id = " + serverId);
								break;
							} else {
								System.out.println("Passed throught serverId " + serverId);
							}
						}
					} else if (getChoice == 3) {
						System.out.println("Delete Key");
						int key = sc.nextInt();
						String value = map2.get(key);
						if (value == null) {
							System.out.println("Key not Found");
						} else {
							bs.map.remove(key);
							for (int q = 0; q < bs.serverIds.size(); q++) {

								int serverId = bs.serverIds.get(q);
								if (bs.serverIds.size() == 1 || key>bs.serverIds.get(bs.serverIds.size()-1)) {
									// System.out.println("Value at key "+key+" = "+value);
									System.out.println("Key is handled by server id = " + serverId);
									System.out.println("Successful Deletion");
									break;
								} else if (serverId >= key) {
									// System.out.println("Value at key "+key+" = "+value);
									System.out.println("Key is handled by server id = " + serverId);
									System.out.println("Successful Deletion");
									break;
								} else {
									System.out.println("Passed throught serverId " + serverId);
								}
							}
							
						}

						
					} else {
						flag = 1;
					}
				} while (flag != 1);
			} catch (Exception e) {
				System.out.println(e);
				break;

			}
		}
	}
}
