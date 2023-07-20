import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextEditorApp extends JFrame {
    private JTextArea textArea;
    private UndoManager undoManager;
    private JCheckBoxMenuItem boldItem;
    private JCheckBoxMenuItem italicItem;
    private JCheckBoxMenuItem underlineItem;

    public TextEditorApp() {
        // Set up the main frame
        setTitle("Text Editor App");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the components
        textArea = new JTextArea();
        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);

        // Set up the menu
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);

        // Add action listener for the "Save" menu item
        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });

        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo");
        JMenuItem redoItem = new JMenuItem("Redo");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");

        // Add action listeners for the edit menu items
        undoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });

        redoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });

        cutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cut();
            }
        });

        copyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copy();
            }
        });

        pasteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                paste();
            }
        });

        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.addSeparator();
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        menuBar.add(editMenu);

        // Font menu
        JMenu fontMenu = new JMenu("Font");
        JMenuItem fontSizeItem = new JMenuItem("Font Size");
        fontMenu.add(fontSizeItem);
        fontMenu.addSeparator();

        boldItem = new JCheckBoxMenuItem("Bold");
        italicItem = new JCheckBoxMenuItem("Italic");
        underlineItem = new JCheckBoxMenuItem("Underline");

        boldItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeFontStyle();
            }
        });

        italicItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeFontStyle();
            }
        });

        underlineItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeFontStyle();
            }
        });

        fontMenu.add(boldItem);
        fontMenu.add(italicItem);
        fontMenu.add(underlineItem);

        fontSizeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeFontSize();
            }
        });

        fontMenu.add(fontSizeItem);
        menuBar.add(fontMenu);

        // Word count menu
        JMenu wordCountMenu = new JMenu("Word Count");
        JMenuItem countWordsItem = new JMenuItem("Count Words");
        countWordsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                countWords();
            }
        });
        wordCountMenu.add(countWordsItem);
        menuBar.add(wordCountMenu);

        // Set the menu bar for the frame
        setJMenuBar(menuBar);

        // Add the text area to the frame
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    protected void changeFontStyle() {
        int style = Font.PLAIN;
        if (boldItem.isSelected()) {
            style += Font.BOLD;
        }
        if (italicItem.isSelected()) {
            style += Font.ITALIC;
        }
        textArea.setFont(textArea.getFont().deriveFont(style));
    }

    private void changeFontSize() {
        String input = JOptionPane.showInputDialog(this, "Enter new font size:");
        if (input != null) {
            try {
                int size = Integer.parseInt(input);
                if (size > 0) {
                    Font currentFont = textArea.getFont();
                    textArea.setFont(currentFont.deriveFont((float) size));
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid font size. Please enter a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid font size. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        int userChoice = fileChooser.showSaveDialog(this);

        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(this, "File saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    private void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }

    private void cut() {
        textArea.cut();
    }

    private void copy() {
        textArea.copy();
    }

    private void paste() {
        textArea.paste();
    }

    private void countWords() {
        String text = textArea.getText();
        String[] words = text.split("\\s+");
        int wordCount = words.length;
        JOptionPane.showMessageDialog(this, "Word count: " + wordCount, "Word Count", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TextEditorApp().setVisible(true));
    }
}
