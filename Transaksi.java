package main;

public class Transaksi <KodeTransaksi, KodeProduk, NomorHP, WaktuPesanan, WaktuPembayaran, StatusBayar>
{
	private KodeTransaksi kode_transaksi;
	private KodeProduk kode_produk;
	private NomorHP nomor_hp;
	private WaktuPesanan waktu_pesanan;
	private WaktuPembayaran waktu_pembayaran;
	private StatusBayar status_bayar;
	
	public Transaksi(KodeTransaksi kode_transaksi, KodeProduk kode_produk, NomorHP nomor_hp, WaktuPesanan waktu_pesanan, WaktuPembayaran waktu_pembayaran, StatusBayar status_bayar) 
	{
		this.kode_transaksi = kode_transaksi;
		this.kode_produk = kode_produk;
		this.nomor_hp = nomor_hp;
		this.waktu_pesanan = waktu_pesanan;
		this.waktu_pembayaran = waktu_pembayaran;
		this.status_bayar = status_bayar;
	}

	public KodeTransaksi getKode_transaksi() 
	{
		return kode_transaksi;
	}

	public void setKode_transaksi(KodeTransaksi kode_transaksi) 
	{
		this.kode_transaksi = kode_transaksi;
	}

	public KodeProduk getKode_produk() 
	{
		return kode_produk;
	}

	public void setKode_produk(KodeProduk kode_produk) 
	{
		this.kode_produk = kode_produk;
	}

	public NomorHP getNomor_hp() 
	{
		return nomor_hp;
	}

	public void setNomor_hp(NomorHP nomor_hp) 
	{
		this.nomor_hp = nomor_hp;
	}

	public WaktuPesanan getWaktu_pesanan() 
	{
		return waktu_pesanan;
	}

	public void setWaktu_pesanan(WaktuPesanan waktu_pesanan) 
	{
		this.waktu_pesanan = waktu_pesanan;
	}

	public WaktuPembayaran getWaktu_pembayaran() 
	{
		return waktu_pembayaran;
	}

	public void setWaktu_pembayaran(WaktuPembayaran waktu_pembayaran) 
	{
		this.waktu_pembayaran = waktu_pembayaran;
	}

	public StatusBayar getStatus_bayar() 
	{
		return status_bayar;
	}

	public void setStatus_bayar(StatusBayar status_bayar) 
	{
		this.status_bayar = status_bayar;
	}

}
