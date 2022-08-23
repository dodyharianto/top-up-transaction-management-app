package main;

public class Produk <KodeProduk, Operator, Nominal, Stok>
{
	private KodeProduk kode_produk;
	private Operator operator;
	private Nominal nominal;
	private Stok stok;
	
	public Produk (KodeProduk kode_produk, Operator operator, Nominal nominal, Stok stok)
	{
		this.kode_produk = kode_produk;
		this.operator = operator;
		this.nominal = nominal;
		this.stok = stok;
	}

	public KodeProduk getKode_produk() 
	{
		return kode_produk;
	}

	public void setKode_produk(KodeProduk kode_produk) 
	{
		this.kode_produk = kode_produk;
	}

	public Operator getOperator() 
	{
		return operator;
	}

	public void setOperator(Operator operator) 
	{
		this.operator = operator;
	}

	public Nominal getNominal() 
	{
		return nominal;
	}

	public void setNominal(Nominal nominal) 
	{
		this.nominal = nominal;
	}

	public Stok getStok() 
	{
		return stok;
	}

	public void setStok(Stok stok) 
	{
		this.stok = stok;
	}
	
}
