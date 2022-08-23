package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Controller 
{
	static Scanner scan = new Scanner(System.in);
	public void lihat_tambah_stok_pemesanan_produk (Connection connection)
	{
		System.out.println("\nLIHAT, TAMBAH STOK, DAN PEMESANAN PRODUK\n");
		String query = "SELECT * FROM produk";
		
		Statement st;
		ResultSet rs;
		PreparedStatement ps;
		try 
		{
			st = connection.createStatement();
			rs = st.executeQuery(query);
			
			ArrayList<Produk<String, String, Integer, Integer>> produk_list = new ArrayList<>();
			while (rs.next())
			{
				String kode_produk = rs.getString("KodeProduk");
				String operator = rs.getString("Operator");
				Integer nominal = rs.getInt("Nominal");
				Integer stok = rs.getInt("Stok");
				
				Produk<String, String, Integer, Integer> produk = new Produk<String, String, Integer, Integer>(kode_produk, operator, nominal, stok);
				produk_list.add(produk);
			}
			
			display_line(65);
			System.out.printf("| %-15s | %-15s | %-15s | %-5s |\n", "KodeProduk", "Operator", "Nominal", "Stok");
			display_line(65);
			for (Produk<String, String, Integer, Integer> p : produk_list)
			{
				System.out.printf("| %-15s | %-15s | %-15d | %-5d |\n", p.getKode_produk(), p.getOperator(), p.getNominal(), p.getStok());
			}
			display_line(65);
			
			String kode_produk = "";
			System.out.println("Masukkan Kode Produk untuk menambahkan stok, pemesanan produk, atau '-' untuk kembali ke Menu Utama");
			
			int jumlah = -1;
			do
			{
				System.out.print("\nKode Produk: ");
				kode_produk = scan.nextLine();
				if (kode_produk.equals("-"))
				{
					return;
				}
				
				query = String.format("SELECT COUNT(*) AS Jumlah FROM produk WHERE KodeProduk = '%s'", kode_produk);
				rs = st.executeQuery(query);
				while (rs.next())
				{
					jumlah = rs.getInt("Jumlah");
				}
				
				if (jumlah == 0)
				{
					System.out.println("Kode tidak ditemukan");
				}
			} while (jumlah == 0); 
			// here
			query = String.format("SELECT * FROM produk WHERE KodeProduk = '%s'", kode_produk);
			rs = st.executeQuery(query);
			int stok_sekarang = 0;
			while (rs.next())
			{
				System.out.println("Operator: " + rs.getString("Operator"));
				System.out.println("Nominal: " + rs.getInt("Nominal"));
				stok_sekarang = rs.getInt("Stok");
				System.out.println("Stok: " + stok_sekarang);
				System.out.println("");
			}
			
			// Produk sudah ditemukan
			System.out.println("MENU");
			System.out.println("[1] Tambah Stok Produk");
			System.out.println("[2] Pemesanan Produk");
			System.out.println("[3] Kembali ke Menu Utama");
			int option = 0;
			do
			{
				System.out.println("Pilih [1 - 3]:");
				try
				{
					option = scan.nextInt();
				}
				catch (InputMismatchException e)
				{
					System.out.print("");					
				}
			} while (option < 1 || option > 3);
			
			int stok_maksimal = 100;
			switch (option)
			{
			case 1: // Tambah Stok Produk
				if (stok_sekarang == stok_maksimal)
				{
					System.out.println("Stok tidak dapat ditambahkan lagi karena sudah penuh");
				}
				else
				{
					Integer stok_tambahan = 0;
					Integer stok_yang_bisa_ditambahkan = stok_maksimal - stok_sekarang;
					do
					{
						System.out.printf("Tambahkan stok [1 - %d]: ", stok_yang_bisa_ditambahkan);
						try
						{
							stok_tambahan = scan.nextInt();
						}
						catch (InputMismatchException e)
						{
							System.out.println("");
						}
					} while (stok_tambahan < 0 || stok_tambahan > stok_yang_bisa_ditambahkan);

					query = String.format("UPDATE produk SET Stok = %d WHERE KodeProduk = '%s'", stok_sekarang + stok_tambahan, kode_produk);
					st.executeUpdate(query);
					
					stok_sekarang += stok_tambahan;
					stok_yang_bisa_ditambahkan = stok_maksimal - stok_sekarang;
					
					System.out.println("Stok berhasil ditambahkan\n");
				}
				
				System.out.println("Tekan ENTER untuk kembali ke Menu Utama");
				scan.nextLine();
				break;
			case 2: // Pemesanan Produk
				if (stok_sekarang == 0)
				{
					System.out.println("Pemesanan tidak dapat dilakukan karena stok kosong\n");
					System.out.println("Tekan ENTER untuk kembali ke Menu Utama");
					scan.nextLine();
					return;
				}
				
				String nomor_hp = "";
				do
				{
					// com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column 'WaktuPesanan' at row 1
					scan.nextLine();
					System.out.print("Masukkan Nomor HP [10-13 digit, angka semua, diawali 08]: ");
					nomor_hp = scan.nextLine();
					if (!nomor_hp.substring(0, 2).equals("08"))
					{
						System.out.println("Nomor HP harus dimulai dengana angka 08");
					}
					else if (nomor_hp.length() < 10 || nomor_hp.length() > 13)
					{
						System.out.println("Nomor HP harus 10-13 digit!");
					}
				} while (!nomor_hp_valid(nomor_hp));
				
				String waktu_pemesanan = java.time.LocalDateTime.now().toString().substring(0, 24);
				
				query = "SELECT COUNT(*) AS JumlahTransaksi FROM transaksi";
				rs = st.executeQuery(query);
				int jumlah_transaksi_sekarang = 0;
				while (rs.next())
				{
					jumlah_transaksi_sekarang = rs.getInt("JumlahTransaksi");
				}
				
				query = "INSERT INTO transaksi VALUES (?, ?, ?, ?, NULL, 'BELUM')";
				ps = connection.prepareStatement(query);
				ps.setInt(1, jumlah_transaksi_sekarang + 1);
				ps.setString(2, kode_produk);
				ps.setString(3, nomor_hp);
				ps.setString(4, waktu_pemesanan);
				
				ps.execute();
				ps.close();
				
				System.out.println("Pemesanan berhasil, segera lakukan pembayaran\n");
				System.out.println("Tekan ENTER untuk kembali ke Menu Utama");
				scan.nextLine();
				return;
			case 3: // Kembali ke Menu Utama
				scan.nextLine();
				return;
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void pembayaran_pemesanan_produk (Connection connection)
	{
		System.out.println("\nPEMBAYARAN DAN PEMESANAN PRODUK\n");
		Statement st;
		ResultSet rs;
		try 
		{
			String query = "SELECT t.KodeTransaksi, p.KodeProduk, t.NomorHP, p.Nominal, p.Stok FROM produk AS p \r\n"
					+ "JOIN transaksi AS t ON p.KodeProduk = t.KodeProduk\r\n"
					+ "WHERE t.StatusBayar = 'BELUM'";
			st = connection.createStatement();
			rs = st.executeQuery(query);
			
			display_line(66);
			System.out.printf("|%-15s|%-15s|%-15s|%-10s|%-5s|\n", "Kode Transaksi", "Kode Produk", "Nomor HP", "Nominal", "Stok");
			display_line(66);
			
			Integer kode_transaksi;
			String kode_produk;
			String nomor_hp;
			Integer nominal;
			Integer stok;
			while (rs.next())
			{
				kode_transaksi = rs.getInt("KodeTransaksi");
				kode_produk = rs.getString("KodeProduk");
				nomor_hp = rs.getString("NomorHP");
				nominal = rs.getInt("Nominal");
				stok = rs.getInt("Stok");
				System.out.printf("|%-15d|%-15s|%-15s|%-10d|%-5d|\n", kode_transaksi, kode_produk, nomor_hp, nominal, stok);
			}
			display_line(66);
			System.out.println("");
			
			String kode_transaksi_string = "";
			System.out.println("Masukkan Kode Transaksi untuk membayar, membatalkan pesanan, atau '-' untuk kembali ke Menu Utama");
			
			Integer kode_transaksi_input = 0;
			int jumlah = -1;
			do
			{
				System.out.print("\nKode Transaksi: ");
				kode_transaksi_string = scan.nextLine();
				if (kode_transaksi_string.equals("-"))
				{
					return;
				}

				kode_transaksi_input = Integer.parseInt(kode_transaksi_string);
				query = String.format("SELECT COUNT(*) AS Jumlah FROM transaksi WHERE KodeTransaksi = %d", kode_transaksi_input);
				rs = st.executeQuery(query);
				while (rs.next())
				{
					jumlah = rs.getInt("Jumlah");
				}
				
				if (jumlah == 0)
				{
					System.out.println("Kode tidak ditemukan");
				}
			} while (jumlah == 0);
			
			query = String.format("SELECT t.KodeTransaksi, p.KodeProduk, t.NomorHP, p.Nominal, p.Stok FROM produk AS p\r\n"
					+ "JOIN transaksi AS t ON p.KodeProduk = t.KodeProduk\r\n"
					+ "WHERE t.KodeTransaksi = %d", kode_transaksi_input);
			rs = st.executeQuery(query);
			
			int stok_sekarang = -1;
			while (rs.next())
			{
				System.out.println("NomorHP: " + rs.getString("NomorHP"));
				System.out.println("Nominal: " + rs.getInt("Nominal"));
				stok_sekarang = rs.getInt("Stok");
				System.out.println("Stok: " + stok_sekarang);
				System.out.println("");
			}
			
			// Transaksi sudah ditemukan
			System.out.println("MENU");
			System.out.println("[1] Pembayaran Pesanan");
			System.out.println("[2] Pembatalan Pesanan");
			System.out.println("[3] Kembali ke Menu Utama");
			int option = 0;
			do
			{
				System.out.println("Pilih [1 - 3]:");
				try
				{
					option = scan.nextInt();
				}
				catch (InputMismatchException e)
				{
					System.out.print("");					
				}
			} while (option < 1 || option > 3);
			
			switch (option)
			{
			case 1: // Pembayaran Pesanan
				if (stok_sekarang == 0)
				{
					System.out.println("Pembayaran tidak dapat dilakukan karena stok sudah habis, lakukan pengisian stok terlebih dahulu");
					System.out.println("Tekan ENTER untuk kembali ke Menu Utama");
					scan.nextLine();
					return;
				}
				
				String kode_produk_utk_bayar = "";
				query = String.format("SELECT KodeProduk FROM transaksi WHERE KodeTransaksi = %d", kode_transaksi_input);
				rs = st.executeQuery(query);
				while (rs.next())
				{
					kode_produk_utk_bayar = rs.getString("KodeProduk");
				}
				
				query = String.format("UPDATE produk SET Stok = %d WHERE KodeProduk = '%s'", stok_sekarang - 1, kode_produk_utk_bayar);
				st.executeUpdate(query);
				stok_sekarang--;
				
				System.out.println("Pembayaran sudah dilakukan, stok produk sudah dikurangi 1\n");
				System.out.println("Tekan ENTER untuk kembali ke Menu Utama");
				scan.nextLine();
				return;
			case 2: // Pembatalan Pesanan
				query = String.format("UPDATE transaksi SET StatusBayar = 'BATAL' WHERE KodeTransaksi = %d", kode_transaksi_input);
				st.executeUpdate(query);

				System.out.println("Tekan ENTER untuk kembali ke Menu Utama");
				scan.nextLine();
				return;
			case 3: // Kembali ke Menu Utama
				return;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public void lihat_laporan_transaksi_pemesanan_berhasil (Connection connection)
	{
		System.out.println("\nLIHAT LAPORAN TRAKSAKSI PESANAN BERHASIL\n");
		Statement st;
		ResultSet rs;
		try 
		{
			String query = "SELECT t.KodeTransaksi, t.NomorHP, p.Nominal FROM produk AS p \r\n"
					+ "JOIN transaksi AS t ON p.KodeProduk = t.KodeProduk\r\n"
					+ "WHERE t.StatusBayar = 'SUDAH'";
			st = connection.createStatement();
			rs = st.executeQuery(query);
			
			display_line(44);
			System.out.printf("|%-15s|%-15s|%-10s|\n", "Kode Transaksi", "Nomor HP", "Nominal");
			display_line(44);
			while (rs.next())
			{
				Integer kode_transaksi = rs.getInt("KodeTransaksi");
				String nomor_hp = rs.getString("NomorHP");
				Integer nominal = rs.getInt("Nominal");
				System.out.printf("|%-15d|%-15s|%-10d|\n", kode_transaksi, nomor_hp, nominal);
			}
			display_line(44);
			
			// Total Transaksi Berhasil
			query = "SELECT COUNT(*) AS TotalTransaksiBerhasil FROM produk AS p \r\n"
					+ "JOIN transaksi AS t ON p.KodeProduk = t.KodeProduk\r\n"
					+ "WHERE t.StatusBayar = 'SUDAH'";
			st = connection.createStatement();
			rs = st.executeQuery(query);
			while (rs.next())
			{
				System.out.println(" TOTAL TRANSAKSI BERHASIL: " + rs.getInt("TotalTransaksiBerhasil"));
			}
			
			// Total Pendapatan
			query = "SELECT SUM(p.Nominal) AS TotalPendapatan FROM produk AS p \r\n"
					+ "JOIN transaksi AS t ON p.KodeProduk = t.KodeProduk\r\n"
					+ "WHERE t.StatusBayar = 'SUDAH'";
			st = connection.createStatement();
			rs = st.executeQuery(query);
			while (rs.next())
			{
				System.out.println(" TOTAL PENDAPATAN: " + rs.getInt("TotalPendapatan"));
			}
			display_line(44);
			
			System.out.println("\nTekan ENTER untuk kembali ke Menu Utama");
			scan.nextLine();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void display_line (int number)
	{
		for (int i = 0; i < number; i++)
		{
			System.out.print("=");
		}
		System.out.println("");
	}
	
	public boolean nomor_hp_valid (String nomor_hp)
	{
		boolean awalan_valid = nomor_hp.substring(0, 2).equals("08");
		boolean panjang_valid = nomor_hp.length() >= 10 && nomor_hp.length() <= 13; // Mengikuti output soal
		return awalan_valid && panjang_valid;
	}
}
