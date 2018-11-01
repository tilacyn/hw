package monotonic_clock;

import org.jetbrains.annotations.NotNull;

/**
 * В теле класса решения разрешено использовать только финальные переменные типа RegularInt.
 * Нельзя volatile, нельзя другие типы, нельзя блокировки, нельзя лазить в глобальные переменные.
 */
public class Solution implements MonotonicClock {
    private final RegularInt c1 = new RegularInt(0);
    private final RegularInt c2 = new RegularInt(0);
    private final RegularInt c3 = new RegularInt(0);

    private final RegularInt d1 = new RegularInt(0);
    private final RegularInt d2 = new RegularInt(0);
    private final RegularInt d3 = new RegularInt(0);

    private void lrWrite(Time time, RegularInt a1, RegularInt a2, RegularInt a3) {
        a1.setValue(time.getD1());
        a2.setValue(time.getD2());
        a3.setValue(time.getD3());
    }

    private void rlWrite(Time time, RegularInt a1, RegularInt a2, RegularInt a3) {
        a3.setValue(time.getD3());
        a2.setValue(time.getD2());
        a1.setValue(time.getD1());
    }


    @Override
    public void write(@NotNull Time time) {
        // write right-to-left
        lrWrite(time, d1, d2, d3);
        rlWrite(time, c1, c2, c3);
    }

    @NotNull
    @Override
    public Time read() {
        // read left-to-right
        int rc1 = c1.getValue();
        int rc2 = c2.getValue();
        int rc3 = c3.getValue();
        int rd3 = d3.getValue();
        int rd2 = d2.getValue();
        int rd1 = d1.getValue();

        if (rc1 == rd1 && rc2 == rd2 && rc3 == rd3) {
            return new Time(rc1, rc2, rc3);
        }

        if (rc1 != rd1) {
            return new Time(rd1, 0, 0);
        } else if (rc2 != rd2) {
            return new Time(rd1, rd2, 0);
        } else {
            return new Time (rd1, rd2, rd3);
        }
    }

}
