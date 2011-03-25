/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vlcflvaudioextractor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.filechooser.FileFilter;

//////////////////////////////////////////////////////// CountWords
public class Main extends JFrame {

    //====================================================== fields
    JTextField   _inputFileNameTF  = new JTextField();
    JTextField   _outputFileNameTF  = new JTextField();
    JFileChooser _fileChooser = new JFileChooser();
     
    JButton      _chooseInput = new JButton("Choose FLV Input File");
    JButton      _chooseOutput = new JButton("Output File");
    JButton      _extract = new JButton("Extract MP3");
    JButton      _about = new JButton("Help/About");
       

    //================================================= constructor
    Main() {
        //... Create / set component characteristics.
        _inputFileNameTF.setEditable(false);
        _outputFileNameTF.setEditable(false);
        
        _chooseOutput.setEnabled(false);
        _extract.setEnabled(false);
  
        //... Create content pane, layout components
        JPanel content = new JPanel();
        content.setPreferredSize(new Dimension(500,150));
        content.setLayout(new GridLayout(3,3,20,20));
        content.add(_inputFileNameTF);
        content.add(_chooseInput);
        content.add(_outputFileNameTF);
        content.add(_chooseOutput);
        content.add(_extract);
        content.add(_about);
       
                  
        _chooseInput.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int retval = _fileChooser.showOpenDialog(Main.this);
                    _fileChooser.addChoosableFileFilter(new MyCustomFilter());
                    if (retval == JFileChooser.APPROVE_OPTION) {
                        //... The user selected a file, get it, use it.
                        File file = _fileChooser.getSelectedFile();
                        
                        //... Update user interface.
                        String inpFileName = file.getAbsolutePath();
                        String outFileName = inpFileName.substring(0, (inpFileName.length()-1)-2) + "mp3";
                        
                        _inputFileNameTF.setText(inpFileName);
                        _outputFileNameTF.setText(outFileName);
                        _extract.setEnabled(true);
                    }
                }
            }
        });
        
        _extract.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
               if (e.getButton() == MouseEvent.BUTTON1) {
               try {
                    String command = "\"C:\\Program Files\\VideoLAN\\VLC\\vlc.exe\" ";    
                    command += "\"" + _inputFileNameTF.getText() + "\"";
                    command += " --sout=#transcode{acodec=mp3,vcodec=dummy}:standard{access=file,mux=raw,dst=\"";
                    command += _outputFileNameTF.getText() + "\"";
                    command += "} vlc://quit";
                    System.out.println(command);
/*
 * Command = "C:\Program Files\VideoLAN\VLC\vlc.exe" "H:\Internet Downloads\Youtube Videos\Gran Vals - Francisco Tarrega.flv" --sout=#transcode{acodec=mp3,vcodec=dummy}:standard{access=file,mux=raw,dst="C:\Output File.mp3"} vlc://quit
 */                   
                             
                    Process p=Runtime.getRuntime().exec(command);
                    p.waitFor(); 
                    BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
                    String line=reader.readLine(); 
                    while(line!=null)  {                   
                        
                        line=reader.readLine(); 
                    } 
                     
                } catch(IOException e1) {
                    JOptionPane.showMessageDialog(null, "Unknown Error Occurred", "FLV Audio Extractor via VLC v1.0 by RK ", JOptionPane.ERROR_MESSAGE);
                } catch(InterruptedException e2) {
                    JOptionPane.showMessageDialog(null, "Unknown Error Occurred", "FLV Audio Extractor via VLC v1.0 by RK ", JOptionPane.ERROR_MESSAGE);
                }
                JOptionPane.showMessageDialog(null, "Done - Check file ->" +_outputFileNameTF.getText() , "FLV Audio Extractor via VLC v1.0 by RK ", JOptionPane.ERROR_MESSAGE);
                _extract.setEnabled(false);
                _inputFileNameTF.setText("");
                _outputFileNameTF.setText("");
               }
            };
        });
        
         _about.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
               if (e.getButton() == MouseEvent.BUTTON1) {
                   JOptionPane.showMessageDialog(null, "Help \n====== \n1) Choose FLV Input File\n2) Click Extract MP3 \n3) Wait for VLC Player to run and finish conversion\n4) Check Output File\n\nReleased under GPL v3 License, report bugs to ravikiranj.pesit@yahoo.com \n" , "FLV Audio Extractor via VLC v1.0 by RK ", JOptionPane.INFORMATION_MESSAGE);
               }
            };
         });
                                
        this.setContentPane(content);
        this.setTitle("FLV Audio Extractor via VLC v1.0 by RK");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();                      // Layout components.
        this.setLocationRelativeTo(null); // Center window.
    }

    class MyCustomFilter extends javax.swing.filechooser.FileFilter {
        @Override
        public boolean accept(File file) {
            // Allow only directories, or files with ".txt" extension
            return file.getAbsolutePath().endsWith(".flv");
        }
        @Override
        public String getDescription() {
            // This description will be displayed in the dialog,
            // hard-coded = ugly, should be done via I18N
            return "FLV files (*.flv)";
        }
    } 
   
    //========================================================= main
    public static void main(String[] args) {
        File vlcFile=new File("C:\\Program Files\\VideoLAN\\VLC\\vlc.exe");   
        if(!vlcFile.exists()) {
             JOptionPane.showMessageDialog(null, "Please install VLC Player (http://www.videolan.org/vlc/) before running this Program", "FLV Audio Extractor via VLC v1.0 by RK ", JOptionPane.ERROR_MESSAGE);
        }else {
            JFrame window = new Main();
            window.setVisible(true);
        }
    }
}
