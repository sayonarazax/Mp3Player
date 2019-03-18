
import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import interfaces.PlayControlListener;
import interfaces.PlayList;
import interfaces.Player;
import interfaces.impl.Mp3PlayList;
import listeners.SearchListener;
import interfaces.impl.Mp3Player;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import utils.ExtensionFileFilter;
import utils.FileUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import static interfaces.impl.Mp3PlayList.PLAYLIST_FILE_DESCRIPTION;
import static interfaces.impl.Mp3PlayList.PLAYLIST_FILE_EXTENSION;
import static interfaces.impl.Mp3Player.MP3_FILE_DESCRIPTION;
import static interfaces.impl.Mp3Player.MP3_FILE_EXTENSION;
import static utils.SkinUtils.changeSkin;

public class Mp3PlayerForm extends JFrame implements PlayControlListener {
    private FileFilter mp3FileFilter = new ExtensionFileFilter(MP3_FILE_EXTENSION, MP3_FILE_DESCRIPTION);
    private FileFilter plsFileFilter = new ExtensionFileFilter(PLAYLIST_FILE_EXTENSION, PLAYLIST_FILE_DESCRIPTION);

    private double posValue = 0.0;

    private boolean moveAutomatic = true;

    private Player player;
    private PlayList playList;

    @Override
    public void playStarted(String name) {
        songLabel.setText(name);
    }

    @Override
    public void processScroll(int position) {
        if (moveAutomatic) {
            System.out.println(moveAutomatic);
            slideProgress.setValue(position);
        }
    }

    @Override
    public void playFinished() {
        playList.selectNextSong();
    }

    private JMenu menu = new JMenu();
    private JMenu service = new JMenu();
    private JMenu changeSkin = new JMenu();
    private JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem popAddPlaylist = new JMenuItem();
    private JMenuItem popSavePlaylist = new JMenuItem();
    private JMenuItem popAddSong = new JMenuItem();
    private JMenuItem popDelSong = new JMenuItem();
    private JMenuItem skin1 = new JMenuItem();
    private JMenuItem skin2 = new JMenuItem();
    private JMenuItem open = new JMenuItem();
    private JMenuItem save = new JMenuItem();
    private JMenuItem exit = new JMenuItem();
    private JMenuBar menuBar = new JMenuBar();
    private JFileChooser fileChooser = new JFileChooser();

    ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");

    public JPanel rootPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addBtn;
    private JButton upBtn;
    private JButton removeBtn;
    private JButton dwnBtn;
    private JList songList;
    private JButton muteBtn;
    private JButton prevBtn;
    private JButton playBtn;
    private JButton pauseBtnb;
    private JButton stopButton;
    private JButton nextBtn;
    private JSlider slider1;
    private JScrollPane jScrollPane;
    private JLabel songLabel;
    private JSlider slideProgress;

    private int volumeValue;


    public Mp3PlayerForm() {
        super("Mp3Player");
        initComponents();
  //      player = new Mp3Player(this);
 //       playList = new Mp3PlayList(player, songList);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new HiFiLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new Mp3PlayerForm();
    }

