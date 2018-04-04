
public class GrosseZahl {
    public static class DivMod {
        public GrosseZahl div;
        public GrosseZahl mod;

        public DivMod(GrosseZahl div, GrosseZahl mod) {
            this.div = div;
            this.mod = mod;
        }
    }

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

    // 123456789
    // -  123456
    // ---------
    public GrosseZahl sub(GrosseZahl rhs) {
        if (this.less(rhs)) {
            return null;
        }

        int diff = this.buffer.length - rhs.buffer.length;
        int length = this.buffer.length;
        int[] tmp = new int[length];
        int borrow = 0;

        for (int i = this.buffer.length - 1; i >= 0; i--) {
            int a = this.buffer[i];
            int b = (i < diff)? 0 : rhs.buffer[i - diff];
            int delta = a - b - borrow;
            borrow = (delta < 0)? 1 : 0;

            delta = (delta < 0)? delta + 10 : delta;
            tmp[i] = delta;
        }

        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i] != 0) {
                break;
            }

            length--;
        }

        length = length == 0? 1 : length;
        int[] buffer = new int[length];

        for (int i = 1; i <= length; i++) {
            buffer[buffer.length - i] = tmp[tmp.length - i];
        }

        return new GrosseZahl(buffer);
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

    public DivMod divmod(GrosseZahl rhs) {
        if (rhs.zero()) {
            return null;
        }

        if (rhs.greater(this)) {
            return new DivMod(new GrosseZahl(0), this);
        }

        GrosseZahl div = new GrosseZahl(0);
        GrosseZahl mod = this;

        while (!mod.less(rhs)) {
            div = div.add(new GrosseZahl(1));
            mod = mod.sub(rhs);
        }

        return new DivMod(div, mod);
    }

    public GrosseZahl ggT(GrosseZahl rhs) {
        if (rhs.zero()) {
            return this;
        }

        return rhs.ggT(this.divmod(rhs).mod);
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
