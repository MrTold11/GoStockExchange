package com.zeromb.gotostock.format;

import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public class TextViewFormatter {

    ValuesFormatter formatter = new ValuesFormatter();

    public TextViewFormatter setTextViewGR(TextView view, String text, boolean green) {
        view.setText(text);

        if (view.getStyle() == null)
            view.setStyle(new Style());

        view.getStyle().setForeground(green ? Color.GREEN : Color.RED);
        return this;
    }

    public TextViewFormatter setTextViewGR(TextView view, String text) {
        view.setText(text);

        if (view.getStyle() == null)
            view.setStyle(new Style());

        view.getStyle().setForeground(Color.WHITE);
        return this;
    }

    public TextViewFormatter formatValue(TextView view, double amount, boolean green) {
        setTextViewGR(view, formatter.formatValue(amount), green);
        return this;
    }

    public TextViewFormatter formatValue(TextView view, double amount) {
        setTextViewGR(view, formatter.formatValue(amount));
        return this;
    }

    public TextViewFormatter formatDelta(TextView view, double amount) {
        setTextViewGR(view, formatter.formatDelta(amount), amount >= 0);
        return this;
    }

    public TextViewFormatter formatPercents(TextView view, double amount) {
        setTextViewGR(view, formatter.formatPercent(amount), amount >= 0);
        return this;
    }

    public TextViewFormatter formatAmount(TextView view, int amount) {
        setTextViewGR(view, formatter.formatAmount(amount));
        return this;
    }

}
