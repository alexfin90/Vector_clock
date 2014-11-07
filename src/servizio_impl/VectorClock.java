package servizio_impl;

/**
 *
 * @author Alex
 */
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

// la classe estenzione di un HashMap usata per gestire il VectorClock
// 
public class VectorClock extends HashMap<String, Integer> implements Serializable
{
	
        /**
	 * funzione per incrementare il clock
	 * @param pUnit - id del vettore che deve essere incrementato rappresenta l'identificativo 
         * del processo che deve essere univo per ogni uno
	 */
	public void incrementClock(String pUnit)
	{
		// se ho già questo id  incremento il clock corrispondente
		if (this.containsKey(pUnit))
		{
                this.put(pUnit, this.get(pUnit)+1);
               // System.out.println("Aggiornamento vector clock"+this.toString());
		}
		// altrimenti memorizzo nella hash map questo nuovo id con valore 1 nel vector clock
		else
		{
             //   System.out.println("Aggiungo nuovo processo nel VectorClock");
		this.put(pUnit, 0);
		}
	}

	/**
	 *  ritorna gli id degli elementi del clock
	 *  la uso dopo nel toString per stampare a video id insieme a i rispettivi valori
	 * @return  ritorna gli id degli elementi del clock
	 */
	public String[] getOrderedIDs()
	{
		String[] lResult = new String[this.size()];

		lResult = this.keySet().toArray(lResult);

		Arrays.sort(lResult);

		return lResult;
	}

	/**
	 * ritorna il valore degli elementi del clock
	 * la uso dopo nel toString per stampare a video id insieme a i rispettivi valori
	 * @return ritorna il valore degli elementi del clock
	 */
	public Integer[] getOrderedValues()
	{
		Integer[] lResult = new Integer[this.size()];
		String[] lKeySet  = this.getOrderedIDs();

		int i = 0;
		for (String lKey : lKeySet)
		{
			lResult[i] = this.get(lKey);
			i++;
		}

		return lResult;
	}

	@Override
        // override get del Hash map
	public Integer get(Object key)
	{
		Integer lResult = super.get(key);

		if (lResult == null)
			lResult = 0;

		return lResult;
	}

	@Override
	public VectorClock clone()
	{
		return (VectorClock) super.clone();
	}

	@Override
        // per stampare sotto forma di stringa tutto il vectorClock
	public String toString()
	{
		String[] lIDs		= this.getOrderedIDs();
		Integer[] lRequests = this.getOrderedValues();

		String lText = "(";

		for (int i = 0; i < lRequests.length; i++)
		{
			lText += lIDs[i];
			lText += " = ";
			lText += lRequests[i].toString();

			if (i + 1 < lRequests.length)
			{
				lText += ", ";
			}
		}

		lText += ")";

		return lText;
	}

	/**
	 * VectorClock operazione di merging Crea un nuovo VectorCLock con il massimo per ogni elemento dei vectorclock
	 * mi serve per aggiornare il vector clock.
	 * @param pOne - Primo VectorClock 
	 * @param pTwo - Secondo VectorClock
	 * 
	 * @return un nuovo vettore con il massimo per ogni elemento A new VectorClock with the maximum for each element di entrambi i clock.
	 */
	public static VectorClock max(VectorClock pOne, VectorClock pTwo)
	{
		// creo un clock
		VectorClock lResult = new VectorClock();

		// scorro gli elementi del clock uno e li metto nel nuovo clock
		for (String lEntry : pOne.keySet())
		{
			lResult.put(lEntry, pOne.get(lEntry));
		}

		// scorro gli elementi del clock due
		for (String lEntry : pTwo.keySet())
		{
			// Inserisco gli elementi del clock due se non sono presenti o se sono più grandi
			if (!lResult.containsKey(lEntry) || lResult.get(lEntry) < pTwo.get(lEntry))
			{
				lResult.put(lEntry, pTwo.get(lEntry));
			}
		}

		// ritorno il clock 
		return lResult;
	}

	/**
	 * Per confrontare due VectorClock 
	 * 
	 * 
	 * GREATER	1		Se Primo > Secondo.
	 * EQUAL	0		Se Primo = Secondo.
	 * SMALLER	-1		Se Primo < Secondo.
	 * SIMULTANEOUS 2	        Se Primo <> Secondo.
	 * 
	 * @param pOne - Primo vclock
	 * @param pTwo - Secondo vclock
	 * 
	 * @return un intero per capire.
	 */
	public static Integer compare(VectorClock pOne, VectorClock pTwo)
	{
		// Inizialmente si suppone che è tutte le cose sono possibili 
		boolean lEqual	 = true;
		boolean lGreater = true;
		boolean lSmaller = true;

		
		for (String lEntry : pOne.keySet())
		{
			// vedo se gli elementi del primo sono presenti nel secondo
			if (pTwo.containsKey(lEntry))
			{
				// se sono diversi non sono più uguali
				// Maggiore / minore dipende dalla differenza .
				if (pOne.get(lEntry) < pTwo.get(lEntry))
				{
					lEqual	 = false;
					lGreater = false;
				}
				if (pOne.get(lEntry) > pTwo.get(lEntry))
				{
					lEqual	 = false;
					lSmaller = false;
				}
			}
			
			else if (pOne.get(lEntry) != 0)
			{
				lEqual	 = false;
				lSmaller = false;
			}
		}

		// guardiamo quelli del secondo
		for (String lEntry : pTwo.keySet())
		{
			// Solo gli elementi che non abbiamo trovato nel primo devono ancora essere controllati.
			if (!pOne.containsKey(lEntry) && (pTwo.get(lEntry) != 0))
			{
				lEqual	 = false;
				lGreater = false;
			}
		}

		// ritorno i valori.
		if (lEqual)
		{
			return 0;
		}
		else if (lGreater && !lSmaller)
		{
			return 1;
		}
		else if (lSmaller && !lGreater)
		{
			return -1;
		}
		else
		{
			return 2;
		}
	}
}