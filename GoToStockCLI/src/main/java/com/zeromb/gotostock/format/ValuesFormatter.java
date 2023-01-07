package com.zeromb.gotostock.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author Mr_Told
 */
public class ValuesFormatter {

    DecimalFormat percentFormat, priceFormat;

    String currencySuffix, amountSuffix;

    public ValuesFormatter() {
        setCurrency("GTB");
        setAmountString("pcs");

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();

        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        percentFormat = new DecimalFormat("0.00", symbols);

        symbols.setGroupingSeparator(' ');
        priceFormat = new DecimalFormat("###,###,##0.##", symbols);
    }

    public ValuesFormatter setCurrency(String s) {
        this.currencySuffix = " " + s;
        return this;
    }

    public ValuesFormatter setAmountString(String s) {
        this.amountSuffix = " " + s;
        return this;
    }

    public String formatPercent(double value) {
        return percentFormat.format(Math.abs(value)) + "%";
    }

    public String formatDelta(double delta) {
        return (delta < 0 ? "- " : "+ ") +
                priceFormat.format(Math.abs(delta)) + currencySuffix;
    }

    public String formatValue(double value) {
        return priceFormat.format(value) + currencySuffix;
    }

    public String formatAmount(int amount) {
        return amount + amountSuffix;
    }

}
