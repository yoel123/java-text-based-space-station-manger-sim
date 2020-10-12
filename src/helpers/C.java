package helpers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;

/**
 *
 * Disclaimer the use of this source is at your own risk. 
 * Permission to use and distribute into your own applications
 *
 * @version 1.0
 */
public final class C {

    /** Sequence used by operating system to separate lines in text files */
    public final static String newline = System.getProperty("line.separator");

    /**
     * This is an object of type GUIConsoleIO which is a inner class. It
     * is a reference to all methods that can be use to modify the content of
     * the window, from adding text to getting user input. This was done so that
     * it is possible to access this object from all classes in the project
     * without going through the pain of passing a reference to other classes or
     * creating multiple instances.
     * <p>Since this object is a Graphical User Interface (GUI) Window, it is
     * critical to only have one unique instance running, thus the reason why
     * the constructor of GUIConsoleIO is private and the instantiation of
     * <code>io</code> is unique and final.</p>
     * <p><strong>Warning!</strong><br/>
     * This window is not designed for heavy console printouts.</p>
     */
    public static final GUIConsoleIO io = new GUIConsoleIO();

    private C() {
    }

    /**
     * <strong>Please ignore this!</strong><br />
     * <p>An instance of this class was already provided. It should be
     * displayed in the current list as <code>io</code>.</p>
     */
    //<editor-fold defaultstate="collapsed" desc="Class GUIConsoleIO Implementation...">
    public static class GUIConsoleIO {

        private GUIConsoleIO() {
            initComponents();
        }

        /**
         * Contains the default style settings for the window.<br />
         * <strong>Note:</strong> input and prompt are the same.
         */
        private void defaultStyles() {

            // default window style
            defaultFont = "Lucida Console";
            pane.setBackground(Color.BLACK);
            pane.setForeground(Color.LIGHT_GRAY);
            pane.setCaretColor(Color.WHITE);
            pane.setFont(new Font(defaultFont, Font.PLAIN, 15));

            // default prompt style
            promptStyle = new SimpleAttributeSet();
            StyleConstants.setFontFamily(promptStyle, defaultFont);
            StyleConstants.setFontSize(promptStyle, 15);
            StyleConstants.setForeground(promptStyle, Color.WHITE);
        }

