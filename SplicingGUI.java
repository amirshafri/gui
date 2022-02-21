import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplicingGUI {
    int total = 0;
    String input = "";
    JPanel stringsPanel;
    JTextField stringInput;
    JTextField threshold;
    JTextArea textarea;
    JComboBox<Integer> jComboBox;
    JComboBox<String> dropDownRule;
    List<SpliceObject> spliceObjects = new ArrayList<SpliceObject>();
    private final static String newline = "\n";
    private float thresholdValue;
    private int stepInput;
    HashMap<String, String[]> patternRegex = new HashMap<String, String[]>();
    private String rule1;
    private String rule2;

    public SplicingGUI() {
        initPatternRegex();
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(0, 1));
        stringInput = setTextField();
        stringsPanel = createLegend("Strings", stringInput);
        frame.add(stringsPanel);
        threshold = setTextField();
        frame.add(createLegend("Threshold", threshold));
        frame.add(createLegendDropDown("Number of Steps"));
        frame.add(legendDropDownRule("Rules Applied"));
        // frame.add(createLegendStaticText("Rules Applied"));
        frame.add(createLegendButton("Execution"));
        JScrollPane scrollPane = createTextArea();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        frame.add(scrollPane, c);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Splicing GUI");
        frame.setSize(500, 800);
        frame.setVisible(true);
    }

    private void initPatternRegex() {
        patternRegex.put("a#d$c#a", new String[] { "ad{1}", "ca{1}" });
        patternRegex.put("a#l$a#l", new String[] { "a.\\b", "a.\\b" });
        patternRegex.put("a#l$b#l", new String[] { "a.\\b", "b.\\b" });
        patternRegex.put("l#a$l#b", new String[] { "\\b.a", "\\b.b" });
    }

    private JTextField setTextField() {
        JTextField txtField = new JTextField(15);
        return txtField;
    }

    private JPanel createLegend(String input, JTextField txtField) {
        JPanel legendPanel = new JPanel();
        legendPanel.setBorder(BorderFactory.createTitledBorder(input));
        legendPanel.setLayout(new GridBagLayout());
        int anchor = GridBagConstraints.WEST;
        int fill = GridBagConstraints.HORIZONTAL;
        int ins = 3;
        Insets insets = new Insets(ins, ins, ins, 3 * ins);
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, anchor, fill, insets, 0, 0);
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        legendPanel.add(txtField, gbc);
        return legendPanel;
    }

    private JPanel createLegendDropDown(String input) {
        Integer[] optionsToChoose = { 1, 2, 3, 4 };
        jComboBox = new JComboBox<>(optionsToChoose);
        jComboBox.setBounds(80, 50, 140, 20);
        JPanel legendPanel = new JPanel();
        legendPanel.setBorder(BorderFactory.createTitledBorder(input));
        legendPanel.setLayout(new GridBagLayout());
        int anchor = GridBagConstraints.WEST;
        int fill = GridBagConstraints.HORIZONTAL;
        int ins = 3;
        Insets insets = new Insets(ins, ins, ins, 3 * ins);
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, anchor, fill, insets, 0, 0);
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        legendPanel.add(jComboBox, gbc);
        return legendPanel;
    }

    private void setRule(String key) {
        String[] rules = patternRegex.get(key);
        rule1 = rules[0];
        rule2 = rules[1];
    }

    private JPanel legendDropDownRule(String input) {
        dropDownRule = new JComboBox<>(convert(patternRegex.keySet()));
        dropDownRule.setBounds(80, 50, 140, 20);
        JPanel legendPanel = new JPanel();
        legendPanel.setBorder(BorderFactory.createTitledBorder(input));
        legendPanel.setLayout(new GridBagLayout());
        int anchor = GridBagConstraints.WEST;
        int fill = GridBagConstraints.HORIZONTAL;
        int ins = 3;
        Insets insets = new Insets(ins, ins, ins, 3 * ins);
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, anchor, fill, insets, 0, 0);
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        legendPanel.add(dropDownRule, gbc);
        return legendPanel;
    }

    public static String[] convert(Set<String> setOfString) {

        // Create String[] of size of setOfString
        String[] arrayOfString = new String[setOfString.size()];

        // Copy elements from set to string array
        // using advanced for loop
        int index = 0;
        for (String str : setOfString)
            arrayOfString[index++] = str;

        // return the formed String[]
        return arrayOfString;
    }

    private JPanel createLegendButton(String input) {
        JButton button = new JButton("Run Submissions");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String key = String.valueOf(dropDownRule.getSelectedItem());
                setRule(key);
                spliceObjects = new ArrayList<SpliceObject>();
                String x = String.valueOf(jComboBox.getSelectedItem());
                stepInput = Integer.parseInt(x);
                thresholdValue = Float.parseFloat(threshold.getText());
                System.out.println(stepInput);
                String output = stringInput.getText();
                textarea.setText("");
                displayOutput(output, 1);
                String[] outputString = output.split(" ");
                if (outputString.length == 4) {
                    if (stepInput >= 2) {
                        calculation(outputString[0], outputString[1], outputString[2], outputString[3], 2);
                    }
                } else {
                    for (String v : outputString) {
                        System.out.println(v);
                    }
                }
            }
        });
        JPanel legendPanel = new JPanel();
        legendPanel.setBorder(BorderFactory.createTitledBorder(input));
        legendPanel.setLayout(new GridBagLayout());
        int anchor = GridBagConstraints.WEST;
        int fill = GridBagConstraints.HORIZONTAL;
        int ins = 3;
        Insets insets = new Insets(ins, ins, ins, 3 * ins);
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, anchor, fill, insets, 0, 0);
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        legendPanel.add(button, gbc);
        return legendPanel;
    }

    // private JPanel createLegendStaticText(String input) {
    // JLabel label = new JLabel("a#d$c#a");
    // JPanel legendPanel = new JPanel();
    // legendPanel.setBorder(BorderFactory.createTitledBorder(input));
    // legendPanel.setLayout(new GridBagLayout());
    // int anchor = GridBagConstraints.WEST;
    // int fill = GridBagConstraints.HORIZONTAL;
    // int ins = 3;
    // Insets insets = new Insets(ins, ins, ins, 3 * ins);
    // GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, anchor,
    // fill, insets, 0, 0);
    // gbc.gridx = 1;
    // gbc.weightx = 0.0;
    // gbc.anchor = GridBagConstraints.EAST;
    // legendPanel.add(label, gbc);
    // return legendPanel;
    // }

    private JScrollPane createTextArea() {
        JPanel panel = new JPanel();
        textarea = new JTextArea(500, 500);
        textarea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textarea);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        panel.add(scrollPane, c);
        return scrollPane;
    }

    private void displayOutput(String output, int step) {
        String steps = "Step " + step + newline;
        textarea.append(steps);
        textarea.append(output + newline);
        stringInput.selectAll();
        textarea.setCaretPosition(textarea.getDocument().getLength());
    }

    public static void main(String[] args) {
        new SplicingGUI();
    }

    public void calculation(String input1, String noInput1, String input2, String noInput2, int step) {
        System.out.println("Step " + step);
        algoCalculate(input1, noInput1, input2, noInput2);
        algoCalculate(input1, noInput1, input1, noInput1);
        algoCalculate(input2, noInput2, input2, noInput2);
        String displayString = "";
        for (SpliceObject v : spliceObjects) {
            displayString = displayString + v.getInput() + " " + String.format("%.04f",v.getValue()) + " ";
        }
        displayOutput(displayString, step);
        if (stepInput > step) {
            step++;
            System.out.println(spliceObjects.size());
            if (spliceObjects.size() == 2) {
                spliceObjects = new ArrayList<SpliceObject>();
                String[] output = displayString.trim().split(" ");
                calculation(output[0], output[1], output[2], output[3], step);
            }
        }

    }

    private void algoCalculate(String input1, String noInput1, String input2, String noInput2) {
        if (isRule1Applied(input1) && isRule2Applied(input2)) {
            String output1 = splitStringInput1(input1);
            System.out.println("Slice String 1 from " + input1 + " to " + output1);
            String output2 = splitStringInput2(input2);
            System.out.println("Slice String 2 from " + input2 + " to " + output2);
            String result = output1 + output2;
            System.out.println(input1 + " + " + input2 + " = " + result);
            float resultCalculation = resultCalculator(noInput1, noInput2);
            System.out.println("a+b-ab=" + noInput1 + "+" + noInput2 + "-" + "(" + noInput1 + "*" + noInput2 + ")="
                    + resultCalculation);
            if (thresholdValue < resultCalculation) {
                SpliceObject o = new SpliceObject();
                o.setInput(result);
                o.setValue(resultCalculation);
                spliceObjects.add(o);
            }
        }
    }

    private boolean isRule1Applied(String input) {
        final String regexRule1 = rule1;
        final Pattern pattern = Pattern.compile(regexRule1);
        final Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            if (matcher.group(0) != null) {
                return true;
            }

        }
        return false;
    }

    private boolean isRule2Applied(String input) {
        final String regexRule1 = rule2;
        final Pattern pattern = Pattern.compile(regexRule1);
        final Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            if (matcher.group(0) != null) {
                return true;
            }
        }
        return false;
    }

    private String splitStringInput1(String input) {
        final String regexRule1 = rule1;
        final Pattern pattern = Pattern.compile(regexRule1);
        final Matcher matcher = pattern.matcher(input);
        String result = null;
        while (matcher.find()) {
            if (matcher.group(0) != null) {
                int index = input.indexOf(matcher.group(0));
                result = input.substring(0, index + 1);
            }
        }
        return result;
    }

    private String splitStringInput2(String input) {
        final String regexRule1 = rule2;
        final Pattern pattern = Pattern.compile(regexRule1);
        final Matcher matcher = pattern.matcher(input);
        String result = null;
        while (matcher.find()) {
            if (matcher.group(0) != null) {
                int index = input.indexOf(matcher.group(0));
                result = input.substring(index + 1);
            }
        }
        return result;
    }

    private float resultCalculator(String input1, String input2) {
        float a = Float.parseFloat(input1);
        float b = Float.parseFloat(input2);
        return a + b - (a * b);
    }
}
