
public class GrosseZahl {
    private int[] buffer;

    private GrosseZahl(int[] digits) {
        this.buffer = digits;
    }
    
    // "12345"
    public GrosseZahl(String bignum) {
        this.buffer = new int[bignum.length()];
        for (int i = 0; i < bignum.length(); i++) {
            buffer[i] = Character.getNumericValue(bignum.charAt(i));
        }
    }

    // 12345
    //
    // 12345 % 10 == 5
    // 12345 / 10 == 1234
    // 1234 % 10 == 4
    public GrosseZahl(int num) {
        if (num == 0) {
            this.buffer = new int[1];
            this.buffer[0] = 0;
            return;
        }

        int length = (int) Math.log10((double) num) + 1;
        this.buffer = new int[length];
        for (int i = length - 1; i >= 0; i--) {
            this.buffer[i] = num % 10;
            num /= 10;
        }
    }

    // 123456789
    // +  123456
    // ---------
    public GrosseZahl add(GrosseZahl rhs) {
        if (this.buffer.length < rhs.buffer.length) {
            return rhs.add(this);
        }

        int diff = this.buffer.length - rhs.buffer.length;
        int length = this.buffer.length;

        int overflows = 0;

        for (int i = length - 1; i >= 0; i--) {
            int a = this.buffer[i];
            int b = (i < diff)? 0 : rhs.buffer[i - diff];
            int c = overflows;

            overflows = (a + b + c >= 10)? 1:0;
        }

        length = length + overflows;
        int[] tmp = new int[length];
        int carry = 0;

        for (int i = this.buffer.length - 1; i >= 0; i--) {
            int a = this.buffer[i];
            int b = (i < diff)? 0 : rhs.buffer[i - diff];
            int sum = a + b + carry;
            tmp[i + overflows] = sum % 10;

            carry = (sum >= 10)? 1:0;
        }

        if (overflows != 0) {
            tmp[0] = carry;
        }

        return new GrosseZahl(tmp);
    }

    public GrosseZahl mult(GrosseZahl rhs) {
        if (rhs.greater(this)) {
            return rhs.mult(this);
        }

        GrosseZahl prod = new GrosseZahl(0);

        for (GrosseZahl i = new GrosseZahl(0); i.less(rhs); i = i.add(new GrosseZahl(1))) {
            prod = prod.add(this);
        }

        return prod;
    }

    public GrosseZahl ggt(GrosseZahl rhs) {
        // TODO
        return null;
    }

    // 123456
    // 123556 
    public boolean less(GrosseZahl rhs) {
        if (this.buffer.length != rhs.buffer.length) {
            return this.buffer.length < rhs.buffer.length;
        }

        for (int i = 0; i < this.buffer.length; i++) {
            if (this.buffer[i] != rhs.buffer[i]) {
                return this.buffer[i] < rhs.buffer[i];
            }
        }

        return false;
    }

    public boolean greater(GrosseZahl rhs) {
        if (this.buffer.length != rhs.buffer.length) {
            return this.buffer.length > rhs.buffer.length;
        }

        for (int i = 0; i < this.buffer.length; i++) {
            if (this.buffer[i] != rhs.buffer[i]) {
                return this.buffer[i] > rhs.buffer[i];
            }
        }

        return false;
    }

    public boolean equal(GrosseZahl rhs) {
        if (this.buffer.length != rhs.buffer.length) {
            return false;
        }

        for (int i = 0; i < this.buffer.length; i++) {
            if (this.buffer[i] != rhs.buffer[i]) {
                return false;
            }
        }

        return true;
    }

    public boolean zero() {
        return this.buffer.length == 1 && this.buffer[0] == 0;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < this.buffer.length; i++) {
            builder.append(this.buffer[i]);
        }

        return builder.toString();
    }
}
