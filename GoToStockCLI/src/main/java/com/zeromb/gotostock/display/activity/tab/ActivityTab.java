package com.zeromb.gotostock.display.activity.tab;

import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public class ActivityTab {

    final Class<? extends ExchangeTabActivity> clazz, leftClazz, rightClazz;
    final int color;
    final String name;

    final TextView textView;

    public ActivityTab(Class<? extends ExchangeTabActivity> clazz, Class<? extends ExchangeTabActivity> leftClazz,
                       Class<? extends ExchangeTabActivity> rightClazz, int color, String name) {
        this.clazz = clazz;
        this.leftClazz = leftClazz;
        this.rightClazz = rightClazz;
        this.color = color;
        this.name = name;

        this.textView = new TextView(name, new Style().setBackground(color));
    }

    public TextView getTextView() {
        return textView;
    }

}
