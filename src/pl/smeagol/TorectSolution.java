package pl.smeagol;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.view.View;

public class TorectSolution extends Activity {
    /** Called when the activity is first created. */
	private EditText edtBricks = null; 
	private CheckBox[][] brick = new CheckBox[4][3];
	int LiczbaKlockow=0;
	private cKlocek[] Klocek = new cKlocek[10];
	private EditText edtConsole = null;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        edtConsole = (EditText)findViewById(R.id.edtConsole);
        edtBricks = (EditText)findViewById(R.id.edtBricks);
        brick[0][0] = (CheckBox)findViewById(R.id.CheckBox1);
        brick[0][1] = (CheckBox)findViewById(R.id.CheckBox5);
        brick[0][2] = (CheckBox)findViewById(R.id.CheckBox9);
        brick[1][0] = (CheckBox)findViewById(R.id.CheckBox2);
        brick[1][1] = (CheckBox)findViewById(R.id.CheckBox6);
        brick[1][2] = (CheckBox)findViewById(R.id.CheckBox10);
        brick[2][0] = (CheckBox)findViewById(R.id.CheckBox3);
        brick[2][1] = (CheckBox)findViewById(R.id.CheckBox7);
        brick[2][2] = (CheckBox)findViewById(R.id.CheckBox11);
        brick[3][0] = (CheckBox)findViewById(R.id.CheckBox4);
        brick[3][1] = (CheckBox)findViewById(R.id.CheckBox8);
        brick[3][2] = (CheckBox)findViewById(R.id.CheckBox12);
        for (int i=0; i<10; i++) {
			Klocek[i] = new cKlocek();
		}
    	//cKlocek[] Klocek=new cKlocek[10];
        //int i = Integer.parseInt(nBricks.getText().toString());
    }
    
    public void clickButtonAdd(View v) {
    	if (LiczbaKlockow==9) edtConsole.append(getString(R.string.maxitems));
    	else {
    		LiczbaKlockow++;
    		Klocek[LiczbaKlockow-1].UstawKlocek(CheckBoxToArray(LiczbaKlockow));
    		edtConsole.append(Klocek[LiczbaKlockow-1].Wyswietl()+"\n");
    	}
    }
    
    public void clickButtonClean(View v) {
    	edtConsole.setText("");
    	LiczbaKlockow=0;
    	for (int i=0; i<10; i++) Klocek[i] = new cKlocek();
    }
    
    public void clickButtonStart(View v) {
    	if (edtBricks.getText().toString().length()==0) edtConsole.append(getString(R.string.nobricksset));
    	else {
    		RoznePlansze(Integer.parseInt(edtBricks.getText().toString()), Klocek);
    	}
    }
  
    public int[][] CheckBoxToArray(int n) {
    	int x=0,y=0,a=4,b=3;
    	
    	for (int i=0;i<4;i++) {
    		for (int j=0;j<3;j++) {
    			if (brick[i][j].isChecked()==true) {
    				if (i>x) x=i;
    				if (j>y) y=j;
    				if (i<a) a=i;
    				if (j<b) b=j;
    			}
    		}
    	}
    	int[][] TempKlocek = new int[x+1-a][y+1-b];
    	for (int i=0;i<=x-a;i++) {
    		for (int j=0;j<=y-b;j++) {
    			if (brick[i+a][j+b].isChecked()==true) {
    				TempKlocek[i][j]=n;
    				brick[i+a][j+b].setChecked(false);
    			} else TempKlocek[i][j]=0;
    		}
    	}
    	return TempKlocek;
    }
    
    public void Wynik(cKlocek[] Klocek, int x, int y) {
		cPlansza Plansza = new cPlansza(x, y);
		int nKlocek=0;
		int nPasuje=0;
		for (;;) {
			if (nKlocek<Klocek.length) {
				if (Klocek[nKlocek].Pozycja()==-1) {
					if (Plansza.WstawKlocek(Plansza.PierwszyWolny().getX(), Plansza.PierwszyWolny().getY(), Klocek[nKlocek])==0) {
						nPasuje++;
						Klocek[nKlocek].Pozycja(nPasuje);
						nKlocek=0;
					} else nKlocek++;
				} else nKlocek++;
			} 
			else {
				int max=-1;
				int maxKlocek=-1;
				boolean Wszystkie=true;
				for (int i=0;i<Klocek.length;i++) {
					if (Klocek[i].Pozycja()==-1) Wszystkie=false;
					if (Klocek[i].Pozycja()>max) {
						max=Klocek[i].Pozycja();
						maxKlocek=i;
					}
				}
				if (maxKlocek==-1) {
					//System.out.println("Niestety nie rozwiazalen problemu, to chyba myszyn ymposybul");
					break;
				}
				if (Wszystkie==false) {
					Klocek[maxKlocek].Pozycja(-1);
					nPasuje--;
					nKlocek=maxKlocek+1;
					Plansza.Usun(maxKlocek+1);
				} else {
					System.out.println();
					edtConsole.append(Plansza.Wyswietl());
					break;
				}
			}
		}
	}
    
	public void RoznePlansze(int nBricks, cKlocek[] Klocek) {
		for (int i=4;i<=(nBricks/4);i++) {
			if (i*(nBricks/i)==nBricks) {
				for (int j=0;j<Klocek.length;j++) Klocek[j].Pozycja(-1);
				Wynik(Klocek,i,nBricks/i);
			}
		}
	}
}

