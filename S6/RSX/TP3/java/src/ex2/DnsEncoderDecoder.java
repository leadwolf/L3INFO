package ex2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;

public class DnsEncoderDecoder {
	public static void main(String[] args) throws IOException {
		
		//Encodage de l'adresse symbolique
		Scanner sc = new Scanner(System.in);
		Q5 encoder = new Q5();
		byte[] message = encoder.typeAddress(sc);
		
		System.err.print(" get inetaddress by name ... (on peut bien entendu mieux faire) ");
		InetAddress destination;
		try {
			destination = InetAddress
					.getByName("193.49.225.15"/*
												 * ou 8.8.8.8 ou celui dans
												 * /etc/resolv.conf ...
												 */);
		} catch (Exception e) {
			System.err.println("[error] :" + e.getMessage());
			return;
		}
		System.err.println("[ok]");

		/* 2) creation d'un DatagramPacket pour l'envoi de la question DNS */
		System.err.println(" preparing  datagrampacket, message size : " + message.length);
		DatagramPacket dp = new DatagramPacket(message, message.length, destination, 53);

		/* 3) creation d'un DatragramSocket (port au choix ) */
		System.err.print(" create datagram socket  ... ");
		DatagramSocket ds;
		try {
			ds = new DatagramSocket();
		} catch (Exception e) {
			System.err.println("[error] :" + e.getMessage());
			return;
		}
		System.err.println("[ok]");

		/* 4) et envoi du packet */
		System.err.print(" send datagram ... ");
		try {
			ds.send(dp);
		} catch (Exception e) {
			System.err.println("[error] :" + e.getMessage());
			return;
		}
		System.err.println("[ok]");

		/* 5) reception du packet */
		dp = new DatagramPacket(new byte[512], 512);
		System.err.print(" receive datagram ... ");
		try {
			ds.receive(dp);
		} catch (Exception e) {
			System.err.println("[error] :" + e.getMessage());
			return;
		}
		System.err.println("[ok]");
		
		/* affichage complet du packet recu (pas tres lisible ...) */
		byte[] rec = dp.getData();
		System.out.println("- message length : " + dp.getLength());
		System.out.println("- message : \n" + new String(rec, 0, dp.getLength()));

		/* affichage des bytes */
		for (int i = 0; i < dp.getLength(); i++) {
			System.out.print("," + Integer.toHexString((rec[i]) & 0xff));
			if ((i + 1) % 16 == 0)
				System.out.println("");
		}
		System.out.println();
		
		Q4.decode(dp.getData());
		
	}
}
