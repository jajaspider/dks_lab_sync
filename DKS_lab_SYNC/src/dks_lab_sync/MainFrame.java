/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dks_lab_sync;

import java.awt.Component;
import javax.swing.JFileChooser;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/*
디렉토리 선택 방법
Swing의 JFileChooser라는 객체가 존재하므로 이것으로 찾아오는 방향으로 구성

디렉토리 내부 구성 찾기
재귀적으로 호출해서 디렉토리 내부의 전체 파일을 찾아와야함

사용할 File객체 메소드
1. isFile() - 파일인지 판별
2. isDirectory() - 폴더인지 판별
3. lastModified() - 최종 수정시간
4. isHidden() - 숨김파일인지 판별
5. getAbsolutePath() - 파일 절대경로
6. length() - 파일의 크기
7. exists() - 파일이 존재하는지 확인
8. renameTo(파일2) - 원래 파일을 매개변수의 파일로 변경함
 ==> 파일1.renameTo(파일2) = 파일1을 파일2로 변경하는듯함
 ==> 폴더 이동도 가능한듯 함

재귀호출시 CanonicalPath 사용(한번 지정된 파일의 절대경로를 끝까지 가짐)
 */
public class MainFrame extends javax.swing.JFrame {

    File[] dir1, dir2;
    Object[][] obj1 = new Object[0][4];
    Object[][] obj2 = new Object[0][4];
    
    DefaultTableModel tm1 = new DefaultTableModel() {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch(columnIndex) {
                case 0: return Boolean.class;
                case 1: return String.class;
                case 2: return String.class;
                case 3: return String.class;
                default: return String.class;
            }
        }
    };
    DefaultTableModel tm2 = new DefaultTableModel() {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch(columnIndex) {
                case 0: return String.class;
                case 1: return String.class;
                case 2: return String.class;
                default: return String.class;
            }
        }
    };
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        init();
    }
    
    public void init() {
        dirTable1.setModel(tm1);
        tm1.addColumn(" ");
        tm1.addColumn("구분");
        tm1.addColumn("이름");
        tm1.addColumn("마지막 수정일");
        
        dirTable2.setModel(tm2);
        tm2.addColumn("구분");
        tm2.addColumn("이름");
        tm2.addColumn("마지막 수정일");
    }
    
    public File fileChoose() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //디렉토리만 선택할 수 있도록 설정함
        
        fileChooser.showDialog(this, null);
        File dir = fileChooser.getSelectedFile(); //디렉토리 선택 시 반환
        
        return dir;
    }
    
    public File[] dirList(String directory) {
        File dir = new File(directory);
        File[] fileList = dir.listFiles();
        
        return fileList;
    }
    
    public Object[][] dir1FilesList(ArrayList<FileData> file, ArrayList<FileData> dir) {
        int size = file.size() + dir.size();
        Object[][] obj = new Object[size][4];
        
        for(int i=0;i<dir.size();i++) {
            obj[i][0] = false;
            obj[i][1] = dir.get(i).getType();
            obj[i][2] = dir.get(i).getName();
            obj[i][3] = dir.get(i).getDate();
        }
        
        for(int i=dir.size(), j=0;i<dir.size()+file.size();i++,j++) {
            obj[i][0] = false;
            obj[i][1] = file.get(j).getType();
            obj[i][2] = file.get(j).getName();
            obj[i][3] = file.get(j).getDate();
        }
        
        return obj;
    }
    
    public Object[][] dir2FilesList(ArrayList<FileData> file, ArrayList<FileData> dir) {
        int size = file.size() + dir.size();
        Object[][] obj = new Object[size][3];
        
        for(int i=0;i<dir.size();i++) {
            obj[i][0] = dir.get(i).getType();
            obj[i][1] = dir.get(i).getName();
            obj[i][2] = dir.get(i).getDate();
        }
        
        for(int i=dir.size(), j=0;i<dir.size()+file.size();i++,j++) {
            obj[i][0] = file.get(j).getType();
            obj[i][1] = file.get(j).getName();
            obj[i][2] = file.get(j).getDate();
        }
        
        return obj;
    }
    
    public void listSet(File[] fileList, int position) {
        ArrayList<FileData> fileArray = new ArrayList<>();
        ArrayList<FileData> dirArray = new ArrayList<>();
        
        try{
            for(int i=0;i<fileList.length;i++){
                File file = fileList[i];
                long time = file.lastModified();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/DD/HH/mm/ss");
                String date = sdf.format(time);
                if(file.isFile()) {
                    fileArray.add(new FileData("파일", file.getName(), date));
                }else if(file.isDirectory()) {
                    dirArray.add(new FileData("폴더", file.getName(), date));
                    //listSet(file.getAbsolutePath(), position);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        if(position == 1) {
            Object[][] list = dir1FilesList(fileArray, dirArray);        
            for(int i=0;i<list.length;i++) {
                tm1.addRow(new Object[0]);
                tm1.setValueAt(list[i][0], i, 0);
                tm1.setValueAt(list[i][1], i, 1);
                tm1.setValueAt(list[i][2], i, 2);
                tm1.setValueAt(list[i][3], i, 3);
            }
        }else if(position == 2) {
            Object[][] list = dir2FilesList(fileArray, dirArray);        
            for(int i=0;i<list.length;i++) {
                tm2.addRow(new Object[0]);
                tm2.setValueAt(list[i][0], i, 0);
                tm2.setValueAt(list[i][1], i, 1);
                tm2.setValueAt(list[i][2], i, 2);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        dirLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        dirLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        dirTable1 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        dirTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("디렉토리1 선택");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("디렉토리2 선택");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        dirTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(dirTable1);

        dirTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(dirTable2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dirLabel1))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dirLabel2))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(dirLabel1)
                    .addComponent(jButton2)
                    .addComponent(dirLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        File dir = fileChoose(); //디렉토리 선택 시 반환
        
        dirLabel1.setText(dir.getAbsolutePath());
        
        dir1 = dirList(dir.getAbsolutePath());
        listSet(dir1, 1);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        File dir = fileChoose(); //디렉토리 선택 시 반환
        
        dirLabel2.setText(dir.getAbsolutePath());
        
        dir2 = dirList(dir.getAbsolutePath());
        listSet(dir2, 2);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dirLabel1;
    private javax.swing.JLabel dirLabel2;
    private javax.swing.JTable dirTable1;
    private javax.swing.JTable dirTable2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}
