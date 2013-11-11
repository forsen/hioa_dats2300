package no.forsen.oblig;

import java.util.*;
import no.forsen.hjelpeklasser.*;

public class SBinTre2<T> implements Beholder<T>
{
	private static final class Node<T>   // en indre nodeklasse
	{
		private T verdi;                   // nodens verdi
		private Node<T> venstre, høyre;    // venstre og høyre barn

		private Node(T verdi, Node<T> v, Node<T> h)  // konstruktør
		{
			this.verdi = verdi;
			venstre = v; høyre = h;
		}

		private Node(T verdi)  // konstruktør
		{
			this(verdi, null, null);
		}

		public String toString(){ return "" + verdi;}

	} // class Node

	private Node<T> rot;                  // peker til rotnoden
	private int antall;                   // antall noder
	private int endringer;                // antall endringer
	private int høyde;                    // treets høyde
	private int antallIngenBarn;          // antall bladnoder
	private int antallToBarn;             // antall noder med to barn
	private int antallEttBarn;            // antall noder med kun ett barn

	private final Comparator<? super T> comp;  // komparator

	public SBinTre2(Comparator<? super T> c)    // konstruktør
	{
		rot = null;
		antall = 0;
		endringer = 0;
		høyde = -1;
		antallIngenBarn = 0;
		antallEttBarn = 0;
		antallToBarn = 0;
		comp = c;
	}

	public static <T extends Comparable<? super T>> SBinTre2<T> lagTre()
	{
		return new SBinTre2<>(Komparator.<T>naturlig());
	}

	public static <T> SBinTre2<T> lagTre(Comparator<? super T> c)
	{
		return new SBinTre2<>(c);
	}

	public int antall()        // antall verdier i treet
	{
		return antall;
	}

	public boolean tom()       // er treet tomt?
	{
		return antall == 0;
	}

	public int høyde()
	{
		return høyde;
	}

	public int antallIngenBarn()
	{
		return antallIngenBarn;
	}

	public int antallEttBarn()
	{
		return antallEttBarn;
	}

	public int antallToBarn()
	{
		return antallToBarn;
	}

	public void nullstill()
	{
		rot = null;
		antall = 0;
		antallIngenBarn = 0; 
		antallEttBarn = 0; 
		antallToBarn = 0; 
	}

	public boolean leggInn(T verdi)
	{
		if (verdi == null) throw
			new NullPointerException("Ulovlig nullverdi!");

		Node<T> p = rot, q = null;               // p starter i roten
		int cmp = 0;                             // hjelpevariabel
		int trehøyde = 1; 

		while (p != null)       // fortsetter til p er ute av treet
		{
			q = p;                                 // q forelder til p
			cmp = comp.compare(verdi,p.verdi);      // bruker komparatoren
			p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
			trehøyde++; 
		}

		p = new Node<>(verdi);                   // oppretter en ny node
		antallIngenBarn++; 

		if (q == null) 
			rot = p;          
		else if (cmp < 0) 
			q.venstre = p;         // til venstre for q
		else 
			q.høyre = p;                        // til høyre for q
		
		if( q != null )
		{
			if( q.høyre == null || q.venstre == null )
			{
				antallEttBarn++;
				antallIngenBarn--;
			}
			else 
			{
				antallToBarn++; 
				antallEttBarn--;
			}
		}


		endringer++;                             // en endring
		antall++;
		høyde = trehøyde;                              // en ny verdi i treet

		return true;
	}

	public boolean inneholder(T verdi)
	{
		if (verdi == null) return false;

		Node<T> p = rot;                            // starter i roten
		while (p != null)                           // sjekker p
		{
			int cmp = comp.compare(verdi,p.verdi);     // sammenligner
			if (cmp < 0) p = p.venstre;               // går til venstre
			else if (cmp > 0) p = p.høyre;            // går til høyre
			else return true;                         // cmp == 0, funnet
		}
		return false;                               // ikke funnet
	}

