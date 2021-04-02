const int releV=8;  //rele za valj priključen na D8
const int releF=9; //rele za bliskavico priključen na D9
const int IR=10;   //IR dioda priključena na D10
int state;        //stanje za void valj()
int stateIR;       //stanje za void ir()
int drop1;      //DROP1 SIZE
int dropdelay;  //DROP2 DELAY
int drop2delay;   //vsota drop1 in dropdelay
int drop2;        //vsota drop1, dropdelay in drop2time
int drop2time;  //DROP2 SIZE
int interval1;     //sem se shrani čas, ko se postopek izvede
int flashDelay;   //FLASHDELAY
int tmp=0;
int key;
byte recievedData;

void setup() {
 pinMode(releV, OUTPUT);        //rele za valj je izhod
 pinMode(releF, OUTPUT);        //rele za bliskavico je izhod
 pinMode(IR, OUTPUT);           //IR dioda je izhod

 state=HIGH;                 //ob zagonu programa sta obe stanji v logičnem stanju "1"
 stateIR=HIGH;

 Serial.begin(9600);
  Serial.println("<Arduino je pripravljen>");
}

void valj() {
if(state==HIGH){
Serial.println("<1234>");
Serial.println(millis());
Serial.println(interval1);
Serial.println(drop1);
  if(/*millis()-interval1>0&&*/millis()-interval1<drop1){              //če je stanje "1" in je čas večji od 0 ms in manjši od velikosti prve kapljice, se ventil valja odpre za čas prve kapljice 
digitalWrite(8,LOW);  

Serial.println("<8 je low>");                                                          
}
if(millis()-interval1>drop1&&millis()-interval1<drop2delay){      
 
digitalWrite(8,HIGH);     
Serial.println("<8 je high>");//ventil se zapre
}
if(millis()-interval1>drop2delay&&millis()-interval1<drop2){       //če je stanje "1" in je čas večji od velikosti prve kapljice ter manjši od vsote velikosti prve kapljice in dropdelaya,se ventilj valja odpre za čas druge kapljice  
                                                                                
digitalWrite(8,LOW);
}
if(millis()-interval1>drop2){                                     //po drugi kapljici se ventil zapre
digitalWrite(8,HIGH);
}
if(millis()-interval1>drop2&&millis()-interval1<flashDelay){       //če je stanje "1" in je čas večji od vsote velikosti prve kapljice, dropdelaya, druge kapljice ter manjši od časa flashDelay,se ventilj valja odpre za čas druge kapljice, se oba releja izklopita
digitalWrite(9,HIGH);
digitalWrite(8,HIGH);                                                                                                 
}
if(millis()-interval1>flashDelay&&millis()-interval1<flashDelay+400){ 
  
digitalWrite(9,LOW);                                                          //če je stanje "1" in večji od flashDelaya in manjši od vsote flashDelaya in 400 ms, rele sproži bliskavico      
                                              
interval1=millis();  
}
//ko se proces zaključi, se čas shrani v interval 1, da lahko program zaženemo več kot enkrat, prav tako se stanje ponastavi na "0", da se ne more proces ponavljati, razen če dobimo nove podatke
state=LOW; 

}
}
void cas(){
 
 drop2delay=drop1+dropdelay;
drop2=drop1+dropdelay+drop2time;                                           //formule za čase

}
void ir(){

if (state==HIGH){                                                        //proženje IR diode 
 for(int i=0; i<16; i++) { 
    digitalWrite(IR, HIGH);
    delayMicroseconds(11);
    digitalWrite(IR, LOW);
    delayMicroseconds(11);
  } 
  delayMicroseconds(7330); 
  for(int i=0; i<16; i++) { 
    digitalWrite(IR, HIGH);
    delayMicroseconds(11);
    digitalWrite(IR, LOW);
    delayMicroseconds(11);
                                                            //stanje se ponastavi na logično "0", da se del kod izvede le enkrat ob zagonu
  }

}

} 
   


void loop() 
{
      cas();
      ir();
      valj(); 
  if (Serial.available()>0)
  {
    tmp++;
    recievedData = Serial.read();
    if(tmp==1){
      drop1=recievedData;
      }
    else if(tmp ==2){
      drop2time=recievedData;
      }
    else if(tmp ==3){
      dropdelay=recievedData;
      }
    else if(tmp ==4){
      flashDelay=recievedData;
      }
    else if(tmp ==5){
      key=recievedData;
      tmp=0;
      if(key == 1){
        Serial.println(state);
      state=HIGH;
      Serial.println(drop1);
      Serial.println(drop2time);
     Serial.println(dropdelay);
     Serial.println(flashDelay);
     Serial.println(state);
        }
      }
    }
 
 
 
}
