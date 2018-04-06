
public class GrosseZahl {
    public static class DivMod {
        public GrosseZahl div;
        public GrosseZahl mod;

        public DivMod(GrosseZahl div, GrosseZahl mod) {
            this.div = div;
            this.mod = mod;
        }
    }

    public final static GrosseZahl ZERO = new GrosseZahl(0);
    public final static GrosseZahl ONE = new GrosseZahl(1);

    private byte[] buffer;

    private GrosseZahl(byte[] digits) {
        this.buffer = digits;
    }
    
    // "12345"
    public GrosseZahl(String bignum) {
        this.buffer = new byte[bignum.length()];
        for (int i = 0; i < bignum.length(); i++) {
            buffer[i] = (byte) Character.getNumericValue(bignum.charAt(i));
        }
    }

    public GrosseZahl(int num) {
        this((long) num);
    }

    // 12345
    //
    // 12345 % 10 == 5
    // 12345 / 10 == 1234
    // 1234 % 10 == 4
    public GrosseZahl(long num) {
        if (num == 0) {
            this.buffer = new byte[1];
            this.buffer[0] = 0;
            return;
        }

        int length = (int) Math.log10((double) num) + 1;
        this.buffer = new byte[length];
        for (int i = length - 1; i >= 0; i--) {
            this.buffer[i] = (byte) (num % 10);
            num /= 10;
        }
    }

    // 123456789
    // +  123456
    // ---------
    public GrosseZahl add(final GrosseZahl rhs) {
        if (this.buffer.length < rhs.buffer.length) {
            return rhs.add(this);
        }

        int diff = this.buffer.length - rhs.buffer.length;
        int length = this.buffer.length;

        byte overflows = 0;

        for (int i = length - 1; i >= 0; i--) {
            byte a = this.buffer[i];
            byte b = (i < diff)? 0 : rhs.buffer[i - diff];
            byte c = overflows;

            overflows = (a + b + c >= 10)? (byte) 1 : (byte) 0;
        }

        length = length + overflows;
        byte[] tmp = new byte[length];
        byte carry = 0;

        for (int i = this.buffer.length - 1; i >= 0; i--) {
            byte a = this.buffer[i];
            byte b = (i < diff)? 0 : rhs.buffer[i - diff];
            byte sum = (byte) (a + b + carry);
            tmp[i + overflows] = (byte) (sum % 10);

            carry = (sum >= 10)? (byte) 1 : (byte) 0;
        }

        if (overflows != 0) {
            tmp[0] = carry;
        }

        return new GrosseZahl(tmp);
    }

    // 123456789
    // -  123456
    // ---------
    public GrosseZahl sub(final GrosseZahl rhs) {
        if (this.less(rhs)) {
            return null;
        }

        int diff = this.buffer.length - rhs.buffer.length;
        int length = this.buffer.length;
        byte[] tmp = new byte[length];
        byte borrow = 0;

        for (int i = this.buffer.length - 1; i >= 0; i--) {
            byte a = this.buffer[i];
            byte b = (i < diff)? 0 : rhs.buffer[i - diff];
            byte delta = (byte) (a - b - borrow);
            borrow = (delta < 0)? (byte) 1 : (byte) 0;

            delta = (delta < 0)? (byte) (delta + 10) : delta;
            tmp[i] = delta;
        }

        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i] != 0) {
                break;
            }

            length--;
        }

        length = length == 0? 1 : length;
        byte[] buffer = new byte[length];

        for (int i = 1; i <= length; i++) {
            buffer[buffer.length - i] = tmp[tmp.length - i];
        }

        return new GrosseZahl(buffer);
    }

    public GrosseZahl mult(final GrosseZahl rhs) {
        if (rhs.greater(this)) {
            return rhs.mult(this);
        }

        GrosseZahl prod = ZERO;

        for (GrosseZahl i = ZERO; i.less(rhs); i = i.add(ONE)) {
            prod = prod.add(this);
        }

        return prod;
    }

    public DivMod divmod(final GrosseZahl rhs) {
        if (rhs.isZero()) {
            return null;
        }

        if (rhs.greater(this)) {
            return new DivMod(ZERO, this);
        }

        GrosseZahl div = ZERO;
        GrosseZahl mod = this;

        while (!mod.less(rhs)) {
            div = div.add(ONE);
            mod = mod.sub(rhs);
        }

        return new DivMod(div, mod);
    }

    public GrosseZahl div(final GrosseZahl rhs) {
        if (rhs.isZero()) {
            return null;
        }

        return this.divmod(rhs).div;
    }

    public GrosseZahl mod(final GrosseZahl rhs) {
        if (rhs.isZero()) {
            return null;
        }

        return this.divmod(rhs).mod;
    }

    public GrosseZahl ggT(final GrosseZahl rhs) {
        if (rhs.isZero()) {
            return this;
        }

        return rhs.ggT(this.divmod(rhs).mod);
    }

    // 123456
    // 123556 
    public boolean less(final GrosseZahl rhs) {
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

    public boolean greater(final GrosseZahl rhs) {
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

    public boolean equal(final GrosseZahl rhs) {
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

    public boolean isZero() {
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
