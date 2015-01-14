import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

public class KeyLength {

	public static void main(String[] args) 
	{
		//s scannerjem vsesemo kodirano besedilo v program
		Scanner sc = new Scanner(System.in);
		String kodirano = sc.next();
		
		//FindKey vrne dolžino ključa med 4 in 20
		int kljuc = FindKey(kodirano);
		
		//pogostost posameznih črk v angleski abecedi
		double[] jezik = new double[] {8.167,1.492,2.782,4.253,12.702,2.228,2.015, 6.094,6.966,0.153, 0.772,4.025,2.406,6.749, 7.507,1.929, 0.095,5.987,6.327,9.056,2.758, 0.978,2.360,0.150,1.974,0.074};
		
		//angleška abeceda
		String abeceda = "abcdefghijklmnopqrstuvwxyz";
		
		//pogostost v kodiranem nizu
		double[][] pogostost = pogostost(kodirano, kljuc);
		
		//v resitev shranimo crke kjuca
		char[] resitev = new char[kljuc];
		
		//sprehodimo se cez cel kjuc
		for(int i=0; i < kljuc;i++)
		{
			//poiscemo vsa odstopanja znotraj vrstice
			double[] vseZaVrsto = new double[26];
			
			//v vrstici je 26 možnih pozicij 
			for(int n=0; n < 26; n++)
			{
				//sestevamo absolutno odstopanje vseh črk
				double napaka=0;
				
				//računanje napake za vsak zamik
				for(int k = 0; k < 26;k++)
				{
					double local = Math.abs( jezik[k] - pogostost[i][(k+n) % 26]);
					napaka += local;
				}
				//skupna napaka v vrsti za zamik n
				vseZaVrsto[n] = napaka;
			}
			
			//poiscemo najmanjso napako v vrsti
			int naj = 0;
		
			for(int b=1; b<26;b++)
			{
				if(vseZaVrsto[b] < vseZaVrsto[naj])
				{
					naj = b;	
				}
			}
			//crka na polozaju najmanjsege napake je del resitve
			resitev[i] = abeceda.charAt(naj);
		}
		
		//izpišemo rešitev
		System.out.print("RESITEV: ");
		for(char crka: resitev)
		{
			System.out.print(crka);
		}
		
		//zapremo scanner
		 sc.close();
	}

	private static int FindKey(String kodirano) //poiscemo dolzino kjuca
	{
		kodirano = kodirano.toLowerCase();
		//kodirano besedilo je shranjeno v kodirano in je v malih crkah
		
		//v arraylist bomo shranjevali dele kodiranega besedila, ki se večkrat ponovijo
		ArrayList<String> SubStrrring = new ArrayList<String>();
		
		//poiščemo podvojevanje in ga shranimo v arraylist
		for(int i=0; i < kodirano.length()-3;i++)
		{
			//del mora biti sestavljen vsaj iz treh crk
			String ponovitev = kodirano.substring(i, i+3);
		
			//ce se del v besedilu ponovi več kot enkrat vrne true
			boolean ponavljanje = Check(ponovitev,kodirano);
			
			if(ponavljanje)
			{
				int n=1;
				//ugotovimo ali je del vecji kot tri crke
				while(ponavljanje)
				{
					if(i+3+n < kodirano.length())
					{
						String novaPonovitev = kodirano.substring(i,i+3+n);
						boolean NovoPonavljanje = Check(novaPonovitev,kodirano);
						
						if(NovoPonavljanje)
						{
							ponovitev = novaPonovitev;
							n++;
						}
						else
						{
							break;
						}
						
					}
					else
					{
						break;
					}
				}
				//ce tega dela se ni v arrylistu ga dodamo
				if(!(SubStrrring.contains(ponovitev)))
				{
					SubStrrring.add(ponovitev);
				}
			}
		}
		//Sedaj so podvojevanje shranjene v SubStrrringu
		
		//ugotoviti moramo kakšne so razdalje med temi ponovljenimi deli
		ArrayList<Integer> razdalje = new ArrayList<Integer>();
		
		//sprehodimo se čez vse ponovitve
		for(int n=0; n<SubStrrring.size();n++)
		{
			//v dump bomo shranjevali začetne indexe ponovitev
			ArrayList<Integer> dump = new ArrayList<Integer>();
			
			//del ki se ponavlja
			String word = SubStrrring.get(n);
			
			for (int i = -1; (i = kodirano.indexOf(word, i + 1)) != -1; ) 
			{
				//razdalja je index +1 in jo dodamo v arraylist
				int k=i+1;
				dump.add(k);
			}
			//izracunamo razliko in jo dodamo v zunanji arraylist
			int razlika = Math.abs(dump.get(1) - dump.get(0));
			razdalje.add(razlika);
		}
		
		//sedaj izračunamo deljitelje razdalij
		ArrayList<Integer> deljitelji = new ArrayList<Integer>();
		
		//za vsako razdaljo...
		for(int stevilo : razdalje)
		{
			 int integer = stevilo;
			 //če je ostanek deljenja 0 potem count deli stevilo, dodamo ga v arraylist
			 for(int count = 2; count <= integer; count++)
			 {
				 if(integer%count == 0)
				 {
					 deljitelji.add(count);
				 }
			 }
		}
		
		//prestejemo vse deljitelje
		 HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();
		 
		 //sprehodimo se čez vse deljitelje
		 for(int i=0; i<deljitelji.size() ;i++)
		 {   
			 //dosedanjemu stevilu pristejemo se enega
			 Integer count = map.get(deljitelji.get(i));         
			 map.put(deljitelji.get(i), count==null?1:count+1);
		 }
		 
		 //sedaj hashmap preslikamo v treemap da ga lahko uredimo
		 TreeMap<Integer, Integer> Tree = new TreeMap<Integer, Integer>();
		
		 for(Entry<Integer, Integer> entry : map.entrySet()) 
		 {
			 int MapKey = entry.getKey();
			 int MapValue = entry.getValue();
			 Tree.put(MapKey, MapValue);
		 }
		 
		 //poiscemo najbolj verjetne kljuce
		 int maxK = -1;
		 int maxV = -1;
		 
		 //System.out.println("Verjetnost moznih kjucev:");
		 
		 //gremo cez cel treeset
		 for (Entry<Integer, Integer> entry : Tree.entrySet()) 
		 {
			 //če je kjuc med 4 in 20 ter je njegova ponovitev 10x ali več
			 if(entry.getKey() >= 4 && entry.getKey() <=20 && entry.getValue() >= 10)
			 {
				 //izracun najbolj verjetne moznosti
				 if(entry.getValue() > maxV)
				 {
					 maxV = entry.getValue();
					 maxK = entry.getKey();
				 }
				 //System.out.println("Kjuc dolzine " + entry.getKey() + ": " + entry.getValue());
			 }
			  
		 }
		 //System.out.println("Najvecja verjetnost za dolzino kjuca je " + maxK);
		 return maxK;
	}

