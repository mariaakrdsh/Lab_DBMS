package org.dbms.database.component.column;

import org.dbms.database.component.Column;

public class MoneyInvlColumn extends Column {

    public MoneyInvlColumn(String name) {
        super(name);
        this.type = ColumnType.MONEY_INVL.name();
    }

    @Override
    public boolean validate(String data) {
        if (data == null || data.isEmpty()) {
            return true;
        }

        String[] parts = data.split("-");
        if (parts.length != 2) {
            return false;
        }

        String lowerBoundStr = parts[0].trim();
        String upperBoundStr = parts[1].trim();
        lowerBoundStr = lowerBoundStr.replace(",", "");
        upperBoundStr = upperBoundStr.replace(",", "");

        try {
            double lowerBound = Double.parseDouble(lowerBoundStr);
            double upperBound = Double.parseDouble(upperBoundStr);

            if (validateMoney(lowerBoundStr) && validateMoney(upperBoundStr)) {
                return lowerBound <= upperBound;
            }
        } catch (NumberFormatException ignored) {
        }

        return false;
    }

    private boolean validateMoney(String money) {

        try {
            double amount = Double.parseDouble(money);

            return amount >= 0 && amount <= 10_000_000_000_000.00;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
