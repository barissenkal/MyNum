import java.util.StringTokenizer;

public class MyNum{

	// Value = mantissa * 10^(-exponent)
	
	public long mantissa;
	public long exponent;

	public MyNum(long mantissa, long exponent) {
		this.mantissa = mantissa;
		this.exponent = exponent;
	}

	public MyNum(double d) throws Exception {
		
		if (d == Double.POSITIVE_INFINITY){
			System.out.println("Improper double: POSITIVE_INFINITY");
			throw new Exception();
		}
		
		exponent=0;
		
		//TODO check highest number I can store.
		
		while(exponent < Long.MAX_VALUE -2 && d < Long.MAX_VALUE/ 10){
			exponent++;
			d *= 10;
			if(d == (int) d) {
				mantissa = (long) d;
				return;
			}
		}
		
		mantissa = (long) d;
		
	}

	public MyNum(String string,boolean isDouble) throws NumberFormatException, Exception {
		this(Double.parseDouble(string));
	}

	private MyNum() {}

	public MyNum add(MyNum num) throws Exception {
		
		if(mantissa == 0){
			return num;
		} else if (num.mantissa == 0){
			return this;
		}
		
		//System.out.println("numOfDigits(mantissa) exponent numOfDigits(num.mantissa) num.exponent " + numOfDigits(mantissa) +" "+ exponent +" "+ numOfDigits(num.mantissa) +" "+ num.exponent);
		
		if(exponent >= 0 && num.exponent >= 0){
			
			long presDiff = -((long)numOfDigits(mantissa) - exponent - ((long)numOfDigits(num.mantissa) - num.exponent));
			
			if(presDiff >= 19){
				return num;
			} else if(presDiff <= -19){
				return this;
			}
			
		} else if (exponent > 0) { // num.exponent < 0
			
			long presDiff = -((long)numOfDigits(mantissa) - exponent - ((long)numOfDigits(num.mantissa) + num.exponent));
			
			if(presDiff >= 19 || presDiff <= -19){
				return num;
			}
			
		} else if (num.exponent > 0) { // exponent < 0
			
			long presDiff = -((long)numOfDigits(mantissa) + exponent - ((long)numOfDigits(num.mantissa) - num.exponent));
			
			if(presDiff >= 19 || presDiff <= -19){
				return this;
			}
			
		}

		
		long lessE;
		long moreE;
		long lessM;
		long moreM;
		
		int diff = (int) (this.exponent - num.exponent);
		
		if(diff < 0){
			lessE = this.exponent;
			lessM = this.mantissa;
			moreE = num.exponent;
			moreM = num.mantissa;
			diff = -diff;
		} else {
			lessE = num.exponent;
			lessM = num.mantissa;
			moreE = this.exponent;
			moreM = this.mantissa;
		}
		
		int i=0;
		while(i < diff){
			
			//System.out.println("lessM lessE  moreM moreE " + lessM +" "+ lessE  +"  "+ moreM +" "+ moreE );
			
			if(lessM <= (Long.MAX_VALUE-1) / 10){
				long temp = lessM * 10;
				lessE++;
				lessM = temp;
				i++;
			} else {
				while(i < diff){
					if(moreM >= 10){
						long temp = moreM /10;
						moreE--;
						moreM = temp;
						i++;
					} else {
						if(diff -i == 1){
							// Dirty fix for numbers that are really close to Long.MAX_VALUE
							moreM = (long) Math.floor((double)moreM/10);
							moreE--;
							break;
						}
						
						System.out.println("Error1.0 " + moreM);
						break;
					}
				}
				break;
			}
		}
		
		if(mantissa > 0 && num.mantissa > 0 && lessE != moreE){
			System.out.println("lessE != moreE " + lessE + " " + moreE + " " + this + " " + num);
			throw new Exception();
		}
//		else {
//			System.out.println("lessM " + lessM + " lessE " + lessE + " moreM " + moreM + " moreE " + moreE);
//		}
		
		
		//Ok to assume exps are equal at this point
		
		long j=10;
		while((lessM > Long.MAX_VALUE - moreM) || (moreM > Long.MAX_VALUE - lessM)){
			if(j == 0 ) {
				System.out.println("Error1.1");
				break;
			}
			
			lessM /=j;
			moreM /=j;
			lessE -=1; //Since lessE = moreE
			
			j *=10;
		}
		
		long temp = lessM + moreM;
		
		if(mantissa > 0 && num.mantissa > 0){
			if(j == 0) System.out.println("F add");
			if(temp <= 0){
				System.out.println("Error1 " + this + " " + num);
				throw new Exception();
			}
		}
		
		return new MyNum(temp,lessE);
	}