	public boolean fjern(T verdi)
	{
		if (verdi == null) return false;

		Node<T> p = rot, q = null;   // q skal være forelder til p

		while (p != null)            // leter etter verdi
		{
			int cmp = comp.compare(verdi,p.verdi);       // sammenligner
			if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
			else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
			else
			break;    // den søkte verdien ligger i p
		}

		if (p == null) return false;   // finner ikke verdi

		if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
		{
			Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
			if (p == rot) rot = b;
			else if (p == q.venstre) q.venstre = b;
			else q.høyre = b;
		}
		else  // Tilfelle 3)
		{
			Node<T> s = p, r = p.høyre;   // finner neste i inorden
			while (r.venstre != null)
			{
				s = r;    // s er forelder til r
				r = r.venstre;
			}

			p.verdi = r.verdi;   // kopierer verdien i r til p

			if (s != p) s.venstre = r.høyre;
			else s.høyre = r.høyre;
		}

		endringer++;
		antall--;   // det er nå én node mindre i treet
		return true;
	}

	private class InordenIterator implements Iterator<T>
	{
		private Stakk<Node<T>> s = new TabellStakk<>();  // for traversering
		private Node<T> p = null;                        // nodepeker
		private int iteratorendringer;                   // iteratorendringer

		private Node<T> først(Node<T> q)   // en hjelpemetode
		{
			while (q.venstre != null)        // starter i q
			{
				s.leggInn(q);                  // legger q på stakken
				q = q.venstre;                 // går videre mot venstre
			}
			
			return q;                        // q er lengst ned til venstre
		}

		public InordenIterator()  // konstruktør
		{
			if (rot == null) return;         // treet er tomt
			p = først(rot);                  // bruker hjelpemetoden
			iteratorendringer = endringer;   // setter treets endringer
		}

		public T next()
		{
			if (iteratorendringer != endringer)
				throw new ConcurrentModificationException();

			if (!hasNext()) throw new NoSuchElementException();

			T verdi = p.verdi;               // tar vare på verdien i noden p

			if (p.høyre != null) p = først(p.høyre);  // p har høyre subtre
			else if (!s.tom()) p = s.taUt();          // p har ikke høyre subtre
			else p = null;                            // stakken er tom

			return verdi;                    // returnerer verdien
		}

