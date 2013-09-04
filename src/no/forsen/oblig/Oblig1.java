package no.forsen.oblig; 

import java.util.NoSuchElementException;
import java.lang.StringBuilder;

public class Oblig1
{

	// Oppgave 1
	public static int maks( int[] a )
	{
		int n = a.length; 
		if( n < 1 )
			throw new NoSuchElementException("Denne tabellen er tom");

		if( n == 1 )
			return a[0];

		for( int i = 0; i < n - 1; i++ )
		{
			if( a[i] > a[i+1] )
			{
				int temp = a[i];
				a[i] = a[i+1];
				a[i+1] = temp;
			}
		}

		return a[n - 1];
	}
	// Det vil bli gjennomført n - 1 sammenligninger i metoden over.
	// Det blir færrest ombyttinger når tabellen er sortert stigende.
	// Det blir flest ombyttinger når den største verdien ligger først i tabellen. Resten av verdiene spiller ingen rolle, 
	// da den største verdien vil bli flyttet alle plassene uansett.


	// Oppgave 2
	public static void sortering( int[] a )
	{
		int n = a.length;

		if( n <= 1 )
			return; 

		for( int i = n; i > 0; i-- )
		{
			for( int j = 0; j < i - 1 ; j++ )
			{
				if( a[j] > a[j + 1])
				{
					int temp = a[j];
					a[j] = a[j + 1];
					a[j + 1] = temp; 
				}
			}
		}
	}
	// For en tabell med lengde n vil det bli (n^2 - n) / 2 sammenligninger.


	// Oppgave 3
	public static int antallUlikeSortert( int[] a )
	{
		int n = a.length; 
		if( n == 0 )
			return 0;

		int antall = 1;
		for( int i = 0; i < n - 1; i++ )
		{
			if( a[i] > a[i+1] )
				throw new IllegalStateException("Tabellen må være sortert");
		
			if( a[i] != a[i+1])
				antall++;
		}

		return antall;
	}
	// Metoden er av første orden uansett tilfelle.

	// Oppgave 4
	public static int antallUlikeUsortert( int[] a )
	{
		int n = a.length;
		if( n == 0 )
			return 0;

		int antall = 0; 

		for( int i = 0; i < n - 1; i++ ) 
		{
			antall++;

			for( int j = i + 1; j < n - 1; j++ )
			{
				if( a[i] == a[j] )
					antall--;
			}
		}

		return antall;
	}
	// Metoden er av andre orden i verste tilfelle

	// Oppgave 5
	public static void rotasjon( char[] a )
	{
		int n = a.length;

		if( n == 0 )
			return; 

		char siste = a[n - 1]; 

		for( int i = n - 1; i > 0; i-- )
		{
			a[i] = a[i - 1];
		}

		a[0] = siste;
	}

	// Oppgave 6
	public static void rotasjon( char[] a, int k )
	{
		int n = a.length;

		if( n == 0 )
			return; 

		if( k > 0 )
		{
			while( k != 0 )
			{
				char siste = a[n - 1];

				for( int i = n - 1; i > 0; i-- )
					a[i] = a[i - 1];

				a[0] = siste;
				k--; 
			}

		}
		else
		{
			while( k != 0 )
			{
				char første = a[0];
				for( int i = 0; i < n - 1; i++ )
					a[i] = a[i + 1];

				a[n - 1] = første; 

				k++;
			}
		} 
	}

	// Oppgave 7
	public static String toString( int[] a, char v, char h, String mellomrom )
	{
		StringBuilder utskrift = new StringBuilder(); 

		utskrift.append( v );

		int n = a.length; 

		if( n != 0 )
		{
			utskrift.append( a[0] );

			for( int i = 1; i < n; i++ )
			{
				utskrift.append( mellomrom ); 
				utskrift.append( a[i] );
			}
		}

		utskrift.append( h );

		return utskrift.toString(); 
	}

	// Oppgave 8 a)
	public static int[] tredjeMinst( int[] a )
	{
		int n = a.length;

		if( n < 3 )
			throw new IllegalArgumentException( "a.length(" + n + ") < 3!");

		int m = 0;
		int nm = 1;
		int nnm = 2; 

		if( a[1] < a[0] )
		{
			m = 1;
			nm = 0; 
		}

		if( a[2] < a[nm] )
		{

			if( a[2] < a[m] )
			{
				nnm = nm;
				nm = m; 
				m = 2;
			}
			else
			{
				nnm = nm;
				nm = 2; 
			}
		}

		int minverdi = a[m];
		int nestminverdi = a[nm];
		int nestnestminverdi = a[nnm];

		for( int i = 3; i < n; i++ )
		{
			if( a[i] < nestnestminverdi )
			{
				if( a[i] < nestminverdi )
				{
					if( a[i] < minverdi )
					{
						nnm = nm;
						nestnestminverdi = nestminverdi;
						
						nm = m; 
						nestminverdi = minverdi;
						
						m = i;
						minverdi = a[m]; 
					}
					else
					{
						nnm = nm; 
						nestnestminverdi = nestminverdi;
						
						nm = i;
						nestminverdi = a[nm];
					}
				}
				else
				{
					nnm = i;
					nestnestminverdi = a[nnm];
				}
			}
		}
		return new int[] {m,nm,nnm};
	}
}


