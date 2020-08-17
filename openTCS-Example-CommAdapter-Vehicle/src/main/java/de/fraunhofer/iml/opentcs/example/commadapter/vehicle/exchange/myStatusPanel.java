/*
 * Created by JFormDesigner on Fri Jun 26 08:07:49 CST 2020
 */

package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import org.opentcs.drivers.vehicle.VehicleProcessModel;
import org.opentcs.drivers.vehicle.management.*;

/**
 * @author Benoni Jiang
 */
public class myStatusPanel extends VehicleCommAdapterPanel {
    public myStatusPanel() {
        initComponents();
    }

    @Override
    public void processModelChange(String a, VehicleProcessModelTO newprocessModle) {

    }

    private void chkBoxEnablePeriodicGetStateActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void buttonGetStateActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Benoni Jiang
        ResourceBundle bundle = ResourceBundle.getBundle("de.fraunhofer.iml.opentcs.example.commadapter.vehicle.Bundle");
        panelPeriodicStateRequests = new JPanel();
        chkBoxEnablePeriodicGetState = new JCheckBox();
        labelGetStateInterval = new JLabel();
        txtGetStateInterval = new JTextField();
        panelManualStateRequest = new JPanel();
        buttonGetState = new JButton();
        panelState = new JPanel();
        panelTelegramContent = new JPanel();
        labelStateTeleCount = new JLabel();
        textFieldStateTeleCount = new JTextField();
        labelStateOS = new JLabel();
        textFieldStateOS = new JTextField();
        labelStateLS = new JLabel();
        textFieldStateLS = new JTextField();
        labelStateCRP = new JLabel();
        textFieldStateCRP = new JTextField();
        fillingLabel2 = new JLabel();
        labelLastOrderID = new JLabel();
        textFieldLastOrderID = new JTextField();
        labelCurrOrderID = new JLabel();
        textFieldCurrOrderID = new JTextField();
        labelLastRcvdOrderID = new JLabel();
        textFieldLastRcvdOrderID = new JTextField();
        fillingLabel1 = new JLabel();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax .
        swing. border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JFor\u006dDesi\u0067ner \u0045valu\u0061tion" , javax. swing .border
        . TitledBorder. CENTER ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "Dia\u006cog"
        , java .awt . Font. BOLD ,12 ) ,java . awt. Color .red ) , getBorder
        () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java
        . beans. PropertyChangeEvent e) { if( "bord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException
        ( ) ;} } );
        setLayout(new GridBagLayout());

        //======== panelPeriodicStateRequests ========
        {
            panelPeriodicStateRequests.setBorder(new TitledBorder(null, "Periodic state requests", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                new Font("Tahoma", Font.BOLD, 11)));
            panelPeriodicStateRequests.setMaximumSize(new Dimension(2147483647, 70));
            panelPeriodicStateRequests.setLayout(new GridBagLayout());

            //---- chkBoxEnablePeriodicGetState ----
            chkBoxEnablePeriodicGetState.setSelected(true);
            chkBoxEnablePeriodicGetState.setText(bundle.getString("EnablePeriodicStateRequests"));
            chkBoxEnablePeriodicGetState.addActionListener(e -> chkBoxEnablePeriodicGetStateActionPerformed(e));
            panelPeriodicStateRequests.add(chkBoxEnablePeriodicGetState, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

            //---- labelGetStateInterval ----
            labelGetStateInterval.setText(bundle.getString("StateRequestInterval"));
            panelPeriodicStateRequests.add(labelGetStateInterval, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 3, 0, 0), 0, 0));

            //---- txtGetStateInterval ----
            txtGetStateInterval.setEditable(false);
            txtGetStateInterval.setColumns(4);
            txtGetStateInterval.setHorizontalAlignment(SwingConstants.RIGHT);
            txtGetStateInterval.setText("500");
            txtGetStateInterval.setMinimumSize(new Dimension(38, 20));
            panelPeriodicStateRequests.add(txtGetStateInterval, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 3, 0, 3), 0, 0));
        }
        add(panelPeriodicStateRequests, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        //======== panelManualStateRequest ========
        {
            panelManualStateRequest.setBorder(new TitledBorder(null, "Manual state requests", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                new Font("Tahoma", Font.BOLD, 11)));
            panelManualStateRequest.setLayout(new CardLayout());

            //---- buttonGetState ----
            buttonGetState.setText(bundle.getString("SendStateRequest"));
            buttonGetState.setEnabled(false);
            buttonGetState.addActionListener(e -> buttonGetStateActionPerformed(e));
            panelManualStateRequest.add(buttonGetState, "card2");
        }
        add(panelManualStateRequest, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        //======== panelState ========
        {
            panelState.setBorder(new EmptyBorder(1, 1, 1, 1));
            panelState.setFont(new Font("Arial", Font.PLAIN, 11));
            panelState.setLayout(new BorderLayout());

            //======== panelTelegramContent ========
            {
                panelTelegramContent.setBorder(new TitledBorder(null, "Last reported state", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                    new Font("Tahoma", Font.BOLD, 11)));
                panelTelegramContent.setLayout(new GridBagLayout());

                //---- labelStateTeleCount ----
                labelStateTeleCount.setText(bundle.getString("OrderID"));
                panelTelegramContent.add(labelStateTeleCount, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 3), 0, 0));

                //---- textFieldStateTeleCount ----
                textFieldStateTeleCount.setEditable(false);
                textFieldStateTeleCount.setBackground(new Color(255, 255, 204));
                textFieldStateTeleCount.setColumns(6);
                textFieldStateTeleCount.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
                textFieldStateTeleCount.setHorizontalAlignment(SwingConstants.TRAILING);
                textFieldStateTeleCount.setText("00000");
                panelTelegramContent.add(textFieldStateTeleCount, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 3, 0), 0, 0));

                //---- labelStateOS ----
                labelStateOS.setText(bundle.getString("OperationState"));
                panelTelegramContent.add(labelStateOS, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 3), 0, 0));

                //---- textFieldStateOS ----
                textFieldStateOS.setEditable(false);
                textFieldStateOS.setBackground(new Color(255, 255, 204));
                textFieldStateOS.setColumns(1);
                textFieldStateOS.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
                textFieldStateOS.setHorizontalAlignment(SwingConstants.TRAILING);
                textFieldStateOS.setText("X");
                panelTelegramContent.add(textFieldStateOS, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 3, 0), 0, 0));

                //---- labelStateLS ----
                labelStateLS.setText(bundle.getString("LoadState"));
                panelTelegramContent.add(labelStateLS, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(0, 6, 0, 3), 0, 0));

                //---- textFieldStateLS ----
                textFieldStateLS.setEditable(false);
                textFieldStateLS.setBackground(new Color(255, 255, 204));
                textFieldStateLS.setColumns(1);
                textFieldStateLS.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
                textFieldStateLS.setHorizontalAlignment(SwingConstants.TRAILING);
                textFieldStateLS.setText("X");
                panelTelegramContent.add(textFieldStateLS, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 3, 0), 0, 0));

                //---- labelStateCRP ----
                labelStateCRP.setText(bundle.getString("currentPoint"));
                panelTelegramContent.add(labelStateCRP, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 6, 0, 3), 0, 0));

                //---- textFieldStateCRP ----
                textFieldStateCRP.setEditable(false);
                textFieldStateCRP.setBackground(new Color(255, 255, 204));
                textFieldStateCRP.setColumns(9);
                textFieldStateCRP.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
                textFieldStateCRP.setHorizontalAlignment(SwingConstants.TRAILING);
                textFieldStateCRP.setText("CURPOINT");
                panelTelegramContent.add(textFieldStateCRP, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 3, 0), 0, 0));
                panelTelegramContent.add(fillingLabel2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- labelLastOrderID ----
                labelLastOrderID.setText(bundle.getString("LastFinishedOrder"));
                panelTelegramContent.add(labelLastOrderID, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 0, 3, 3), 0, 0));

                //---- textFieldLastOrderID ----
                textFieldLastOrderID.setEditable(false);
                textFieldLastOrderID.setBackground(new Color(255, 255, 204));
                textFieldLastOrderID.setColumns(6);
                textFieldLastOrderID.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
                textFieldLastOrderID.setHorizontalAlignment(SwingConstants.TRAILING);
                textFieldLastOrderID.setText("00000");
                panelTelegramContent.add(textFieldLastOrderID, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 3, 0), 0, 0));

                //---- labelCurrOrderID ----
                labelCurrOrderID.setText(bundle.getString("currentOrderID"));
                panelTelegramContent.add(labelCurrOrderID, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(0, 6, 0, 3), 0, 0));

                //---- textFieldCurrOrderID ----
                textFieldCurrOrderID.setEditable(false);
                textFieldCurrOrderID.setBackground(new Color(255, 255, 204));
                textFieldCurrOrderID.setColumns(6);
                textFieldCurrOrderID.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
                textFieldCurrOrderID.setHorizontalAlignment(SwingConstants.TRAILING);
                textFieldCurrOrderID.setText("11111");
                panelTelegramContent.add(textFieldCurrOrderID, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 3, 0), 0, 0));

                //---- labelLastRcvdOrderID ----
                labelLastRcvdOrderID.setText(bundle.getString("lastReceivedOrderID"));
                panelTelegramContent.add(labelLastRcvdOrderID, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 6, 0, 3), 0, 0));

                //---- textFieldLastRcvdOrderID ----
                textFieldLastRcvdOrderID.setEditable(false);
                textFieldLastRcvdOrderID.setBackground(new Color(255, 255, 204));
                textFieldLastRcvdOrderID.setColumns(6);
                textFieldLastRcvdOrderID.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
                textFieldLastRcvdOrderID.setHorizontalAlignment(SwingConstants.TRAILING);
                textFieldLastRcvdOrderID.setText("22222");
                panelTelegramContent.add(textFieldLastRcvdOrderID, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 3, 0), 0, 0));
            }
            panelState.add(panelTelegramContent, BorderLayout.CENTER);
        }
        add(panelState, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- fillingLabel1 ----
        fillingLabel1.setFont(new Font("Arial", Font.PLAIN, 11));
        add(fillingLabel1, new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Benoni Jiang
    private JPanel panelPeriodicStateRequests;
    private JCheckBox chkBoxEnablePeriodicGetState;
    private JLabel labelGetStateInterval;
    private JTextField txtGetStateInterval;
    private JPanel panelManualStateRequest;
    private JButton buttonGetState;
    private JPanel panelState;
    private JPanel panelTelegramContent;
    private JLabel labelStateTeleCount;
    private JTextField textFieldStateTeleCount;
    private JLabel labelStateOS;
    private JTextField textFieldStateOS;
    private JLabel labelStateLS;
    private JTextField textFieldStateLS;
    private JLabel labelStateCRP;
    private JTextField textFieldStateCRP;
    private JLabel fillingLabel2;
    private JLabel labelLastOrderID;
    private JTextField textFieldLastOrderID;
    private JLabel labelCurrOrderID;
    private JTextField textFieldCurrOrderID;
    private JLabel labelLastRcvdOrderID;
    private JTextField textFieldLastRcvdOrderID;
    private JLabel fillingLabel1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
