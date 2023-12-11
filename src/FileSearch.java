import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;


public class FileSearch extends JFrame {

    private JTextArea originalTextArea, filteredTextArea;
    private JTextField searchTextField;
    private Path filePath;
    private File loadedFile;


    public FileSearch() {
        super("File Search GUI");

        originalTextArea = new JTextArea(20, 30);
        filteredTextArea = new JTextArea(20, 30);
        searchTextField = new JTextField(20);

        JButton loadButton = new JButton("Load File");
        JButton searchButton = new JButton("Search");
        JButton quitButton = new JButton("Quit");

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFile();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(loadButton);
        controlPanel.add(searchTextField);
        controlPanel.add(searchButton);
        controlPanel.add(quitButton);

        JPanel displayPanel = new JPanel(new GridLayout(1, 2));
        displayPanel.add(new JScrollPane(originalTextArea));
        displayPanel.add(new JScrollPane(filteredTextArea));

        add(controlPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().toPath();
            loadedFile = filePath.toFile(); // Convert the Path to a File object

            try (Stream<String> lines = Files.lines(filePath)) {
                originalTextArea.setText(lines.reduce("", (s1, s2) -> s1 + s2 + "\n"));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchFile() {
        if (filePath != null) {
            String searchString = searchTextField.getText().trim().toLowerCase();

            try (Stream<String> lines = Files.lines(filePath)) {
                filteredTextArea.setText(""); // Clear the existing content

                if (!searchString.isEmpty()) {
                    lines.filter(line -> line.toLowerCase().contains(searchString))
                            .forEach(filteredLine -> filteredTextArea.append(filteredLine + "\n"));
                } else {
                    // If search string is empty, display all lines
                    lines.forEach(line -> filteredTextArea.append(line + "\n"));
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please load a file first.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


        public static void main (String[] args){
        SwingUtilities.invokeLater(() -> new FileSearch());

    }
}