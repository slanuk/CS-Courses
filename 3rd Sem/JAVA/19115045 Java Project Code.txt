import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AttendanceManagement implements ActionListener
{
    static File f = new File("C:\\Users\\kunal\\Pictures");
    static int ln;
    static String roll;
    static void createFolder()
    {
        if(!f.exists())
        {
            f.mkdirs();
        }
    }
    static void readFile()
    {
        try
        {
            FileReader fr = new FileReader(f+"\\attendance.txt");
            //System.out.println("file exists!");
        }
        catch (FileNotFoundException ex)
        {
            try
            {
                FileWriter fw = new FileWriter(f+"\\attendance.txt");
                //System.out.println("File created");
            }
            catch (IOException ex1)
            {
                Logger.getLogger(AttendanceManagement.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } 
    }
    static void addData(String r,String m,String d)
    {
        try
        {
            RandomAccessFile raf = new RandomAccessFile(f+"\\attendance.txt", "rw");
            for(int i=0;i<ln;i++)
            {
                raf.readLine();
            }
            if(d.length()<2)
            {
                d="0"+d;
            }
            raf.writeBytes(r+d+m+"\r\n");
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(AttendanceManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(AttendanceManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    static int Attendance(String usr)
    {
        int count=0;
        try
        {
            RandomAccessFile raf = new RandomAccessFile(f+"\\attendance.txt", "rw");
            for(int i=0;i<ln;i++)
            {
                String line = raf.readLine();
                roll=line.substring(0,8);
                //date=line.substring(8,10);
                //month=line.substring(10);
                if(roll.equals(usr))
                {
                    count++;
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(AttendanceManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(AttendanceManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return(count);    
    }
    static void countLines()
    {
        try
        {
            ln=0;
            RandomAccessFile raf = new RandomAccessFile(f+"\\attendance.txt", "rw");
            for(int i=0;raf.readLine()!=null;i++)
            {
                ln++;
            }
            System.out.println("number of lines:"+ln);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(AttendanceManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(AttendanceManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @SuppressWarnings("unchecked")//
    public static void main(String[] args)
    {
         JFrame frame=new JFrame("Term Project");
         frame.setSize(1000,500);
         frame.setLayout(null); 
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         JLabel l1 =new JLabel("CSE Attendance Management");
         l1.setBounds(200, 1, 600, 50);
         l1.setFont(new java.awt.Font("Arial", 0, 36));
         frame.add(l1);
         JLabel l2 =new JLabel("Attendance Marking");
         l2.setBounds(100, 70, 300, 50);
         l2.setFont(new java.awt.Font("Arial", 0, 27));
         frame.add(l2);
         JLabel l3 =new JLabel("Roll Number");
         l3.setBounds(50, 140, 100, 50);
         frame.add(l3);
         l3.setFont(new java.awt.Font("Arial", 0, 18));
         JLabel l4 =new JLabel("Month");
         l4.setBounds(50, 210, 100, 50);
         frame.add(l4);
         l4.setFont(new java.awt.Font("Arial", 0, 18));
         JLabel l5 =new JLabel("Date of month");
         l5.setBounds(50, 280, 120, 50);
         frame.add(l5);
         l5.setFont(new java.awt.Font("Arial", 0, 18));
         JLabel l6 =new JLabel("Show Attendance");
         l6.setBounds(650, 70, 300, 50);
         frame.add(l6);
         l6.setFont(new java.awt.Font("Arial", 0, 27));
         JLabel l7 =new JLabel("Roll Number");
         l7.setBounds(600, 140, 100, 50);
         frame.add(l7);
         l7.setFont(new java.awt.Font("Arial", 0, 18));
         JButton b1=new JButton("Mark Present");
         b1.setBounds(100, 350, 150, 30);
         frame.add(b1);
         JButton b2=new JButton("Show Attendance");
         b2.setBounds(650, 210, 150, 30);
         frame.add(b2);
         JTextField t1 =new JTextField();
         t1.setBounds(200, 150, 150, 30);
         frame.add(t1);
         JTextField t2 =new JTextField();
         t2.setBounds(200, 220, 150, 30);
         frame.add(t2);
         JTextField t3 =new JTextField();
         t3.setBounds(200, 290, 150, 30);
         frame.add(t3);
         JTextField t4 =new JTextField();
         t4.setBounds(750, 150, 150, 30);
         frame.add(t4);
         b1.addActionListener(new ActionListener()
           {
             @Override
             public void actionPerformed(ActionEvent e)
             {
                 createFolder(); 
                 readFile();
                 countLines();
                 addData(t1.getText(),t2.getText(),t3.getText());
                 JOptionPane.showMessageDialog(null, t1.getText()+" Marked Present for "+t2.getText()+t3.getText());
             }
         });
         b2.addActionListener(new ActionListener()
         {
             @Override
             public void actionPerformed(ActionEvent e)
             {
                 createFolder(); 
                 readFile();
                 countLines();
                 int present=Attendance(t4.getText());
                 JOptionPane.showMessageDialog(null, t4.getText()+" was present for "+present+" lectures");
             }
         });
         frame.setVisible(true);
    }
    @Override
public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    
}
}
