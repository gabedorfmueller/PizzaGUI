import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzaGUIFrame extends JFrame implements ActionListener {

    // Panels
    private JPanel crustPanel;
    private JPanel sizePanel;
    private JPanel toppingsPanel;
    private JPanel receiptPanel;
    private JPanel buttonPanel;

    // Crust radio buttons
    private JRadioButton thinCrust;
    private JRadioButton regularCrust;
    private JRadioButton deepDishCrust;
    private ButtonGroup crustGroup;

    // Size combo box
    private JComboBox<String> sizeCombo;

    // Toppings checkboxes
    private JCheckBox[] toppingBoxes;

    // Receipt area
    private JTextArea receiptArea;

    // Buttons
    private JButton orderButton;
    private JButton clearButton;
    private JButton quitButton;

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        buildCrustPanel();
        buildSizePanel();
        buildToppingsPanel();
        buildReceiptPanel();
        buildButtonPanel();

        // Layout (not a single vertical stack)
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);

        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.add(toppingsPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(receiptPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void buildCrustPanel() {
        crustPanel = new JPanel();
        crustPanel.setBorder(new TitledBorder("Crust Type"));

        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDishCrust = new JRadioButton("Deep-dish");

        crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDishCrust);

        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepDishCrust);
    }

    private void buildSizePanel() {
        sizePanel = new JPanel();
        sizePanel.setBorder(new TitledBorder("Pizza Size"));

        String[] sizes = {"Small - $8", "Medium - $12", "Large - $16", "Super - $20"};
        sizeCombo = new JComboBox<>(sizes);

        sizePanel.add(sizeCombo);
    }

    private void buildToppingsPanel() {
        toppingsPanel = new JPanel();
        toppingsPanel.setBorder(new TitledBorder("Toppings ($1 each)"));
        toppingsPanel.setLayout(new GridLayout(3, 2));

        String[] toppings = {
                "Pepperoni", "Sausage", "Mushrooms",
                "Onions", "Bacon", "Green Peppers"
        };

        toppingBoxes = new JCheckBox[toppings.length];

        for (int i = 0; i < toppings.length; i++) {
            toppingBoxes[i] = new JCheckBox(toppings[i]);
            toppingsPanel.add(toppingBoxes[i]);
        }
    }

    private void buildReceiptPanel() {
        receiptPanel = new JPanel();
        receiptPanel.setBorder(new TitledBorder("Receipt"));

        receiptArea = new JTextArea(25, 25);
        receiptArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(receiptArea);
        receiptPanel.add(scrollPane);
    }

    private void buildButtonPanel() {
        buttonPanel = new JPanel();

        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");

        orderButton.addActionListener(this);
        clearButton.addActionListener(this);
        quitButton.addActionListener(this);

        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == orderButton) {
            processOrder();
        } else if (e.getSource() == clearButton) {
            clearForm();
        } else if (e.getSource() == quitButton) {
            quitProgram();
        }
    }

    private void processOrder() {
        receiptArea.setText("");

        // Validate crust
        String crust = null;
        if (thinCrust.isSelected()) crust = "Thin";
        if (regularCrust.isSelected()) crust = "Regular";
        if (deepDishCrust.isSelected()) crust = "Deep-dish";

        if (crust == null) {
            JOptionPane.showMessageDialog(this, "Please select a crust type.");
            return;
        }

        // Size and base price
        int sizeIndex = sizeCombo.getSelectedIndex();
        double[] basePrices = {8, 12, 16, 20};
        double basePrice = basePrices[sizeIndex];
        String size = sizeCombo.getSelectedItem().toString();

        // Toppings
        StringBuilder toppingList = new StringBuilder();
        int toppingCount = 0;

        for (JCheckBox box : toppingBoxes) {
            if (box.isSelected()) {
                toppingList.append(box.getText()).append("\n");
                toppingCount++;
            }
        }

        if (toppingCount == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one topping.");
            return;
        }

        double toppingCost = toppingCount * 1.0;
        double subtotal = basePrice + toppingCost;
        double tax = subtotal * 0.07;
        double total = subtotal + tax;

        // Build receipt
        receiptArea.append("=========================================\n");
        receiptArea.append("Type of Crust & Size        Price\n");
        receiptArea.append(crust + " / " + size + "     $" + basePrice + "\n\n");

        receiptArea.append("Ingredients                 Price\n");
        for (JCheckBox box : toppingBoxes) {
            if (box.isSelected()) {
                receiptArea.append(box.getText() + "     $1.00\n");
            }
        }

        receiptArea.append("\nSub-total:                 $" + String.format("%.2f", subtotal) + "\n");
        receiptArea.append("Tax:                       $" + String.format("%.2f", tax) + "\n");
        receiptArea.append("-----------------------------------------\n");
        receiptArea.append("Total:                     $" + String.format("%.2f", total) + "\n");
        receiptArea.append("=========================================\n");
    }

    private void clearForm() {
        crustGroup.clearSelection();
        sizeCombo.setSelectedIndex(0);

        for (JCheckBox box : toppingBoxes) {
            box.setSelected(false);
        }

        receiptArea.setText("");
    }

    private void quitProgram() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to quit?",
                "Confirm Quit",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}