package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main 
{
	static Scanner scan = new Scanner(System.in);
	public Main() 
	{
		// Singleton Design Pattern
		DBConnector dbc = DBConnector.get_instance();
		Connection connection = dbc.connect();
		
		boolean running = true;
		while (running)
		{
			System.out.println("MENU UTAMA");
			System.out.println("[1] Lihat, Tambah Stok, dan Pemesanan Produk");
			System.out.println("[2] Pembayaran / Pembatalan Pesanan");
			System.out.println("[3] Lihat Laporan Transaksi Pesanan Berhasil");
			System.out.println("[4] Keluar");
			
			int option = 0;
			do
			{
				System.out.print("Pilih [1 - 4]: ");
				try
				{
					option = scan.nextInt();
				}
				catch (InputMismatchException e)
				{
					System.out.println("");
				}
			} while (option < 1 || option > 4);
			
			Controller c = new Controller();
			switch (option)
			{
			case 1:
				c.lihat_tambah_stok_pemesanan_produk(connection);
				break;
			case 2:
				c.pembayaran_pemesanan_produk(connection);
				break;
			case 3:
				c.lihat_laporan_transaksi_pemesanan_berhasil(connection);
				break;
			case 4:
				try 
				{
					connection.close();
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
				running = false;
				break;
			}
		}
	}

	public static void main(String[] args) 
	{
		new Main();
	}

}
