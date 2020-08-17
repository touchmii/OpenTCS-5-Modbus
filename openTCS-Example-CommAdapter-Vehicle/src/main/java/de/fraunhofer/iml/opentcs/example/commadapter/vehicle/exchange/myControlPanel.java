/*
 * Created by JFormDesigner on Fri Jun 26 08:07:09 CST 2020
 */

package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
//import java.util.Comparator;
//import java.util.Call
import static java.util.Objects.requireNonNull;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.border.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.google.inject.assistedinject.Assisted;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.ExampleProcessModel;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.OrderRequest;
import org.opentcs.components.kernel.services.VehicleService;
import org.opentcs.customizations.ServiceCallWrapper;
import org.opentcs.drivers.vehicle.VehicleProcessModel;
import org.opentcs.drivers.vehicle.management.*;
import org.opentcs.util.CallWrapper;
import org.opentcs.util.Comparators;
import org.opentcs.data.model.Point;
import org.opentcs.util.gui.StringListCellRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Benoni Jiang
 */
public class myControlPanel extends VehicleCommAdapterPanel {

    private static final Logger LOG = LoggerFactory.getLogger((ControlPanel.class));

    private static final int MAX_LAST_ORDERS = 20;

    private final DefaultListModel<OrderRequest> lastOrderListModel = new DefaultListModel<>();

    private final VehicleService vehicleService;

    private final CallWrapper callWrapper;

    private ExampleProcessModelTO processModel;

    @Inject
    public myControlPanel(@Assisted ExampleProcessModelTO processModel,
                          @Assisted VehicleService vehicleService,
                          @ServiceCallWrapper CallWrapper callWrapper) {
        this.processModel = requireNonNull(processModel, "processModel");
        this.vehicleService = requireNonNull(vehicleService, "vehicleService");
        this.callWrapper = requireNonNull(callWrapper, "callWrepper");
        initComponents();
//        initComboBoxes();
//        initGuiContent();
    }

    private void initComBoBoxes() {
        try {
            destinationComboBox.removeAllItems();
            callWrapper.call(() -> vehicleService.fetchObjects(Point.class)).stream()
                    .sorted(Comparators.objectsByName())
                    .forEach(point -> destinationComboBox.addItem(point));
            actionComboBox.removeAllItems();
            for (OrderRequest.OrderAction curAction : OrderRequest.OrderAction.values()) {
                actionComboBox.addItem(curAction);
            }
        } catch (Exception ex) {
            LOG.warn("Error fetching points", ex);
        }
    }

    private void initGuiContent() {
        for (VehicleProcessModel.Attribute attribute : VehicleProcessModel.Attribute.values()) {
            processModelChange(attribute.name(), processModel);
        }
        for (ExampleProcessModel.Attribute attribute : ExampleProcessModel.Attribute.values()) {
            processModelChange(attribute.name(), processModel);
        }
    }

    @Override
    public void processModelChange(String attributeChanged, VehicleProcessModelTO newProcessModle) {
        if (!(newProcessModle instanceof ExampleProcessModelTO)) {
            return;
        }
        processModel = (ExampleProcessModelTO) newProcessModle;

        if (Objects.equals(attributeChanged,
                VehicleProcessModel.Attribute.COMM_ADAPTER_ENABLED.name())) {
            updateCommAdapterEnabled(processModel.isCommAdapterEnabled());
        }

    }

    private void updateCommAdapterEnabled(boolean enabled) {
        SwingUtilities.invokeLater(() -> {
            enableAdapterCheckBox.setSelected(enabled);

            hostTextField.setEditable(!enabled);
            portTextField.setEditable(!enabled);
            aliveTimeoutTextField.setEditable(!enabled);
            disconnectOnTimeoutChkBox.setEnabled(!enabled);
            reconnectOnConnectionLossChkBox.setEnabled(!enabled);
            enableLoggingChkBox.setEnabled(!enabled);
        });
    }

    private void enableAdapterCheckBoxActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void disconnectOnTimeoutChkBoxActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void reconnectOnConnectionLossChkBoxActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void enableLoggingChkBoxActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void sendOrderButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void repeatLastOrderButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Benoni Jiang
        ResourceBundle bundle = ResourceBundle.getBundle("de.fraunhofer.iml.opentcs.example.commadapter.vehicle.Bundle");
        connectionSettingsPanel = new JPanel();
        enableAdapterCheckBox = new JCheckBox();
        hostLabel = new JLabel();
        hostTextField = new JTextField();
        connectedButton = new JButton();
        portLabel = new JLabel();
        portTextField = new JTextField();
        activeButton = new JButton();
        aliveTimeoutLable = new JLabel();
        aliveTimeoutTextField = new JTextField();
        disconnectOnTimeoutChkBox = new JCheckBox();
        reconnectOnConnectionLossChkBox = new JCheckBox();
        enableLoggingChkBox = new JCheckBox();
        setOrderPanel = new JPanel();
        destinationLabel = new JLabel();
        destinationComboBox = new JComboBox<>();
        actionLabel = new JLabel();
        actionComboBox = new JComboBox<>();
        orderIdLabel = new JLabel();
        orderIdTextField = new JTextField();
        sendOrderButton = new JButton();
        repeatOrderPanel = new JPanel();
        lastOrdersScrollPane = new JScrollPane();
        lastOrdersList = new JList<>();
        lastOrderDetailsPanel = new JPanel();
        destinationLabel1 = new JLabel();
        lastDestinationTextField = new JTextField();
        actionLabel1 = new JLabel();
        lastActionTextField = new JTextField();
        lastOrderIdLabel = new JLabel();
        lastOrderIdTextField = new JTextField();
        repeatLastOrderButton = new JButton();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .
        EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frmDes\u0069gner \u0045valua\u0074ion" , javax. swing .border . TitledBorder. CENTER ,javax . swing
        . border .TitledBorder . BOTTOM, new java. awt .Font ( "D\u0069alog", java .awt . Font. BOLD ,12 ) ,
        java . awt. Color .red ) , getBorder () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( )
        { @Override public void propertyChange (java . beans. PropertyChangeEvent e) { if( "\u0062order" .equals ( e. getPropertyName () ) )
        throw new RuntimeException( ) ;} } );

        //======== connectionSettingsPanel ========
        {
            connectionSettingsPanel.setBorder(new TitledBorder(null, "Connection to vehicle", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                new Font("Tahoma", Font.BOLD, 11)));
            connectionSettingsPanel.setLayout(new GridBagLayout());
            ((GridBagLayout)connectionSettingsPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
            ((GridBagLayout)connectionSettingsPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

            //---- enableAdapterCheckBox ----
            enableAdapterCheckBox.setText(bundle.getString("EnableAdapter"));
            enableAdapterCheckBox.addActionListener(e -> enableAdapterCheckBoxActionPerformed(e));
            connectionSettingsPanel.add(enableAdapterCheckBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

            //---- hostLabel ----
            hostLabel.setText(bundle.getString("VehicleIpAddress"));
            connectionSettingsPanel.add(hostLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 3, 0, 3), 0, 0));

            //---- hostTextField ----
            hostTextField.setColumns(12);
            hostTextField.setText("XXX.XXX.XXX.XXX");
            connectionSettingsPanel.add(hostTextField, new GridBagConstraints(2, 0, 4, 1, 10.0, 10.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 3, 3), 0, 0));

            //---- connectedButton ----
            connectedButton.setIcon(new ImageIcon(getClass().getResource("/de/fraunhofer/iml/opentcs/example/commadapter/vehicle/res/symbols/LEDGray.gif")));
            connectedButton.setText(bundle.getString("AdapterConnected"));
            connectedButton.setBorderPainted(false);
            connectedButton.setDisabledIcon(new ImageIcon(getClass().getResource("/de/fraunhofer/iml/opentcs/example/commadapter/vehicle/res/symbols/LEDRed.gif")));
            connectedButton.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/de/fraunhofer/iml/opentcs/example/commadapter/vehicle/res/symbols/LEDGreen.gif")));
            connectedButton.setEnabled(false);
            connectionSettingsPanel.add(connectedButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

            //---- portLabel ----
            portLabel.setText(bundle.getString("VehicleTcpPort"));
            connectionSettingsPanel.add(portLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 3, 0, 3), 0, 0));

            //---- portTextField ----
            portTextField.setDocument(new PlainDocument(){
                @Override
                public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                    try {
                        String oldString = getText(0, getLength());
                        StringBuilder newString = new StringBuilder(oldString);
                        newString.insert(offs, str);
                        int newValue = Integer.parseInt(newString.toString());
                        if (newValue >= 1 && newValue <= 65535) {
                            super.insertString(offs, str, a);
                        }
                    }
                    catch(NumberFormatException e) { }
                }
            }
            );
            portTextField.setColumns(6);
            portTextField.setText("XXXXX");
            connectionSettingsPanel.add(portTextField, new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 3, 3), 0, 0));

            //---- activeButton ----
            activeButton.setIcon(new ImageIcon(getClass().getResource("/de/fraunhofer/iml/opentcs/example/commadapter/vehicle/res/symbols/LEDGray.gif")));
            activeButton.setText(bundle.getString("ControlPanel.AdapterActive"));
            activeButton.setBorderPainted(false);
            activeButton.setDisabledIcon(new ImageIcon(getClass().getResource("/de/fraunhofer/iml/opentcs/example/commadapter/vehicle/res/symbols/LEDRed.gif")));
            activeButton.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/de/fraunhofer/iml/opentcs/example/commadapter/vehicle/res/symbols/LEDGreen.gif")));
            activeButton.setEnabled(false);
            connectionSettingsPanel.add(activeButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

            //---- aliveTimeoutLable ----
            aliveTimeoutLable.setText(bundle.getString("ControlPanel.IdleAfter"));
            connectionSettingsPanel.add(aliveTimeoutLable, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 3, 0, 3), 0, 0));

            //---- aliveTimeoutTextField ----
            aliveTimeoutTextField.setDocument(new PlainDocument(){
                @Override
                public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                    try {
                        Integer.parseInt(str);
                        super.insertString(offs, str, a);
                    }
                    catch(NumberFormatException e) { }
                }
            });
            aliveTimeoutTextField.setColumns(6);
            aliveTimeoutTextField.setText("XXXXX");
            connectionSettingsPanel.add(aliveTimeoutTextField, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 3, 3), 0, 0));

            //---- disconnectOnTimeoutChkBox ----
            disconnectOnTimeoutChkBox.setText(bundle.getString("ControlPanel.DisconnectOnTimeout"));
            disconnectOnTimeoutChkBox.addActionListener(e -> disconnectOnTimeoutChkBoxActionPerformed(e));
            connectionSettingsPanel.add(disconnectOnTimeoutChkBox, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

            //---- reconnectOnConnectionLossChkBox ----
            reconnectOnConnectionLossChkBox.setText(bundle.getString("ControlPanel.DisconnectOnConnectionLoss"));
            reconnectOnConnectionLossChkBox.addActionListener(e -> reconnectOnConnectionLossChkBoxActionPerformed(e));
            connectionSettingsPanel.add(reconnectOnConnectionLossChkBox, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

            //---- enableLoggingChkBox ----
            enableLoggingChkBox.setText(bundle.getString("ControlPanel.EnableLogging"));
            enableLoggingChkBox.addActionListener(e -> enableLoggingChkBoxActionPerformed(e));
            connectionSettingsPanel.add(enableLoggingChkBox, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        }

        //======== setOrderPanel ========
        {
            setOrderPanel.setBorder(new TitledBorder(null, "New order telegram", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                new Font("Tahoma", Font.BOLD, 11)));
            setOrderPanel.setLayout(new GridBagLayout());

            //---- destinationLabel ----
            destinationLabel.setText(bundle.getString("OrderDestination"));
            setOrderPanel.add(destinationLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 3, 0, 3), 0, 0));

            //---- destinationComboBox ----
            destinationComboBox.setEditor(new TCSObjectComboBoxEditor());
            destinationComboBox.setRenderer(new StringListCellRenderer<Point>(point -> point.getName()));
            destinationComboBox.setEditable(true);
            setOrderPanel.add(destinationComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 3, 3), 0, 0));

            //---- actionLabel ----
            actionLabel.setText(bundle.getString("OrderAction"));
            setOrderPanel.add(actionLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 3, 0, 3), 0, 0));
            setOrderPanel.add(actionComboBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 3, 3), 0, 0));

            //---- orderIdLabel ----
            orderIdLabel.setText(bundle.getString("OrderID"));
            setOrderPanel.add(orderIdLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 3, 0, 3), 0, 0));

            //---- orderIdTextField ----
            orderIdTextField.setColumns(6);
            orderIdTextField.setText("1");
            setOrderPanel.add(orderIdTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 3, 3), 0, 0));

            //---- sendOrderButton ----
            sendOrderButton.setText(bundle.getString("SendNewOrder"));
            sendOrderButton.addActionListener(e -> sendOrderButtonActionPerformed(e));
            setOrderPanel.add(sendOrderButton, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 3, 3, 3), 0, 0));
        }

        //======== repeatOrderPanel ========
        {
            repeatOrderPanel.setBorder(new TitledBorder(null, "Last orders sent to vehicle", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                new Font("Tahoma", Font.BOLD, 11)));
            repeatOrderPanel.setMinimumSize(new Dimension(199, 170));

            //======== lastOrdersScrollPane ========
            {

                //---- lastOrdersList ----
                lastOrdersList.setModel(lastOrderListModel);
                lastOrdersList.setCellRenderer(new OrderListCellRenderer());
                lastOrdersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                lastOrdersScrollPane.setViewportView(lastOrdersList);
            }

            //======== lastOrderDetailsPanel ========
            {
                lastOrderDetailsPanel.setLayout(new GridBagLayout());

                //---- destinationLabel1 ----
                destinationLabel1.setText(bundle.getString("OrderDestination"));
                lastOrderDetailsPanel.add(destinationLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 3, 0, 3), 0, 0));

                //---- lastDestinationTextField ----
                lastDestinationTextField.setEditable(false);
                lastDestinationTextField.setColumns(12);
                lastDestinationTextField.setHorizontalAlignment(SwingConstants.RIGHT);
                lastOrderDetailsPanel.add(lastDestinationTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(0, 0, 3, 3), 0, 0));

                //---- actionLabel1 ----
                actionLabel1.setText(bundle.getString("OrderAction"));
                lastOrderDetailsPanel.add(actionLabel1, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 3, 0, 3), 0, 0));

                //---- lastActionTextField ----
                lastActionTextField.setEditable(false);
                lastActionTextField.setColumns(12);
                lastActionTextField.setHorizontalAlignment(SwingConstants.RIGHT);
                lastOrderDetailsPanel.add(lastActionTextField, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(0, 0, 3, 3), 0, 0));

                //---- lastOrderIdLabel ----
                lastOrderIdLabel.setText(bundle.getString("OrderID"));
                lastOrderDetailsPanel.add(lastOrderIdLabel, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 3, 0, 3), 0, 0));

                //---- lastOrderIdTextField ----
                lastOrderIdTextField.setEditable(false);
                lastOrderIdTextField.setColumns(6);
                lastOrderDetailsPanel.add(lastOrderIdTextField, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(0, 0, 3, 3), 0, 0));

                //---- repeatLastOrderButton ----
                repeatLastOrderButton.setText(bundle.getString("SendLastOrderAgain"));
                repeatLastOrderButton.setEnabled(false);
                repeatLastOrderButton.addActionListener(e -> repeatLastOrderButtonActionPerformed(e));
                lastOrderDetailsPanel.add(repeatLastOrderButton, new GridBagConstraints(0, 12, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 3, 3, 3), 0, 0));
            }

            GroupLayout repeatOrderPanelLayout = new GroupLayout(repeatOrderPanel);
            repeatOrderPanel.setLayout(repeatOrderPanelLayout);
            repeatOrderPanelLayout.setHorizontalGroup(
                repeatOrderPanelLayout.createParallelGroup()
                    .addGroup(repeatOrderPanelLayout.createSequentialGroup()
                        .addComponent(lastOrdersScrollPane, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastOrderDetailsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0))
            );
            repeatOrderPanelLayout.setVerticalGroup(
                repeatOrderPanelLayout.createParallelGroup()
                    .addGroup(repeatOrderPanelLayout.createSequentialGroup()
                        .addGroup(repeatOrderPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(lastOrdersScrollPane)
                            .addComponent(lastOrderDetailsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0))
            );
        }

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(connectionSettingsPanel, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
                            .addGap(6, 6, 6)
                            .addComponent(setOrderPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addComponent(repeatOrderPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(connectionSettingsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(setOrderPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(6, 6, 6)
                    .addComponent(repeatOrderPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Benoni Jiang
    private JPanel connectionSettingsPanel;
    private JCheckBox enableAdapterCheckBox;
    private JLabel hostLabel;
    private JTextField hostTextField;
    private JButton connectedButton;
    private JLabel portLabel;
    private JTextField portTextField;
    private JButton activeButton;
    private JLabel aliveTimeoutLable;
    private JTextField aliveTimeoutTextField;
    private JCheckBox disconnectOnTimeoutChkBox;
    private JCheckBox reconnectOnConnectionLossChkBox;
    private JCheckBox enableLoggingChkBox;
    private JPanel setOrderPanel;
    private JLabel destinationLabel;
    private JComboBox<Point> destinationComboBox;
    private JLabel actionLabel;
    private JComboBox<OrderRequest.OrderAction> actionComboBox;
    private JLabel orderIdLabel;
    private JTextField orderIdTextField;
    private JButton sendOrderButton;
    private JPanel repeatOrderPanel;
    private JScrollPane lastOrdersScrollPane;
    private JList<OrderRequest> lastOrdersList;
    private JPanel lastOrderDetailsPanel;
    private JLabel destinationLabel1;
    private JTextField lastDestinationTextField;
    private JLabel actionLabel1;
    private JTextField lastActionTextField;
    private JLabel lastOrderIdLabel;
    private JTextField lastOrderIdTextField;
    private JButton repeatLastOrderButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
