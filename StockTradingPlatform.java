import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

class Stock {
    String symbol;
    String name;
    double price;

    Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}

class User {
    String name;
    double balance = 100000;

    HashMap<String, Integer> portfolio = new HashMap<>();
    HashMap<String, Double> averageCost = new HashMap<>();

    User(String name) {
        this.name = name;
    }

    boolean buy(Stock stock, int quantity) {
        double cost = stock.price * quantity;

        if (quantity <= 0 || cost > balance) {
            return false;
        }

        int oldQuantity = portfolio.getOrDefault(stock.symbol, 0);
        double oldCost = averageCost.getOrDefault(stock.symbol, 0.0);

        double newAverage =
                (oldCost * oldQuantity + cost) / (oldQuantity + quantity);

        portfolio.put(stock.symbol, oldQuantity + quantity);
        averageCost.put(stock.symbol, newAverage);

        balance -= cost;
        return true;
    }

    boolean sell(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.symbol, 0);

        if (quantity <= 0 || quantity > owned) {
            return false;
        }

        balance += stock.price * quantity;

        int remaining = owned - quantity;

        if (remaining == 0) {
            portfolio.remove(stock.symbol);
            averageCost.remove(stock.symbol);
        } else {
            portfolio.put(stock.symbol, remaining);
        }

        return true;
    }
}

public class StockTradingPlatform extends JFrame {

    ArrayList<Stock> stocks = new ArrayList<>();

    User user = new User("Haris");

    DefaultTableModel stockModel, portfolioModel;

    JLabel balanceLabel, profitLabel;

    JComboBox<String> stockBox;
    JTextField quantityField;

    Random random = new Random();

    StockTradingPlatform() {
        setTitle("Stock Trading Platform");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        stocks.add(new Stock("TCS", "Tata Consultancy", 3500));
        stocks.add(new Stock("INFY", "Infosys", 1800));
        stocks.add(new Stock("RELIANCE", "Reliance", 2900));
        stocks.add(new Stock("HDFCBANK", "HDFC Bank", 1700));
        stocks.add(new Stock("ITC", "ITC Limited", 450));

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("STOCK TRADING PLATFORM",
                JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 25));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));

        stockModel = new DefaultTableModel(
                new String[]{"Symbol", "Company", "Price"}, 0);

        JTable stockTable = new JTable(stockModel);
        center.add(new JScrollPane(stockTable));

        portfolioModel = new DefaultTableModel(
                new String[]{"Symbol", "Quantity", "Avg Cost", "Value"}, 0);

        JTable portfolioTable = new JTable(portfolioModel);
        center.add(new JScrollPane(portfolioTable));

        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(4, 2, 10, 10));

        stockBox = new JComboBox<>();

        for (Stock s : stocks) {
            stockBox.addItem(s.symbol);
        }

        quantityField = new JTextField();

        JButton buyButton = new JButton("BUY");
        JButton sellButton = new JButton("SELL");
        JButton refreshButton = new JButton("Refresh Market");

        balanceLabel = new JLabel();
        profitLabel = new JLabel();

        bottom.add(new JLabel("Select Stock:"));
        bottom.add(stockBox);

        bottom.add(new JLabel("Quantity:"));
        bottom.add(quantityField);

        bottom.add(buyButton);
        bottom.add(sellButton);

        bottom.add(refreshButton);
        bottom.add(balanceLabel);

        add(bottom, BorderLayout.SOUTH);

        JPanel topInfo = new JPanel();
        topInfo.add(profitLabel);
        add(topInfo, BorderLayout.NORTH);

        buyButton.addActionListener(e -> trade(true));
        sellButton.addActionListener(e -> trade(false));

        refreshButton.addActionListener(e -> {
            updatePrices();
            updateTables();
        });

        updateTables();

        setVisible(true);
    }

    Stock getSelectedStock() {
        String symbol = (String) stockBox.getSelectedItem();

        for (Stock s : stocks) {
            if (s.symbol.equals(symbol)) {
                return s;
            }
        }

        return null;
    }

    void trade(boolean buying) {
        try {
            int quantity = Integer.parseInt(quantityField.getText());

            Stock stock = getSelectedStock();

            if (stock == null) return;

            boolean success;

            if (buying) {
                success = user.buy(stock, quantity);
            } else {
                success = user.sell(stock, quantity);
            }

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Transaction successful!");
                quantityField.setText("");
                updateTables();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid quantity, insufficient balance, or shares.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Enter a valid quantity.");
        }
    }

    void updatePrices() {
        for (Stock s : stocks) {
            double change = (random.nextDouble() * 0.10) - 0.05;
            s.price = Math.max(1, s.price * (1 + change));
        }
    }

    void updateTables() {
        stockModel.setRowCount(0);

        for (Stock s : stocks) {
            stockModel.addRow(new Object[]{
                    s.symbol, s.name, String.format("₹%.2f", s.price)
            });
        }

        portfolioModel.setRowCount(0);

        double invested = 0;
        double currentValue = 0;

        for (String symbol : user.portfolio.keySet()) {
            int quantity = user.portfolio.get(symbol);
            double avg = user.averageCost.get(symbol);

            Stock stock = null;

            for (Stock s : stocks) {
                if (s.symbol.equals(symbol)) {
                    stock = s;
                    break;
                }
            }

            if (stock != null) {
                double value = stock.price * quantity;

                invested += avg * quantity;
                currentValue += value;

                portfolioModel.addRow(new Object[]{
                        symbol, quantity,
                        String.format("₹%.2f", avg),
                        String.format("₹%.2f", value)
                });
            }
        }

        balanceLabel.setText(
                String.format("Balance: ₹%.2f", user.balance));

        profitLabel.setText(String.format(
                "Portfolio P/L: ₹%.2f | Total Assets: ₹%.2f",
                currentValue - invested,
                user.balance + currentValue));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StockTradingPlatform::new);
    }
}
