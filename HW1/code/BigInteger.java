import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
  
  
public class BigInteger{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "Wrong Input";
  
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("");
    public int size;
    public boolean sign = true;
    public byte[] digits;
  
    public BigInteger(int i){
        sign = i>=0;
        this.size = 1;
        this.digits = new byte[1];
        this.digits[0]=(byte)i;
    }
  
    public BigInteger(byte[] num1){
        this.size = num1.length;
        int cnt;
        for(cnt = this.size-1; cnt>=0; cnt--){
            if(num1[cnt] != 0) break;
        }
        this.size = cnt+1;
        if(cnt <= 0) this.size = 1;
        this.digits = new byte[this.size];
        for(int i=0; i<this.size; i++) this.digits[i] = num1[i];
    }
  
    public BigInteger(String s){
        this.size = s.length();
        this.digits = new byte[this.size];
        for(int i=0; i<this.size; i++) this.digits[i] = (byte)(s.charAt(this.size-i-1) - '0');
    }
  
    public BigInteger add(BigInteger big){
        if(!big.sign){
            big.sign = true;
            return this.subtract(big);
        }
        if(!this.sign){
            this.sign = true;
            return big.subtract(this);
        }

        int max_len = Math.max(this.size, big.size)+1;
        byte[] add_digits = new byte[max_len];
        byte carry = 0;

        for(int i=0; i<max_len-1; i++){
            byte curr = carry;
            if(i<this.size) curr += this.digits[i];
            if(i<big.size) curr += big.digits[i];
            if(curr>=10){
                carry = 1; curr-= 10;
            }
            else{
                carry = 0;
            }
            add_digits[i] = curr;
        }
        add_digits[max_len-1] = carry;
        return new BigInteger(add_digits);
    }
  
    public BigInteger subtract(BigInteger big){
        if(!big.sign){
            big.sign = true;
            return this.add(big);
        }
        if(!this.sign){
            this.sign = true;
            BigInteger ans = this.add(big);
            if(ans.size==1 && ans.digits[0] == 0) return ans;
            ans.sign = false;
            return ans;
        }

        boolean comp = true;
        if(this.size<big.size){
            comp = false;
        }
        else if(this.size == big.size){
            for(int cnt = this.size-1; cnt>=0; cnt--){
                if(this.digits[cnt] != big.digits[cnt]){
                    comp = this.digits[cnt] > big.digits[cnt];
                    break;
                }
            }
        }
        if(!comp){
            BigInteger ans = big.subtract(this);
            if(ans.size==1 && ans.digits[0] == 0) return ans;
            ans.sign = false;
            return ans;
        }
        
        int max_len = this.size;
        byte[] sub_digits = new byte[max_len];
        byte borrow = 0;
        for(int i=0; i<max_len; i++){
            byte curr = borrow;
            curr += this.digits[i];
            if(i<big.size) curr -= big.digits[i];
            if(curr<0){
                borrow = -1; curr+= 10;
            }
            else{
                borrow = 0;
            }
            sub_digits[i] = curr;
        }
        return new BigInteger(sub_digits);
    }
    
  
    public BigInteger multiply(BigInteger big){
        boolean sign = this.sign == big.sign;
        BigInteger res = new BigInteger(0);
        for(int i=0; i<big.size; i++){
            byte[] mul_digits = new byte[this.size+i+1];
            for(int j=0; j<i; j++) mul_digits[j] = 0;
            byte carry = 0;
            for(int k=0; k<this.size; k++){
                int curr = (int)(carry + this.digits[k]*big.digits[i]);
                carry = (byte)(curr/10);
                mul_digits[k+i] = (byte)(curr%10);
            }
            mul_digits[this.size+i] = carry;
            res = res.add(new BigInteger(mul_digits));
        }
        res.sign = sign;
        return res;
    }
  
    @Override
    public String toString(){
        String s = "";
        if(!this.sign && (this.size!=1 || this.digits[0]!=0)) s+= '-' ;
        for(int i = this.size-1; i>=0; i--) s+= (char)('0'+ this.digits[i]);
        return s;
    }
  
    static BigInteger evaluate(String input) throws IllegalArgumentException{
        char oper;
        int start = 0;
        int go = 0;

        String stripped = input.replaceAll("\\s+", "");
        String num[] = stripped.split("\\D+");

        if(num[0].equals("")) go++;
        BigInteger num1 = new BigInteger(num[go]);
        num1.sign = (stripped.charAt(0) != '-');
        if(stripped.charAt(0) == '-' || stripped.charAt(0) == '+') start = 1;
        oper = stripped.charAt(start + num1.size);
        
        BigInteger num2 = new BigInteger(num[go+1]);
        num2.sign = (stripped.charAt(start+num1.size+1) != '-');
        if(oper == '+') return num1.add(num2);
        else if(oper == '-') return num1.subtract(num2);
        else return num1.multiply(num2);
    }
  
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
  
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
            return false;
        }
    }
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