	public MyNum multiply(MyNum num) throws Exception {
		
		if(this.mantissa == 0 || num.mantissa == 0){
			System.out.println("FFF MEEE " + this + " " + num);
			throw new Exception();
		}
		
		long tempE = this.exponent + num.exponent;
		
		//temp.mantissa = this.mantissa * num.mantissa;
		
		long templong1 = this.mantissa;
		long templong2 = num.mantissa;
		
		long i1=10;
		long i2=10;
		
		while(templong1 > Long.MAX_VALUE / templong2 || templong2 > Long.MAX_VALUE / templong1){
			
			if(i1 == 0 || i2 == 0) {
				System.out.println("F multi");
				break;
			}
			
			if(templong1 > templong2){
				templong1 /= i1;
				tempE -= 1;
				i1 *=10;
			} else {
				templong2 /= i2;
				tempE -= 1;
				i2 *=10;
			}
			
			if(templong2 == 0 || templong1 == 0) {
				System.out.println("F multi 2");
				break;
			}			
			
		}
		
		long tempM = templong1 * templong2;
		
		
		if (mantissa > 0 && num.mantissa > 0){
			if(tempM <= 0){
				System.out.println("Error2 " + tempM + " " + this.toString() + " "  +num.toString());
				throw new Exception();
			}
			if (tempE <= 0 && exponent > 0 && num.exponent > 0){
				System.out.println("Error2.1 " + tempE  + " " + this.toString() + " "  +num.toString());
				throw new Exception();
			}
		}
		
		return new MyNum(tempM,tempE);
	}

	public MyNum divide(MyNum num) throws Exception {
		
		if(num.mantissa < 0){
			System.out.println("F who now? "+num.mantissa);
		}

		long dividend = this.mantissa;
		long divisor = num.mantissa;
		
		
		long exp3 = this.exponent-num.exponent;
		long man3 = 0;
		long temp2;
		
		
		while(dividend != 0){
			
			temp2 = 0;
			if(dividend > divisor){
				
				while(dividend >= (temp2 + divisor)){
					if(temp2 > Long.MAX_VALUE - divisor){
						break;
					}
					temp2 += divisor;
					man3++;
				}
				
				dividend -= temp2;
				
			} else if(dividend == divisor){
				man3++;
				break;
			} else {
				
				if( man3 > Long.MAX_VALUE / 10){
					//Enough precision has been reached
					break;
				}
				
				if(dividend <= Long.MAX_VALUE / 10){
					//System.out.println("dividend*=10;");
					dividend *= 10;
					man3 *= 10;
					exp3++;
				} else {
					//System.out.println("divisor/=10;");
					if(divisor < 10){
						System.out.println("Error3.0 divisor=" +divisor+ " "+ this + " " + num);
						throw new Exception();
					}
					divisor /= 10;
					man3 *= 10;
					exp3++;
				}
				
			}
			
		}
		
		if(man3 <= 0 && mantissa > 0 && num.mantissa > 0){ 
			System.out.println("Error3 " + this + " " + num);
			throw new Exception();
		}
		
		return new MyNum(man3,exp3);
	}


//	public String toString(){
//		return ""+toDouble();
//	}
	
	public String toString(){
		return mantissa +" * 10^"+ (-exponent);
	}

	public double toDouble(){
		return (double) mantissa * Math.pow(10,-exponent);
	}


	//checks if probability has an expected value
	public boolean isBetweenZeroAndOne(){
		return mantissa != 0 && numOfDigits(mantissa) <= exponent;
	}

	private int numOfDigits(long num){
		return (int) Math.floor(Math.log10(num) + 1);
	}
	
	
	public boolean isBiggerThan(MyNum lastValue) {
		
		long biggestDigit1 =  numOfDigits(mantissa) -exponent;
		long biggestDigit2 =  numOfDigits(lastValue.mantissa) -lastValue.exponent;
		
		if(biggestDigit1 == biggestDigit2){
			return mantissa > (lastValue.mantissa * Math.pow(10, exponent-lastValue.exponent));
		} else {
			return biggestDigit1 > biggestDigit2;
		}
		
	}
	
	public static int locationOfBiggestNumInArray(MyNum[] a){
		
		int lastLocation = 0;
		MyNum lastValue = a[0];

		for(int i=1; i<a.length;i++){
			if(a[i].isBiggerThan(lastValue)){
				lastLocation = i;
				lastValue = a[i];
			}
		}

		return lastLocation;
	}
	
	public static MyNum parse(String str){
		
		StringTokenizer st = new StringTokenizer(str, " * 10^-");
		
		return new MyNum(Long.parseLong(st.nextToken()),Long.parseLong(st.nextToken()));
		
	}
	
	public boolean isZero(){
		return mantissa == 0;
	}
	
}