class cKoordynaty {
	private int x;
	private int y;
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void putX(int x) {
		this.x=x;
	}
	public void putY(int y) {
		this.y=y;
	}
}

class cKlocek {
	private int Matryca[][];
	private int x=0;
	private int y=0;
	private int Pozycja=-1;
	
	public void UstawKlocek(int[][] TempMatryca) {
		Matryca = new int[TempMatryca.length][TempMatryca[0].length];
		for (x=0;x<TempMatryca.length;x++) {
			for (y=0;y<TempMatryca[0].length;y++) {
				Matryca[x][y]=TempMatryca[x][y];
			}
		}
	}
	public String Wyswietl() {
		String S="";
		for (int i=0;i<Matryca.length;i++) {
			for (int j=0;j<Matryca[0].length;j++) {
				S+=Matryca[i][j];
			}
			S+="\n";
		}
		return S;
	}

	public void Pozycja(int n) {
		this.Pozycja=n;
	}
	public int Pozycja() {
		return this.Pozycja;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int[][] Pokaz() {
		return Matryca;
	}
}

class cPlansza {
	private int Matryca[][];// = new int[TorectHack.RozmiarX][TorectHack.RozmiarY];
	
	public int WstawKlocek(int x, int y, cKlocek Klocek) {
		int i,j;
		int[][] TempMatryca=new int[Matryca.length][Matryca[0].length]; //matryca tymczasowa
		for (i=0;i<TempMatryca.length;i++) {		//kopiowanie matrycy
			for (j=0;j<TempMatryca[0].length;j++) {
				TempMatryca[i][j]=Matryca[i][j];
			}
		}
		int Przes=0; //przesuniecie klocka w lewo jezeli ma puste pola
		for (i=0;i<Klocek.getY();i++) {
			if (Klocek.Pokaz()[0][i]!=0) break; else Przes--;
		}
		y+=Przes;
		boolean Pasuje=true;				 //zakladamy ze klocek pasuje
		for (i=0;i<Klocek.getX();i++) {
			for (j=0;j<Klocek.getY();j++) {
					if ((x+i<TempMatryca.length) && (y+j>=0) && (y+j<TempMatryca[0].length)) { 		//czy nie wychodzi poza planszę?
						if (TempMatryca[x+i][y+j]==0) { 											//czy mozna umieścić na matrycy? czyli w tym punkcie matryca jest 0 bodz klocek
							TempMatryca[x+i][y+j]=Klocek.Pokaz()[i][j];	
						} else if (Klocek.Pokaz()[i][j]!=0) Pasuje=false;							// nie można umieścić, nie pasuje
					} else Pasuje=false; 															//za planszą, czyli nie pasuje
			}
		}
		if (Pasuje==true) {
			for (i=0;i<TempMatryca.length;i++) {
				for (j=0;j<TempMatryca[0].length;j++) {
					Matryca[i][j]=TempMatryca[i][j];
				}
			}
			return 0;
		} else return -1;
	}

	public void Usun(int n) {  //usuwa klocek z planszy
		for (int i=0;i<Matryca.length;i++){
			for (int j=0;j<Matryca[0].length;j++) if (Matryca[i][j]==n) Matryca[i][j]=0;
		}
	}
	
	
	public cKoordynaty PierwszyWolny() {
		int i=0;
		int j=0;
		for (i=0;i<Matryca.length;i++) {
			for (j=0;j<Matryca[0].length-1;j++) {
				if (Matryca[i][j]==0) break;
			}
			if (Matryca[i][j]==0) break;
		}
		cKoordynaty Koord = new cKoordynaty();
		Koord.putX(i);
		Koord.putY(j);
		return Koord;
	}
	
	public String Wyswietl() {
		String S="";
		for (int i=0;i<Matryca.length;i++) {
			for (int j=0;j<Matryca[0].length;j++) {
				if (Matryca[i][j]<10) S+="  "; else S+=" "; 
				S+=Matryca[i][j];
			}
			S+="\n";
		}
		S+="\n";
		return S;
	}
	
	public cPlansza(int x, int y) {
		Matryca=new int[x][y];
		for (int i=0;i<Matryca.length;i++) {
			for (int j=0;j<Matryca[0].length;j++) {
				Matryca[i][j]=0;
			}
		}
	}
	
}