	private static boolean Check(String ponovitev, String kodirano) 
	{
		int lastIndex = 0;
		int count =0;
		
		while(lastIndex != -1){

		       lastIndex = kodirano.indexOf(ponovitev,lastIndex);

		       if( lastIndex != -1){
		             count ++;
		             lastIndex+=ponovitev.length();
		      }
		}
		if(count > 1)
		{
			return true;
		}
		else
		{
			return false;
		}	
	}

	public static double[][] pogostost (String kodirano, int kljuc)
	{
		kodirano = kodirano.toLowerCase();
		
		//kodirano besedilo razdelimo na kjuc-delov
		String[] razdeljeno = new String[kljuc];
		for(int i=0; i<kodirano.length();i++)
		{
			int k = i % kljuc;
			
			if( i < kljuc)
			{
				razdeljeno[k] = ""+kodirano.charAt(i);
			}
			else
			{
				razdeljeno[k] += kodirano.charAt(i);
			}
		}
		
		//izracun pogostosti crk v posameznem kjuc-nizu
		HashMap<Integer,TreeMap<Character,Integer>> analiza = new HashMap<Integer,TreeMap<Character,Integer>>();
		
		//za vsak niz
		for(int i=0; i < razdeljeno.length;i++)
		{
			TreeMap<Character,Integer> crke = new TreeMap<Character,Integer>();
			
			for(int n=0; n < razdeljeno[i].length();n++)
			{
				char c = razdeljeno[i].charAt(n);
				int vrednost;
				
				if(crke.containsKey(c))
				{
					vrednost = crke.get(c);
				}
				else
				{
					vrednost = 0;
				}
				vrednost++;
				
				crke.put(c,vrednost);
			}
			analiza.put(i, crke);
		}
		//System.out.println(analiza.toString());
		
		//izracun pogostosti v %
		/////////////////////////////////////////////////////////////////////////
		HashMap<Integer,TreeMap<Character,Double>> procenti = new HashMap<Integer,TreeMap<Character,Double>>();
		
		
		 for (Entry<Integer,TreeMap<Character,Integer>> entry : analiza.entrySet()) 
		 {
			 int n= entry.getKey(); //katera vrsta v kljucu smo
			 
			 TreeMap<Character,Integer> crke = entry.getValue(); //shranjene crke, pogostost
			 
			 int vseCrke= 0;
			 
			 for(Entry<Character, Integer> c : crke.entrySet()) 
			 {
				 
				 vseCrke += c.getValue();
			 }
			 
			 TreeMap<Character,Double> zaVrsto = new TreeMap<Character,Double>(); //tukaj noter bomo shranjevali % za lokalne črke
			 
			 //int VseCrke = crke.size();
			 
			 for(Entry<Character, Integer> c : crke.entrySet()) 
			 {
				 
				 double result = (double) c.getValue() / vseCrke;
				 result = result * 100;
				 
				 BigDecimal bd = new BigDecimal(result).setScale(3, RoundingMode.HALF_EVEN);
				 result = bd.doubleValue();
				
				 zaVrsto.put(c.getKey(), result);
			 }
			 procenti.put(n, zaVrsto);
			  
		 }
		//System.out.println(procenti.toString());
		
		//pogostost v ang jeziku
		//double[] jezik = new double[] {8.167,1.492,2.782,4.253,12.702,2.228,2.015, 6.094,6.966,0.153, 0.772,4.025,2.406,6.749, 7.507,1.929, 0.095,5.987,6.327,9.056,2.758, 0.978,2.360,0.150,1.974,0.074};
		String abeceda = "abcdefghijklmnopqrstuvwxyz";

		//naredimo 2d array z odstoti
		double[][] pog = new double[kljuc][26];
		
		for (Entry<Integer, TreeMap<Character, Double>> entry : procenti.entrySet()) 
		{
			int vrsta = entry.getKey();
			
			TreeMap<Character,Double> odstotki = entry.getValue();
			
			for(int i=0; i < abeceda.length();i++)
			{
				Character crka = Character.valueOf(abeceda.charAt(i));
				double vrednost;
				
				if(odstotki.containsKey(crka))
				{

					vrednost = odstotki.get(crka);
				}
				else
				{
					vrednost =0;
				}
				pog[vrsta][i] = vrednost;
			}
		}
		
		//vrnemo 2d array z odstoti pogostosti črk
		return pog;
		
	}
}