		public boolean hasNext()
		{
			return p != null;
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	public Iterator<T> iterator()
	{
		return new InordenIterator();
	}

	public String toString()
	{
		StringBuilder s = new StringBuilder();   // StringBuilder
		s.append('[');                           // starter med [
		if (!tom()) toString(rot,s);             // den rekursive metoden
		s.append(']');                           // avslutter med ]
		return s.toString();                     // returnerer
	}

	private static <T> void toString(Node<T> p, StringBuilder s)
	{
		if (p.venstre != null)                   // p har et venstre subtre
		{
			toString(p.venstre, s);                // komma og mellomrom etter
			s.append(',').append(' ');             // den siste i det venstre
		}                                        // subtreet til p

		s.append(p.verdi);                       // verdien i p

		if (p.høyre != null)                     // p har et høyre subtre
		{
			s.append(',').append(' ');             // komma og mellomrom etter
			toString(p.høyre, s);                  // p siden p ikke er den
		}                                        // siste noden i inorden
	}

	public int antall(T verdi)
	{
		if (verdi == null) 
			return 0;

		int antall = 0; 

		Node<T> p = rot;                            // starter i roten
		while (p != null)                           // sjekker p
		{
			int cmp = comp.compare(verdi,p.verdi);     // sammenligner
			if (cmp < 0) 
				p = p.venstre;               // går til venstre
			else if (cmp > 0) 
				p = p.høyre;            // går til høyre
			else 
			{
				antall++;                         // cmp == 0, funnet
				p = p.høyre; 
			}
		}
		return antall;                               // ikke funnet	
	}

	public T min()
	{
		if (tom()) throw new NoSuchElementException("Treet er tomt!");

		Node<T> p = rot;
		while (p.venstre != null) p = p.venstre;
		return p.verdi;
	}

	public T nestMin()
	{
		if( antall() < 2 )
			throw new NoSuchElementException( "Treet har mindre enn 2 elementer!" );

		if( rot.venstre == null )
			return rot.høyre.verdi; 

		Node<T> p = rot, q = rot;

		while( p.venstre != null )
		{
			q = p; 
			p = p.venstre; 
		} 
		if( p.høyre != null )
			return p.høyre.verdi; 

		return q.verdi; 
	}

	public T minFjern()
	{
		if( tom() )
			throw new NoSuchElementException( "Treet er tomt!" );

		Node<T> p = rot, q = rot; 

		while( p.venstre != null )
		{
			q = p; 
			p = p.venstre; 
		}

		T verdi = p.verdi;

		if( p == rot )
		{
			nullstill(); 
			return verdi; 
		}
		else if( p.høyre == null )
		{
			q.venstre = null; 
			if( q.høyre != null )
			{
				antallToBarn--; 
				antallEttBarn++;
				antallIngenBarn--; 
			}
			else
				antallEttBarn--;
		}
		else
		{
			q.venstre = p.høyre; 
			antallEttBarn--; 
		}

		return verdi;

	}

	public T maks()
	{
		if( tom() )
			throw new NoSuchElementException( "Treet er tomt!" );

		Node<T> p = rot; 
		while( p.høyre != null )
			p = p.høyre; 
		return p.verdi; 
	}

	public T nestMaks()
	{
		if( antall() < 2 )
			throw new NoSuchElementException( "Treen har mindre enn 2 elementer!" );

		if( rot.høyre == null )
			return rot.venstre.verdi; 

		Node<T> p = rot, q = rot; 

		while( p.høyre != null )
		{
			q = p; 
			p = p.høyre; 
		}
		if( p.venstre != null )
			return p.venstre.verdi;
		return q.verdi; 
	}

	public int maksFjernAlle()
	{
		if( tom() )
			return 0;
		if( antall() == 1 )
		{
			rot = null; 
			antall--;
			antallIngenBarn--; 
			return 1;
		}

		Node<T> p = rot, q = null; 

		int antallFjernes = 1; 

		while( p.høyre != null )
		{
			if( p.verdi != p.høyre.verdi )
			{
				antallFjernes = 1; 
				q = p; 
				p = p.høyre;
			}
			else
			{
				antallFjernes++; 
				p = p.høyre; 
			}
		}

		if( p == rot )
		{
			rot = rot.venstre; 
			antall--; 
			antallEttBarn--; 
			return 1; 
		}

		if( q.høyre.venstre != null )
		{
			if( q.høyre.høyre != null )
			{
				antallToBarn--; 
				antallEttBarn = antallEttBarn - (antallFjernes - 2); 
				antallIngenBarn--;
			}
			else
				antallEttBarn--; 

			q.høyre = q.høyre.venstre; 
		}
		else
		{
			q.høyre = null; 
			if( q.venstre != null )
			{
				antallToBarn--; 
				antallEttBarn++;
				antallIngenBarn--; 
			}
			else
				antallEttBarn--;
			
			if( antallFjernes > 1 )
				antallEttBarn = antallEttBarn - (antallFjernes - 1) ; 
			
		}

		antall -= antallFjernes;

		return antallFjernes;



	}

	public String høyreGren()
	{
		return null;  // foreløpig kode
	}

	public String omvendtString()
	{
		return null;  // foreløpig kode    
	}

	public String[] grener()
	{
		return null;  // foreløpig kode    
	}

	private class BladnodeIterator implements Iterator<T>
	{
	// Instansvariabler, konstruktør og eventuelle
	// hjelpemetoder skal inn her

	public boolean hasNext()
	{
		return false;  // foreløpig kode
	}

	public T next()
	{
		return null;  // foreløpig koden
	}

	public void remove()
	{
		throw new UnsupportedOperationException();
	}

	}  // BladnodeIterator

	public Iterator<T> bladnodeiterator()
	{
		return new BladnodeIterator();
	}

} // SBinTre2