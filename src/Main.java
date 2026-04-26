import java.util.*;

class Stock {
    String name;
    double price;

    Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

class Transaction {
    String type;
    String stockName;
    int quantity;
    double price;

    Transaction(String type, String stockName, int quantity, double price) {
        this.type = type;
        this.stockName = stockName;
        this.quantity = quantity;
        this.price = price;
    }

    void display() {
        System.out.println(type + " | " + stockName + " | Qty: " + quantity + " | Price: " + price);
    }
}

class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    Map<String, Double> avgPrice = new HashMap<>();
    List<Transaction> history = new ArrayList<>();
    double balance = 10000;

    void buyStock(Stock stock, int qty) {
        double cost = stock.price * qty;

        if (balance >= cost) {
            balance -= cost;

            int currentQty = holdings.getOrDefault(stock.name, 0);
            double currentAvg = avgPrice.getOrDefault(stock.name, 0.0);

            double newAvg = ((currentQty * currentAvg) + (qty * stock.price)) / (currentQty + qty);

            holdings.put(stock.name, currentQty + qty);
            avgPrice.put(stock.name, newAvg);

            history.add(new Transaction("BUY", stock.name, qty, stock.price));

            System.out.println("✅ Bought " + qty + " shares of " + stock.name);
        } else {
            System.out.println("❌ Insufficient balance");
        }
    }

    void sellStock(Stock stock, int qty) {
        int currentQty = holdings.getOrDefault(stock.name, 0);

        if (currentQty >= qty) {
            holdings.put(stock.name, currentQty - qty);

            double revenue = stock.price * qty;
            balance += revenue;

            history.add(new Transaction("SELL", stock.name, qty, stock.price));

            System.out.println("✅ Sold " + qty + " shares of " + stock.name);
        } else {
            System.out.println("❌ Not enough shares to sell");
        }
    }

    void viewPortfolio(Map<String, Stock> market) {
        System.out.println("\n💼 Portfolio:");
        double totalValue = balance;

        for (String stockName : holdings.keySet()) {
            int qty = holdings.get(stockName);
            double price = market.get(stockName).price;
            double value = qty * price;

            System.out.println(stockName + " | Qty: " + qty + " | Current Price: " + price + " | Value: " + value);

            totalValue += value;
        }

        System.out.println("💰 Balance: " + balance);
        System.out.println("📊 Total Net Worth: " + totalValue);
    }

    void viewTransactions() {
        System.out.println("\n📜 Transaction History:");
        for (Transaction t : history) {
            t.display();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Market data
        Map<String, Stock> market = new HashMap<>();
        market.put("APPLE", new Stock("APPLE", 150));
        market.put("GOOGLE", new Stock("GOOGLE", 2800));
        market.put("TESLA", new Stock("TESLA", 700));

        Portfolio portfolio = new Portfolio();

        while (true) {
            System.out.println("\n===== STOCK TRADING PLATFORM =====");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Transaction History");
            System.out.println("6. Simulate Market Change");
            System.out.println("7. Exit");

            System.out.print("Choose: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n📈 Market Data:");
                    for (Stock s : market.values()) {
                        System.out.println(s.name + " - Price: " + s.price);
                    }
                    break;

                case 2:
                    System.out.print("Enter stock name: ");
                    String buyName = sc.next().toUpperCase();

                    if (market.containsKey(buyName)) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        portfolio.buyStock(market.get(buyName), qty);
                    } else {
                        System.out.println("❌ Stock not found");
                    }
                    break;

                case 3:
                    System.out.print("Enter stock name: ");
                    String sellName = sc.next().toUpperCase();

                    if (market.containsKey(sellName)) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        portfolio.sellStock(market.get(sellName), qty);
                    } else {
                        System.out.println("❌ Stock not found");
                    }
                    break;

                case 4:
                    portfolio.viewPortfolio(market);
                    break;

                case 5:
                    portfolio.viewTransactions();
                    break;

                case 6:
                    // simulate price changes
                    for (Stock s : market.values()) {
                        double change = (Math.random() * 20) - 10; // -10 to +10
                        s.price += change;
                        if (s.price < 1) s.price = 1;
                    }
                    System.out.println("📊 Market updated!");
                    break;

                case 7:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}