    public void initComponents() {
        context.getBean("mp3PlayerForm");
        player = (Mp3Player) context.getBean("Mp3Player");
        playList = (Mp3PlayList) context.getBean("Mp3PlayList");
        player.setVolume(slider1.getValue());
        slider1.setMaximum(Mp3Player.MAX_VOLUME);

        setDefaultLookAndFeelDecorated(true);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        popAddSong.setIcon(new ImageIcon(getClass().getResource("images/Add.png")));
        popAddSong.setText("Добавить mp3");
        popAddSong.setName("");
        popupMenu.add(popAddSong);

        popDelSong.setIcon(new ImageIcon(getClass().getResource("images/Remove.png")));
        popDelSong.setText("Удалить mp3");
        popDelSong.setName("");
        popupMenu.add(popDelSong);

        popAddPlaylist.setIcon(new ImageIcon(getClass().getResource("images/open_folder.png")));
        popAddPlaylist.setText("Открыть плэйлист");
        popAddPlaylist.setName("");
        popupMenu.add(popAddPlaylist);

        popSavePlaylist.setIcon(new ImageIcon(getClass().getResource("images/Save.png")));
        popSavePlaylist.setText("Сохранить плэйлист");
        popSavePlaylist.setName("");
        popupMenu.add(popSavePlaylist);

        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Выбрать файл");
        fileChooser.setMultiSelectionEnabled(true);
        addListeners();

        songList.setComponentPopupMenu(popupMenu);
        jScrollPane.setViewportView(songList);

        slider1.setMaximum(200);
        slider1.setMinorTickSpacing(5);
        slider1.setSnapToTicks(true);
        slider1.setToolTipText("Изменить громкость");
        slider1.setValue(200);
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sliderChangeListner(e);
            }
        });

        slideProgress.setMaximum(1000);
        slideProgress.setMinorTickSpacing(1);
        slideProgress.setSnapToTicks(true);
        slideProgress.setValue(0);
        slideProgress.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                slideProgressMousePressed(e);
            }

            public void mouseReleased(MouseEvent e) {
                slideProgressMouseReleased(e);
            }
        });


        setResizable(false);
        setContentPane(rootPanel);
        setJMenuBar(createMenu());
        setVisible(true);
    }


    public void addListeners() {
        searchField.addFocusListener(new SearchListener(searchField));
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnAddSongActionPerformed(e);
            }
        });
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnDeleteSongActionPerformed(e);
            }
        });
        dwnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnDownActionPerformed(e);
            }
        });
        upBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnUpActionPerformed(e);
            }
        });
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPlaylistActionPerformed(e);
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePlaylistActionPerformed(e);
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuExitActionPerformed(e);
            }
        });
        playBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnPlayActionPerformed(e);
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopButtonActionPerformed(e);
            }
        });
        pauseBtnb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseButtonActionListener(e);
            }
        });
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextButtonActionPerformed(e);
            }
        });
        prevBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prevButtonActionPerformed(e);
            }
        });
        muteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                muteButtonActionPerformed(e);
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonSearchActionPerformed(e);
            }
        });
        popAddSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnAddSongActionPerformed(e);
            }
        });
        popDelSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnDeleteSongActionPerformed(e);
            }
        });
        popAddPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPlaylistActionPerformed(e);
            }
        });
        popSavePlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePlaylistActionPerformed(e);
            }
        });
    }

    public JMenuBar createMenu() {
        menu.setText("Файл");
        open.setIcon(new ImageIcon(getClass().getResource("images/open_folder.png")));
        open.setText("Открыть плэйлист");
        open.setName("");
        menu.add(open);

        save.setIcon(new ImageIcon(getClass().getResource("images/Save.png")));
        save.setText("Сохранить плэйлист");
        save.setName("");
        menu.add(save);

        exit.setIcon(new ImageIcon(getClass().getResource("images/exit.png")));
        exit.setText("Выйти из программы");
        exit.setName("");
        menu.add(exit);

        service.setText("Сервис");
        changeSkin.setIcon(new ImageIcon(getClass().getResource("images/Gear.png")));
        changeSkin.setText("Поменять скин");
        changeSkin.setName("");

        skin1.setText("Cкин 1");
        skin1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSkin1(e);
            }
        });

        skin2.setText("Cкин 2");
        skin2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSkin2(e);
            }
        });

        changeSkin.add(skin1);
        changeSkin.add(skin2);
        service.add(changeSkin);
        menuBar.add(menu);
        menuBar.add(service);
        return menuBar;
    }

    public void menuSkin1(ActionEvent e) {
        changeSkin(this, UIManager.getSystemLookAndFeelClassName());
    }

    public void menuSkin2(ActionEvent e) {
        changeSkin(this, new HiFiLookAndFeel());
    }

    public void buttonSearchActionPerformed(ActionEvent e) {

    }

    public void btnAddSongActionPerformed(ActionEvent e) {
        FileUtils.addFileFilter(fileChooser, mp3FileFilter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            playList.addSong(fileChooser.getSelectedFiles());
        }
    }

    public void btnDeleteSongActionPerformed(ActionEvent e) {
        playList.delete();
    }

    public void btnDownActionPerformed(ActionEvent e) {
        int nextIndex = songList.getSelectedIndex() + 1;
        if (nextIndex <= songList.getModel().getSize() - 1)
            songList.setSelectedIndex(nextIndex);
    }

    public void btnUpActionPerformed(ActionEvent e) {
        int nextIndex = songList.getSelectedIndex() - 1;
        if (nextIndex >= 0)
            songList.setSelectedIndex(nextIndex);
    }

    public void openPlaylistActionPerformed(ActionEvent e) {
        FileUtils.addFileFilter(fileChooser, plsFileFilter);
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            playList.openPlaylist(fileChooser.getSelectedFile());
        }
    }

    public void savePlaylistActionPerformed(ActionEvent e) {
        FileUtils.addFileFilter(fileChooser, plsFileFilter);
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.exists()) {
                int resultOverride = JOptionPane.showConfirmDialog(this, "Файл существует", "Перезаписать?", JOptionPane.YES_NO_CANCEL_OPTION);
                switch (resultOverride) {
                    case JOptionPane.NO_OPTION:
                        savePlaylistActionPerformed(e);
                    case JOptionPane.CANCEL_OPTION:
                        fileChooser.cancelSelection();
                        return;
                }
            }
            fileChooser.approveSelection();
            playList.savePlayList(selectedFile);
        }
    }

    public void menuExitActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    public void btnPlayActionPerformed(ActionEvent e) {
        playList.playFile();
    }

    public void stopButtonActionPerformed(ActionEvent e) {
        player.stop();
    }

    public void pauseButtonActionListener(ActionEvent e) {
        player.pause();
    }

    public void sliderChangeListner(ChangeEvent e) {
        player.setVolume(slider1.getValue());
        if (slider1.getValue() == 0) {
            muteBtn.setSelected(true);
        } else {
            muteBtn.setSelected(false);
        }
    }

    public void slideProgressStateChanged(ChangeEvent e) {
        if (slideProgress.getValueIsAdjusting() == true) {
            if (moveAutomatic = true) {
                moveAutomatic = false;
                posValue = slideProgress.getValue() * 1.0 / 1000;
                processSeek(posValue);
            } else {
                moveAutomatic = false;
       ///         movingFromJump = true;
            }
        }
    }

    private void slideProgressMouseReleased(MouseEvent e) {//GEN-FIRST:event_slideProgressMouseReleased
        if (slideProgress.getValueIsAdjusting() == false) {
            posValue = slideProgress.getValue() * 1.0 / 1000;
            player.jump(posValue);
        }

        moveAutomatic = true;
    }

    private void slideProgressMousePressed(MouseEvent e) {//GEN-FIRST:event_slideProgressMousePressed
        moveAutomatic = false;
    }

    public void processSeek(double bytes) {
        try {
            //           long skipBytes = (long) Math.round(((Integer) bytesLen).intValue() * bytes);
            //          player.jump(skipBytes);
        } catch (Exception e) {
            e.printStackTrace();
  //          movingFromJump = false;
        }
    }

    public void nextButtonActionPerformed(ActionEvent e) {
        playList.selectNextSong();
    }

    public void prevButtonActionPerformed(ActionEvent e) {
        playList.selectPreviousSonf();
    }

    public void muteButtonActionPerformed(ActionEvent e) {
        if (muteBtn.isSelected()) {
            volumeValue = slider1.getValue();
            slider1.setValue(0);
        } else {
            slider1.setValue(volumeValue);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.setMinimumSize(new Dimension(400, 400));
        rootPanel.setPreferredSize(new Dimension(400, 400));
        rootPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 20, 0, 20), -1, -1));
        rootPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        searchField = new JTextField();
        Font searchFieldFont = this.$$$getFont$$$(null, -1, 14, searchField.getFont());
        if (searchFieldFont != null) searchField.setFont(searchFieldFont);
        panel1.add(searchField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(150, 30), null, 0, false));
        searchButton = new JButton();
        searchButton.setIcon(new ImageIcon(getClass().getResource("/images/search.png")));
        searchButton.setText("Найти");
        panel1.add(searchButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 8, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
        addBtn = new JButton();
        addBtn.setIcon(new ImageIcon(getClass().getResource("/images/Add.png")));
        addBtn.setRequestFocusEnabled(true);
        addBtn.setRolloverEnabled(true);
        addBtn.setText("");
        addBtn.setToolTipText("Добавить песню");
        addBtn.setVerticalAlignment(0);
        addBtn.putClientProperty("html.disable", Boolean.FALSE);
        panel2.add(addBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        upBtn = new JButton();
        upBtn.setIcon(new ImageIcon(getClass().getResource("/images/arrowUp.png")));
        upBtn.setText("");
        panel2.add(upBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeBtn = new JButton();
        removeBtn.setIcon(new ImageIcon(getClass().getResource("/images/Remove.png")));
        removeBtn.setText("");
        panel2.add(removeBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dwnBtn = new JButton();
        dwnBtn.setIcon(new ImageIcon(getClass().getResource("/images/arrowDown.png")));
        dwnBtn.setInheritsPopupMenu(true);
        dwnBtn.setText("");
        panel2.add(dwnBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 2, new Insets(10, 10, 10, 10), -1, -1));
        rootPanel.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        jScrollPane = new JScrollPane();
        panel3.add(jScrollPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 5, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        songList = new JList();
        songList.setEnabled(true);
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("mp3ListModel");
        songList.setModel(defaultListModel1);
        songList.setSelectionMode(1);
        jScrollPane.setViewportView(songList);
        prevBtn = new JButton();
        prevBtn.setIcon(new ImageIcon(getClass().getResource("/images/Prev.png")));
        prevBtn.setText("");
        panel3.add(prevBtn, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        playBtn = new JButton();
        playBtn.setIcon(new ImageIcon(getClass().getResource("/images/Play.png")));
        playBtn.setText("");
        panel3.add(playBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pauseBtnb = new JButton();
        pauseBtnb.setIcon(new ImageIcon(getClass().getResource("/images/Pause.png")));
        pauseBtnb.setText("");
        panel3.add(pauseBtnb, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stopButton = new JButton();
        stopButton.setIcon(new ImageIcon(getClass().getResource("/images/Stop.png")));
        stopButton.setText("");
        panel3.add(stopButton, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nextBtn = new JButton();
        nextBtn.setIcon(new ImageIcon(getClass().getResource("/images/Next.png")));
        nextBtn.setText("");
        panel3.add(nextBtn, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 10, 0, 10), -1, -1));
        rootPanel.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        muteBtn = new JButton();
        muteBtn.setIcon(new ImageIcon(getClass().getResource("/images/Speaker.png")));
        muteBtn.setSelected(false);
        muteBtn.setSelectedIcon(new ImageIcon(getClass().getResource("/images/Mute.png")));
        muteBtn.setText("");
        panel4.add(muteBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        slider1 = new JSlider();
        panel4.add(slider1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        songLabel = new JLabel();
        songLabel.setRequestFocusEnabled(true);
        songLabel.setText("              ");
        songLabel.setVisible(true);
        rootPanel.add(songLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        slideProgress = new JSlider();
        slideProgress.setAutoscrolls(false);
        slideProgress.setMaximum(1000);
        slideProgress.setMinorTickSpacing(1);
        slideProgress.setValue(0);
        rootPanel.add(slideProgress, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }


}