        private void initComponents() {
            frame = new JFrame("");
            pane = new JTextPane();
            doc = pane.getStyledDocument();
            defaultStyles();
            redirectSystemStreams();
            fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getAvailableFontFamilyNames();
            InputPolicy cp = new InputPolicy();
            pane.addKeyListener(cp);
            pane.setMargin(new Insets(0, 10, 0, 10));
            pane.setEditable(false);
            caret = pane.getCaret();
            caret.setBlinkRate(250);
            caret.addChangeListener(cp);
            maxInput = -1;
            editing = false;

            Action backspace = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!editing) {
                        return;
                    }
                    int dot = caret.getDot();
                    int mark = caret.getMark();
                    if (dot < inputStart || mark < inputStart) {
                        return;
                    }
                    if (dot != mark) {
                        int start = pane.getSelectionStart();
                        int end = pane.getSelectionEnd();
                        replaceRange("", start, end);
                    } else if (caret.getDot() > inputStart) {
                        replaceRange("", dot - 1, dot);
                    }
                }
            };
            pane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, false), "backspace");
            pane.getActionMap().put("backspace", backspace);

            Action enter = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!editing) {
                        return;
                    }
                    try {
                        input = doc.getText(inputStart,
                                doc.getLength() - inputStart);
                        lastInputStart = inputStart;
                        lastInputEnd = doc.getLength();
                    } catch (BadLocationException ex) {
                        System.err.println(ex.getMessage());
                        System.exit(1);
                    }
                    editing = false;
                    promptVal = null;
                    selected = false;
                    maxInput = -1;

                    pane.setEditable(false);
                    caret.setVisible(false);
                    printText(newline, null);
                    latch.countDown();
                }
            };
            pane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "enter");
            pane.getActionMap().put("enter", enter);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = new Dimension((int) (screenSize.width / 2),
                    (int) (screenSize.height / 2));
            int x = (int) (frameSize.width / 2);
            int y = (int) (frameSize.height / 2);
            frame.setBounds(x, y, frameSize.width, frameSize.height);
            frame.add(new JScrollPane(pane));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }

        /**
         * Takes you to the bottom of the window.
         */
        public void gotoEnd() {
            goTo(doc.getLength());
        }

        private void goTo(final int index) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    caret.setDot(index);
                }

            });
        }

        /**
         * Use this method to set the window default font. The default font
         * style is "Lucida Console", <code>Font.<em>PLAIN</em></code>, and
         * font-size 12. You can use this as a reference to customize your font.
         * @param font name of the font currently available on your system.
         * If you want a list of available fonts
         * click start->control panel->Appearance and personalization.
         * @param style Whether you want to the font to be <b>bold</b>,
         * <i>italic</i>, or <u>underline</u>, and much more. Type
         * <code>Font.</code> and a list of all available style will be
         * displayed.
         * @param size How big or small you want the font. Type a number
         * anywhere between 8 and 72
         */
        public void setFont(final String font, final int style, final int size) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    pane.setFont(new Font(font, style, size));
                }

            });
        }

        /**
         * Creates an attribute set where you can set the font, font-size,
         * font-color, and add decorations such as bold, underline,
         * italic, and strikethrough. Then use the object returned with any
         * method that require an argument of type SimpleAttributeSet. The
         * attribute set is used to customize text.
         * @param com
         * is a String which can contain a combination of font name, a number
         * which will be the font size, a color e.g. "yellow", and any
         * decoration such as "bold", "underline", "italic", or "strikethrough".
         * <p>here is an example: "consolas 12 white bold"<br />
         * This String will set the font to consolas. The size will be 12. The
         * font color is white and the font style is bold.
         * </p>
         * @return a SimpleAttributeSet object to style text.
         */
        public SimpleAttributeSet getAttribute(String com) {
            if (com == null) {
                throw new IllegalArgumentException("getAttribute "
                    + "argument can't be null!");
            }
            SimpleAttributeSet returnedAttr = new SimpleAttributeSet();

            // parse for font family name
            {
                String font = defaultFont;
                for (int j = 0; j < fontNames.length; j++) {
                    if (com.indexOf(fontNames[j]) != -1) {
                        font = fontNames[j];
                    }
                }
                StyleConstants.setFontFamily(returnedAttr, font);
                com = com.replace(font, " ");
            }

            Matcher m = Pattern.compile("\\d+").matcher(com);
            if (m.find()) {
                String number = com.substring(m.start(), m.end());
                int size = Integer.parseInt(number);
                size = (size < 8) ? 8 : size;
                size = (size > 72) ? 72 : size;
                StyleConstants.setFontSize(returnedAttr, size);
            }

            String[] cmds = com.split("\\s+");
            for (int j = 0; j < cmds.length; j++) {
                if (cmds[j].equals("bold")) {
                    StyleConstants.setBold(returnedAttr, true);
                } else if (cmds[j].equals("underline")) {
                    StyleConstants.setUnderline(returnedAttr, true);
                } else if (cmds[j].equals("italic")) {
                    StyleConstants.setItalic(returnedAttr, true);
                } else if (cmds[j].equals("strikethrough")) {
                    StyleConstants.setStrikeThrough(returnedAttr, true);
                }
                returnedAttr = fgColorApp(returnedAttr, cmds[j]);
                returnedAttr = bgColorApp(returnedAttr, cmds[j]);
            }
            return returnedAttr;
        }

        private SimpleAttributeSet fgColorApp(SimpleAttributeSet attr, String color) {
            if (attr == null) {
                attr = new SimpleAttributeSet();
            }
            if (color.equals("black")) {
                StyleConstants.setForeground(attr, Color.BLACK);
            } else if (color.equals("blue")) {
                StyleConstants.setForeground(attr, Color.BLUE);
            } else if (color.equals("cyan")) {
                StyleConstants.setForeground(attr, Color.CYAN);
            }else if (color.equals("dark_gray")) {
                StyleConstants.setForeground(attr, Color.DARK_GRAY);
            }else if (color.equals("magenta")) {
                StyleConstants.setForeground(attr, Color.MAGENTA);
            }else if (color.equals("orange")) {
                StyleConstants.setForeground(attr, Color.ORANGE);
            }else if (color.equals("pink")) {
                StyleConstants.setForeground(attr, Color.PINK);
            }else if (color.equals("red")) {
                StyleConstants.setForeground(attr, Color.RED);
            }else if (color.equals("white")) {
                StyleConstants.setForeground(attr, Color.WHITE);
            }else if (color.equals("yellow")) {
                StyleConstants.setForeground(attr, Color.YELLOW);
            }else if (color.equals("gray")) {
                StyleConstants.setForeground(attr, Color.GRAY);
            }else if (color.equals("green")) {
                StyleConstants.setForeground(attr, Color.GREEN);
            }else if (color.equals("light_gray")) {
                StyleConstants.setForeground(attr, Color.LIGHT_GRAY);
            }
            return attr;
        }

        private SimpleAttributeSet bgColorApp(SimpleAttributeSet attr,
                String color) {
            if (attr == null) {
                attr = new SimpleAttributeSet();
            }
            if (color.equals("bg_black")) {
                StyleConstants.setBackground(attr, Color.BLACK);
            } else if (color.equals("bg_blue")) {
                StyleConstants.setBackground(attr, Color.BLUE);
            } else if (color.equals("bg_cyan")) {
                StyleConstants.setBackground(attr, Color.CYAN);
            }else if (color.equals("bg_dark_gray")) {
                StyleConstants.setBackground(attr, Color.DARK_GRAY);
            }else if (color.equals("bg_magenta")) {
                StyleConstants.setBackground(attr, Color.MAGENTA);
            }else if (color.equals("bg_orange")) {
                StyleConstants.setBackground(attr, Color.ORANGE);
            }else if (color.equals("bg_pink")) {
                StyleConstants.setBackground(attr, Color.PINK);
            }else if (color.equals("bg_red")) {
                StyleConstants.setBackground(attr, Color.RED);
            }else if (color.equals("bg_white")) {
                StyleConstants.setBackground(attr, Color.WHITE);
            }else if (color.equals("bg_yellow")) {
                StyleConstants.setBackground(attr, Color.YELLOW);
            }else if (color.equals("bg_gray")) {
                StyleConstants.setBackground(attr, Color.GRAY);
            }else if (color.equals("bg_green")) {
                StyleConstants.setBackground(attr, Color.GREEN);
            }else if (color.equals("bg_light_gray")) {
                StyleConstants.setBackground(attr, Color.LIGHT_GRAY);
            }
            return attr;
        }

        /**
         * Erase all text currently displayed in the window.
         */
        public void clear() {
            pane.setText("");
        }

        /**
         * Changes the color of text at the prompt when getting user
         * input. Type "<code>Color.</code>" when entering the argument for
         * this method and a default list of colors should appear.
         * @param color of the text at the prompt.
         */
        public void setPromptColor(Color color) {
            StyleConstants.setForeground(promptStyle, color);
        }

        /**
         * Set the prompt style. You can use the method getAttribute(String
         * font, int size, Color color, boolean bold, boolean underline, boolean
         * italic, boolean strikethrough) to create an object to use with this
         * method.
         * @param attr Contains a set a attribute to which text at the prompt
         * will be formatted.
         */
        public void setPromptStyle(SimpleAttributeSet attr) {
            promptStyle = attr;
        }

        /**
         * Changes the blinking cursor color. Type "<code>Color.</code>"
         * when entering the argument for this method and a default list of
         * colors should appear.
         * @param color to which the blinking cursor will change
         */
        public void setCaretColor(final Color color) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    pane.setCaretColor(color);
                }

            });
        }

        /**
         * Changes the default printed text color. This is the color in which
         * the text printed will be when using any of the {@code print()} or
         * {@code println()} method with a single parameter.
         * Type "<code>Color.</code>" when entering the argument for this method
         * and a default list of colors should appear.
         * @param color to which the text or foreground color of the printed
         * text will be.
         */
        public void setTextColor(final Color color) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    pane.setForeground(color);
                }

            });
        }

        /**
         * Changes the window background color. It is the color of the
         * background on which the text is displayed. Type
         * "<code>Color.</code>" when entering the argument for this method
         * and a default list of colors should appear.
         * @param color to which the background will change
         */
        public void setBackgroundColor(final Color color) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    pane.setBackground(color);
                }

            });
        }

        /**
         * Changes the window title. The window title is located in the
         * decoration bar on the same level as the minimize, expand, and exit
         * buttons.
         * @param title the text to which the title will change.
         */
        public void setTitle(final String title) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    frame.setTitle(title);
                }

            });
        }

        /**
         * Has the same functionality as the method
         * <code>print(String txt)</code> and gives an additional
         * option to fully customize printout with a SimpleAttributeSet
         * object. <p>You can use the method getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough) to create an object to use with this
         * method.</p>
         *
         * @see println(String txt)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param txt The
         * <code>String</code> to be printed
         * @param attr gives more flexibility to customize
         * printout
         */
        public void print(String txt, SimpleAttributeSet attr) {
            printText(txt, attr);
        }

        /**
         * Prints a string. If the argument is
         * <code>null</code> then the string
         * <code>"null"</code> is printed. Otherwise, the string's characters
         * are converted into bytes according to the platform's default
         * character encoding, and these bytes are written in exactly the manner
         * of the styled document insert string method.
         *
         * @param str The
         * <code>String</code> to be printed
         */
        public void print(String str) {
            printText(str, null);
        }

        /**
         * Has the same functionality as the method
         * <code>print(String str)</code> and gives an additional option
         * to change the printout color. Type "<code>Color.</code>" when
         * entering the second argument for this method and a default list of colors
         * should appear.
         *
         * @see print(String str)
         * @param str The
         * <code>String</code> to be printed
         * @param color The color of the printout.
         */
        public void print(String str, Color color) {
            printText(str, attr(color));
        }

        /**
         * Prints an object. The string produced by the
         * <code>{@link
         * java.lang.String#valueOf(Object)}</code> method is translated into
         * bytes according to the platform's default character encoding, and
         * these bytes are written in exactly the manner of the styled document
         *  insertString method.
         *
         * @param obj The
         * <code>Object</code> to be printed
         * @see java.lang.Object#toString()
         */
        public void print(Object obj) {
            printText(String.valueOf(obj), null);
        }

        /**
         * Has the same functionality as the method
         * <code>print(Object obj) </code> and gives an additional
         * option to change the printout color. Type "<code>Color.</code>"
         * when entering the second argument for this method and a default list of
         * colors should appear.
         *
         * @see print(Object obj)
         * @param obj The
         * <code>Object</code> to be printed
         * @param color The color of the printout.
         */
        public void print(Object obj, Color color) {
            printText(String.valueOf(obj), attr(color));
        }

        /**
         * Has the same functionality as the method
         * <code>print(Object obj) </code> and gives an additional
         * option to fully customize the printout. <p>You can use the method
         * getAttribute(String font, int size, Color color, boolean bold,
         * boolean underline, boolean italic, boolean strikethrough) to create
         * an object to use with this method.</p>
         *
         * @see print(Object obj)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param obj The
         * <code>Object</code> to be printed
         * @param satt styles the printout.
         */
        public void print(Object obj, SimpleAttributeSet satt) {
            printText(String.valueOf(obj), satt);
        }

        /**
         * Prints a double value. The string produced by String.valueOf(double)
         * is translated into bytes according to the platform's default character
         * encoding, and these bytes are inserted into the styled document using
         * the insertString method.
         *
         * @param d
         *  Value of type double to be printed to the GUI console.
         */
        public void print(double d) {
            printText(String.valueOf(d), null);
        }

        /**
         * Has the same functionality as the method
         * <code>print(double d)</code> and gives an additional option
         * to change the printout color. Type "<code>Color.</code>" when
         * entering the argument for this method and a default list of colors
         * should appear.
         *
         * @see print(double d)
         * @param d The
         * <code>double</code> to be printed
         * @param color The color of the printout.
         */
        public void print(double d, Color color) {
            printText(String.valueOf(d), attr(color));
        }

        /**
         * Has the same functionality as the method
         * <code>print(double d)</code> and gives an additional option
         * to fully customize the printout. <p>You can use the method
         * getAttribute(String font, int size, Color color, boolean bold,
         * boolean underline, boolean italic, boolean strikethrough) to create
         * an object to use with this method.</p>
         *
         * @see print(double d)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param d The
         * <code>double</code> to be printed
         * @param satt Styles the printout.
         */
        public void print(double d, SimpleAttributeSet satt) {
            printText(String.valueOf(d), satt);
        }

        /**
         * Prints a float value. The string produced by String.valueOf(float)
         * is translated into bytes according to the platform's default character
         * encoding, and these bytes are inserted into the styled document using
         * the insertString method.
         *
         * @param f
         *  Value of type float to be printed to the GUI console.
         */
        public void print(float f) {
            printText(String.valueOf(f), null);
        }

        /**
         * Has the same functionality as the method
         * <code>print(float f)</code> and gives an additional option to
         * change the printout color. Type "<code>Color.</code>" when
         * entering the second argument for this method and a default list of colors
         * should appear.
         *
         * @see print(float f)
         * @param f The
         * <code>float</code> to be printed
         * @param color The color of the printout.
         */
        public void print(float f, Color color) {
            printText(String.valueOf(f), attr(color));
        }

        /**
         * Has the same functionality as the method
         * <code>print(float f)</code> and gives an additional option to
         * fully customize the printout. <p>You can use the method
         * getAttribute(String font, int size, Color color, boolean bold,
         * boolean underline, boolean italic, boolean strikethrough) to create
         * an object to use with this method.</p>
         *
         * @see print(float f)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param f The
         * <code>float</code> to be printed
         * @param satt Styles the printout.
         */
        public void print(float f, SimpleAttributeSet satt) {
            printText(String.valueOf(f), satt);
        }

        /**
         * Prints a long value. The string produced by String.valueOf(long)
         * is translated into bytes according to the platform's default character
         * encoding, and these bytes are inserted into the styled document using
         * the insertString method.
         *
         * @param l
         *  Value of type long to be printed to the GUI console.
         */
        public void print(long l) {
            printText(String.valueOf(l), null);
        }

        /**
         * Has the same functionality as the method
         * <code>print(long l)</code> and gives an additional option to
         * change the printout color. Type "<code>Color.</code>" when
         * entering the second argument for this method and a default list of colors
         * should appear.
         *
         * @see print(long l)
         * @param l The
         * <code>long</code> to be printed
         * @param color The color of the printout.
         */
        public void print(long l, Color color) {
            printText(String.valueOf(l), attr(color));
        }

        /**
         * Has the same functionality as the method
         * <code>print(long l)</code> and gives an additional option to
         * fully customize the printout color. <p>You can use the method
         * getAttribute(String font, int size, Color color, boolean bold,
         * boolean underline, boolean italic, boolean strikethrough) to create
         * an object to use with this method.</p>
         *
         * @see print(long l)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param l The
         * <code>long</code> to be printed
         * @param satt Styles the printout.
         */
        public void print(long l, SimpleAttributeSet satt) {
            printText(String.valueOf(l), satt);
        }

        /**
         * Prints a integer value. The string produced by String.valueOf(int)
         * is translated into bytes according to the platform's default character
         * encoding, and these bytes are inserted into the styled document using
         * the insertString method.
         *
         * @param i
         *  Value of type int to be printed to the GUI console.
         */
        public void print(int i) {
            printText(String.valueOf(i), null);
        }

        /**
         * Has the same functionality as the method
         * <code>print(int i)</code> and gives an additional option to
         * change the printout color. Type "<code>Color.</code>" when
         * entering the second argument for this method and a default list of colors
         * should appear.
         *
         * @see print(int i)
         * @param i The
         * <code>int</code> to be printed
         * @param color The color of the printout.
         */
        public void print(int i, Color color) {
            printText(String.valueOf(i), attr(color));
        }

        /**
         * Has the same functionality as the method
         * <code>print(int i)</code> and gives an additional option to
         * fully customize the printout. <p>You can use the method
         * getAttribute(String font, int size, Color color, boolean bold,
         * boolean underline, boolean italic, boolean strikethrough) to create
         * an object to use with this method.</p>
         *
         * @see print(int i)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param i The
         * <code>int</code> to be printed
         * @param satt Styles the printout.
         */
        public void print(int i, SimpleAttributeSet satt) {
            printText(String.valueOf(i), satt);
        }

        /**
         * Prints a Character value. The string produced by String.valueOf(char)
         * is translated into bytes according to the platform's default character
         * encoding, and these bytes are inserted into the styled document using
         * the insertString method.
         *
         * @param c
         *  Value of type char to be printed to the GUI console.
         */
        public void print(char c) {
            printText(String.valueOf(c), null);
        }

        /**
         * Has the same functionality as the method
         * <code>print(char c)</code> and gives an additional option
         * to change the printout color. Type "<code>Color.</code>" when
         * entering the second argument for this method and a default list of colors
         * should appear.
         *
         * @see print(char c)
         * @param c The
         * <code>char</code> to be printed
         * @param color The color of the printout.
         */
        public void print(char c, Color color) {
            printText(String.valueOf(c), attr(color));
        }

        /**
         * Has the same functionality as the method
         * <code>print(char c)</code> and gives an additional option
         * to fully customize the printout. <p>You can use the method
         * getAttribute(String font, int size, Color color, boolean bold,
         * boolean underline, boolean italic, boolean strikethrough) to create
         * an object to use with this method.</p>
         *
         * @see print(char c)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param c The
         * <code>char</code> to be printed
         * @param satt Styles the printout.
         */
        public void print(char c, SimpleAttributeSet satt) {
            printText(String.valueOf(c), satt);
        }

        /* Methods that do terminate lines -----------------------------------*/

        /**
         * Has the same functionality as the method
         * <code>println(String str)</code> and gives an additional
         * option to fully customize printout with a SimpleAttributeSet
         * object. <p>You can use the method getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough) to create an object to use with this
         * method.</p>
         *
         * @param txt
         * @see println(String str)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         * @param attr gives more flexibility to customize
         * printout
         */
        public void println(String txt, SimpleAttributeSet attr) {
            printText(txt + newline, attr);
        }

        /**
         * Terminates the current line by writing the line separator
         * string. The line separator string is defined by the system property
         * <code>line.separator</code>, and is not necessarily a single newline
         * character (
         * <code>'\n'</code>).
         */
        public void println() {
            printText(newline, null);
        }

        /**
         * Prints a string and terminates the line. If the argument is
         * <code>null</code> then the string
         * <code>"null"</code> is printed. Otherwise, the string's characters
         * are converted into bytes according to the platform's default
         * character encoding, and these bytes are written in exactly the manner
         * of the styled document insert string method.
         *
         * @param str The
         * <code>String</code> to be printed
         */
        public void println(String str) {
            printText(str + newline, null);
        }

        /**
         * Has the same functionality as the method
         * <code>println(String str)</code> and gives an additional
         * option to change printout color. Type "<code>Color.</code>" when
         * entering the second argument for this method and a default list of colors
         * should appear.
         *
         * @see println(String str)
         * @param str The
         * <code>String</code> to be printed
         * @param color The color of the printout.
         */
        public void println(String str, Color color) {
            printText(str + newline, attr(color));
        }

        /**
         * Prints an object and terminates the line. The string produced
         * by the <code>{@link java.lang.String#valueOf(Object)}</code>
         * method is translated into bytes according to the platform's default
         * character encoding, and these bytes are written in exactly the manner
         * of the styled document insertString method.
         *
         * @param obj The
         * <code>Object</code> to be printed
         * @see java.lang.Object#toString()
         */
        public void println(Object obj) {
            printText(String.valueOf(obj) + newline, null);
        }

        /**
         * Has the same functionality as the method
         * <code>println(Object obj)</code> and gives an additional
         * option to change the printout color. Type "<code>Color.</code>"
         * when entering the second argument for this method and a default list
         * of colors should appear.
         *
         * @see println(Object obj)
         * @param obj The
         * <code>Object</code> to be printed
         * @param color The color of the printout.
         */
        public void println(Object obj, Color color) {
            printText(String.valueOf(obj) + newline, attr(color));
        }

        /**
         * Has the same functionality as the method
         * <code>println(Object obj)</code> and gives an additional
         * option to fully customize the printout.
         * <p>You can use the method getAttribute(String
         * font, int size, Color color, boolean bold, boolean underline, boolean
         * italic, boolean strikethrough) to create an object to use with this
         * method.</p>
         *
         * @see println(Object obj)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param obj The
         * <code>Object</code> to be printed
         * @param satt Styles the printout.
         */
        public void println(Object obj, SimpleAttributeSet satt) {
            printText(String.valueOf(obj) + newline, satt);
        }

        /**
         * Prints a double value and terminates the line. The string
         * produced by String.valueOf(double) is translated into bytes according
         * to the platform's default character encoding, and these bytes are
         * inserted into the styled document using the insertString method.
         *
         * @param d
         *  Value of type double to be printed to the GUI console.
         */
        public void println(double d) {
            printText(String.valueOf(d) + newline, null);
        }

        /**
         * Has the same functionality as the method
         * <code>println(double d)</code> and gives an additional option
         * to change the printout color. Type "<code>Color.</code>" when
         * entering the second argument for this method and a default list of colors
         * should appear.
         *
         * @see println(double d)
         * @param d The
         * <code>double</code> to be printed
         * @param color The color of the printout.
         */
        public void println(double d, Color color) {
            printText(String.valueOf(d) + newline, attr(color));
        }

        /**
         * Has the same functionality as the method
         * <code>println(double d)</code> and gives an additional option
         * to fully customize the printout.
         * <p>You can use the method getAttribute(String
         * font, int size, Color color, boolean bold, boolean underline, boolean
         * italic, boolean strikethrough) to create an object to use with this
         * method.</p>
         *
         * @see println(double d)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param d The
         * <code>double</code> to be printed
         * @param satt Styles the printout.
         */
        public void println(double d, SimpleAttributeSet satt) {
            printText(String.valueOf(d) + newline, satt);
        }

        /**
         * Prints a float value and terminates the line. The string
         * produced by String.valueOf(float) is translated into bytes according
         * to the platform's default character encoding, and these bytes are
         * inserted into the styled document using the insertString method.
         *
         * @param f
         *  Value of type <code>float</code> to be printed to the GUI console.
         */
        public void println(float f) {
            printText(String.valueOf(f) + newline, null);
        }

        /**
         * Has the same functionality as the method
         * <code>println(float f)</code> and
         * gives an additional option to change the printout color.
         * Type "<code>Color.</code>" when entering the second argument for this
         * method and a default list of colors should appear.
         *
         * @see println(float f)
         * @param f The
         * <code>float</code> to be printed
         * @param color The color of the printout.
         */
        public void println(float f, Color color) {
            printText(String.valueOf(f) + newline, attr(color));
        }

        /**
         * Has the same functionality as the method
         * <code>println(float f)</code> and
         * gives an additional option to fully customize the printout.
         * <p>You can use the method getAttribute(String
         * font, int size, Color color, boolean bold, boolean underline, boolean
         * italic, boolean strikethrough) to create an object to use with this
         * method.</p>
         *
         * @see println(float f)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param f The
         * <code>float</code> to be printed
         * @param satt Styles the printout.
         */
        public void println(float f, SimpleAttributeSet satt) {
            printText(String.valueOf(f) + newline, satt);
        }

        /**
         * Prints a long value and terminates the line. The string produced
         * by String.valueOf(long) is translated into bytes according to the
         * platform's default character encoding, and these bytes are inserted
         * into the styled document using the insertString method.
         *
         * @param l
         *  Value of type long to be printed to the GUI console.
         */
        public void println(long l) {
            printText(String.valueOf(l) + newline, null);
        }

        /**
         * Has the same functionality as the method
         * <code>println(long l)</code> and gives an additional option
         * to change the printout color. Type "<code>Color.</code>" when
         * entering the second argument for this method and a default list of colors
         * should appear.
         *
         * @see println(long l)
         * @param l The
         * <code>long</code> to be printed
         * @param color The color of the printout.
         */
        public void println(long l, Color color) {
            printText(String.valueOf(l) + newline, attr(color));
        }

        /**
         * Has the same functionality as the method
         * <code>println(long l)</code> and gives an additional option
         * to fully customize the printout.
         * <p>You can use the method getAttribute(String
         * font, int size, Color color, boolean bold, boolean underline, boolean
         * italic, boolean strikethrough) to create an object to use with this
         * method.</p>
         *
         * @see println(long l)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param l The
         * <code>long</code> to be printed
         * @param satt Styles the printout.
         */
        public void println(long l, SimpleAttributeSet satt) {
            printText(String.valueOf(l) + newline, satt);
        }

        /**
         * Prints a integer value and terminates the line. The string
         * produced by String.valueOf(int) is translated into bytes according to
         * the platform's default character encoding, and these bytes are
         * inserted into the styled document using the insertString method.
         *
         * @param i
         *  Value of type int to be printed to the GUI console.
         */
        public void println(int i) {
            printText(String.valueOf(i) + newline, null);
        }

        /**
         * Has the same functionality as the method
         * <code>println(int i)</code> and gives an additional option to
         * change the printout color. Type "<code>Color.</code>" when
         * entering the second argument for this method and a default list of colors
         * should appear.
         *
         * @see println(int i)
         * @param i The
         * <code>int</code> to be printed
         * @param color The color of the printout.
         */
        public void println(int i, Color color) {
            printText(String.valueOf(i) + newline, attr(color));
        }

        /**
         * Has the same functionality as the method
         * <code>println(int i)</code> and gives an additional option to
         * fully customize the printout.
         * <p>You can use the method getAttribute(String
         * font, int size, Color color, boolean bold, boolean underline, boolean
         * italic, boolean strikethrough) to create an object to use with this
         * method.</p>
         *
         * @see println(int i)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param i The
         * <code>int</code> to be printed
         * @param satt Styles the printout.
         */
        public void println(int i, SimpleAttributeSet satt) {
            printText(String.valueOf(i) + newline, satt);
        }

        /**
         * Prints a Character value and terminates the line. The string
         * produced by String.valueOf(char) is translated into bytes according
         * to the platform's default character encoding, and these bytes are
         * inserted into the styled document using the insertString method.
         *
         * @param c
         *  Value of type char to be printed to the GUI console.
         */
        public void println(char c) {
            printText(String.valueOf(c) + newline, null);
        }

        /**
         * Has the same functionality as the method
         * <code>println(char c)</code> and gives an additional option
         * to change the printout color. Type "<code>Color.</code>" when
         * entering the second argument for this method and a default list of
         * colors should appear.
         *
         * @see println(char c)
         * @param c The
         * <code>char</code> to be printed
         * @param color The color of the printout.
         */
        public void println(char c, Color color) {
            printText(String.valueOf(c) + newline, attr(color));
        }

        /**
         * Has the same functionality as the method
         * <code>println(char c)</code> and gives an additional option
         * to fully customize the printout.
         * <p>You can use the method getAttribute(String
         * font, int size, Color color, boolean bold, boolean underline, boolean
         * italic, boolean strikethrough) to create an object to use with this
         * method.</p>
         *
         * @see println(char c)
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param c The
         * <code>char</code> to be printed
         * @param satt Styles the printout.
         */
        public void println(char c, SimpleAttributeSet satt) {
            printText(String.valueOf(c) + newline, satt);
        }

        /**
         * Styles your last input located at the last prompt index. This
         * is useful is you want to add some cool feedback when someone input
         * was faulty. e.g. <br/>
         * You could <s>strikethrough</s> the last input or turn it red to show
         * the input was wrong and then get the user input again.<br />
         * It could work great for correct input like turning it
         * <strong>bold</strong> green.
         *
         * @see getAttribute(String font, int
         * size, Color color, boolean bold, boolean underline, boolean italic,
         * boolean strikethrough)
         *
         * @param attr Custom style for the last input.
         */
        public void styleLastInput(SimpleAttributeSet attr) {
            doc.setCharacterAttributes(lastInputStart, lastInputEnd, attr, false);
        }

        /**
         * Hides the user input by masking all characters with a
         * circular shaped one.
         * This method has the same functionality as
         * <code>nextLine()</code> method except that the characters are not
         * revealed.
         * <p><strong>Note:</strong><br />
         * Before this method can be use, you must install a password font. One
         * can be found at
         * http://www.director-online.com/dougwiki/index.php?title=Password_Font_(ttf)
         * <br />And the name of the font must be 'password'
         *
         * @see nextLine()
         *
         * @return keyboard input from user as a String.
         */
        public String nextPassword() {
            return passwordPrompt();
        }

        /**
         * Hides the user input by masking all characters with a
         * circular shaped one.
         * This method has the same functionality as
         * <code>nextLine()</code> method except that the characters are not
         * revealed.
         * <p><strong>Note:</strong><br />
         * Before this method can be use, you must install a password font. One
         * can be found at
         * http://www.director-online.com/dougwiki/index.php?title=Password_Font_(ttf)
         * <br />And the name of the font must be 'password'
         *
         * @see nextLine()
         *
         * @param l maximum length of password.
         * @return keyboard input from user as a String.
         */
        public String nextPassword(int l) {
            maxInput = l;
            return passwordPrompt();
        }

        /**
         * Captures user input from the keyboard. Every time data is
         * inserted in the text pane, a variable will be updated with length of
         * the styled document. This is done so that when a prompt to capture
         * user input is initiated, the prompt method will know exactly which
         * piece of data to return beginning with the index of the last string
         *  insertion to the end of the styled document.
         *
         * @return keyboard input from user as a String.
         */
        public String nextLine() {
            return prompt();
        }

        /**
         * Captures user input from the keyboard. Every time data is
         * inserted in the text pane, a variable will be updated with length of
         * the styled document. This is done so that when a prompt to capture
         * user input is initiated, the prompt method will know exactly which
         * piece of data to return beginning with the index of the last string
         *  insertion to the end of the styled document.
         *
         * @param l maximum length of input
         * @return keyboard input from user as a String.
         */
        public String nextLine(int l) {
            maxInput = l;
            return prompt();
        }

        /**
         * Captures user input from the keyboard as a double. Every time
         * data is inserted in the text pane, a variable will be updated with
         * length of the styled document. This is done so that when a prompt to
         * capture user input is initiated, the prompt method will know exactly
         * which piece of data to return beginning with the index of the last
         * string insertion to the end of the styled document.
         *
         * @return keyboard input from user as a double.
         */
        public double nextDouble() {
            return Double.valueOf(prompt());
        }

        /**
         * Captures user input from the keyboard as a double. Every time
         * data is inserted in the text pane, a variable will be updated with
         * length of the styled document. This is done so that when a prompt to
         * capture user input is initiated, the prompt method will know exactly
         * which piece of data to return beginning with the index of the last
         * string insertion to the end of the styled document.
         *
         * @param l maximum length of input
         * @return keyboard input from user as a double.
         */
        public double nextDouble(int l) {
            maxInput = l;
            return Double.valueOf(prompt());
        }

        /**
         * Captures user input from the keyboard as a float. Every time
         * data is inserted in the text pane, a variable will be updated with
         * length of the styled document. This is done so that when a prompt to
         * capture user input is initiated, the prompt method will know exactly
         * which piece of data to return beginning with the index of the last
         * string insertion to the end of the styled document.
         *
         * @return keyboard input from user as a <code>float</code>.
         */
        public float nextFloat() {
            return Float.valueOf(prompt());
        }

        /**
         * Captures user input from the keyboard as a float. Every time
         * data is inserted in the text pane, a variable will be updated with
         * length of the styled document. This is done so that when a prompt to
         * capture user input is initiated, the prompt method will know exactly
         * which piece of data to return beginning with the index of the last
         * string insertion to the end of the styled document.
         *
         * @param l maximum length of input
         * @return keyboard input from user as a <code>float</code>.
         */
        public float nextFloat(int l) {
            maxInput = l;
            return Float.valueOf(prompt());
        }

        /**
         * Captures user input from the keyboard as a long. Every time
         * data is inserted in the text pane, a variable will be updated with
         * length of the styled document. This is done so that when a prompt to
         * capture user input is initiated, the prompt method will know exactly
         * which piece of data to return beginning with the index of the last
         * string insertion to the end of the styled document.
         *
         * @return keyboard input from user as a <code>long</code>.
         */
        public long nextLong() {
            return Long.valueOf(prompt());
        }

        /**
         * Captures user input from the keyboard as a long. Every time
         * data is inserted in the text pane, a variable will be updated with
         * length of the styled document. This is done so that when a prompt to
         * capture user input is initiated, the prompt method will know exactly
         * which piece of data to return beginning with the index of the last
         * string insertion to the end of the styled document.
         *
         * @param l maximum length of input
         * @return keyboard input from user as a <code>long</code>.
         */
        public long nextLong(int l) {
            maxInput = l;
            return Long.valueOf(prompt());
        }

        /**
         * Captures user input from the keyboard as an integer. Every time
         * data is inserted in the text pane, a variable will be updated with
         * length of the styled document. This is done so that when a prompt to
         * capture user input is initiated, the prompt method will know exactly
         * which piece of data to return beginning with the index of the last
         * string insertion to the end of the styled document.
         *
         * @return keyboard input from user as a <code>int</code>.
         */
        public int nextInt() {
            return Integer.valueOf(prompt());
        }

        /**
         * Captures user input from the keyboard as an integer. Every time
         * data is inserted in the text pane, a variable will be updated with
         * length of the styled document. This is done so that when a prompt to
         * capture user input is initiated, the prompt method will know exactly
         * which piece of data to return beginning with the index of the last
         * string insertion to the end of the styled document.
         *
         * @param l maximum length of input.
         * @return keyboard input from user as a <code>int</code>.
         */
        public int nextInt(int l) {
            maxInput = l;
            return Integer.valueOf(prompt());
        }

        private SimpleAttributeSet attr(Color color) {
            SimpleAttributeSet s = new SimpleAttributeSet();
            StyleConstants.setForeground(s, color);
            return s;
        }

        private void replaceRange(String str, int start, int end) {
            try {
                if (doc instanceof AbstractDocument) {
                    ((AbstractDocument) doc).replace(start, end - start, str,
                            null);
                } else {
                    doc.remove(start, end - start);
                    doc.insertString(start, str, null);
                }
            } catch (BadLocationException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        /**
         * Causes the prompt to contain an input before user types in
         * anything. It is like a default value or like a shortcut instead of
         * having to re-type the same data over and over again, this method can
         * be use to filling the prompt with a value which is to be re-used
         * several times in a row.
         *
         * @param val
         * Text that will be at the prompt.
         * @param s
         */
        public void setPromptVal(String val, boolean s) {
            promptVal = val;
            selected = s;
        }

        private String passwordPrompt() {
            String oldFont = StyleConstants.getFontFamily(promptStyle);
            StyleConstants.setFontFamily(promptStyle, "password");
            String inp = prompt();
            StyleConstants.setFontFamily(promptStyle, oldFont);
            return inp;
        }

        private String prompt() {
            printText("\r", promptStyle);
            inputStart = doc.getLength();
            editing = true;
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    pane.setEditable(true);
                    caret.setVisible(true);
                    caret.setDot(inputStart);
                }

            });
            if (promptVal != null) {
                printText(promptVal, promptStyle);
                if (selected) {
                    select(inputStart, doc.getLength());
                }
            }
            try {
                latch = new CountDownLatch(1);
                latch.await();
            } catch (java.lang.InterruptedException ex) {
                System.err.print("The latch failed:"
                        + "\n" + ex.getMessage());
            }
            return input;
        }

        private class InputPolicy implements KeyListener, ChangeListener {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!editing) {
                    return;
                }
                int maxRegion = inputStart + maxInput;
                if (caret.getDot() < inputStart || caret.getMark() < inputStart
                         || (maxInput > -1 && doc.getLength() >= maxRegion)) {
                    pane.setEditable(false);
                } else if (!pane.isEditable()) {
                    pane.setEditable(true);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (!editing) {
                    return;
                }
                if (caret.getDot() < inputStart && editing) {
                    caret.setDot(pane.getText().length());
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}

        } // InputPolicy class end ---------------------------------------------

        private void printText(String txt, SimpleAttributeSet attr) {
            try {
                doc.insertString(doc.getLength(), txt, attr);
            } catch (BadLocationException ex) {
                C.io.println("Error occurred in printText");
                C.io.println(ex.getMessage());
            }
        }

        private void select(final int start, final int end) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    pane.select(start, end);
                }

            });
        }

        private void redirectSystemStreams() {
            OutputStream out = new OutputStream() {
                @Override
                public void write(final int b) throws IOException {
                    printText(String.valueOf((char) b), null);
                }
                @Override
                public void write(byte[] b, int off, int len) throws IOException {
                    printText(new String(b, off, len), null);
                }
                @Override
                public void write(byte[] b) throws IOException {
                    write(b, 0, b.length);
                }
            };
            System.setOut(new PrintStream(out, true));
            System.setErr(new PrintStream(out, true));
        }

        private JFrame frame;
        private JTextPane pane;
        private CountDownLatch latch;
        private StyledDocument doc;
        private Caret caret;
        private SimpleAttributeSet promptStyle;
        private String[] fontNames;
        private String input;
        private String promptVal;
        private String defaultFont;
        private int inputStart;
        private int lastInputStart;
        private int lastInputEnd;
        private int maxInput;
        private boolean editing;
        private boolean selected;
    } //</editor-fold>
}