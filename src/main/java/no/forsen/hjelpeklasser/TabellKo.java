package no.forsen.hjelpeklasser;

import java.util.NoSuchElementException;

public class TabellKo<T> implements Ko<T>
{
	private T[] a; 
	private int fra;
	private int til; 

	public TabellKo( int lengde )
	{
		if( lengde < 1 )
			throw new IllegalArgumentException( "Må ha positiv lengde!" );

		a = (T[]) new Object[ lengde ]; 

		fra = til = 0; 
	}

	public TabellKo()
	{
		this( 8 ); 
	}

	public int antall()
	{
		return fra <= til ? til - fra : a.length + til - fra;
	}

	public void leggInn( T t )
	{
		a[til] = t; 
		til++; 
		if( til == a.length )
			til = 0;
		if( fra == til )
			a = utvidTabell( 2*a.length );
	}

	public T[] utvidTabell( int lengde )
	{
		T[] b = (T[]) new Object[lengde];

		System.arraycopy(a, fra, b, 0, a.length - fra );

		System.arraycopy( a, 0, b, a.length - fra, fra );

		fra = 0; 
		til = a.length; 

		return b; 
	}

	public T taUt()
	{
		if( fra == til )
			throw new NoSuchElementException( "Køen er tom!" );

		T temp = a[fra];
		a[fra] = null; 
		fra++;
		if( fra == a.length )
			fra = 0; 
		return temp;
	}

	public T kikk()
	{
		if( fra == til )
			throw new NoSuchElementException( "Køen er tom!" );

		return a[fra]; 
	}

	public boolean tom()
	{
		return fra == til;
	}

	public void nullstill()
	{
		for( int i = 0; i < a.length; i++ ) 
			a[i] = null; 

		fra = til = 0; 
	}

	public int indeksTil( T t )
	{
		if( fra == til )
			throw new NoSuchElementException( "Køen er tom!" );
		for( int i = fra; i < til; i++ ) 
		{
			if( a[i].equals(t) )
				return i; 
		}

		return -1; 
	}

	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append('['); 
		int indeks;

		if( !tom() )
			s.append( a[fra] ); 
		if( fra <= til )
			indeks = til;
		else
			indeks = til+a.length;

		for( int i = fra + 1; i < indeks ; i++ ) 
		{
			s.append( ',' ).append( ' ' ).append( i >= a.length?a[i-a.length]:a[i] );
		}

		s.append(']');
		return s.toString();
	}
}